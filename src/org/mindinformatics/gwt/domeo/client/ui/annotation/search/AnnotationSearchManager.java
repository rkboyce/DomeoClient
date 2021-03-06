package org.mindinformatics.gwt.domeo.client.ui.annotation.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.mindinformatics.gwt.domeo.client.IDomeo;
import org.mindinformatics.gwt.domeo.model.MAnnotation;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class AnnotationSearchManager {

	private HashMap<String, ASearchComponent> annotationSearchComponents = new HashMap<String, ASearchComponent>();

	public List<MAnnotation> search(IDomeo domeo, String accessFilter,
			Long setLocalId, String typeFilter, String textSearch) {
		//System.out.println("1**|textSearch:" + textSearch);
		List<MAnnotation> found = new ArrayList<MAnnotation>();
		for (MAnnotation annotation : domeo.getAnnotationPersistenceManager()
				.getAllAnnotations()) {
			ISearchComponent sc = domeo.getAnnotationSearchManager()
					.getAnnotationSearchComponent(domeo,
							annotation.getClass().getName());
			
			//System.out.println("0 |annotation name:" + annotation.getClass().getName());
			if (sc.filterBySet(annotation, setLocalId)) {
				//System.out.println("0 | 1");
				if (sc.filterByType(annotation, typeFilter)) {
					//System.out.println("0 | 2");
					if (sc.filterByAccess(annotation, accessFilter)) {
						//System.out.println("0 | 3");
						if (sc.filterByText(annotation, textSearch))
							found.add(annotation);
					}
				}
			}
		}
		return found;
	}

	public List<MAnnotation> search(IDomeo domeo, String accessFilter,
			String typeFilter, String textSearch) {
		//System.out.println("2**|textSearch:" + textSearch);
		List<MAnnotation> found = new ArrayList<MAnnotation>();
		for (MAnnotation annotation : domeo.getAnnotationPersistenceManager()
				.getAllAnnotations()) {
			ISearchComponent sc = domeo.getAnnotationSearchManager()
					.getAnnotationSearchComponent(domeo,
							annotation.getClass().getName());
			if (sc.filterByType(annotation, typeFilter)) {
				if (sc.filterByAccess(annotation, accessFilter)) {
					if (sc.filterByText(annotation, textSearch))
						found.add(annotation);
				}
			}
		}
		return found;
	}

	public List<MAnnotation> search(IDomeo domeo, String accessFilter,
			String textSearch) {
		//System.out.println("3**|textSearch:" + textSearch);
		List<MAnnotation> found = new ArrayList<MAnnotation>();
		for (MAnnotation annotation : domeo.getAnnotationPersistenceManager()
				.getAllAnnotations()) {
			ISearchComponent sc = domeo.getAnnotationSearchManager()
					.getAnnotationSearchComponent(domeo,
							annotation.getClass().getName());
			if (sc.filterByAccess(annotation, accessFilter)) {
				if (sc.filterByText(annotation, textSearch))
					found.add(annotation);
			}
		}
		return found;
	}

	public boolean registerAnnotationCard(String annotationName,
			ASearchComponent cardGenerator) {
		if (!annotationSearchComponents.containsKey(annotationName)) {
			annotationSearchComponents.put(annotationName, cardGenerator);
			return true;
		}
		return false;
	}

	public ASearchComponent getAnnotationSearchComponent(IDomeo domeo,
			String annotationName) {
		if (annotationSearchComponents.containsKey(annotationName)) {
			return annotationSearchComponents.get(annotationName);
		} else {
			domeo.getLogger().exception(this,
					"No search component found for: " + annotationName);
			domeo.getLogger().warn(this, "Default search component applied");
			return new ASearchComponent(domeo);
		}
	}
}
