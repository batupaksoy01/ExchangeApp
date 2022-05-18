package batu.springframework.exchangeapp.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class ServiceAspect {
	
	@Before("execution(* batu.springframework.exchangeapp.service.*.*(..))")
	public void beforeServiceMethods(JoinPoint joinPoint) { 
		String logMessage = getTargetClassName(joinPoint) + "." + joinPoint.getSignature().getName() + 
				".start: params=" + Arrays.toString(joinPoint.getArgs());
		log.info(logMessage);
	}
	
	@Before("execution(* batu.springframework.exchangeapp.client.*.*(..))")
	public void beforeClientMethods(JoinPoint joinPoint) {
		String logMessage = getTargetClassName(joinPoint) + "." + joinPoint.getSignature().getName() + 
				".start: params=" + Arrays.toString(joinPoint.getArgs());
		log.info(logMessage);
	}
	
	@After("execution(* batu.springframework.exchangeapp.service.*.*(..))")
	public void afterServiceMethods(JoinPoint joinPoint) {
		String logMessage = getTargetClassName(joinPoint) + "." + joinPoint.getSignature().getName() + ".end";
		log.info(logMessage);
	}
	
	@After("execution(* batu.springframework.exchangeapp.client.*.*(..))")
	public void afterClientMethods(JoinPoint joinPoint) {
		String logMessage = getTargetClassName(joinPoint) + "." + joinPoint.getSignature().getName() + ".end";
		log.info(logMessage);
	}
	
	private String getTargetClassName(JoinPoint joinPoint) {
		String fullClassName = joinPoint.getTarget().getClass().getName();
		
		return fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
	}
}
