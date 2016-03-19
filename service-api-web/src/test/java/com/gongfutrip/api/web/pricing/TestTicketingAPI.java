/***************************************************************************************************
 *   @Title: TestTicketingAPI.java                                                                             *
 *   @Date:  2015年8月28日 上午11:49:40                                                                 *
 *   @Since: JDK 1.7                                                                               *
 *   @Copyright: All material herein ©2015 Marco Polo Technology Co., Ltd, all rights reserved     * 
 * *********************************************************************************************** *
 *    注意： 本内容仅限于深圳马可孛罗科技有限公司内部使用，禁止转发													   *
 ***************************************************************************************************/
package com.gongfutrip.api.web.pricing;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gongfutrip.service.api.dto.TicketingDTO;
import com.gongfutrip.service.api.interfaces.result.DefaultResult;
import com.gongfutrip.service.api.model.TicketingResponse;
import com.gongfutrip.service.api.web.TestParamHandler;

/** 
 * @ClassName: TestTicketingAPI 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author Jango 
 * @date 2015年8月28日 上午11:49:40 
 * @since JDK 1.7 
 */
public class TestTicketingAPI {
//    private static String host = "http://localhost:8080";

    // private static String host = "http://192.168.1.135:8881";

     private static String host = "http://api.pkfare.com";


    private static long orderNum = 1017436284;

    public static void main(String[] args) throws Exception {
        ticketing(host);
    }

    public static TicketingResponse ticketing(String host) throws Exception {
        // 取shopping
        String content = BaseTest.httpExecute(host, "/ticketing", getTicketParam());

        System.out.println("---------ticketing result begin---------------");
        System.out.println(content);
        System.out.println("---------ticketing result end---------------");
        DefaultResult shoppingResult = JSON.parseObject(content, DefaultResult.class);
        if (shoppingResult.getErrorCode().equals("0")) {
            return JSON.parseObject(JSON.toJSONString(shoppingResult.getData()), TicketingResponse.class);
        }
        System.out.println(shoppingResult.getErrorMsg());
        return null;
    }

    private static String getTicketParam() throws Exception {
        /***组装ticketing参数*/
        Map<String, Object> ticketingParams = new HashMap<String, Object>();
        ticketingParams.put("authentication", TestParamHandler.getAuth());
        ticketingParams.put("ticketing", getTicketingDTO());
        String jsonTicketingParams = JSONObject.toJSONString(ticketingParams);
        System.out.println("---------ticketing param begin---------------");
        System.out.println(jsonTicketingParams);
        System.out.println("---------ticketing param begin---------------");
        return Base64.encodeBase64String(jsonTicketingParams.getBytes("utf-8"));
    }

    private static TicketingDTO getTicketingDTO() {
        TicketingDTO dto = new TicketingDTO();
        dto.setPnr("2UNUKP");
        dto.setOrderNum(orderNum);
        dto.setName("Jango/Wang");
        dto.setEmail("jango.wang@gongfutrip.com");
        dto.setTelNum("13249073855");
        return dto;
    }

}
