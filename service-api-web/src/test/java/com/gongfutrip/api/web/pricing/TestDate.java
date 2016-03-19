//package com.gongfutrip.api.web.pricing;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.util.Calendar;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.apache.commons.codec.binary.Base64;
//import org.apache.http.client.ClientProtocolException;
//import org.junit.Test;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.gongfutrip.codec.SerializeUtils;
//import com.gongfutrip.service.api.dto.PrecisePricingDTO;
//import com.gongfutrip.service.api.dto.SearchSolutionDTO;
//import com.gongfutrip.service.api.dto.PrecisePricingDTO.PreciseSegment;
//import com.gongfutrip.service.api.interfaces.result.DefaultResult;
//import com.gongfutrip.service.api.model.Response;
//import com.gongfutrip.service.api.web.TestParamHandler;
//
//public class TestDate {
//    
//    private static String host = "http://localhost:8080";
//    
//    @Test
//    public void test() throws Exception{
//        
//        SearchSolutionDTO solutionDTO = TestParamHandler.getShoppingDTO();
//        Response response = send(solutionDTO);
//        
////        Response response = parse();
//        
//        String date1 = solutionDTO.getSearchAirLegs().get(0).getDepartureDate();
//        String date2 = solutionDTO.getSearchAirLegs().get(1).getDepartureDate();
//        
//        // 取shopping
//        //组装pricing参数
//        List<PrecisePricingDTO> dtos = TestShoppingXPrecisePricingAPI.buildSolutions(response);
//        
//        for(PrecisePricingDTO dto : dtos){
//            Map<String, List<PreciseSegment>> map = dto.getJourneys();
//            for(Entry<String, List<PreciseSegment>> entry : map.entrySet()){
//                String key = entry.getKey();
//                if(key.equals("journey_0")){
//                    String d = entry.getValue().get(0).getDepartureDate();
//                    if(!d.equals(date1)){
//                        System.out.println("wrong date");
//                    }
//                }
//                if(key.equals("journey_1")){
//                    String d = entry.getValue().get(0).getDepartureDate();
//                    if(!d.equals(date2)){
//                        System.out.println("wrong date");
//                    }
//                }
//            }
//        }
//        
//    }
//    
//    public static void main(String[] args) {
//        Long a = 1444191900000l;
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(a);
//        
//        
//    }
//    
//    private Response send(SearchSolutionDTO solutionDTO) throws Exception{
//        Map<String, Object> shoppingParams = new HashMap<String, Object>();
//        shoppingParams.put("authentication", TestParamHandler.getAuth());
//        shoppingParams.put("search", solutionDTO);
//        String jsonshoppingParams = JSONObject.toJSONString(shoppingParams);
//        
//        Response response = null;
//        byte[] content = BaseTest.httpExecuteForShopping(host, "/shopping", Base64.encodeBase64String(jsonshoppingParams.getBytes("utf-8")));
//        String result = new String(SerializeUtils.uncompress(content));
//        System.out.println("---------shopping result begin---------------");
//        System.out.println(result);
//        System.out.println("---------shopping result end---------------");
//        DefaultResult shoppingResult = JSON.parseObject(result, DefaultResult.class);
//        if (shoppingResult.getErrorCode().equals("0")) {
//            response = JSON.parseObject(JSON.toJSONString(shoppingResult.getData()), Response.class);
//        }
//        System.out.println(shoppingResult.getErrorMsg());
//        return response;
//    }
//    
//    private Response parse() throws Exception{
//        File file = new File("d://t.txt");
//        FileReader reader = new FileReader(file);
//        BufferedReader bufferedReader = new BufferedReader(reader);
//        StringBuilder a = new StringBuilder();
//        String line = null;
//        while((line = bufferedReader.readLine()) != null){
//            a.append(line);
//        }
//        DefaultResult result = JSON.parseObject(a.toString(), DefaultResult.class);
//        Response response = JSON.parseObject(JSON.toJSONString(result.getData()), Response.class);
//        return response;
//    }
//}
