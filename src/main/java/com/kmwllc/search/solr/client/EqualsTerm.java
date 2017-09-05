package com.kmwllc.search.solr.client;

public class EqualsTerm extends Term {

  public EqualsTerm(String field, String term) {
    this(field, term, -1);
  }

  public EqualsTerm(String field, String term, float boost) {
    super(field, term, boost);
    //EqualsTerm.SOLN = "EQUALS(";
  }

  @Override
  public String toString() {
    // we need to escape the term for the sep
    String escaped = getTerm().replaceAll(SEP, "\\" + SEP);

    if ((escaped.contains(" ") || escaped.contains(",")) && !escaped.startsWith("\"") && !escaped.endsWith("\"")) {
      escaped = escaped.replace("\"", "\\\"");
      escaped = "\"" + escaped + "\"";
    }

    String boostTerm = "";
    if (getBoost() >= 0) {
      boostTerm = "," + getBoost();
    }
    return "" + getField() + SEP + "EQUALS(" + escaped + boostTerm + ")" + EOLN;
  }

}
