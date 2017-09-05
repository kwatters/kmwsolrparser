package com.kmwllc.solr.parser;

import org.apache.lucene.search.Query;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.request.SolrQueryRequest;
import org.apache.solr.search.QParser;
import org.apache.solr.search.SyntaxError;
import com.kmwllc.search.solr.client.Expression;
import com.kmwllc.search.solr.client.QueryUtils;
import com.thoughtworks.xstream.XStream;

/**
 * Hello world!
 *
 */
public class KMWQueryParser extends QParser {

  private XStream xstream = null;

  public KMWQueryParser(String qstr, SolrParams localParams, SolrParams params, SolrQueryRequest req) {
    super(qstr, localParams, params, req);
    xstream = QueryUtils.initXStream();
  }

  @Override
  public Query parse() throws SyntaxError {
    Expression queryExpression = null;
    try {
      queryExpression = (Expression)xstream.fromXML(qstr);
    } catch (Exception e) {
      // TODO: better error handling / reporting? for now just the underlying exception.
      throw new SyntaxError(e);
    }
    Query lucQuery = QueryUtils.toLuceneQuery(queryExpression, req);      
    return lucQuery;    
  }

}