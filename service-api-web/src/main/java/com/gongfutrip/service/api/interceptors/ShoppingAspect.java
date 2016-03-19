package com.gongfutrip.service.api.interceptors;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

@Component
@Aspect
public class ShoppingAspect {
  
  @Pointcut("@annotation(com.gongfutrip.service.api.interceptors.LimitFilter)")
  public void limitPoint(){}
  
  
  @Before("limitPoint()")
  public void beforShopping(JoinPoint point){
    HttpServletRequest request = ((ServletWebRequest) RequestContextHolder.getRequestAttributes()).getRequest();
    System.out.println(request.getParameter("param"));
    System.out.println(request.getParameter("param"));
    
  }
  
}
