/***************************************************************************************************
 *   @Title: TicketingController.java                                                                             *
 *   @Date:  2015年8月8日 下午5:41:47                                                                 *
 *   @Since: JDK 1.7                                                                               *
 *   @Copyright: All material herein ©2015 Marco Polo Technology Co., Ltd, all rights reserved     * 
 * *********************************************************************************************** *
 *    注意： 本内容仅限于深圳马可孛罗科技有限公司内部使用，禁止转发													   *
 ***************************************************************************************************/
package com.gongfutrip.service.api.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gongfutrip.response.Result;
import com.gongfutrip.service.api.core.constant.ErrorCode;
import com.gongfutrip.service.api.core.exception.ParamException;
import com.gongfutrip.service.api.core.model.PartnerVO;
import com.gongfutrip.service.api.core.service.AuthenticationService;
import com.gongfutrip.service.api.core.util.ExceptionProcessor;
import com.gongfutrip.service.api.dto.Authentication;
import com.gongfutrip.service.api.dto.TicketingDTO;
import com.gongfutrip.service.api.interfaces.TicketingService;
import com.gongfutrip.service.api.interfaces.result.DefaultResult;
import com.gongfutrip.service.api.web.util.LogUtils;
import com.gongfutrip.service.api.web.util.ParamHandler;

/** 
 * @ClassName: TicketingController 
 * @Description: 出票
 * @author Jango 
 * @date 2015年8月8日 下午5:41:47 
 * @since JDK 1.7 
 */
@Controller
public class TicketingController {
    private static Logger logger = LoggerFactory.getLogger(TicketingController.class);

    @Resource(name = "ticketingService")
    private TicketingService ticketingService;

    @Resource(name = "authenticationService")
    private AuthenticationService authenticationService;

    @ResponseBody
    @RequestMapping(value = "/ticketing")
    public Object ticketing(HttpServletRequest request) {
      Result result = null;
        try {
            long start = System.currentTimeMillis();
            Authentication authentication = ParamHandler.convertParam(request, "authentication", Authentication.class);
            TicketingDTO ticketingDTO = ParamHandler.convertParam(request, "ticketing", TicketingDTO.class);
            PartnerVO partner = (PartnerVO)authenticationService.authentication(authentication).getData();
            result = ticketingService.ticketing(partner, ticketingDTO);
            long end = System.currentTimeMillis();
            LogUtils.addToLog(request, result, end - start, ticketingDTO.getOrderNum());
            return result;
        } catch (ParamException e) {
            return ExceptionProcessor.getParamExceptionResult(e);
        } catch (Exception e) {
            logger.error("TicketingController Exception.", e);
            return new DefaultResult(ErrorCode.Basic.SYSTEM_ERROR);
        }
    }
}
