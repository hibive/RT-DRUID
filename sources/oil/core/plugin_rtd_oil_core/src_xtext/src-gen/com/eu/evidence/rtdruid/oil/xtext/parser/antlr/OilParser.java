/*
* generated by Xtext
*/
package com.eu.evidence.rtdruid.oil.xtext.parser.antlr;

import org.eclipse.xtext.parser.antlr.XtextTokenStream;

import com.eu.evidence.rtdruid.oil.xtext.services.OilGrammarAccess;
import com.google.inject.Inject;

public class OilParser extends org.eclipse.xtext.parser.antlr.AbstractAntlrParser {
	
	@Inject
	private OilGrammarAccess grammarAccess;
	
	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT", "RULE_INCLUDE");
	}
	
	@Override
	protected com.eu.evidence.rtdruid.oil.xtext.parser.antlr.internal.InternalOilParser createParser(XtextTokenStream stream) {
		return new com.eu.evidence.rtdruid.oil.xtext.parser.antlr.internal.InternalOilParser(stream, getGrammarAccess());
	}
	
	@Override 
	protected String getDefaultRuleName() {
		return "OilFile";
	}
	
	public OilGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}
	
	public void setGrammarAccess(OilGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
