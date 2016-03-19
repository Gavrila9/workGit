/***************************************************************************************************
 *   @Title: BookingController.java                                                                *
 *   @Date:  2015年7月24日 上午10:39:46                                                                *
 *   @Since: JDK 1.7                                                                               *
 *   @Copyright: All material herein ©2015 Marco Polo Technology Co., Ltd, all rights reserved     * 
 * *********************************************************************************************** *
 *    注意： 本内容仅限于深圳马可孛罗科技有限公司内部使用，禁止转发													   *
 ***************************************************************************************************/
package com.gongfutrip.service.api.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
import com.gongfutrip.service.api.dto.BookingDTO;
import com.gongfutrip.service.api.dto.PreciseBookingDTO;
import com.gongfutrip.service.api.interfaces.BookingService;
import com.gongfutrip.service.api.interfaces.result.DefaultResult;
import com.gongfutrip.service.api.web.util.LogUtils;
import com.gongfutrip.service.api.web.util.ParamHandler;

/** 
 * @ClassName: BookingController 
 * @Description: Booking 入口
 * @author Jango 
 * @date 2015年7月24日 上午10:39:46 
 * @since JDK 1.7 
 */
@Controller
public class BookingController {

    @Resource(name = "bookingService")
    private BookingService bookingService;
    @Resource(name = "authenticationService")
    private AuthenticationService authenticationService;

    @ResponseBody
    @RequestMapping(value = "/booking")
    public Object booking(HttpServletRequest request) {
      Result result = null;
        try {
            long start = System.currentTimeMillis();
            Authentication authentication = ParamHandler.convertParam(request, "authentication", Authentication.class);
            BookingDTO bookingDTO = ParamHandler.convertParam(request, "booking", BookingDTO.class);
            PartnerVO partner = (PartnerVO)authenticationService.authentication(authentication).getData();
            result = bookingService.booking(partner, bookingDTO);
            long end = System.currentTimeMillis();
            LogUtils.addBookingLog(request, result, end - start);
            return result;
        } catch (ParamException e) {
            return ExceptionProcessor.getParamExceptionResult(e);
        } catch (Exception e) {
            return new DefaultResult(ErrorCode.Basic.SYSTEM_ERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/preciseBooking")
    public Object preciseBooking(HttpServletRequest request) {
        Result result = null;
        try {
            long start = System.currentTimeMillis();
            Authentication authentication = ParamHandler.convertParam(request, "authentication", Authentication.class);
            PreciseBookingDTO bookingDTO = ParamHandler.convertParam(request, "booking", PreciseBookingDTO.class);
            PartnerVO partner = (PartnerVO)authenticationService.authentication(authentication).getData();
            result = bookingService.preciseBooking(partner, bookingDTO);
            long end = System.currentTimeMillis();
            LogUtils.addBookingLog(request, result, end - start);
            return result;
        } catch (ParamException e) {
            return ExceptionProcessor.getParamExceptionResult(e);
        } catch (Exception e) {
            return new DefaultResult(ErrorCode.Basic.SYSTEM_ERROR);
        }
    }
}
