/***************************************************************************************************
 *   @Title: AuthenticationInterceptor.java                                                                             *
 *   @Date:  2015年8月18日 下午8:11:33                                                                 *
 *   @Since: JDK 1.7                                                                               *
 *   @Copyright: All material herein ©2015 Marco Polo Technology Co., Ltd, all rights reserved     * 
 * *********************************************************************************************** *
 *    注意： 本内容仅限于深圳马可孛罗科技有限公司内部使用，禁止转发													   *
 ***************************************************************************************************/
package com.gongfutrip.service.api.interceptors;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gongfutrip.codec.SerializeUtils;
import com.gongfutrip.other.DateUtil;
import com.gongfutrip.redis.RedisUtil;
import com.gongfutrip.response.Result;
import com.gongfutrip.service.api.core.constant.ErrorCode;
import com.gongfutrip.service.api.core.exception.BusinessException;
import com.gongfutrip.service.api.core.exception.ParamException;
import com.gongfutrip.service.api.core.model.PartnerVO;
import com.gongfutrip.service.api.core.model.RouteQueryLimit;
import com.gongfutrip.service.api.core.service.AuthenticationService;
import com.gongfutrip.service.api.core.service.RouteQueryLimitService;
import com.gongfutrip.service.api.core.util.ExceptionProcessor;
import com.gongfutrip.service.api.dto.Authentication;
import com.gongfutrip.service.api.dto.SearchAirLegDTO;
import com.gongfutrip.service.api.dto.SearchSolutionDTO;
import com.gongfutrip.service.api.web.util.ParamHandler;

/** 
 * @ClassName: AuthenticationInterceptor 
 * @Description: 拦截验证权限 
 * @author Jango 
 * @date 2015年8月18日 下午8:11:33 
 * @since JDK 1.7 
 */
