package com.eu.evidence.rtdruid.test.modules.oil.xtext.validation;

import com.eu.evidence.rtdruid.oil.xtext.model.OilFile;
import com.eu.evidence.rtdruid.test.modules.oil.xtext.OilInjectorProvider;
import com.eu.evidence.rtdruid.test.modules.oil.xtext.PackageProvider;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.junit4.InjectWith;
import org.eclipse.xtext.junit4.util.ParseHelper;
import org.eclipse.xtext.junit4.validation.ValidationTestHelper;
import org.eclipse.xtext.validation.Issue;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function0;
import org.eclipselabs.xtext.utils.unittesting.FluentIssueCollection;
import org.eclipselabs.xtext.utils.unittesting.XtextRunner2;
import org.eclipselabs.xtext.utils.unittesting.XtextTest;
import org.junit.Test;
import org.junit.runner.RunWith;

@SuppressWarnings("all")
@RunWith(XtextRunner2.class)
@InjectWith(OilInjectorProvider.class)
public class SplittedElementsTests extends XtextTest {
  private static String path = new Function0<String>() {
    public String apply() {
      String _computeBundleClassPath = PackageProvider.DEFAULT.computeBundleClassPath(com.eu.evidence.rtdruid.test.modules.oil.xtext.validation.SimpleTests.class);
      return _computeBundleClassPath;
    }
  }.apply();
  
  public SplittedElementsTests() {
    super(SplittedElementsTests.path);
  }
  
  @Inject
  private ParseHelper<OilFile> parser;
  
  @Inject
  private ValidationTestHelper tester;
  
