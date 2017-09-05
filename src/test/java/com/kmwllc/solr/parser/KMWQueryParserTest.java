package com.kmwllc.solr.parser;

import java.io.BufferedOutputStream;
import java.io.OutputStreamWriter;

import org.apache.solr.SolrTestCaseJ4;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.request.SolrQueryRequest;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kmwllc.search.solr.client.AndExpression;
import com.kmwllc.search.solr.client.Expression;
import com.kmwllc.search.solr.client.GraphExpression;
import com.kmwllc.search.solr.client.QueryUtils;
import com.kmwllc.search.solr.client.Term;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;

/**
 * Unit test for simple App.
 */
//@Ignore
public class KMWQueryParserTest extends SolrTestCaseJ4 {

  @BeforeClass
  public static void beforeTests() throws Exception {
    initCore("solrconfig.xml","schema.xml", "solr", "graph");
  }

  @Test
  public void testKMWParser() throws Exception {
    // init xstream serializer       
    XStream xstream = QueryUtils.initXStream();
    buildIndex();
    // build the start node query
    AndExpression startNodes1 = new AndExpression(new Term("id", "doc_1"));
    // create a graph expression
    GraphExpression ge1 = new GraphExpression(startNodes1, "node_id", "edge_id", -1, null, true, false);

    // create a send root node query
    AndExpression startNodes2 = new AndExpression(new Term("id", "doc_8"));
    GraphExpression ge2 = new GraphExpression(startNodes2, "node_id", "edge_id", 2, null, true, false);
    // create an and of 2 expressions
    AndExpression graphAnd = new AndExpression();
    graphAnd.add(ge1);
    graphAnd.add(ge2);
    // debug 
    prettyPrint(xstream, graphAnd);
    // SolrQueryResponse qres = h.queryAndResponse(qr.getParams().get(CommonParams.QT), qr);
    SolrQueryRequest qr = createRequest("{!kmwparser}" + xstream.toXML(graphAnd));
    //String response = h.query(qr);
    //System.out.println(response);
    assertQ(qr,"//*[@numFound='4']");
    System.out.println("done.");

  }


  private void prettyPrint(XStream xstream, Expression exp) {
    BufferedOutputStream stdout = new BufferedOutputStream(System.out);
    xstream.marshal(exp, new PrettyPrintWriter(new OutputStreamWriter(stdout)));
    System.out.println("\n");
  }


  private void buildIndex() {
    assertU(adoc("id", "doc_1", "node_id", "1", "edge_id", "2", "text", "foo", "title", "foo10"));
    assertU(adoc("id", "doc_2", "node_id", "2", "edge_id", "3", "text", "foo"));
    assertU(commit());
    assertU(adoc("id", "doc_3", "node_id", "3", "edge_id", "4", "edge_id", "5", "table", "foo"));
    assertU(adoc("id", "doc_4", "node_id", "4", "table", "foo"));
    assertU(commit());
    assertU(adoc("id", "doc_5", "node_id", "5", "edge_id", "7", "table", "bar"));
    assertU(adoc("id", "doc_6", "node_id", "6", "edge_id", "3" ));
    assertU(adoc("id", "doc_7", "node_id", "7", "edge_id", "1" ));
    assertU(adoc("id", "doc_8", "node_id", "8", "edge_id", "1", "edge_id", "2" ));
    assertU(adoc("id", "doc_9", "node_id", "9"));
    assertU(commit());
    // update docs so they're in a new segment.
    assertU(adoc("id", "doc_1", "node_id", "1", "edge_id", "2", "text", "foo"));
    assertU(adoc("id", "doc_2", "node_id", "2", "edge_id", "3", "edge_id", "9", "text", "foo11"));
    assertU(commit());
    // a graph for testing traversal filter 10 - 11 -> (12 | 13)
    assertU(adoc("id", "doc_10", "node_id", "10", "edge_id", "11", "title", "foo"));
    assertU(adoc("id", "doc_11", "node_id", "11", "edge_id", "12", "edge_id", "13", "text", "foo11"));
    assertU(adoc("id", "doc_12", "node_id", "12", "text", "foo10"));
    assertU(adoc("id", "doc_13", "node_id", "13", "edge_id", "12", "text", "foo10"));  
    assertU(commit());
  }

  private SolrQueryRequest createRequest(String query) {
    SolrQueryRequest qr = req(query);
    NamedList<Object> par = qr.getParams().toNamedList();
    par.add("debug", "true");
    par.add("rows", "10");
    par.add("fl", "id,node_id,edge_id");
    // par.add("defType", "kmwparser");
    par.remove("qt");
    SolrParams newp = SolrParams.toSolrParams(par);
    qr.setParams(newp);
    return qr;
  }

}