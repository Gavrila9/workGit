/***************************************************************************************************
 *   @Title: TestCancelAPI.java                                                                             *
 *   @Date:  2015年8月27日 下午6:09:45                                                                 *
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
import com.gongfutrip.service.api.dto.CancelDTO;
import com.gongfutrip.service.api.interfaces.result.DefaultResult;
import com.gongfutrip.service.api.web.TestParamHandler;

/** 
 * @ClassName: TestCancelAPI 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author Jango 
 * @date 2015年8月27日 下午6:09:45 
 * @since JDK 1.7 
 */
public class TestCancelAPI {
    // private static String host = "http://localhost:8080";

    // private static String host = "http://192.168.1.133:8881";

    // private static String host = "http://192.168.1.135:8881";

    private static String host = "http://api.pkfare.com";

    public static void main(String[] args) throws Exception {

        testCancel(host, Long.valueOf(1012918452), null);

    }

    /** 
     * @Title: testPreciseBooking 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param host2
     * @return 
     */
    public static String testCancel(String host, Long orderNum, String virtualPnr) throws Exception {
      System.out.println(getCancelParam(orderNum, virtualPnr));
//        String content = BaseTest.httpExecute(host, "/cancel", getCancelParam(orderNum, virtualPnr));
//        System.out.println(" ----cancel result begin----------------  ");
//        System.out.println(content);
//        System.out.println(" ----cancel result end------------------ ");
//        DefaultResult bookingResult = JSON.parseObject(content, DefaultResult.class);
//        if (bookingResult.getErrorCode().equals("0"))
//            return JSON.parseObject(JSON.toJSONString(bookingResult.getData()), String.class);
//        System.out.println(bookingResult.getErrorMsg());
        return null;
    }

    /** 
     * @Title: getCancelParam 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param res
     * @return 
     * @throws Exception 
     */
    private static String getCancelParam(Long orderNum, String virtualPnr) throws Exception {
        CancelDTO dto = new CancelDTO();
        dto.setOrderNum(orderNum);
        dto.setPnr(virtualPnr);

        Map<String, Object> pricingParams = new HashMap<String, Object>();
        pricingParams.put("authentication", TestParamHandler.getAuth());
        pricingParams.put("cancel", dto);
        String jsonpricingParams = JSONObject.toJSONString(pricingParams);

        System.out.println(" ----cancel param begin----------------  ");
        System.out.println(jsonpricingParams);
        System.out.println(" ----cancel param end------------------ ");
        return Base64.encodeBase64String(jsonpricingParams.getBytes("utf-8"));
    }
}
