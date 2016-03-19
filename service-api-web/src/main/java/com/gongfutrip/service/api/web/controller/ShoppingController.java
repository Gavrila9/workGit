/***************************************************************************************************
 *   @Title: ShoppingController.java                                                                             *
 *   @Date:  2015年7月6日 下午9:38:53                                                                 *
 *   @Since: JDK 1.7                                                                               *
 *   @Copyright: All material herein ©2015 Marco Polo Technology Co., Ltd, all rights reserved     * 
 * *********************************************************************************************** *
 *    注意： 本内容仅限于深圳马可孛罗科技有限公司内部使用，禁止转发													   *
 ***************************************************************************************************/
package com.gongfutrip.service.api.web.controller;
import java.text.MessageFormat;
import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gongfutrip.codec.SerializeUtils;
import com.gongfutrip.response.Result;
import com.gongfutrip.service.api.core.exception.ParamException;
import com.gongfutrip.service.api.core.model.PartnerVO;
import com.gongfutrip.service.api.core.model.RouteQueryLimit;
import com.gongfutrip.service.api.core.service.AuthenticationService;
import com.gongfutrip.service.api.core.util.ExceptionProcessor;
import com.gongfutrip.service.api.dto.Authentication;
import com.gongfutrip.service.api.dto.SearchSolutionDTO;
import com.gongfutrip.service.api.interfaces.ShoppingService;
import com.gongfutrip.service.api.web.util.LogUtils;
import com.gongfutrip.service.api.web.util.ParamHandler;

/** 
 * @ClassName: ShoppingController 
 * @Description: Shopping 入口 
 * @author Jango 
 * @date 2015年7月6日 下午9:38:53 
 * @since JDK 1.7 
 */
@Controller
@Aspect
public class ShoppingController {
    @Resource(name = "shoppingService")
    private ShoppingService shoppingService;
    @Resource(name = "authenticationService")
    private AuthenticationService authenticationService;
    
    private static Logger logger=LoggerFactory.getLogger("shoppinglogger");
    
    @ResponseBody
    @RequestMapping(value = "/shopping")
    public Object shopping(HttpServletRequest request) throws IOException {
        Result result = null;
        try {
            long start = System.currentTimeMillis();
            Authentication authentication = ParamHandler.convertParam(request, "authentication", Authentication.class);
            SearchSolutionDTO queryDTO = ParamHandler.convertParam(request, "search", SearchSolutionDTO.class);
            List<RouteQueryLimit> limits = (List<RouteQueryLimit>)request.getAttribute("limits");
            result = authenticationService.authentication(authentication);
            PartnerVO partner = (PartnerVO)result.getData();
            result = shoppingService.shopping(partner, queryDTO,limits);
            long end = System.currentTimeMillis();
            LogUtils.addToLog(request, result, end - start, null);
        } catch (ParamException e) {
            result = ExceptionProcessor.getParamExceptionResult(e);
        } catch (Exception e) {
        	
            result = ExceptionProcessor.getExceptionResult(e);
            //new DefaultResult(ErrorCode.Basic.SYSTEM_ERROR);
        }
//        return result;
        return SerializeUtils.compress(new ObjectMapper().writeValueAsBytes(result));
    }
    
    @ResponseBody
    @RequestMapping(value = "/shopping_g")
    public Object shopping_g(HttpServletRequest request) throws IOException {
        Result result = null;
        try {
            long start = System.currentTimeMillis();
            Authentication authentication = ParamHandler.convertParam(request, "authentication", Authentication.class);
            SearchSolutionDTO queryDTO = ParamHandler.convertParam(request, "search", SearchSolutionDTO.class);
            List<RouteQueryLimit> limits = (List<RouteQueryLimit>)request.getAttribute("limits");
            result = authenticationService.authentication(authentication);
            PartnerVO partner = (PartnerVO)result.getData();
            result = shoppingService.shopping(partner, queryDTO,limits);
            long end = System.currentTimeMillis();
            LogUtils.addToLog(request, result, end - start, null);
        } catch (ParamException e) {
            result = ExceptionProcessor.getParamExceptionResult(e);
        } catch (Exception e) {
            result = ExceptionProcessor.getExceptionResult(e);
            //new DefaultResult(ErrorCode.Basic.SYSTEM_ERROR);
        }
        return result;
//        return SerializeUtils.compress(new ObjectMapper().writeValueAsBytes(result));
    }
}
