/***************************************************************************************************
 *   @Title: TestOrderPricing.java                                                                             *
 *   @Date:  2015年9月11日 上午10:45:54                                                                 *
 *   @Since: JDK 1.8                                                                               *
 *   @Copyright: All material herein ©2015 Marco Polo Technology Co., Ltd, all rights reserved     * 
 * *********************************************************************************************** *
 *    注意： 本内容仅限于深圳马可孛罗科技有限公司内部使用，禁止转发													   *
 ***************************************************************************************************/
package com.gongfutrip.api.web.pricing;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSONObject;
import com.gongfutrip.service.api.dto.OrderPricingDTO;
import com.gongfutrip.service.api.web.TestParamHandler;

/** 
 * @ClassName: TestOrderPricing 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author Jango 
 * @date 2015年9月11日 上午10:45:54 
 * @since JDK 1.8 
 */
public class TestOrderPricingAPI {

    // private static String host = "http://localhost:8080";

    // private static String host = "http://192.168.1.133:8881";

    // private static String host = "http://192.168.1.135:8881";

    private static String host = "http://api.pkfare.com";

    public static void main(String[] args) throws Exception {
//        testOrderPricing(host, 1013590291);
//        
        System.out.println(TestOrderPricingAPI.getOrderPricingParams(1012918275L));
    }


//    public static void testOrderPricing(String host, long orderNum) throws Exception {
//
//        String param = getOrderPricingParams(orderNum);
//
//        String content = BaseTest.httpExecute(host, "/orderPricing", param);
//        System.out.println("---------PrecisePricing result begin---------------");
//        System.out.println(content);
//        System.out.println("---------PrecisePricing result end---------------");
//    }

    /** 
     * @Title: getOrderPricingParams 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @return 
     * @throws UnsupportedEncodingException 
     */
    public static String getOrderPricingParams(Long orderNum) throws UnsupportedEncodingException {

        OrderPricingDTO opDTO = new OrderPricingDTO();
        opDTO.setOrderNum(orderNum);

        Map<String, Object> pricingParams = new HashMap<String, Object>();
        pricingParams.put("authentication", TestParamHandler.getAuth());
        pricingParams.put("orderPricing", opDTO);
        String jsonpricingParams = JSONObject.toJSONString(pricingParams);
        System.out.println("---------PrecisePricing param begin---------------");
        System.out.println(jsonpricingParams);
        System.out.println("---------PrecisePricing param end---------------");
        return Base64.encodeBase64String(jsonpricingParams.getBytes("utf-8"));
    }
}
