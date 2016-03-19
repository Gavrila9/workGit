/***************************************************************************************************
 *   @Title: TestBookingAPI.java                                                                             *
 *   @Date:  2015年8月11日 下午5:43:11                                                                 *
 *   @Since: JDK 1.7                                                                               *
 *   @Copyright: All material herein ©2015 Marco Polo Technology Co., Ltd, all rights reserved     * 
 * *********************************************************************************************** *
 *    注意： 本内容仅限于深圳马可孛罗科技有限公司内部使用，禁止转发													   *
 ***************************************************************************************************/
package com.gongfutrip.api.web.pricing;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gongfutrip.service.api.dto.BookingDTO;
import com.gongfutrip.service.api.dto.PreciseBookingDTO;
import com.gongfutrip.service.api.interfaces.result.DefaultResult;
import com.gongfutrip.service.api.model.BookingResponse;
import com.gongfutrip.service.api.model.Flight;
import com.gongfutrip.service.api.model.PricingResponse;
import com.gongfutrip.service.api.model.Response;
import com.gongfutrip.service.api.model.Segment;
import com.gongfutrip.service.api.model.Solution;
import com.gongfutrip.service.api.util.RedisUtil;
import com.gongfutrip.service.api.web.TestParamHandler;


/** 
 * @ClassName: TestBookingAPI 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author Jango 
 * @date 2015年8月11日 下午5:43:11 
 * @since JDK 1.7 
 */
public class TestBookingAPI {
    private static String host = "http://localhost:8080";

    // private static String host = "http://192.168.1.133:8881";

   // private static String host = "http://192.168.1.135:8881/test_api";

    // private static String host = "http://api.pkfare.com";

    public static void main(String[] args) throws Exception {

        // 调用顺序：shopping---->[从shopping结果中随机抽取组航班]pricing---->booking
        // BookingResponse res = testBooking(host);

        // 调用顺序：shopping---->[从shopping结果中随机抽取组航班]precisePricing---->preciseBooking
        BookingResponse res = testPreciseBooking(host);
        if (res != null) {
            // TestOrderPricingAPI.testOrderPricing(host, res.getOrderNum());
            // TestCancelAPI.testCancel(host, res.getOrderNum(), res.getVirtualPnr());
        }
        /*for (int i = 0; i < 15; i++) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        BookingResponse res = testPreciseBooking(host);
                        if (res != null) {
                            TestCancelAPI.testCancel(host, res.getOrderNum(), res.getVirtualPnr());
                        }
                    } catch (Exception e) {
                    }

                }
            }).start();
        }*/
    }