  @Test
  public void undefinedElement() {
    try {
      {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("IMPLEMENTATION a {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("BOOLEAN [");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("INT32 b;");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("BOOLEAN d;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("},");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("FALSE");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("] a;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("ALARM_TYPE at;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("ALARM {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("};");
        _builder.newLine();
        _builder.append("CPU s {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS aa {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("undefined = 1;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("};");
        _builder.newLine();
        OilFile _parse = this.parser.parse(_builder);
        final OilFile model = _parse;
        Resource _eResource = model.eResource();
        List<Issue> _validate = this.tester.validate(model);
        ArrayList<String> _arrayList = new ArrayList<String>();
        FluentIssueCollection _fluentIssueCollection = new FluentIssueCollection(_eResource, _validate, _arrayList);
        this.issues = _fluentIssueCollection;
        FluentIssueCollection _errorsOnly = this.issues.errorsOnly();
        FluentIssueCollection _inLine = _errorsOnly.inLine(17);
        FluentIssueCollection _under = _inLine.under(com.eu.evidence.rtdruid.oil.xtext.model.Parameter.class);
        FluentIssueCollection _oneOfThemContains = _under.oneOfThemContains("Couldn\'t resolve reference");
        FluentIssueCollection _oneOfThemContains_1 = _oneOfThemContains.oneOfThemContains("\'undefined\'");
        this.assertConstraints(_oneOfThemContains_1);
        FluentIssueCollection _errorsOnly_1 = this.issues.errorsOnly();
        FluentIssueCollection _inLine_1 = _errorsOnly_1.inLine(17);
        FluentIssueCollection _under_1 = _inLine_1.under(com.eu.evidence.rtdruid.oil.xtext.model.Parameter.class);
        FluentIssueCollection _oneOfThemContains_2 = _under_1.oneOfThemContains("Expected a valid");
        this.assertConstraints(_oneOfThemContains_2);
      }
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void splittedOS() {
    try {
      {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("IMPLEMENTATION a {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("ALARM_TYPE at;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("ALARM {};");
        _builder.newLine();
        _builder.append("};");
        _builder.newLine();
        _builder.append("CPU s {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("ALARM alarm1 {};");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("ALARM alarm2 {};");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS aa {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("at = alarm1;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS aa {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("at = alarm2;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("};");
        _builder.newLine();
        OilFile _parse = this.parser.parse(_builder);
        final OilFile model = _parse;
        Resource _eResource = model.eResource();
        List<Issue> _validate = this.tester.validate(model);
        ArrayList<String> _arrayList = new ArrayList<String>();
        FluentIssueCollection _fluentIssueCollection = new FluentIssueCollection(_eResource, _validate, _arrayList);
        this.issues = _fluentIssueCollection;
        FluentIssueCollection _errorsOnly = this.issues.errorsOnly();
        FluentIssueCollection _inLine = _errorsOnly.inLine(11);
        FluentIssueCollection _under = _inLine.under(com.eu.evidence.rtdruid.oil.xtext.model.Parameter.class);
        FluentIssueCollection _oneOfThemContains = _under.oneOfThemContains("Only one value expected");
        this.assertConstraints(_oneOfThemContains);
        FluentIssueCollection _errorsOnly_1 = this.issues.errorsOnly();
        FluentIssueCollection _inLine_1 = _errorsOnly_1.inLine(15);
        FluentIssueCollection _under_1 = _inLine_1.under(com.eu.evidence.rtdruid.oil.xtext.model.Parameter.class);
        FluentIssueCollection _oneOfThemContains_1 = _under_1.oneOfThemContains("Only one value expected");
        this.assertConstraints(_oneOfThemContains_1);
      }
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void splittedOSEnum() {
    try {
      {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("IMPLEMENTATION a {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("BOOLEAN [");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("INT32 b;");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("BOOLEAN d;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("},");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("FALSE");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("] a;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("};");
        _builder.newLine();
        _builder.append("CPU s {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS aa {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS aa {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b=1;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("};");
        _builder.newLine();
        OilFile _parse = this.parser.parse(_builder);
        final OilFile model = _parse;
        Resource _eResource = model.eResource();
        List<Issue> _validate = this.tester.validate(model);
        ArrayList<String> _arrayList = new ArrayList<String>();
        FluentIssueCollection _fluentIssueCollection = new FluentIssueCollection(_eResource, _validate, _arrayList);
        this.issues = _fluentIssueCollection;
      }
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void splittedOSEnum2() {
    try {
      {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("IMPLEMENTATION a {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("BOOLEAN [");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("INT32 b;");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("BOOLEAN d;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("},");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("FALSE");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("] a;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("};");
        _builder.newLine();
        _builder.append("CPU s {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS aa {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("d = FALSE;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS aa {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b=1;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("};");
        _builder.newLine();
        OilFile _parse = this.parser.parse(_builder);
        final OilFile model = _parse;
        Resource _eResource = model.eResource();
        List<Issue> _validate = this.tester.validate(model);
        ArrayList<String> _arrayList = new ArrayList<String>();
        FluentIssueCollection _fluentIssueCollection = new FluentIssueCollection(_eResource, _validate, _arrayList);
        this.issues = _fluentIssueCollection;
      }
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void splittedOSEnum3() {
    try {
      {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("IMPLEMENTATION a {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("BOOLEAN [");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("INT32 b1;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("},");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("FALSE");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("] a;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("BOOLEAN [");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("INT32 b2;");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("BOOLEAN d;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("},");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("FALSE");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("] a;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("BOOLEAN [");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("INT32 b3;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("},");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("FALSE");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("] a;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("};");
        _builder.newLine();
        _builder.append("CPU s {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS aa {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("d = FALSE;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b3 = 3;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS aa {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b1=1;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b2=2;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("};");
        _builder.newLine();
        OilFile _parse = this.parser.parse(_builder);
        final OilFile model = _parse;
        Resource _eResource = model.eResource();
        List<Issue> _validate = this.tester.validate(model);
        ArrayList<String> _arrayList = new ArrayList<String>();
        FluentIssueCollection _fluentIssueCollection = new FluentIssueCollection(_eResource, _validate, _arrayList);
        this.issues = _fluentIssueCollection;
      }
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void splittedOSMultiValue() {
    try {
      {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("IMPLEMENTATION a {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("BOOLEAN [");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("INT32 b1[];");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("},");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("FALSE");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("] a;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("BOOLEAN [");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("INT32 b2;");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("BOOLEAN d;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("},");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("FALSE");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("] a;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("BOOLEAN [");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("INT32 b3;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("},");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("FALSE");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("] a;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("};");
        _builder.newLine();
        _builder.append("CPU s {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS aa {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b1=10;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b1=11;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("d = FALSE;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b1=1;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b3 = 3;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS aa {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b1=12;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b2=2;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("};");
        _builder.newLine();
        OilFile _parse = this.parser.parse(_builder);
        final OilFile model = _parse;
        Resource _eResource = model.eResource();
        List<Issue> _validate = this.tester.validate(model);
        ArrayList<String> _arrayList = new ArrayList<String>();
        FluentIssueCollection _fluentIssueCollection = new FluentIssueCollection(_eResource, _validate, _arrayList);
        this.issues = _fluentIssueCollection;
      }
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Test
  public void splittedOSMultiValueEnum() {
    try {
      {
        StringConcatenation _builder = new StringConcatenation();
        _builder.append("IMPLEMENTATION a {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("BOOLEAN [");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("INT32 b1[];");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("},");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("FALSE");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("] a[];");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("BOOLEAN [");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("INT32 b2;");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("BOOLEAN d;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("},");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("FALSE");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("] a[];");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("BOOLEAN [");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("INT32 b3;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("},");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("FALSE");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("] a[];");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("};");
        _builder.newLine();
        _builder.append("CPU s {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS aa {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b1=10;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b1=11;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b3 = 31;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("d = FALSE;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b1=1;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b3 = 32;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b2=22;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("OS aa {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("a = TRUE {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b1=12;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("b2=21;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("d = TRUE;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("};");
        _builder.newLine();
        _builder.append("};");
        _builder.newLine();
        OilFile _parse = this.parser.parse(_builder);
        final OilFile model = _parse;
        Resource _eResource = model.eResource();
        List<Issue> _validate = this.tester.validate(model);
        ArrayList<String> _arrayList = new ArrayList<String>();
        FluentIssueCollection _fluentIssueCollection = new FluentIssueCollection(_eResource, _validate, _arrayList);
        this.issues = _fluentIssueCollection;
      }
    } catch (Exception _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
}
