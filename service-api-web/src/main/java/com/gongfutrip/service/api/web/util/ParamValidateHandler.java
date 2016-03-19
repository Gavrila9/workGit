/***************************************************************************************************
 *   @Title: ParamValidateHandler.java                                                                             *
 *   @Date:  2015年7月14日 下午3:13:44                                                                 *
 *   @Since: JDK 1.7                                                                               *
 *   @Copyright: All material herein ©2015 Marco Polo Technology Co., Ltd, all rights reserved     * 
 * *********************************************************************************************** *
 *    注意： 本内容仅限于深圳马可孛罗科技有限公司内部使用，禁止转发													   *
 ***************************************************************************************************/
package com.gongfutrip.service.api.web.util;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gongfutrip.response.Result;
import com.gongfutrip.service.api.core.constant.CabinClass;
import com.gongfutrip.service.api.core.constant.ErrorCode;
import com.gongfutrip.service.api.core.constant.FlightType;
import com.gongfutrip.service.api.core.exception.ParamException;
import com.gongfutrip.service.api.interfaces.result.DefaultResult;

/** 
 * @ClassName: ParamValidateHandler 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author Jango 
 * @date 2015年7月14日 下午3:13:44 
 * @since JDK 1.7 
 */
public class ParamValidateHandler {

    public Result shoppingParamValidate(HttpServletRequest request) {
        String params = request.getParameter("params");
        if (params == null || params.isEmpty()) {
            throw new ParamException("params is empty.");
        }
        JSONObject jsonParams = JSONObject.parseObject(params);
        if (jsonParams == null || jsonParams.isEmpty()) {
            throw new ParamException("params is not a JSON format.");
        }
        JSONObject authJson = (JSONObject) jsonParams.get("authentication");
        if (authJson == null || authJson.isEmpty()) {
            throw new ParamException("authentication info is empty.");
        }

        JSONObject searchJson = (JSONObject) jsonParams.get("search");
        if (searchJson == null || searchJson.isEmpty()) {
            throw new ParamException("search params is empty.");
        }

        try {
            Integer adults = searchJson.getInteger("adults");
            Integer children = searchJson.getInteger("children");

            if ((adults == null || adults == 0) && (children == null || children == 0)) {
                throw new ParamException("There are a number of adults or children cannot be empty .");
            }
        } catch (JSONException e) {
            throw new ParamException("Number of adults or children must be numeric.", e);
        }

        String flightType = searchJson.getString("flightType");
        if (flightType == null || flightType.isEmpty()) {
            throw new ParamException("flightType must be not null .");
        }
        if (!FlightType.isFlightType(flightType)) {
            throw new ParamException("Invalid value of flightType .");
        }

        String cabin = searchJson.getString("cabin");
        if (cabin == null || cabin.isEmpty()) {
            throw new ParamException("cabin must be not null .");
        }
        if (!CabinClass.BUSINESS.getCode().equals(cabin) || !FlightType.ROUND_TRIP.getCode().equals(flightType)) {
            throw new ParamException("Invalid value of flightType .");
        }

        return new DefaultResult(ErrorCode.Basic.SUCCESS.getCode(), ErrorCode.Basic.SUCCESS.getMsg());
    }
}