    /** 
     * @Title: testPreciseBooking 
     * @Description: TODO(这里用一句话描述这个方法的作用) 
     * @param host2
     * @return ;
     */
    public static BookingResponse testPreciseBooking(String host) throws Exception {
        // pricing 后直接 preciseBooking
        // PricingResponse res = TestPricingAPI.testPricingRandom(host);
        // precisePricing 后直接 preciseBooking
        PricingResponse res = TestPrecisePricingAPI.testPrecisePricing(host);
        // shopping 后直接 preciseBooking
        // Response res = TestShoppingAPI.getShopping(host);
        if (null == res) {
            System.out.println("preciseBooking or shopping result is null");
            return null;
        }
        //String param = getPreciseBookingParam(res);

         String param =
        		 "eyJhdXRoZW50aWNhdGlvbiI6eyJwYXJ0bmVySWQiOiJSa3NWU1g3UGZabTF5RjA0YWRCV1lzQ0Q3TTRcdTAwM2QiLCJzaWduIjoiMTdlNzA3YjVkOWRhZTg0NTkwNjVkYmIxMzllNWYxYmQifSwiYm9va2luZyI6eyJwYXNzZW5nZXJzIjpbeyJiaXJ0aGRheSI6IjE5OTItMDYtMDIiLCJjYXJkRXhwaXJlZERhdGUiOiIyMDMwLTA2LTA1IiwiY2FyZE51bSI6IksxMTE5MDg3NiIsImNhcmRUeXBlIjoiUCIsImZpcnN0TmFtZSI6IkNFU0hJIiwibGFzdE5hbWUiOiJMSSIsIm5hdGlvbmFsaXR5IjoiQ04iLCJwc2dUeXBlIjoiQURUIiwic2V4IjoiRiJ9LHsiYmlydGhkYXkiOiIyMDAxLTA5LTA1IiwiY2FyZEV4cGlyZWREYXRlIjoiMjAyNS0wOS0wNSIsImNhcmROdW0iOiJINzc3ODU1MjQiLCJjYXJkVHlwZSI6IlAiLCJmaXJzdE5hbWUiOiJDRVNISSIsImxhc3ROYW1lIjoiUkVOIiwibmF0aW9uYWxpdHkiOiJDTiIsInBzZ1R5cGUiOiJBRFQiLCJzZXgiOiJNIn1dLCJzb2x1dGlvbiI6eyJmYXJlVHlwZSI6IlBVQkxJQyIsImpvdXJuZXlzIjp7ImpvdXJuZXlfMCI6W3siYWlybGluZSI6IktBIiwiYXJyaXZhbCI6IkhLRyIsImFycml2YWxEYXRlIjoiMjAxNi0wMy0yMCIsImFycml2YWxUaW1lIjoiMDY6MzAiLCJib29raW5nQ29kZSI6IlYiLCJkZXBhcnR1cmUiOiJQRUsiLCJkZXBhcnR1cmVEYXRlIjoiMjAxNi0wMy0yMCIsImRlcGFydHVyZVRpbWUiOiIwMzowMCIsImZsaWdodE51bSI6Ijk5NyJ9LHsiYWlybGluZSI6IktBIiwiYXJyaXZhbCI6IlRQRSIsImFycml2YWxEYXRlIjoiMjAxNi0wMy0yMCIsImFycml2YWxUaW1lIjoiMDk6NDUiLCJib29raW5nQ29kZSI6IlYiLCJkZXBhcnR1cmUiOiJIS0ciLCJkZXBhcnR1cmVEYXRlIjoiMjAxNi0wMy0yMCIsImRlcGFydHVyZVRpbWUiOiIwODoxMCIsImZsaWdodE51bSI6IjQ4NiJ9XSwiam91cm5leV8xIjpbeyJhaXJsaW5lIjoiQ1giLCJhcnJpdmFsIjoiSEtHIiwiYXJyaXZhbERhdGUiOiIyMDE2LTAzLTI5IiwiYXJyaXZhbFRpbWUiOiIwNzo0NSIsImJvb2tpbmdDb2RlIjoiViIsImRlcGFydHVyZSI6IlRQRSIsImRlcGFydHVyZURhdGUiOiIyMDE2LTAzLTI5IiwiZGVwYXJ0dXJlVGltZSI6IjA2OjA1IiwiZmxpZ2h0TnVtIjoiNDYzIn0seyJhaXJsaW5lIjoiS0EiLCJhcnJpdmFsIjoiUEVLIiwiYXJyaXZhbERhdGUiOiIyMDE2LTAzLTI5IiwiYXJyaXZhbFRpbWUiOiIxODoyMCIsImJvb2tpbmdDb2RlIjoiViIsImRlcGFydHVyZSI6IkhLRyIsImRlcGFydHVyZURhdGUiOiIyMDE2LTAzLTI5IiwiZGVwYXJ0dXJlVGltZSI6IjE1OjAwIiwiZmxpZ2h0TnVtIjoiOTAyIn1dfX19fQ==";

        String content = BaseTest.httpExecute(host, "/preciseBooking", param);
        System.out.println("---------preciseBooking result begin---------------");
        System.out.println(content);
        System.out.println("---------preciseBooking result end---------------");
        DefaultResult bookingResult = JSON.parseObject(content, DefaultResult.class);
        if (bookingResult.getErrorCode().equals("0"))
            return JSON.parseObject(JSON.toJSONString(bookingResult.getData()), BookingResponse.class);
        System.out.println(bookingResult.getErrorMsg());
        return null;
    }

