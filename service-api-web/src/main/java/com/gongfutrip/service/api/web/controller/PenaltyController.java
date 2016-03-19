package com.gongfutrip.service.api.web.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gongfutrip.response.Result;
import com.gongfutrip.service.api.core.exception.BusinessException;
import com.gongfutrip.service.api.core.exception.ParamException;
import com.gongfutrip.service.api.core.model.PartnerVO;
import com.gongfutrip.service.api.core.service.AuthenticationService;
import com.gongfutrip.service.api.core.util.ExceptionProcessor;
import com.gongfutrip.service.api.dto.Authentication;
import com.gongfutrip.service.api.interfaces.PenaltyService;
import com.gongfutrip.service.api.interfaces.result.DefaultResult;
import com.gongfutrip.service.api.web.util.ParamHandler;

@Controller
public class PenaltyController {

    @Resource(name = "penaltyService")
    private PenaltyService penaltyService;
    @Resource(name = "authenticationService")
    private AuthenticationService authenticationService;
    
    @RequestMapping(value = "/penalty")
    public @ResponseBody Object getRule(HttpServletRequest req,HttpServletResponse res, String key) {
        Result result = new DefaultResult();
        try {
            long start = System.currentTimeMillis();
            Authentication authentication = ParamHandler.convertParam(req, "authentication", Authentication.class);
            PartnerVO partner  = (PartnerVO)authenticationService.authentication(authentication).getData();
            String flight_keys = ParamHandler.convertParam(req, "flightIds", String.class);
            result = penaltyService.getPenalty(partner,flight_keys.split(","));
            System.out.println("总体耗时:" + (System.currentTimeMillis() - start) + "ms");
            return JSONObject.toJSONString(result);
        } catch (ParamException e) {
            return ExceptionProcessor.getParamExceptionResult(e);
        } catch (BusinessException e) {
            return ExceptionProcessor.getBusinessExceptionResult(e);
        } catch (Exception e) {
            return ExceptionProcessor.getExceptionResult(e);
        }

    }
}
