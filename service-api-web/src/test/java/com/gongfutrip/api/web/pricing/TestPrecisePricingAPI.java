package com.gongfutrip.api.web.pricing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gongfutrip.service.api.dto.PrecisePricingDTO;
import com.gongfutrip.service.api.interfaces.result.DefaultResult;
import com.gongfutrip.service.api.model.Flight;
import com.gongfutrip.service.api.model.PricingResponse;
import com.gongfutrip.service.api.model.Response;
import com.gongfutrip.service.api.model.Segment;
import com.gongfutrip.service.api.model.Solution;
import com.gongfutrip.service.api.web.TestParamHandler;

public class TestPrecisePricingAPI {

   //private static String host = "http://localhost:8080";

    // private static String host = "http://192.168.1.133:8881";
    // private static String host = "http://192.168.1.135:8881";

   private static String host = "http://api.pkfare.com/test_api";

    public static void main(String[] args) throws Exception {
//      String test = "{\"airline\":\"MF\",\"arrival\":\"FOC\",\"arrivalDate\":\"2015-01-04\",\"arrivalTime\":\"22:15\",\"departure\":\"HKG\",\"departureDate\":\"2015-01-04\",\"departureTime\":\"20:40\",\"flightNum\":\"8002\"}";
//      Map<String,Object> pricingParams = new HashMap<String, Object>();
//      pricingParams.put("authentication", TestParamHandler.getAuth());
//      pricingParams.put("pricing", test);
//      System.out.println(Base64.encodeBase64String(JSON.toJSONBytes(pricingParams)));
        testPrecisePricing(host);
    }


    public static PricingResponse testPrecisePricing(String host) throws Exception {
        Response res = TestShoppingAPI.getShopping(host);
        if (null == res) {
            System.out.println("shopping result is null");
            return null;
        }
        String param = getPrecisePricingParams(res);
        // String param =
        // "eyJhdXRoZW50aWNhdGlvbiI6eyJwYXJ0bmVySWQiOiJVOENWeEhlbllRMVI5UnM4OTd1OGg2Z1A2clE9Iiwic2lnbiI6ImVjZDk2YWM1OWUwYmMyZjc3ZWFlMTQ1MTU5MDQ2Y2M2In0sInByaWNpbmciOnsiYWR1bHRzIjoxLCJjaGlsZHJlbiI6MCwiaW5mYW50cyI6MCwiam91cm5leXMiOnsiam91cm5leV8wIjpbeyJhaXJsaW5lIjoiR0EiLCJhcnJpdmFsIjoiRFBTIiwiYXJyaXZhbERhdGUiOiIyMDE1LTA5LTExIiwiYXJyaXZhbFRpbWUiOiIyMDo0NSIsImJvb2tpbmdDb2RlIjoiViIsImRlcGFydHVyZSI6IkhLRyIsImRlcGFydHVyZURhdGUiOiIyMDE1LTA5LTExIiwiZGVwYXJ0dXJlVGltZSI6IjE1OjQ1IiwiZmxpZ2h0TnVtIjoiODU3In1dLCJqb3VybmV5XzEiOlt7ImFpcmxpbmUiOiJHQSIsImFycml2YWwiOiJIS0ciLCJhcnJpdmFsRGF0ZSI6IjIwMTUtMDktMjIiLCJhcnJpdmFsVGltZSI6IjE0OjQ1IiwiYm9va2luZ0NvZGUiOiJTIiwiZGVwYXJ0dXJlIjoiRFBTIiwiZGVwYXJ0dXJlRGF0ZSI6IjIwMTUtMDktMjIiLCJkZXBhcnR1cmVUaW1lIjoiMDk6NDAiLCJmbGlnaHROdW0iOiI4NTYifV19fX0=";

        String content = BaseTest.httpExecute(host, "/precisePricing", param);
        System.out.println("---------PrecisePricing result begin---------------");
        System.out.println(content);
        System.out.println("---------PrecisePricing result end---------------");
        DefaultResult pricingResult = JSON.parseObject(content, DefaultResult.class);
        if (pricingResult.getErrorCode().equals("0"))
            return JSON.parseObject(JSON.toJSONString(pricingResult.getData()), PricingResponse.class);
        System.out.println(pricingResult.getErrorMsg());
        return null;
    }

    private static String getPrecisePricingParams(Response response) throws Exception {

        PrecisePricingDTO pricingDTO = null;
        // shopping 后直接 preciseBooking
        Solution solution = null;

        // 获取随机的solution
        int size = response.getSolutions().size();
        int luckyOne = RandomUtils.nextInt(0, size);
        solution = response.getSolutions().get(luckyOne);

        // 指定获取的solution
         //solution = getSolution(response);

        Map<String, Flight> flights = new HashMap<String, Flight>();

        Map<String, List<String>> fks = solution.getJourneys();

        for (Entry<String, List<String>> entry : fks.entrySet()) {
            String key = entry.getKey();
            List<String> fids = entry.getValue();

            String flightId = null;
            // 获取指定的flight组合
          //  flightId = getFlightId(key, fids);
           flightId = getRandomFlightId(fids);

            List<Flight> rflights = response.getFlights();
            for (Flight flight : rflights) {
                if (flightId.equals(flight.getFlightId()) || flightId.equals(flight.getFlightId())) {
                    List<String> segIds = flight.getSegmengtIds();
                    List<Segment> segs = new ArrayList<Segment>();
                    for (String segId : segIds) {
                        List<Segment> ss = response.getSegments();
                        for (Segment segment : ss) {
                            if (segId.equals(segment.getSegmentId())) {
                                segs.add(segment);
                            }
                        }
                    }
                    flight.setSegments(segs);
                    flights.put(key, flight);
                }
            }
        }



        pricingDTO = TestParamHandler.getPrecisePricingDTO(solution, flights);
        Map<String, Object> pricingParams = new HashMap<String, Object>();
        pricingParams.put("authentication", TestParamHandler.getAuth());
        pricingParams.put("pricing", pricingDTO);
        String jsonpricingParams = JSONObject.toJSONString(pricingParams);
        System.out.println("---------PrecisePricing param begin---------------");
        System.out.println(jsonpricingParams);
        System.out.println("---------PrecisePricing param end---------------");
        System.out.println(Base64.encodeBase64String(jsonpricingParams.getBytes("utf-8")));
        return Base64.encodeBase64String(jsonpricingParams.getBytes("utf-8"));
    }


    /** 
     * @Title: getRandomFlightId 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param fids
     * @return 
     */
    private static String getRandomFlightId(List<String> fids) {
        int size = fids.size();
        int luckyOne = RandomUtils.nextInt(0, size);
        return fids.get(luckyOne);
    }


    /** 
     * @Title: getHasStopSolution 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param res
     * @return 
     */
   private static Solution getSolution(Response res) {
        List<Solution> ss = res.getSolutions();
        return ss.get(new Random().nextInt(ss.size()));
//        for (Solution solution : ss) {
//            if ("b6792f28ae7ce93831e6cb916b2b60af".equals(solution.getSolutionId())) {
//                return solution;
//            }
//        }
    }
}