    public static BookingResponse testBooking(String host) throws Exception {

        PricingResponse res = TestPricingAPI.testPricingRandom(host);

        if (res == null) {
            System.out.println("pricing or shopping result is null");
            return null;
        }

        String content = BaseTest.httpExecute(host, "/booking", getBookingParam(res));
        System.out.println("---------booking result begin---------------");
        System.out.println(content);
        System.out.println("---------booking result end---------------");
        DefaultResult bookingResult = JSON.parseObject(content, DefaultResult.class);
        if (bookingResult.getErrorCode().equals("0"))
            return JSON.parseObject(JSON.toJSONString(bookingResult.getData()), BookingResponse.class);
        System.out.println(bookingResult.getErrorMsg());
        return null;
    }

    private static String getBookingParam(PricingResponse res) throws UnsupportedEncodingException {
        BookingDTO bookingDTO = TestParamHandler.getBookingDTO(res);
        Map<String, Object> pricingParams = new HashMap<String, Object>();
        pricingParams.put("authentication", TestParamHandler.getAuth());
        pricingParams.put("booking", bookingDTO);
        String jsonpricingParams = JSONObject.toJSONString(pricingParams);
        System.out.println("---------booking param begin---------------");
        System.out.println(jsonpricingParams);
        System.out.println("---------booking param end---------------");
        return Base64.encodeBase64String(jsonpricingParams.getBytes("utf-8"));
    }

    private static String getPreciseBookingParam(Object res) throws UnsupportedEncodingException {
        PreciseBookingDTO preciseBookingDTO = null;
        // shopping 后直接 preciseBooking
        Solution solution = null;
        /* if (res instanceof Response) {
             Response response = (Response) res;
             // 随机抽取一条记录
             int size = response.getSolutions().size();
             int luckyOne = RandomUtils.nextInt(0, size);
             solution = response.getSolutions().get(luckyOne);
             // 指定获取一条中转的solution
             // solution = getHasStopSolution(response);
         } else if (res instanceof PricingResponse) {
             PricingResponse response = (PricingResponse) res;
             solution = response.getSolution();
         } else {
             return null;
         }*/
        PricingResponse response = (PricingResponse) res;
        solution = response.getSolution();

        Map<String, List<String>> journeys = solution.getJourneys();

        Map<String, Flight> flights = new HashMap<String, Flight>();

        for (Entry<String, List<String>> entry : journeys.entrySet()) {
            String key = entry.getKey();
            List<String> fids = entry.getValue();
            for (String fid : fids) {
                List<Flight> rflights = response.getFlights();
                for (Flight flight : rflights) {
                    if (fid.equals(flight.getFlightId()) || fid.equals(flight.getFlightId())) {
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
        }

        preciseBookingDTO = TestParamHandler.getPreciseBookingDTO(solution, flights);

        Map<String, Object> pricingParams = new HashMap<String, Object>();
        pricingParams.put("authentication", TestParamHandler.getAuth());
        pricingParams.put("booking", preciseBookingDTO);
        String jsonpricingParams = JSONObject.toJSONString(pricingParams);

        System.out.println("---------preciseBooking param begin---------------");
        System.out.println(jsonpricingParams);
        System.out.println("---------preciseBooking param end---------------");
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
            Map<String, List<String>> jfs = solution.getJourneys();
            for (Entry<String, List<String>> jf : jfs.entrySet()) {
                // String key = jf.getKey();
                List<String> fs = jf.getValue();
                Flight f = (Flight) RedisUtil.getJedisX().getObject(fs.get(0));
                /*if (f.getTransferCount() == 0) {
                    return solution;
                }*/
                List<Segment> segs = f.getSegments();
                for (Segment seg : segs) {
                    if (seg.getAirline().equals("KA") && seg.getFlightNum().equals("902")) {
                        return solution;
                    }

                }
            }
        }
        return null;
    }
}
