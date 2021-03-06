package org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.test;

import org.mindinformatics.gwt.domeo.component.textmining.src.ITextminingRequestCompleted;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.IBioPortalConnector;
import org.mindinformatics.gwt.domeo.plugins.resource.bioportal.service.IBioPortalItemsRequestCompleted;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class StandaloneBioPortalConnector implements IBioPortalConnector {

	@Override
	public void searchTerm(IBioPortalItemsRequestCompleted completionCallback,
			String textQuery, String virtualIds)
			throws IllegalArgumentException {
		completionCallback.returnTerms(TermsProviderMockup.getTerms());
	}

	@Override
	public void searchTerm(IBioPortalItemsRequestCompleted completionCallback,
			String textQuery, String virtualIds, int pageNumber, int pageSize)
			throws IllegalArgumentException {
		completionCallback.returnTerms(TermsProviderMockup.getTerms());
	}

	@Override
	public void textmine(
			ITextminingRequestCompleted completionCallback,
			String url, String textContent, boolean longestOnly,
			boolean wholeWordOnly, boolean filterNumbers,
			boolean withDefaultStopWords, boolean isStopWordsCaseSenstive,
			boolean scored, boolean withSynonyms)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}
}
