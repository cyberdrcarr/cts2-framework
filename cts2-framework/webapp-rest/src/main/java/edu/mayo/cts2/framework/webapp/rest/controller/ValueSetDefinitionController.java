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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.mayo.cts2.framework.model.core.FilterComponent;
import edu.mayo.cts2.framework.model.directory.DirectoryResult;
import edu.mayo.cts2.framework.model.service.core.Query;
import edu.mayo.cts2.framework.model.service.exception.UnknownResourceReference;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinition;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionDirectory;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionDirectoryEntry;
import edu.mayo.cts2.framework.model.valuesetdefinition.ValueSetDefinitionMsg;
import edu.mayo.cts2.framework.service.command.Filter;
import edu.mayo.cts2.framework.service.command.Page;
import edu.mayo.cts2.framework.service.command.restriction.ValueSetDefinitionQueryServiceRestrictions;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionMaintenanceService;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionQueryService;
import edu.mayo.cts2.framework.service.profile.valuesetdefinition.ValueSetDefinitionReadService;

/**
 * The Class ValueSetDefinitionController.
 *
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@Controller
public class ValueSetDefinitionController extends AbstractServiceAwareController {

	@Cts2Service
	private ValueSetDefinitionQueryService valueSetDefinitionQueryService;
	
	@Cts2Service
	private ValueSetDefinitionReadService valueSetDefinitionReadService;
	
	@Cts2Service
	private ValueSetDefinitionMaintenanceService valueSetDefinitionMaintenanceService;

	/**
	 * Gets the value set definitions of value set.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @param page the page
	 * @param valueSetName the value set name
	 * @return the value set definitions of value set
	 */
	@RequestMapping(value={
			PATH_VALUESETDEFINITIONS_OF_VALUESET}, method=RequestMethod.GET)
	@ResponseBody
	public ValueSetDefinitionDirectory getValueSetDefinitionsOfValueSet(
			HttpServletRequest httpServletRequest,
			ValueSetDefinitionQueryServiceRestrictions restrictions,
			Filter filter,
			Page page,
			@PathVariable(VAR_VALUESETID) String valueSetName) {
		
		return this.getValueSetDefinitionsOfValueSet(
				httpServletRequest, 
				null,
				restrictions,
				filter,
				page, 
				valueSetName);
	}
	
	/**
	 * Gets the value set definitions of value set.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @param page the page
	 * @param valueSetName the value set name
	 * @return the value set definitions of value set
	 */
	@RequestMapping(value={
			PATH_VALUESETDEFINITIONS_OF_VALUESET}, method=RequestMethod.POST)
	@ResponseBody
	public ValueSetDefinitionDirectory getValueSetDefinitionsOfValueSet(
			HttpServletRequest httpServletRequest,
			@RequestBody Query query,
			ValueSetDefinitionQueryServiceRestrictions restrictions,
			Filter filter,
			Page page,
			@PathVariable(VAR_VALUESETID) String valueSetName) {
		
		restrictions.setValueset(valueSetName);
		
		return this.getValueSetDefinitions(httpServletRequest, query, restrictions, filter, page);
	}
	
	/**
	 * Does value set definition exist.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param valueSetName the value set name
	 * @param valueSetDefinitionDocumentUri the value set definition document uri
	 */
	@RequestMapping(value=PATH_VALUESETDEFINITION_OF_VALUESET_BYID, method=RequestMethod.HEAD)
	@ResponseBody
	public void doesValueSetDefinitionExist(
			HttpServletResponse httpServletResponse,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@PathVariable(VAR_VALUESETDEFINITIONID) String valueSetDefinitionDocumentUri) {
		
		boolean exists = this.valueSetDefinitionReadService.exists(valueSetDefinitionDocumentUri);
		
		this.handleExists(valueSetName, UnknownResourceReference.class, httpServletResponse, exists);
	}
	
	/**
	 * Gets the value set definitions of value set count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @param valueSetName the value set name
	 * @return the value set definitions of value set count
	 */
	@RequestMapping(value={
			PATH_VALUESETDEFINITIONS_OF_VALUESET}, method=RequestMethod.HEAD)
	@ResponseBody
	public void getValueSetDefinitionsOfValueSetCount(
			HttpServletResponse httpServletResponse,
			@RequestBody Query query,
			ValueSetDefinitionQueryServiceRestrictions restrictions,
			Filter filter,
			@PathVariable(VAR_VALUESETID) String valueSetName) {
		
		restrictions.setValueset(valueSetName);
		
		this.getValueSetDefinitionsCount(httpServletResponse, query, restrictions, filter);
	}
	
	/**
	 * Gets the value set definitions.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @param page the page
	 * @return the value set definitions
	 */
	@RequestMapping(value={
			PATH_VALUESETDEFINITIONS}, method=RequestMethod.POST)
	@ResponseBody
	public ValueSetDefinitionDirectory getValueSetDefinitions(
			HttpServletRequest httpServletRequest,
			@RequestBody Query query,
			ValueSetDefinitionQueryServiceRestrictions restrictions,
			Filter filter,
			Page page) {
		
		FilterComponent filterComponent = this.processFilter(filter, this.valueSetDefinitionQueryService);
	
		DirectoryResult<ValueSetDefinitionDirectoryEntry> directoryResult = 
			this.valueSetDefinitionQueryService.getResourceSummaries(
					query, 
					filterComponent, 
					restrictions, 
					page);
		
		ValueSetDefinitionDirectory directory = this.populateDirectory(
				directoryResult, 
				page, 
				httpServletRequest, 
				ValueSetDefinitionDirectory.class);
		
		return directory;
	}
	
	/**
	 * Gets the value set definitions count.
	 *
	 * @param httpServletResponse the http servlet response
	 * @param query the query
	 * @param restrictions the restrictions
	 * @param filter the filter
	 * @return the value set definitions count
	 */
	@RequestMapping(value={
			PATH_VALUESETDEFINITIONS}, method=RequestMethod.HEAD)
	@ResponseBody
	public void getValueSetDefinitionsCount(
			HttpServletResponse httpServletResponse,
			@RequestBody Query query,
			ValueSetDefinitionQueryServiceRestrictions restrictions,
			Filter filter) {
		
		FilterComponent filterComponent = this.processFilter(filter, this.valueSetDefinitionQueryService);
		
		int count =
			this.valueSetDefinitionQueryService.count(
					query, 
					filterComponent, 
					restrictions);
		
		this.setCount(count, httpServletResponse);
	}
	
	/**
	 * Gets the value set definition by name.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param valueSetName the value set name
	 * @param valueSetDefinitionDocumentUri the value set definition document uri
	 * @return the value set definition by name
	 */
	@RequestMapping(value={	
			PATH_VALUESETDEFINITION_OF_VALUESET_BYID
			},
		method=RequestMethod.GET)
	@ResponseBody
	public ValueSetDefinitionMsg getValueSetDefinitionByName(
			HttpServletRequest httpServletRequest,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@PathVariable(VAR_VALUESETDEFINITIONID) String valueSetDefinitionDocumentUri) {
		
		ValueSetDefinition valueSetDefinition = 
			this.valueSetDefinitionReadService.
				read(valueSetDefinitionDocumentUri);
		
		ValueSetDefinitionMsg msg = new ValueSetDefinitionMsg();
		msg.setValueSetDefinition(valueSetDefinition);
		
		msg = this.wrapMessage(msg, httpServletRequest);

		return msg;
	}
	
	/**
	 * Creates the value set definition.
	 *
	 * @param httpServletRequest the http servlet request
	 * @param valueSetDefinition the value set definition
	 * @param changeseturi the changeseturi
	 * @param valueSetName the value set name
	 * @param valueSetDefinitionDocumentUri the value set definition document uri
	 */
	@RequestMapping(value=PATH_VALUESETDEFINITION_OF_VALUESET_BYID, method=RequestMethod.PUT)
	@ResponseBody
	public void createValueSetDefinition(
			HttpServletRequest httpServletRequest,
			@RequestBody ValueSetDefinition valueSetDefinition,
			@RequestParam(required=false) String changeseturi,
			@PathVariable(VAR_VALUESETID) String valueSetName,
			@PathVariable(VAR_VALUESETDEFINITIONID) String valueSetDefinitionDocumentUri) {
			
		this.valueSetDefinitionMaintenanceService.createResource(changeseturi, valueSetDefinition);
	}
}