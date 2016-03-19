/***************************************************************************************************
 * @Title: ParamHandler.java *
 * @Date: 2015年7月6日 下午9:52:14 *
 * @Since: JDK 1.7 *
 * @Copyright: All material herein ©2015 Marco Polo Technology Co., Ltd, all rights reserved *
 *             ******
 *             ********************************************************************************
 *             ********* * 注意： 本内容仅限于深圳马可孛罗科技有限公司内部使用，禁止转发 *
 ***************************************************************************************************/
package com.gongfutrip.service.api.web.util;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gongfutrip.service.api.core.exception.ParamException;

/**
 * @ClassName: ParamHandler
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author Jango
 * @date 2015年7月6日 下午9:52:14
 * @since JDK 1.7
 */
public class ParamHandler {
  private static Logger logger = LoggerFactory.getLogger(ParamHandler.class);

  private static String decodeParamFromRequest(HttpServletRequest request) {
    String encodedParams = request.getParameter("param");
    if (encodedParams == null || encodedParams.isEmpty()) {
      throw new ParamException("Invalid parameters, can't find params from request.");
    }
    try {
      return new String(Base64.decodeBase64(encodedParams), "utf-8");
    } catch (UnsupportedEncodingException e) {
      logger.error("解码参数时发生异常.", e);
      throw new ParamException(
          "Invalid parameters, params decode error used base64 and encoding utf-8.");
    } catch (Exception e) {
      logger.error("转换参数时发生异常.param:{}", e, encodedParams);
      throw new ParamException("Invalid parameters.");
    }
  }

  /**
   * 提取参数
   * 
   * @param req
   * @param field
   * @param c
   * @return
   */
  public static <T> T convertParam(HttpServletRequest req, String field, Class<T> c) {
    String params = decodeParamFromRequest(req);
    try {
      JSONObject json = JSONObject.parseObject(params);
      T t = json.getObject(field, c);
      if (t == null) {
        throw new ParamException("field must be not null.");
      }
      return t;
    } catch (Exception e) {
      logger.error("转换参数时发生异常.param:{}", e, params);
      throw new ParamException("Invalid param.");
    }
  }

  /**
   * 提取集合参数无解码
   * 
   * @param req
   * @param field
   * @param c
   * @return
   */
  public static <T> T convertParamByArrNoDecode(HttpServletRequest req, String field, Class<T> c) {
    String params = (String)req.getAttribute(field);
    try {
      T t = JSONObject.parseObject(params, c);
      if (t == null) {
        throw new ParamException("field must be not null.");
      }
      return t;
    } catch (Exception e) {
      logger.error("转换参数时发生异常.param:{}", e, params);
      throw new ParamException("Invalid param.");
    }
  }

}
