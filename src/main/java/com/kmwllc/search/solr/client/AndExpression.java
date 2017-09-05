package com.kmwllc.search.solr.client;

public class AndExpression extends Expression {

  public AndExpression() {
    super(Operator.AND);
  }
  
	public AndExpression(Term...terms) {
		super(Operator.AND);
		for (Term t : terms) {
		  getTerms().add(t);
		}
	}

}
