/***************************************************************************************************
 *   @Title: CancelController.java                                                                             *
 *   @Date:  2015年8月3日 下午3:19:48                                                                 *
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

import com.alibaba.fastjson.JSON;
import com.gongfutrip.codec.Base64_16;
import com.gongfutrip.response.Result;
import com.gongfutrip.service.api.core.constant.ErrorCode;
import com.gongfutrip.service.api.core.exception.ParamException;
import com.gongfutrip.service.api.core.model.PartnerVO;
import com.gongfutrip.service.api.core.service.AuthenticationService;
import com.gongfutrip.service.api.core.util.ExceptionProcessor;
import com.gongfutrip.service.api.dto.Authentication;
import com.gongfutrip.service.api.dto.CancelDTO;
import com.gongfutrip.service.api.interfaces.CancelService;
import com.gongfutrip.service.api.interfaces.result.DefaultResult;
import com.gongfutrip.service.api.web.util.LogUtils;
import com.gongfutrip.service.api.web.util.ParamHandler;

/** 
 * @ClassName: CancelController 
 * @Description:  
 * @author Jango 
 * @date 2015年8月3日 下午3:19:48 
 * @since JDK 1.7 
 */
@Controller
public class CancelController {

    @Resource(name = "cancelService")
    private CancelService cancelService;
    @Resource(name = "authenticationService")
    private AuthenticationService authenticationService;

    @ResponseBody
    @RequestMapping(value = "/cancel")
    public Object cancel(HttpServletRequest request) {
      Result result = null;
        try {
            long start = System.currentTimeMillis();
            Authentication authentication = ParamHandler.convertParam(request, "authentication", Authentication.class);
            CancelDTO cancelDTO = ParamHandler.convertParam(request, "cancel", CancelDTO.class);
            PartnerVO partner  = (PartnerVO)authenticationService.authentication(authentication).getData();
            result = cancelService.cancel(partner, cancelDTO);
            long end = System.currentTimeMillis();
            LogUtils.addToLog(request, result, end - start, cancelDTO.getOrderNum());
            return result;
        } catch (ParamException e) {
            return ExceptionProcessor.getParamExceptionResult(e);
        } catch (Exception e) {
            return new DefaultResult(ErrorCode.Basic.SYSTEM_ERROR);
        }
    }
}
