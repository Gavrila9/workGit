package com.gongfutrip.api.web.pricing;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gongfutrip.other.BeanUtil;
import com.gongfutrip.other.DateUtil;
import com.gongfutrip.service.api.dto.PrecisePricingDTO;
import com.gongfutrip.service.api.dto.SegmentDTO;
import com.gongfutrip.service.api.model.Flight;
import com.gongfutrip.service.api.model.Response;
import com.gongfutrip.service.api.model.Segment;
import com.gongfutrip.service.api.model.Solution;
import com.gongfutrip.service.api.web.TestParamHandler;

public class TestShoppingXPrecisePricingAPI {

     private static String host = "http://localhost:8080";

//    private static String host = "http://192.168.1.135:8881";
    // private static String host = "http://192.168.1.133:8881";

//    private static String host = "http://api.pkfare.com";
    
     public static void main(String[] args) throws Exception {
        Response response = TestShoppingAPI.getShopping(host);
        List<Solution> solutions = response.getSolutions();
        //组装pricing参数
        List<PrecisePricingDTO> dtos = buildSolutions(response);
        for(int i = 0 ; i < solutions.size() ; i ++){
            PrecisePricingDTO dto = dtos.get(i);
            Solution solution = solutions.get(i);
            HttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(host + "/precisePricing");
            Map<String, Object> pricingParams = new HashMap<String, Object>();
            pricingParams.put("authentication", TestParamHandler.getAuth());
            pricingParams.put("pricing", dto);
            String jsonpricingParams = JSONObject.toJSONString(pricingParams);
            String base64enpricingParams = Base64.encodeBase64String(jsonpricingParams.getBytes("utf-8"));
            NameValuePair nameValuePair = new BasicNameValuePair("param", base64enpricingParams);
            List<NameValuePair> parameters = new ArrayList<NameValuePair>();
            parameters.add(nameValuePair);
            HttpEntity entity = new UrlEncodedFormEntity(parameters);
            post.setEntity(entity);
            HttpResponse httpResponse = client.execute(post);
            String content = EntityUtils.toString(httpResponse.getEntity());
            JSONObject result = JSON.parseObject(content);
            Solution psolution = result.getJSONObject("data").getObject("solution", Solution.class);
            if(solution.getAdtFare().compareTo(psolution.getAdtFare()) != 0){
                System.out.println("shopping 票价 : "+solution.getAdtFare()+" precisePricing 票价 : "+psolution.getAdtFare());
                System.out.println("shopping 税费 : "+solution.getAdtTax()+" precisePricing 税费 : "+psolution.getAdtTax());
                System.out.println(JSON.toJSONString(dto));
                System.out.println(content);
                break;
            } else if(solution.getAdtTax().compareTo(psolution.getAdtTax()) != 0){
                System.out.println("shopping 票价 : "+solution.getAdtFare()+" precisePricing 票价 : "+psolution.getAdtFare());
                System.out.println("shopping 税费 : "+solution.getAdtTax()+" precisePricing 税费 : "+psolution.getAdtTax());
                System.out.println(JSON.toJSONString(dto));
                System.out.println(content);
                break;
            } else {
                System.out.println("shopping价格一致 : shopping 票价 :"+solution.getAdtFare()+" precisePricing 票价 : "+psolution.getAdtFare()+" shopping 税费 : "+solution.getAdtTax()+" precisePricing 税费 : "+psolution.getAdtTax());
            }
        }
    }
 
    public static List<PrecisePricingDTO> buildSolutions(Response response) throws Exception{
        List<Flight> flights = response.getFlights();
        Map<String, Flight> flightMap = new HashMap<String, Flight>();
        for(Flight flight : flights){
            flightMap.put(flight.getFlightId(), flight);
        }
        List<Segment> segments = response.getSegments();
        Map<String, Segment> segmentMap = new HashMap<String, Segment>();
        for(Segment segment : segments){
            segmentMap.put(segment.getSegmentId(), segment);
        }
     
        List<Solution> solutions = response.getSolutions();
        List<PrecisePricingDTO> precisePricingDTOs = new ArrayList<PrecisePricingDTO>(solutions.size());
        
        for(Solution solution : solutions){
            PrecisePricingDTO precisePricingDTO = new PrecisePricingDTO();
            Map<String, List<SegmentDTO>> preciseJourneys = new HashMap<String, List<SegmentDTO>>();
            Map<String, List<String>> journeys = solution.getJourneys();
            List<List<String>> list = new ArrayList<List<String>>();
            list.add(journeys.get("journey_0"));
            if(journeys.containsKey("journey_1")){
                list.add(journeys.get("journey_1"));
            }
            List<List<String>> result = new ArrayList<List<String>>();
            cartesianProduct(list,0,new ArrayList<String>(20),result);
            
            for(List<String> flightIds : result){
                for(int i = 0 ; i < flightIds.size() ; i ++){
                    String flightId = flightIds.get(i);
                    Flight flight = flightMap.get(flightId);
                    List<String> segmentIds = flight.getSegmengtIds();
                    List<SegmentDTO> preciseSegments = new ArrayList<SegmentDTO>(segmentIds.size());
                    for(String segmentId : segmentIds){
                      SegmentDTO preciseSegment = new SegmentDTO();
                        Segment segment = segmentMap.get(segmentId);
                        preciseSegment.setAirline(segment.getAirline());
                        preciseSegment.setDeparture(segment.getDeparture());
                        preciseSegment.setArrival(segment.getArrival());
                        Long dl = segment.getDepartureDate();
                        Long al = segment.getArrivalDate();
                        Calendar dc = Calendar.getInstance();
                        dc.setTimeInMillis(dl);
                        Calendar ac = Calendar.getInstance();
                        ac.setTimeInMillis(al);
                        preciseSegment.setDepartureDate(DateUtil.format(dc.getTime(), "yyyy-MM-dd"));
                        preciseSegment.setDepartureTime(DateUtil.format(dc.getTime(), "HH:mm"));
                        preciseSegment.setArrivalDate(DateUtil.format(ac.getTime(), "yyyy-MM-dd"));
                        preciseSegment.setArrivalTime(DateUtil.format(ac.getTime(), "HH:mm"));
                        
//                        preciseSegment.setDepartureDate(segment.getStrDepartureDate());
//                        preciseSegment.setArrivalDate(segment.getStrArrivalDate());
//                        preciseSegment.setDepartureTime(segment.getStrDepartureTime());
//                        preciseSegment.setArrivalTime(segment.getStrArrivalTime());
                        
                        preciseSegment.setBookingCode(segment.getBookingCode());
                        preciseSegment.setFlightNum(segment.getFlightNum());
                        preciseSegments.add(preciseSegment);
                    }
                    preciseJourneys.put("journey_"+i, preciseSegments);
                }
            }
            precisePricingDTO.setJourneys(preciseJourneys);
            precisePricingDTO.setAdults(1);
            precisePricingDTO.setChildren(0);
            precisePricingDTO.setInfants(0);
            precisePricingDTOs.add(precisePricingDTO);
        }
        return precisePricingDTOs;
    }
    
    public static void cartesianProduct(List<List<String>> list, int i, List<String> tmps, List<List<String>> result) throws Exception {
        List<String> currentList = list.get(i);
        for (String s : currentList) {
            if(tmps.size() > i){
                tmps.set(i, s);
            } else {
                tmps.add(s);
            }
            if (i < list.size() - 1) {
                cartesianProduct(list, i + 1, tmps, result);
            } else {
                List<String> rst = BeanUtil.clone(tmps);
                result.add(rst);
            }
        }
    }
    
}
