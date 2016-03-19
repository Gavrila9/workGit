package com.gongfutrip.service.api.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gongfutrip.response.Result;
import com.gongfutrip.service.api.core.constant.ErrorCode;
import com.gongfutrip.service.api.core.exception.BusinessException;
import com.gongfutrip.service.api.core.exception.ParamException;
import com.gongfutrip.service.api.core.model.PartnerVO;
import com.gongfutrip.service.api.core.service.AuthenticationService;
import com.gongfutrip.service.api.core.util.ExceptionProcessor;
import com.gongfutrip.service.api.dto.Authentication;
import com.gongfutrip.service.api.dto.OrderPricingDTO;
import com.gongfutrip.service.api.interfaces.PricingService;
import com.gongfutrip.service.api.interfaces.result.DefaultResult;
import com.gongfutrip.service.api.web.util.LogUtils;
import com.gongfutrip.service.api.web.util.ParamHandler;


/**
 * 重新精确验价
 * @author meizs
 *
 */
@Controller
public class OrderPricingController {

    @Resource
    private PricingService pricingService;
    @Resource(name = "authenticationService")
    private AuthenticationService authenticationService;

    @ResponseBody
    @RequestMapping("/orderPricing")
    public Result orderPricing(HttpServletRequest req) {
      Result result = null;
        try {
            long start = System.currentTimeMillis();
            Authentication authentication = ParamHandler.convertParam(req, "authentication", Authentication.class);
            OrderPricingDTO opd = ParamHandler.convertParam(req, "orderPricing", OrderPricingDTO.class);
            Long orderNum = opd.getOrderNum();
            PartnerVO partner = (PartnerVO)authenticationService.authentication(authentication).getData();
            result = pricingService.orderPricing(partner, orderNum);
            long end = System.currentTimeMillis();
            LogUtils.addOrderPricingLog(req, result, end - start, orderNum);
            return result;
        } catch (ParamException e) {
            return ExceptionProcessor.getParamExceptionResult(e);
        } catch (BusinessException e) {
            return ExceptionProcessor.getExceptionResult(e);
        } catch (Exception e) {
            return new DefaultResult(ErrorCode.Basic.SYSTEM_ERROR);
        }
    }
}
