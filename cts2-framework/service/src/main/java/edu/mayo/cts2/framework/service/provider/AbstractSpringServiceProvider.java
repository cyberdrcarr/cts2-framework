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
package edu.mayo.cts2.framework.service.provider;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import edu.mayo.cts2.framework.core.config.PluginConfig;
import edu.mayo.cts2.framework.service.profile.Cts2Profile;

/**
 * The Class AbstractSpringServiceProvider.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public abstract class AbstractSpringServiceProvider implements ServiceProvider {
	
	public static final String TEST_CONTEXT_SYSTEM_PROP = "test.context";
	
	private final Log log = LogFactory.getLog(getClass().getName());
	
	private ApplicationContext applicationContext;

	/* (non-Javadoc)
	 * @see edu.mayo.cts2.framework.service.provider.ServiceProvider#getService(java.lang.Class)
	 */
	public synchronized <T extends Cts2Profile> T getService(Class<T> serviceClass) {

		try {
			return this.applicationContext.getBean(serviceClass);
		} catch (BeansException e) {
			log.warn(e);
			return null;
		}
		
	}
	
	protected ApplicationContext buildParentApplicationContext(PluginConfig pluginConfig){
		
		GenericApplicationContext parent = new GenericApplicationContext();
		
		BeanDefinitionBuilder builder = 
				BeanDefinitionBuilder.
					genericBeanDefinition(RuntimePluginConfigFactory.class).
						addPropertyValue("pluginConfig", pluginConfig);
		    
        String beanName = WordUtils.uncapitalize(
				PluginConfig.class.getSimpleName());
        
        BeanDefinition def = 
        		builder.getBeanDefinition();
      
	    parent.registerBeanDefinition(
	    				beanName, 
	    				def);
	    
	    parent.refresh();
	    
	    return parent;
	}

	public void initialize(PluginConfig pluginConfig) {
		String prop = System.getProperty(TEST_CONTEXT_SYSTEM_PROP);

		ApplicationContext parent = this.buildParentApplicationContext(pluginConfig);

		if(BooleanUtils.toBoolean(prop)){
			log.warn("Using Test Context for: " + this.getClass().getCanonicalName());
			this.applicationContext = this.getIntegrationTestApplicationContext(parent);
		} else {
			this.applicationContext = this.getApplicationContext(parent);	
		}	
	}
	
	public static class RuntimePluginConfigFactory implements FactoryBean<PluginConfig> {

		private PluginConfig pluginConfig;
		
		public PluginConfig getObject() throws Exception {
			return this.pluginConfig;
		}

		public Class<?> getObjectType() {
			return PluginConfig.class;
		}

		public boolean isSingleton() {
			return true;
		}

		public PluginConfig getPluginConfig() {
			return pluginConfig;
		}

		public void setPluginConfig(PluginConfig pluginConfig) {
			this.pluginConfig = pluginConfig;
		}
	}
	
	public void destroy() {
		//
	}

	protected abstract ApplicationContext getApplicationContext(ApplicationContext parent);
	
	protected abstract ApplicationContext getIntegrationTestApplicationContext(ApplicationContext parent);

	protected ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
