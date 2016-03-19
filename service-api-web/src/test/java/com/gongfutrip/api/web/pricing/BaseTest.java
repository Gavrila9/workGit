/***************************************************************************************************
 *   @Title: BaseTest.java                                                                             *
 *   @Date:  2015年8月12日 上午10:39:00                                                                 *
 *   @Since: JDK 1.7                                                                               *
 *   @Copyright: All material herein ©2015 Marco Polo Technology Co., Ltd, all rights reserved     * 
 * *********************************************************************************************** *
 *    注意： 本内容仅限于深圳马可孛罗科技有限公司内部使用，禁止转发													   *
 ***************************************************************************************************/
package com.gongfutrip.api.web.pricing;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/** 
 * @ClassName: BaseTest 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author Jango 
 * @date 2015年8月12日 上午10:39:00 
 * @since JDK 1.7 
 */
public class BaseTest {

    public static String httpExecute(String host, String namespace, String param) throws UnsupportedEncodingException,
            IOException, ClientProtocolException {
        NameValuePair nameValuePair = new BasicNameValuePair("param", param);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(nameValuePair);

        HttpPost post = new HttpPost(host + namespace);
        HttpEntity entity = new UrlEncodedFormEntity(parameters);
        post.setEntity(entity);
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(post);
        return EntityUtils.toString(response.getEntity());
    }

    public static byte[] httpExecuteForShopping(String host, String namespace, String param)
            throws UnsupportedEncodingException, IOException, ClientProtocolException {
        NameValuePair nameValuePair = new BasicNameValuePair("param", param);
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(nameValuePair);

        HttpPost post = new HttpPost(host + namespace);
        //post.setHeader("Content-Type" ,"text/html;charset=utf8");
        HttpEntity entity = new UrlEncodedFormEntity(parameters);
        post.setEntity(entity);
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(post);
        return EntityUtils.toByteArray(response.getEntity());
    }
}
