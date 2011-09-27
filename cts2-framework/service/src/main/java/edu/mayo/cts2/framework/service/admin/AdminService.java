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
package edu.mayo.cts2.framework.service.admin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import edu.mayo.cts2.framework.core.config.Cts2Config;

/**
 * The Class AdminService.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Component
public class AdminService {

	private Log log = LogFactory.getLog(getClass());

	@Resource
	private Cts2Config cts2Config;

	public void removePlugin(String pluginName) {
		File pluginFile = this.findPluginFile(pluginName);
		
		try {
			FileUtils.forceDelete(pluginFile);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Set<PluginDescription> getPluginDescriptions() {
		File pluginDirectory = new File(this.cts2Config.getPluginsDirectory());

		String currentPlugin = this.cts2Config.getProperty("provider.name");

		Set<PluginDescription> returnSet = new HashSet<PluginDescription>();

		File[] files = pluginDirectory.listFiles();

		if (ArrayUtils.isEmpty(files)) {
			log.warn("No Plugins Loaded.");
		} else {
			for (File plugin : pluginDirectory.listFiles()) {
				if (plugin.isDirectory()) {
					Properties props = this.getPluginProperties(plugin);

					String name = props.getProperty("provider.name");
					returnSet.add(new PluginDescription(name, props
							.getProperty("provider.version"), props
							.getProperty("provider.description"), name
							.equals(currentPlugin)));
				}
			}
		}
		return returnSet;
	}
	
	public void activatePlugin(PluginReference plugin) {
		this.cts2Config.setProperty("provider.name", plugin.getPluginName());	
		this.cts2Config.setProperty("provider.version", plugin.getPluginVersion());	
	}
	
	private File findPluginFile(String pluginName){
		File pluginDirectory = new File(this.cts2Config.getPluginsDirectory());
		File[] files = pluginDirectory.listFiles();
		
		if (! ArrayUtils.isEmpty(files)) {
			for (File plugin : pluginDirectory.listFiles()) {
				if (plugin.isDirectory()) {
					Properties props = this.getPluginProperties(plugin);

					String foundName = props.getProperty("provider.name");
					
					if(foundName.equals(pluginName)){
						return plugin;
					}
				}
			}
		}
		
		return null;
	}

	/**
	 * Install plugin.
	 * 
	 * @param source
	 *            the source
	 * @param destination
	 *            the destination
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void installPlugin(InputStream source, File destination)
			throws IOException {
		ZipInputStream zis = new ZipInputStream(source);
		byte[] buffer = new byte[1024];
		for (ZipEntry zip; (zip = zis.getNextEntry()) != null;) {
			File file = new File(destination, zip.getName());
			if (zip.isDirectory()) {
				file.mkdir();
			} else {
				FileOutputStream fos = new FileOutputStream(file);
				for (int length; (length = zis.read(buffer)) > 0;) {
					fos.write(buffer, 0, length);
				}
				fos.close();
			}
			zis.closeEntry();
		}
		zis.close();
	}

	private Properties getPluginProperties(File plugin) {
		Properties props = new Properties();
		try {
			props.load(new FileInputStream(new File(plugin.getPath()
					+ File.separator + "plugin.properties")));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		return props;
	}
}