public class AuthenticationInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(AuthenticationInterceptor.class);


    @Resource(name = "authenticationService")
    private AuthenticationService authenticationService;

    @Resource(name = "routeQueryLimitService")
    private RouteQueryLimitService routeQueryLimitService;

    private ExecutorService threadPool = Executors.newFixedThreadPool(5);

    /** (非 Javadoc) 
     * <p>Title: preHandle</p> 
     * <p>Description: </p> 
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception 
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object) 
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        try {
            Authentication auth = ParamHandler.convertParam(request, "authentication", Authentication.class);

            /**验证访问权限开关是否打开*/
            String limitKey = new StringBuilder(auth.getPartnerId()).append(uri).append("SWITCH").toString();
            String limit = RedisUtil.getJedisX().getString(limitKey);
            if (null != limit && "OFF".equals(limit.toString())) {
                printResult(uri, response, ErrorCode.Business.NOT_ALLOWED.getCode(), ErrorCode.Business.NOT_ALLOWED.getMsg());
                return false;
            }

            /**验证访问授权*/
            Result result = authenticationService.authentication(auth);
            if (!ErrorCode.isSuccess(result.getErrorCode())) {
                printResult(uri, response, ErrorCode.Param.INVALID_PARAM.getCode(), "Authentication failed.");
                return false;
            }

            PartnerVO partner = (PartnerVO) result.getData();
            if (uri.indexOf("/shopping")>-1) {

                SearchSolutionDTO queryDTO = ParamHandler.convertParam(request, "search", SearchSolutionDTO.class);

//                String strStart = RedisUtil.getJedisX().getString(DigestUtils.md5Hex("OUT-OF-WORK-TIME-START-SHOPPIN"));
//                String strEnd = RedisUtil.getJedisX().getString(DigestUtils.md5Hex("OUT-OF-WORK-TIME-END-SHOPPIN"));
//                if (null != strStart && null != strEnd) {
//                    Calendar start = Calendar.getInstance();
//                    start.set(Calendar.HOUR_OF_DAY, Integer.valueOf(strStart));
//                    start.set(Calendar.MINUTE, 0);
//                    start.set(Calendar.SECOND, 0);
//                    Calendar end = Calendar.getInstance();
//                    String nextDay =
//                            RedisUtil.getJedisX().getString(DigestUtils.md5Hex("OUT-OF-WORK-TIME-NEXT_DAY-SHOPPIN"));
//                    if (null != nextDay && "YES".equals(nextDay)) {
//                        end.add(Calendar.DAY_OF_MONTH, 1);
//                    }
//                    end.set(Calendar.HOUR_OF_DAY, Integer.valueOf(strEnd));
//                    end.set(Calendar.MINUTE, 0);
//                    end.set(Calendar.SECOND, 0);
//                    Calendar curr = Calendar.getInstance();
//                    if (curr.before(end) && curr.after(start)) {
//                        printResult(uri, response, ErrorCode.Business.NOT_ALLOWED.getCode(),
//                                ErrorCode.Business.NOT_ALLOWED.getMsg());
//                        return false;
//                    }
//                }
                List<SearchAirLegDTO> sals = queryDTO.getSearchAirLegs();
                if (null != sals && sals.size() > 0) {
                    SearchAirLegDTO dto = sals.get(0);
                    Long gtpInfoId = Long.valueOf(partner.getGtpInfoId());
                    Integer gourpId = partner.getGroupId();
                    List<RouteQueryLimit> limits =
                            routeQueryLimitService.validateQueryLimit(gtpInfoId, gourpId, dto.getOrigin(),
                                    dto.getDestination(),queryDTO.getAirline());
                    Long dayBetween = DateUtil.daysBetween(new Date(), DateUtil.parse(dto.getDepartureDate(), "yyyy-MM-dd"));
                    if(dayBetween>365){
                      logger.error("异常日期,比对当前时间:"+DateUtil.format(new Date(), "yyyy-MM-dd")+" 目标时间:"+dto.getDepartureDate());
                    }
                    if (CollectionUtils.isEmpty(limits)||dayBetween>60) {
                        printResult(uri, response, ErrorCode.Business.NOT_ALLOWED.getCode(),
                                ErrorCode.Business.NOT_ALLOWED.getMsg());
                        return false;
                    }else{
                    System.out.println(limits.toString());
                      request.setAttribute("limits", limits);
                    }
                }
            }
            return true;
        } catch (ParamException e) {
            Result result = ExceptionProcessor.getParamExceptionResult(e);
            printResult(uri, response, result.getErrorCode(), result.getErrorMsg());
        } catch (BusinessException e) {
            Result result = ExceptionProcessor.getBusinessExceptionResult(e);
            printResult(uri, response, result.getErrorCode(), result.getErrorMsg());
        } catch (Exception e) {
            logger.error("接口权限验证时异常.", e);
            printResult(uri, response, ErrorCode.Basic.SYSTEM_ERROR.getCode(), ErrorCode.Basic.SYSTEM_ERROR.getMsg());
        }
        return false;
    }

    /**
     * @Title: printResult 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param uri
     * @param response 
     * @param code
     * @param msg
     * @throws IOException
     */
    private void printResult(String uri, HttpServletResponse response, String code, String msg) throws IOException {

        JSONObject jsonObj = new JSONObject();
        jsonObj.put("errorCode", code);
        jsonObj.put("errorMsg", msg);

        if (uri.indexOf("/shopping")>-1) {
            OutputStream out = response.getOutputStream();
            out.write(SerializeUtils.compress(jsonObj.toJSONString().getBytes()));
        } else {
            PrintWriter out = response.getWriter();
            out.print(jsonObj.toJSONString());
        }
    }


    /** (非 Javadoc) 
     * <p>Title: postHandle</p> 
     * <p>Description: </p> 
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception 
     * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView) 
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {}

    /** (非 Javadoc) 
     * <p>Title: afterCompletion</p> 
     * <p>Description: </p> 
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception 
     * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception) 
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {}

}
