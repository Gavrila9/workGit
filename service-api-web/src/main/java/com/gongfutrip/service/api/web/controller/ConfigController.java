/***************************************************************************************************
 *   @Title: ReloadLogConfigController.java                                                                             *
 *   @Date:  2015年7月18日 下午6:46:51                                                                 *
 *   @Since: JDK 1.7                                                                               *
 *   @Copyright: All material herein ©2015 Marco Polo Technology Co., Ltd, all rights reserved     *
 * *********************************************************************************************** *
 *    注意： 本内容仅限于深圳马可孛罗科技有限公司内部使用，禁止转发													   *
 ***************************************************************************************************/
package com.gongfutrip.service.api.web.controller;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.ModelAndView;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.util.StatusPrinter;

import com.gongfutrip.redis.RedisUtil;
import com.gongfutrip.service.api.core.constant.CacheSalt;
import com.gongfutrip.service.api.core.model.RouteQueryLimit;
import com.gongfutrip.service.api.core.service.RouteQueryLimitService;

/**
 * @ClassName: ReloadLogConfigController
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Jango
 * @date 2015年7月18日 下午6:46:51
 * @since JDK 1.7
 */
@Controller
@RequestMapping(value = "/config")
public class ConfigController {
    private static Logger logger = LoggerFactory.getLogger(ConfigController.class);

    @Resource(name = "routeQueryLimitService")
    private RouteQueryLimitService routeQueryLimitService;

    @ResponseBody
    @RequestMapping(value = "/reloadLogConfig")
    public Object reloadLogConfig(HttpServletRequest request) {
        try {
            ServletContext servletContext = request.getSession().getServletContext();
            WebApplicationContext webApplicationContext =
                    (WebApplicationContext) servletContext
                            .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
            org.springframework.core.io.Resource res = webApplicationContext.getResource("classpath:logback.xml");
            File externalConfigFile = res.getFile();
            LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
            if (externalConfigFile.exists() && externalConfigFile.isFile() && externalConfigFile.canRead()) {
                JoranConfigurator configurator = new JoranConfigurator();
                configurator.setContext(lc);
                lc.reset();
                configurator.doConfigure(externalConfigFile.getAbsolutePath());
                StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
                logger.info("Reload logback config success. ");
                return "OK !";
            }
        } catch (Exception e) {
            logger.error("重新加载日志配置文件时异常。", e);
        }
        return "FAIL !";
    }

    @ResponseBody
    @RequestMapping(value = "/refreshLimitInfoCache")
    public Object refreshLimitInfoCache(Long gtpInfoId, Integer groupId, String source) {
        try {
            String key = new StringBuilder(CacheSalt.LIMIT_SALT).append(source).append(gtpInfoId).toString();
            @SuppressWarnings("unchecked")
            Map<String, List<RouteQueryLimit>> limits =
                    (Map<String, List<RouteQueryLimit>>) RedisUtil.getJedisX().hGetObject(key, groupId.toString());
            if (null != limits && limits.size() > 0) {
                RedisUtil.getJedisX().hDelete(key, groupId.toString());
            }
            limits = this.routeQueryLimitService.getRouteQueryLimits(gtpInfoId, groupId, source);
            if (null != limits && limits.size() > 0) {
                logger.info("limits size:{}", limits.size());
            } else {
                logger.info("Cache is null.");
            }
            return "OK";
        } catch (Exception e) {
            logger.error("重新加载日志配置文件时异常。", e);
        }
        return "FAIL !";
    }


    @ResponseBody
    @RequestMapping(value = "/refreshWorkConfig")
    public ModelAndView refreshWorkConfig(String start, String end, String strSwitch, String nextDay) {
        try {
            if ("ON".equals(strSwitch)) {
                RedisUtil.getJedisX().setString(DigestUtils.md5Hex("OUT-OF-WORK-TIME-START-SHOPPIN"), 0, start);
                RedisUtil.getJedisX().setString(DigestUtils.md5Hex("OUT-OF-WORK-TIME-END-SHOPPIN"), 0, end);
            } else if ("OFF".equals(strSwitch)) {
                RedisUtil.getJedisX().delete(DigestUtils.md5Hex("OUT-OF-WORK-TIME-START-SHOPPIN"));
                RedisUtil.getJedisX().delete(DigestUtils.md5Hex("OUT-OF-WORK-TIME-END-SHOPPIN"));
                RedisUtil.getJedisX().delete(DigestUtils.md5Hex("OUT-OF-WORK-TIME-NEXT_DAY-SHOPPIN"));
            }
            if (StringUtils.isNotBlank(nextDay)) {
                RedisUtil.getJedisX().setString(DigestUtils.md5Hex("OUT-OF-WORK-TIME-NEXT_DAY-SHOPPIN"), 0, nextDay);
            }
            if (StringUtils.isEmpty(start)) {
                start = RedisUtil.getJedisX().getString(DigestUtils.md5Hex("OUT-OF-WORK-TIME-START-SHOPPIN"));
            }
            if (StringUtils.isEmpty(end)) {
                end = RedisUtil.getJedisX().getString(DigestUtils.md5Hex("OUT-OF-WORK-TIME-END-SHOPPIN"));
            }
            if (StringUtils.isEmpty(nextDay)) {
                nextDay = RedisUtil.getJedisX().getString(DigestUtils.md5Hex("OUT-OF-WORK-TIME-NEXT_DAY-SHOPPIN"));
            }
            Map<String, String> configs = new HashMap<String, String>();
            configs.put("start", start);
            configs.put("end", end);
            configs.put("nextDay", nextDay);
            configs.put("strSwitch", strSwitch);
            return new ModelAndView("config", configs);
        } catch (Exception e) {
            logger.error("重新加载工作时间配置文件时异常。", e);
            return new ModelAndView("index");
        }
    }
}
