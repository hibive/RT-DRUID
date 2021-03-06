/*
 * generated by Xtext
 */
package com.eu.evidence.rtdruid.oil.xtext;

import org.eclipse.xtext.resource.EObjectAtOffsetHelper;
import org.eclipse.xtext.resource.ILocationInFileProvider;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.validation.IDiagnosticConverter;

import com.eu.evidence.rtdruid.oil.xtext.parser.EObjectAtIncludedOffsetHelper;
import com.eu.evidence.rtdruid.oil.xtext.parser.IncldeLocationInFileProvider;
import com.eu.evidence.rtdruid.oil.xtext.parser.MainOilParser;
import com.eu.evidence.rtdruid.oil.xtext.parser.MyResource;
import com.eu.evidence.rtdruid.oil.xtext.parser.OilIncludedLexer;
import com.eu.evidence.rtdruid.oil.xtext.validation.OilDiagnosticConverter;

/**
 * Use this class to register components to be used at runtime / without the Equinox extension registry.
 */
public class OilRuntimeModule extends AbstractOilRuntimeModule {

//	@Override
//	public Class<? extends org.eclipse.xtext.parser.IParser> bindIParser() {
//		return com.eu.evidence.rtdruid.oil.xtext.parser.antlr.OilParser.class;
//	}
	
	
	@Override
	public Class<? extends org.eclipse.xtext.parser.antlr.Lexer> bindLexer() {
		return OilIncludedLexer.class;
		//com.eu.evidence.rtdruid.oil.xtext.parser.antlr.internal.InternalOilLexer.class;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public com.google.inject.Provider<com.eu.evidence.rtdruid.oil.xtext.parser.antlr.internal.InternalOilLexer> provideInternalOilLexer() {
		return (com.google.inject.Provider) org.eclipse.xtext.parser.antlr.LexerProvider.create(OilIncludedLexer.class);
		//com.eu.evidence.rtdruid.oil.xtext.parser.antlr.internal.InternalOilLexer.class);
	}
	
	public com.google.inject.Provider<OilIncludedLexer> provideOilIncludedLexer() {
		return org.eclipse.xtext.parser.antlr.LexerProvider.create(OilIncludedLexer.class);
		//com.eu.evidence.rtdruid.oil.xtext.parser.antlr.internal.InternalOilLexer.class);
	}

	@Override
	public void configureRuntimeLexer(com.google.inject.Binder binder) {
		binder.bind(org.eclipse.xtext.parser.antlr.Lexer.class)
				.annotatedWith(com.google.inject.name.Names.named(org.eclipse.xtext.parser.antlr.LexerBindings.RUNTIME))
				.to(OilIncludedLexer.class);
						//com.eu.evidence.rtdruid.oil.xtext.parser.antlr.internal.InternalOilLexer.class);
	}
	
	
	public  Class<? extends ILocationInFileProvider> bindILocationInFileProvider() {
		return IncldeLocationInFileProvider.class;
		//com.eu.evidence.rtdruid.oil.xtext.parser.antlr.internal.InternalOilLexer.class);
	}

	public Class<? extends EObjectAtOffsetHelper> bindEObjectAtOffsetHelper() {
		return EObjectAtIncludedOffsetHelper.class;
	}
	
	@Override
	public Class<? extends XtextResource> bindXtextResource() {
		return MyResource.class;//LazyLinkingResource.class;
	}

	@Override
	public Class<? extends org.eclipse.xtext.parser.IParser> bindIParser() {
		return MainOilParser.class;
	}
	
	public Class<? extends IDiagnosticConverter> bindIDiagnosticConverter() {
		return OilDiagnosticConverter.class;
	}
}


