package com.eu.evidence.rtdruid.test.modules.oil.codewriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.w3c.dom.Document;

import com.eu.evidence.rtdruid.internal.modules.oil.exceptions.OilCodeWriterException;
import com.eu.evidence.rtdruid.internal.modules.oil.reader.OilReader;
import com.eu.evidence.rtdruid.modules.oil.abstractions.IOilWriterBuffer;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.CommonUtils;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.RtosFactory;
import com.eu.evidence.rtdruid.vartree.ITreeInterface;
import com.eu.evidence.rtdruid.vartree.IVarTree;
import com.eu.evidence.rtdruid.vartree.VarTreeUtil;

/**
 * This class can be use to simplify and standardize a Code Writer test
 * 
 * @author Nicola Serreli
 *
 */
public abstract class AbstractCodeWriterTest {
	
	@Rule public TestName name = new TestName();
	
	@Before
	public void setUp() throws Exception {
		System.err.flush();
		System.out.println("\n\n************\n TEST " + getClass().getName() + " - " + name.getMethodName() + "\n************\n\n");
		System.out.flush();
	}
	
	@After
	public void setDown() throws Exception {
		System.err.flush();
		System.out.flush();
	}
		
	/**
	 * A small class used to retur the IVarTree and computed Buffers
	 * 
	 * @author Nicola Serreli
	 */
	protected class DefaultTestResult {
		public final IVarTree vt;
		public final IOilWriterBuffer[] buffers;
		public DefaultTestResult(IVarTree vt, IOilWriterBuffer[] buffers) {
			this.vt = vt;
			this.buffers = buffers;
		}
	}
	

	/**
	 * This is a standard method that can be used to read an oil file and write
	 * the configuration on standard output
	 * 
	 * @param oil_text
	 *            the oil configuration
	 * @param expected_cpu
	 *            the expected number of cpu
	 *            
	 * @return both the loaded IVarTree and computed Buffers
	 */
	public DefaultTestResult commonWriterTest(String oil_text, int expected_cpu) {
		IVarTree vt = VarTreeUtil.newVarTree();
		(new OilReader()).load(new ByteArrayInputStream(oil_text.getBytes()), vt, null, null);

		// -------------- search rtos ----------------
		ITreeInterface ti = vt.newTreeInterface();

		String[] prefix = CommonUtils.getAllRtos(ti);
		assertEquals(prefix.length, expected_cpu);

		// --------------- write -----------------

		IOilWriterBuffer[] buffers = null;
		try {
			buffers = RtosFactory.INSTANCE.write(vt, prefix);
		} catch (OilCodeWriterException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Write fail: " + e.getMessage(), e);
		}

		assertNotNull(buffers != null);
		assertEquals(buffers.length, expected_cpu);
		for (int i=0; i<expected_cpu; i++)
			System.out.println("buff " + i + ":\n" + (buffers[i]).toString());

		return new DefaultTestResult(vt, buffers);
	}

    public static String oilToXmltext(InputStream input) throws TransformerFactoryConfigurationError, TransformerException {
        Document xml = (new OilReader()).loadAsXml(input, null, null);
        return xmlToText(xml);
    }

    public static String xmlToText(Document doc) throws TransformerFactoryConfigurationError, TransformerException {
    	ByteArrayOutputStream output = new ByteArrayOutputStream();

		Transformer transformer = TransformerFactory.newInstance()
				.newTransformer();

		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		// Transform the source XML
		transformer.transform(new DOMSource(doc), new StreamResult(output));

		return output.toString();
    }

}
