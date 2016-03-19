package com.gongfutrip.service.api.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.gongfutrip.service.api.dto.PrecisePricingDTO;
import com.gongfutrip.service.api.dto.PricingDTO;
import com.gongfutrip.service.api.interfaces.PricingService;
import com.gongfutrip.service.api.interfaces.result.DefaultResult;
import com.gongfutrip.service.api.web.util.LogUtils;
import com.gongfutrip.service.api.web.util.ParamHandler;

@Controller
public class PricingController {

    @Autowired
    private PricingService pricingService;
    @Resource(name = "authenticationService")
    private AuthenticationService authenticationService;

    @ResponseBody
    @RequestMapping(value = "/pricing")
    public Object pricing(HttpServletRequest request) {
        Result result = null;
        try {
            Authentication authentication = ParamHandler.convertParam(request, "authentication", Authentication.class);
            PricingDTO pricingDTO = ParamHandler.convertParam(request, "pricing", PricingDTO.class);
            long start = System.currentTimeMillis();
            PartnerVO partner = (PartnerVO)authenticationService.authentication(authentication).getData();
            result = pricingService.pricing(partner, pricingDTO);
            long end = System.currentTimeMillis();
            LogUtils.addToLog(request, result, end - start, null);
            return result;
        } catch (ParamException e) {
            return ExceptionProcessor.getParamExceptionResult(e);
        } catch (Exception e) {
            return new DefaultResult(ErrorCode.Basic.SYSTEM_ERROR);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/precisePricing")
    public Object precisePricing(HttpServletRequest request) {
      Result result = null;
        try {
            Authentication authentication = ParamHandler.convertParam(request, "authentication", Authentication.class);
            PrecisePricingDTO precisePricingDTO = ParamHandler.convertParam(request, "pricing", PrecisePricingDTO.class);
            long start = System.currentTimeMillis();
            PartnerVO partner = (PartnerVO)authenticationService.authentication(authentication).getData();
            result = pricingService.precisePricing(partner, precisePricingDTO);
            long end = System.currentTimeMillis();
            LogUtils.addToLog(request, result, end - start, null);
            return result;
        } catch (ParamException e) {
            return ExceptionProcessor.getParamExceptionResult(e);
        } catch (Exception e) {
            return new DefaultResult(ErrorCode.Basic.SYSTEM_ERROR);
        }
    }
}
