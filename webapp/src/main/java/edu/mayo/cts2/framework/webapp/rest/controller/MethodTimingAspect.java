/*
 * Copyright: (c) 2004-2011 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.mayo.cts2.framework.webapp.rest.controller;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang.ArrayUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import edu.mayo.cts2.framework.model.exception.ExceptionFactory;
import edu.mayo.cts2.framework.webapp.rest.command.QueryControl;

/**
 * The Class MethodTimingAspect.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE)
public class MethodTimingAspect {
	
	private ExecutorService executorService = Executors.newCachedThreadPool();
	
	/**
	 * Execute.
	 *
	 * @param pjp the pjp
	 * @return the object
	 * @throws Throwable the throwable
	 */
	@Around("execution(public *"
	        + " edu.mayo.cts2.framework.webapp.rest.controller.*.*(..,edu.mayo.cts2.framework.webapp.rest.command.QueryControl,..))")
	    public Object execute(final ProceedingJoinPoint pjp) throws Throwable {

		QueryControl queryControl = null;
		
		//this should never happen
		if(ArrayUtils.isEmpty(pjp.getArgs())){
			throw new IllegalStateException("Pointcut failure!");
		}
		
		for(Object arg : pjp.getArgs()){
			if(arg.getClass() == QueryControl.class){
				queryControl = (QueryControl) arg;
				break;
			}
		}
		
		//this also should never happen
		if(queryControl == null){
			throw new IllegalStateException("Pointcut failure!");
		}
		
		Future<Object> future = this.executorService.submit(new Callable<Object>(){

			@Override
			public Object call() {
				try {
					return pjp.proceed();
				} catch (Throwable e) {
					
					if(e instanceof Error){
						throw (Error)e;
					}
					
					if(e instanceof RuntimeException){
						throw (RuntimeException)e;
					}
					
					throw new RuntimeException(e);
				}
			}
		});
		
		long time = queryControl.getTimelimit();

		try {
			if(time < 0){
				return future.get();
			} else {
				return future.get(time, TimeUnit.SECONDS);
			}
		} catch (ExecutionException e) {
			throw e.getCause();
		} catch (TimeoutException e) {
			throw ExceptionFactory.createTimeoutException(e.getMessage());
		}
	}
}
