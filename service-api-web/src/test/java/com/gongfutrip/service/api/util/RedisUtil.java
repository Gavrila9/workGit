/***************************************************************************************************
 *   @Title: RedisUtil.java                                                                             *
 *   @Date:  2015年7月27日 下午8:42:59                                                                 *
 *   @Since: JDK 1.7                                                                               *
 *   @Copyright: All material herein ©2015 Marco Polo Technology Co., Ltd, all rights reserved     * 
 * *********************************************************************************************** *
 *    注意： 本内容仅限于深圳马可孛罗科技有限公司内部使用，禁止转发													   *
 ***************************************************************************************************/
package com.gongfutrip.service.api.util;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;

import com.gongfutrip.redis.JedisClusterX;


/** 
 * @ClassName: RedisUtil 
 * @Description: 
 * @author Jango 
 * @date 2015年7月27日 下午8:42:59 
 * @since JDK 1.7 
 */
public class RedisUtil {

    public static JedisClusterX getJedisX() {
        // return get_Local_JedisX();
        return get_Online_JedisX();

    }

    public static void main(String[] args) {
        // getJedisX().setString("Testaaa", 100, "aasdfsd");
        String val =
                "shiro_cache{\"empty\":false,\"primaryPrincipal\":133,\"realmNames\":[\"security.shiro.logic.MyShiroRealm_0\"]}";
        getJedisX().setString(val, 100, "aasdfsd");
        System.out.println(getJedisX().getString(val));

        // Long val = getJedisX().llen(DigestUtils.md5Hex("TO_BE_CANCELED_ORDERS"));

    }

    /**开发环境redis*/
    protected static JedisClusterX get_Local_JedisX() {
        HostAndPort h1 = new HostAndPort("192.168.1.135", 7000);
        HostAndPort h2 = new HostAndPort("192.168.1.135", 7001);
        HostAndPort h0 = new HostAndPort("192.168.1.135", 7002);
        HostAndPort h3 = new HostAndPort("192.168.1.133", 7000);
        HostAndPort h4 = new HostAndPort("192.168.1.133", 7001);
        HostAndPort h5 = new HostAndPort("192.168.1.133", 7002);
        Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
        hostAndPorts.add(h0);
        hostAndPorts.add(h1);
        hostAndPorts.add(h2);
        hostAndPorts.add(h3);
        hostAndPorts.add(h4);
        hostAndPorts.add(h5);
        return new JedisClusterX(hostAndPorts);
    }

    /**线上环境redis*/
    protected static JedisClusterX get_Online_JedisX() {
        HostAndPort h2 = new HostAndPort("121.40.34.207", 7001);
        HostAndPort h0 = new HostAndPort("121.40.34.207", 7002);
        HostAndPort h3 = new HostAndPort("121.40.34.207", 7000);
        HostAndPort h4 = new HostAndPort("121.43.111.204", 7000);
        HostAndPort h5 = new HostAndPort("121.43.111.204", 7002);
        HostAndPort h6 = new HostAndPort("121.43.111.204", 7002);
        Set<HostAndPort> hostAndPorts = new HashSet<HostAndPort>();
        hostAndPorts.add(h0);
        hostAndPorts.add(h2);
        hostAndPorts.add(h3);
        hostAndPorts.add(h4);
        hostAndPorts.add(h5);
        hostAndPorts.add(h6);
        return new JedisClusterX(hostAndPorts);
    }
}
