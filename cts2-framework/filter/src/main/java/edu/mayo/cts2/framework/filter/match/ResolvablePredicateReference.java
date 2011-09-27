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
package edu.mayo.cts2.framework.filter.match;

import edu.mayo.cts2.framework.model.castor.MarshallSuperClass;
import edu.mayo.cts2.framework.model.core.PredicateReference;

/**
 * The Class ResolvablePredicateReference.
 *
 * @param <T> the generic type
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
public class ResolvablePredicateReference<T> extends PredicateReference implements MarshallSuperClass {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 5500382462242484409L;
	
	/** The attribute resolver. */
	private AttributeResolver<T> attributeResolver;
	
	/**
	 * Instantiates a new resolvable predicate reference.
	 *
	 * @param attributeResolver the attribute resolver
	 */
	public ResolvablePredicateReference(AttributeResolver<T> attributeResolver){
		this.attributeResolver = attributeResolver;
	}
	
	/**
	 * Gets the model attribute value.
	 *
	 * @param modelObject the model object
	 * @return the model attribute value
	 */
	public Iterable<String> getModelAttributeValue(T modelObject){
		return this.attributeResolver.resolveAttribute(modelObject);
	}
	
	/**
	 * To predicate reference.
	 *
	 * @param <T> the generic type
	 * @param ref the ref
	 * @param attributeResolver the attribute resolver
	 * @return the resolvable predicate reference
	 */
	public static <T> ResolvablePredicateReference<T> toPredicateReference(
			PredicateReference ref, AttributeResolver<T> attributeResolver){
		ResolvablePredicateReference<T> returnRef = 
			new ResolvablePredicateReference<T>(attributeResolver);

		returnRef.setName(ref.getName());
		returnRef.setNamespace(ref.getNamespace());
		returnRef.setUri(ref.getUri());
		returnRef.setHref(ref.getHref());
		
		return returnRef;
	}

}
