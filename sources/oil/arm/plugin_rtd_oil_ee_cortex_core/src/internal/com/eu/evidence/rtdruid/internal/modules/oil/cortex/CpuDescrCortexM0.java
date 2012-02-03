package com.eu.evidence.rtdruid.internal.modules.oil.cortex;
/**
 * Created on 21/09/2011
 *
 * $Id$
 */


import com.eu.evidence.rtdruid.internal.modules.oil.keywords.IWritersKeywords;
import com.eu.evidence.rtdruid.modules.oil.codewriter.erikaenterprise.hw.CpuHwDescription;

/**
 * The description of a Cortex M0
 * 
 * @author Nicola Serreli
 */
public class CpuDescrCortexM0 extends CpuHwDescription {

	/**
	 * 
	 */
	public CpuDescrCortexM0() {
		super(IWritersKeywords.CPU_CORTEX_M0, "PRIVATE",
				new String[] { "SYS_SIZE" }, new String[] {}, 32, 4, 4, 32);
	}
}
