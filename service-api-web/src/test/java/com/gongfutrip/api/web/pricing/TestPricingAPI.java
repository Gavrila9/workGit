package com.gongfutrip.api.web.pricing;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import junit.framework.TestCase;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gongfutrip.service.api.dto.PricingDTO;
import com.gongfutrip.service.api.interfaces.result.DefaultResult;
import com.gongfutrip.service.api.model.PricingResponse;
import com.gongfutrip.service.api.model.Response;
import com.gongfutrip.service.api.model.Solution;
import com.gongfutrip.service.api.web.TestParamHandler;



public class TestPricingAPI extends TestCase {

     private static String host = "http://localhost:8080";

    // private static String host = "http://192.168.1.135:8881";

//    private static String host = "http://api.pkfare.com/test";

    public static void main(String[] args) throws Exception {
        testPricingRandom(host);
    }

    /**随机取一条shopping记录进行pricing*/
    public static PricingResponse testPricingRandom(String host) throws Exception {
        Response shoppingResponse = TestShoppingAPI.getShopping(host);
        if (shoppingResponse == null) {
            return null;
        }
        String content = BaseTest.httpExecute(host, "/pricing?param=", getPricingParams(shoppingResponse));
        System.out.println("---------pricing result begin---------------");
        System.out.println(content);
        System.out.println("---------pricing result end---------------");
        DefaultResult pricingResult = JSON.parseObject(content, DefaultResult.class);
        if (pricingResult.getErrorCode().equals("0"))
            return JSON.parseObject(JSON.toJSONString(pricingResult.getData()), PricingResponse.class);
        System.out.println(pricingResult.getErrorMsg());
        return null;
    }


    private static String getPricingParams(Response shoppingResponse) throws UnsupportedEncodingException {
        // int size = shoppingResponse.getSolutions().size();
        // int luckyOne = RandomUtils.nextInt(0, size);
        // Solution solution = shoppingResponse.getSolutions().get(luckyOne);

        Solution solution = getHasStopSolution(shoppingResponse);



        PricingDTO pricingDTO = TestParamHandler.getPricingDTO(shoppingResponse, solution, 1);
        Map<String, Object> pricingParams = new HashMap<String, Object>();
        pricingParams.put("authentication", TestParamHandler.getAuth());
        pricingParams.put("pricing", pricingDTO);
        String jsonpricingParams = JSONObject.toJSONString(pricingParams);
        System.out.println("---------pricing param begin---------------");
        System.out.println(jsonpricingParams);
        System.out.println("---------pricing param end---------------");
        System.out.println(Base64.encodeBase64String(jsonpricingParams.getBytes("utf-8")));
        return Base64.encodeBase64String(jsonpricingParams.getBytes("utf-8"));
    }

    /** 
     * @Title: getHasStopSolution 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param res
     * @return 
     */
    private static Solution getHasStopSolution(Response res) {
        List<Solution> ss = res.getSolutions();
       for (Solution solution : ss) {
            /*Map<String, List<String>> jfs = solution.getJourneys();
            for (Entry<String, List<String>> jf : jfs.entrySet()) {

            }*/
//            if ("6aa1b50f3ff24a799a30767b181c8903".equals(solution.getSolutionId())) {
//                return solution;
//            }
       }
        return ss.get(new Random().nextInt(ss.size()));
    }
}
