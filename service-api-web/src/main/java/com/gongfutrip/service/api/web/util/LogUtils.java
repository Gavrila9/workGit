/***************************************************************************************************
 *   @Title: LogUtils.java                                                                             *
 *   @Date:  2015年9月7日 下午2:56:32                                                                 *
 *   @Since: JDK 1.8                                                                               *
 *   @Copyright: All material herein ©2015 Marco Polo Technology Co., Ltd, all rights reserved     * 
 * *********************************************************************************************** *
 *    注意： 本内容仅限于深圳马可孛罗科技有限公司内部使用，禁止转发													   *
 ***************************************************************************************************/
package com.gongfutrip.service.api.web.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.gongfutrip.response.Result;
import com.gongfutrip.service.api.core.constant.ErrorCode;

/** 
 * @ClassName: LogUtils 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author Jango 
 * @date 2015年9月7日 下午2:56:32 
 * @since JDK 1.8 
 */
public class LogUtils {
    private static Logger logger = LoggerFactory.getLogger("requestCounter");
    private static Logger bookingLogger = LoggerFactory.getLogger("bookingLogger");
    private static Logger orderPricingLogger = LoggerFactory.getLogger("pricingResult");

    /** 
     * @Title: addToLog 
     * @Description: 
     * @param request
     * @param result 
     */
    public static void addToLog(HttpServletRequest request, Result result, long millis, Long orderNum) {
        try {
            String param = request.getParameter("param");
            String uri = request.getRequestURI();
            String clientIp = WebUtil.getClentIp(request);
            if (ErrorCode.isSuccess(result.getErrorCode())) {
                logger.info("uri:{}, ip:{}, orderNum:{}, param:{}, mills:{}ms", new Object[] {uri, clientIp, orderNum,
                        new String(Base64.decodeBase64(param), "utf-8"), millis});
            } else {
                logger.error("uri:{}, ip:{}, orderNum:{}, param:{}, errorMsg:{}, mills:{}ms", new Object[] {uri, clientIp,
                        orderNum, new String(Base64.decodeBase64(param), "utf-8"), result.getErrorMsg(), millis});
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("转码异常。", e);
        }
    }

    /** 
     * @Title: addBookingLog 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param request
     * @param result
     * @param l 
     */
    public static void addBookingLog(HttpServletRequest request, Result result, long millis) {
        addToLog(request, result, millis, null);
        logDetail(request, result, millis, bookingLogger);
    }

    /** 
     * @Title: addOrderPricingLog 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param req
     * @param result
     * @param l
     * @param orderNum 
     */
    public static void addOrderPricingLog(HttpServletRequest request, Result result, long millis, Long orderNum) {
        addToLog(request, result, millis, orderNum);
        logDetail(request, result, millis, orderPricingLogger);

    }

    private static void logDetail(HttpServletRequest request, Result result, long millis, Logger orderPricingLogger) {
        try {
            String param = request.getParameter("param");
            String uri = request.getRequestURI();
            String clientIp = WebUtil.getClentIp(request);
            if (ErrorCode.isSuccess(result.getErrorCode())) {
                orderPricingLogger.info("uri:{}, ip:{},  param:{}, result:{},mills:{}ms", new Object[] {uri, clientIp,
                        new String(Base64.decodeBase64(param), "utf-8"), JSON.toJSONString(result), millis});
            } else {
                orderPricingLogger.error("uri:{}, ip:{},  param:{}, errorMsg:{}, mills:{}ms", new Object[] {uri, clientIp,
                        new String(Base64.decodeBase64(param), "utf-8"), result.getErrorMsg(), millis});
            }
        } catch (UnsupportedEncodingException e) {
            logger.error("转码异常。", e);
        }
    }
}
