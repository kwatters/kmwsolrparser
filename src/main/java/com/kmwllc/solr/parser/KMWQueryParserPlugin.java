package com.kmwllc.solr.parser;

import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.QParserPlugin;

public class KMWQueryParserPlugin extends QParserPlugin {

	// KMW Query Parser parser name
	public static final String NAME = "kmwparser";

	@Override
	public QParser createParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
		// return the kmw parser for this request.
		return new KMWQueryParser(qstr, localParams, params, req);
	}

}