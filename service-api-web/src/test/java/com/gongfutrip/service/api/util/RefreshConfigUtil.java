/***************************************************************************************************
 *   @Title: RefreshConfigUtil.java                                                                             *
 *   @Date:  2015年9月11日 下午12:26:18                                                                 *
 *   @Since: JDK 1.8                                                                               *
 *   @Copyright: All material herein ©2015 Marco Polo Technology Co., Ltd, all rights reserved     * 
 * *********************************************************************************************** *
 *    注意： 本内容仅限于深圳马可孛罗科技有限公司内部使用，禁止转发													   *
 ***************************************************************************************************/
package com.gongfutrip.service.api.util;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

import com.gongfutrip.service.api.core.constant.CacheSalt;
import com.gongfutrip.service.api.core.model.PartnerVO;
import com.gongfutrip.service.api.core.model.RouteQueryLimit;
import com.gongfutrip.service.api.model.Solution;


/** 
 * @ClassName: RefreshConfigUtil 
 * @Description: TODO(这里用一句话描述这个类的作用) 
 * @author Jango 
 * @date 2015年9月11日 下午12:26:18 
 * @since JDK 1.8 
 */
public class RefreshConfigUtil extends RedisUtil {

    public static void main(String[] args) {
        // refreshPartnerInfo();

        // refreshRouteQueryLimit();

        // refreshShoppingSwitch();
        Solution s = (Solution) getJedisX().getObject("544be4432dcf059fe43cedb32a707c21");
        System.out.println(s.isEnableBooking());
    }

    /**
     * @Title: refreshPartnerInfo 
     * @Description: 刷新API Partner缓存信息
     */
    private static void refreshPartnerInfo() {
        String key =
                DigestUtils.md5Hex(new StringBuilder().append("U8CVxHenYQ1R9Rs897u8h6gP6rQ=").append(CacheSalt.PARTNER_SALT)
                        .toString());
        PartnerVO partner = (PartnerVO) getJedisX().getObject(key);
        System.out.println(partner.getTradeChannel());
        getJedisX().delete(key);

    }

    /**
     * @Title: refreshRouteQueryLimit 
     * @Description: 刷新采购商查询OD权限
     */
    private static void refreshRouteQueryLimit() {
        String key = new StringBuilder(CacheSalt.LIMIT_SALT).append("API").append(75).toString();
        List<RouteQueryLimit> limits = (List<RouteQueryLimit>) getJedisX().getObject(key);
        System.out.println(limits.size());
        // delete
        // System.out.println(getOnline_API_JedisX().delete(key));
    }

    private static void refreshShoppingSwitch() {
        String workTime = getJedisX().getString(DigestUtils.md5Hex("WORK-TIME-OF-SHOPPIN"));
        String outOfWorkTime = getJedisX().getString(DigestUtils.md5Hex("OUT-OF-WORK-TIME-OF-SHOPPIN"));

        System.out.println(workTime + " - - " + outOfWorkTime);

    }

}
