/***************************************************************************************************
 *   @Title: TestRefreshLogConfig.java                                                                             *
 *   @Date:  2015年9月1日 上午10:40:54                                                                 *
 *   @Since: JDK 1.7                                                                               *
 *   @Copyright: All material herein ©2015 Marco Polo Technology Co., Ltd, all rights reserved     * 
 * *********************************************************************************************** *
 *    注意： 本内容仅限于深圳马可孛罗科技有限公司内部使用，禁止转发													   *
 ***************************************************************************************************/
package com.gongfutrip.api.web.pricing;


/** 
 * @ClassName: TestRefreshLogConfig 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author Jango 
 * @date 2015年9月1日 上午10:40:54 
 * @since JDK 1.7 
 */
public class TestRefreshLogConfig {

    // private static String host = "http://localhost:8080";

    // private static String host = "http://192.168.1.133:8881";
    private static String host = "http://192.168.1.135:8881";

    // private static String host = "http://api.pkfare.com";

    public static void main(String[] args) throws Exception {
        refreshLogConfig(host);
    }

    private static void refreshLogConfig(String host2) throws Exception {
        String content = BaseTest.httpExecute(host, "/config/reloadLogConfig", null);
        System.out.println(content);
    }

}
