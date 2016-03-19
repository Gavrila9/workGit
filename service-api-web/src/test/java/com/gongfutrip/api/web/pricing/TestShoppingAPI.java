package com.gongfutrip.api.web.pricing;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.codec.binary.Base64;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gongfutrip.codec.SerializeUtils;
import com.gongfutrip.service.api.interfaces.result.DefaultResult;
import com.gongfutrip.service.api.model.Response;
import com.gongfutrip.service.api.web.TestParamHandler;

public class TestShoppingAPI {

//    private static String host = "http://localhost:8080";

//     private static String host = "http://192.168.1.133:8881";

//     private static String host = "http://192.168.1.135:8881";
	

    // private static String host = "http://120.197.100.90:8881";
    // private static String host = "http://121.40.196.142:8881";

     private static String host = "http://localhost:8080";

    private static ExecutorService service = Executors.newFixedThreadPool(1);

    public static void main(String[] args) throws Exception {
         getShopping(host);
        //
        // for (int j = 0; j < 120; j++) {
        // new Thread(() -> {
        // for (int i = 0; i < 10000; i++) {
        // try {
        // service.submit(new Runnable() {
        // @Override
        // public void run() {
        // try {
        // getShopping(host);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // }).get(8000, TimeUnit.MILLISECONDS);
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // }).start();
        // }
    }

    public static Response getShopping(String host) throws Exception {
      String param = getShoppingParams();
        //String param =
       //"eyJhdXRoZW50aWNhdGlvbiI6eyJwYXJ0bmVySWQiOiJSa3NWU1g3UGZabTF5RjA0YWRCV1lzQ0Q3TTQ9Iiwic2lnbiI6IjE3ZTcwN2I1ZDlkYWU4NDU5MDY1ZGJiMTM5ZTVmMWJkIn0sInNlYXJjaCI6eyJhZHVsdHMiOjEsImFpcmxpbmUiOiIiLCJjaGlsZHJlbiI6MCwiaW5mYW50cyI6MCwibm9uc3RvcCI6MCwic2VhcmNoQWlyTGVncyI6W3siY2FiaW5DbGFzcyI6IkVjb25vbXkiLCJkZXBhcnR1cmVEYXRlIjoiMjAxNi0wNS0wMSIsImRlc3RpbmF0aW9uIjoiU1lEIiwib3JpZ2luIjoiSEtHIn1dLCJzb2x1dGlvbnMiOjIwfX0=";
        // 取shopping
        byte[] content = BaseTest.httpExecuteForShopping(host, "/shopping", param);
        String result = null;
        try {
            result = new String(SerializeUtils.uncompress(content));
        } catch (Exception e) {
            result = new String(content);
        }
        System.out.println("---------shopping result begin---------------");
        System.out.println(result);
        System.out.println("---------shopping result end---------------");
        DefaultResult shoppingResult = JSON.parseObject(result, DefaultResult.class);
        if (shoppingResult.getErrorCode().equals("0")) {
            return JSON.parseObject(JSON.toJSONString(shoppingResult.getData()), Response.class);
        }
        System.out.println(shoppingResult.getErrorMsg());
        return null;
    }

    private static String getShoppingParams() throws UnsupportedEncodingException {
        Map<String, Object> shoppingParams = new HashMap<String, Object>();
        shoppingParams.put("authentication", TestParamHandler.getAuth());
        // shoppingParams.put("authentication", TestParamHandler.getTianTaiAuth());
        shoppingParams.put("search", TestParamHandler.getShoppingDTO());
        String jsonshoppingParams = JSONObject.toJSONString(shoppingParams);
        System.out.println("--------------shopping param begin----------------------------------------");
        System.out.println(jsonshoppingParams);
        System.out.println("--------------shopping param end----------------------------------------");
        return Base64.encodeBase64String(jsonshoppingParams.getBytes("utf-8"));
    }

}
