package org.mindinformatics.gwt.domeo.plugins.persistence.json.unmarshalling;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.AnnotationFactory;
import org.mindinformatics.gwt.domeo.model.MAnnotationSet;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IDomeoOntology;
import org.mindinformatics.gwt.domeo.model.persistence.ontologies.IPavOntology;
import org.mindinformatics.gwt.domeo.plugins.persistence.json.model.JsAnnotationSet;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Note: If the native method returns undefined it means the field is not present
 * and that can be detected by comparing the result to null
 * 
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */ 
public class AnnotationSetJsonUnmarshaller extends AUnmarshaller implements IUnmarshaller {

	private IDomeo _domeo;
	
	public AnnotationSetJsonUnmarshaller(IDomeo domeo) { _domeo = domeo; }
	
	@Override
	public MAnnotationSet unmarshallWithoutValidating(JsonUnmarshallingManager manager, JavaScriptObject json) {
		return (MAnnotationSet) super.unmarshallWithoutValidating(manager, json);
	}
	
	@Override
	public MAnnotationSet unmarshall(JsonUnmarshallingManager manager, JavaScriptObject json, String validation) {
		// Triggers input validation (skip when validation is OFF: IUnmarshaller.OFF_VALIDATION)
		validation(json, validation);

		JsAnnotationSet jsonSet = (JsAnnotationSet) json;
		
		/**
		 * LineageUri, lastSaved, versionNumber, previousVersion can
		 * all be null if the Annotation Set has not been saved yet.
		 */
		MAnnotationSet set = AnnotationFactory.createAnnotationSet(
			jsonSet.getId(), jsonSet.getLineageUri(), 
			jsonSet.getFormattedLastSaved(), 
			jsonSet.getVersionNumber(), jsonSet.getPreviousVersion(), 
			_domeo.getPersistenceManager().getCurrentResource(), 
			jsonSet.getLabel(), jsonSet.getDescription());
		set.setType(jsonSet.getType());
		
		/*
		 * 	When the createdOn in input is null, the interpretation is that the
		 * 	Annotation Set has been generated at runtime and has not been saved
		 *  yet. Therefore we set the createdOn after import. 
		 */
		Date createdOn = jsonSet.getFormattedCreatedOn();
		if(createdOn==null) { createdOn = new Date(); }
		set.setCreatedOn(createdOn);
		
		/*
		 *  When the createdWith in input is null, the interpretation is that the
		 *  Annotation Set has been generated at runtime and has not been saved
		 *  yet. Therefore we set the createdWith after import with the value
		 *  of this tool
		 */
		if(jsonSet.getCreatedWith()!=null && jsonSet.getCreatedWith().trim()!="") {
			_domeo.getUnmarshaller().cacheForGeneratorAgentsLazyBinding(jsonSet.getCreatedWith(), null, jsonSet.getId());
		} else {
			set.setCreatedWith(_domeo.getAgentManager().getSoftware());
		}
		
		/*
		 *  When the createdBy in input is null, the interpretation is that the
		 *  Annotation Set has been generated at runtime and has not been saved
		 *  yet. Therefore we set the createdBy after import with the value
		 *  of the current user that triggered the process.
		 */
		if(jsonSet.getCreatedBy()!=null && jsonSet.getCreatedBy().trim()!="") {
			_domeo.getUnmarshaller().cacheForAgentsLazyBinding(jsonSet.getCreatedBy(), null, jsonSet.getId());
		} else {
			set.setCreatedBy(_domeo.getAgentManager().getUserPerson());
		}
		
		// Imports management, if the imports are not present it means the data 
		// have been generated by an agent that used this tool.
		if(jsonSet.getFormattedImportedOn()!=null) {
			set.setImportedOn(jsonSet.getFormattedImportedOn());
		}
		if(jsonSet.getImportedFrom()!=null && jsonSet.getImportedFrom().trim().length()>0) {
			_domeo.getUnmarshaller().cacheForImportedFromAgentsLazyBinding(jsonSet.getImportedFrom(), null, jsonSet.getId());
		}
		if(jsonSet.getImportedBy()!=null && jsonSet.getImportedBy().trim().length()>0) {
			_domeo.getUnmarshaller().cacheForImportedByAgentsLazyBinding(jsonSet.getImportedBy(), null, jsonSet.getId());
		}
		
		return set;
	}
	
	@Override
	protected void validation(JavaScriptObject json, String validation) {
		//verifyType(json, IDomeoOntology.annotationSet);
		
		JsAnnotationSet jsonSet = (JsAnnotationSet) json;
		Map<String, String> notNullableFieldsMap = new HashMap<String, String>();
		notNullableFieldsMap.put(IDomeoOntology.generalId, jsonSet.getId());
		notNullableFieldsMap.put(IDomeoOntology.generalType, jsonSet.getType());
		
		if(!jsonSet.getType().equals(IDomeoOntology.annotationSet) && 
				!jsonSet.getType().equals(IDomeoOntology.bibliographySet) &&
				!jsonSet.getType().equals(IDomeoOntology.discussionSet)) {
			_domeo.getLogger().warn(this, "validation", "Annotation Container (Set) not recognized: " + jsonSet.getType());
		}
		
		if(validation.equals(IUnmarshaller.IMPORT_VALIDATION)) {
			notNullableFieldsMap.put(IPavOntology.importedFrom, jsonSet.getImportedFrom());
			notNullableFieldsMap.put(IPavOntology.importedBy, jsonSet.getImportedBy());
			notNullableFieldsMap.put(IPavOntology.importedOn, jsonSet.getImportedOn());
		} else if(validation.equals(IUnmarshaller.LOAD_VALIDATION)) {
			notNullableFieldsMap.put(IPavOntology.createdOn, jsonSet.getCreatedOn());
			notNullableFieldsMap.put(IPavOntology.createdBy, jsonSet.getCreatedBy());
			notNullableFieldsMap.put(IPavOntology.createdWith, jsonSet.getCreatedWith());
			notNullableFieldsMap.put(IPavOntology.lineageUri, jsonSet.getLineageUri());
			notNullableFieldsMap.put(IPavOntology.lastSavedOn, jsonSet.getLineageUri());
			notNullableFieldsMap.put(IPavOntology.versionNumber, jsonSet.getVersionNumber());
		}
		completeValidation(notNullableFieldsMap);
	}
}