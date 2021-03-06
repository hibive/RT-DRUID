/*
 * Created on 2017/02/1
 *
 */
package com.eu.evidence.rtdruid.internal.modules.oil.codewriter.erikaenterprise;

import static com.eu.evidence.rtdruid.internal.modules.oil.codewriter.erikaenterprise.ErikaEnterpriseWriter.checkOrDefault;
import static com.eu.evidence.rtdruid.modules.oil.codewriter.common.AbstractRtosWriter.getOsProperty;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import com.eu.evidence.rtdruid.desk.Messages;
import com.eu.evidence.rtdruid.internal.modules.oil.exceptions.OilCodeWriterException;
import com.eu.evidence.rtdruid.internal.modules.oil.keywords.ISimpleGenResKeywords;
import com.eu.evidence.rtdruid.internal.modules.oil.keywords.IWritersKeywords;
import com.eu.evidence.rtdruid.modules.oil.abstractions.IOilObjectList;
import com.eu.evidence.rtdruid.modules.oil.abstractions.IOilWriterBuffer;
import com.eu.evidence.rtdruid.modules.oil.abstractions.ISimpleGenRes;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.AbstractRtosWriter;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.CommonUtils;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.HostOsUtils;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.OilWriterBuffer;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.OsType;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.SWCategoryManager;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.SectionWriter;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.comments.FileTypes;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.comments.ICommentWriter;
import com.eu.evidence.rtdruid.modules.oil.codewriter.erikaenterprise.SectionWriterIsr;
import com.eu.evidence.rtdruid.modules.oil.codewriter.erikaenterprise.SectionWriterKernelCounterHw;
import com.eu.evidence.rtdruid.modules.oil.codewriter.erikaenterprise.hw.EEStackData;
import com.eu.evidence.rtdruid.modules.oil.codewriter.erikaenterprise.hw.EEStacks;
import com.eu.evidence.rtdruid.modules.oil.erikaenterprise.constants.IEEWriterKeywords;
import com.eu.evidence.rtdruid.modules.oil.erikaenterprise.interfaces.IExtractKeywordsExtentions;
import com.eu.evidence.rtdruid.modules.oil.erikaenterprise.interfaces.IExtractObjectsExtentions;
import com.eu.evidence.rtdruid.modules.oil.renesas.interfaces.RenesasConstants;
import com.eu.evidence.rtdruid.vartree.ITreeInterface;
import com.eu.evidence.rtdruid.vartree.IVarTree;
import com.eu.evidence.rtdruid.vartree.IVariable;

/**
 * This writer build files for a Renesas RL 78 
 * 
 * @author Nicola Serreli
 */
public class SectionWriterHalRl78 extends SectionWriter 
	implements IEEWriterKeywords,
			IExtractObjectsExtentions,
			IExtractKeywordsExtentions {

	public static final String MCU_R5F10PPJ = "R5F10PPJ";
	public static final String MCU_R5F10BMG = "R5F10BMG";
	public static final String MCU_R5F104LE = "R5F104LE";
	public static final String MCU_R5F10AGF = "R5F10AGF";
	public static final String MCU_R5F10PLG = "R5F10PLG";
	public static final String MCU_R5F10PLJ = "R5F10PLJ";
//	private static final String EEOPT__EE_RL78_S2 = "EE_RL78_S2__";
	private static final String EEOPT__EE_RL78_S3 = "EE_RL78_S3__";
	private static final String EEOPT__MCU_R5F10XXX = "EE_R5F10XXX__";
	private static final String EEOPT__MCU_R5F104XX = "EE_R5F104XX__";
	private static final String EEOPT__MCU_R5F104LE = "EE_R5F104LE__";
	private static final String EEOPT__MCU_R5F10PXX = "EE_R5F10PXX__";
	private static final String EEOPT__MCU_R5F10PLG = "EE_R5F10PLG__";
	private static final String EEOPT__MCU_R5F10PLJ = "EE_R5F10PLJ__";
	private static final String EEOPT__MCU_R5F10PPJ = "EE_R5F10PPJ__";
	private static final String EEOPT__MCU_R5F10AXX = "EE_R5F10AXX__";
	private static final String EEOPT__MCU_R5F10AGF = "EE_R5F10AGF__";
	private static final String EEOPT__MCU_R5F10BXX = "EE_R5F10BXX__";
	private static final String EEOPT__MCU_R5F10BMG = "EE_R5F10BMG__";
	private static final String EEOPT__RL78_CPU = "EE_RL78__";
	final String indent1 = IWritersKeywords.INDENT;
	final String indent2 = indent1 + IWritersKeywords.INDENT;

	public final static String EEOPT__CCRL_COMPILER__ = "EE_CCRL__";
	public final static String EEOPT__CA78K0R_COMPILER__ = "EE_CA78K0R__";
	
	
	/** The Erika Enterprise Writer that call this section writer */
	protected final ErikaEnterpriseWriter parent;
	
	/** All data */
	protected final IVarTree vt;

	private static final String R5F10_MCU = "R5F10XXX";
	private static final String ERR_CPU_TYPE = "RL78";
	private static final String SGR_OS_APPL_SHARED_STACK_ID = "sgr__os_application__shared_stack_id__integer";
	private static final String SGR_OS_CPU_SYS_STACK_SIZE = "sgr__os_cpu_system_stack_size";
	
	private static final String SGR_OS_MCU_MODEL = "sgr__os_cpu__mcu_model";

	
	private static final String STACK_BASE_NAME = "EE_stack_";
	private static final long DEFAULT_SYS_STACK_SIZE = 4096;
	
	private static final String EE_RL78_SYSTEM_TIMER_HANDLER = "EE_rl78_system_timer_handler";
	
	private final SectionWriterIsr isrWriter;
	private final SectionWriterKernelCounterHw counterHwWriter;
	
	/**
	 * 
	 */
	public SectionWriterHalRl78() {
		this(null);
	}
	
	/**
	 * 
	 */
	public SectionWriterHalRl78(ErikaEnterpriseWriter parent) {
		super(IWritersKeywords.CPU_RL78,
				new String[] {}, //
				new String[] { //
					IWritersKeywords.CPU_NIOSII,//
					IWritersKeywords.CPU_JANUS,//
					IWritersKeywords.CPU_MPC5xx,//
					IWritersKeywords.CPU_NIOSII, //
					IWritersKeywords.CPU_AVR5,
					IWritersKeywords.CPU_PIC_30,
					IWritersKeywords.CPU_RX200
					},
					SWCategoryManager.defaultInstance.newCategory(SWCategoryManager.TYPE_CPU));
		
		this.parent = parent;
		this.vt = parent == null ? null : parent.getVt();
		
		isrWriter = new SectionWriterIsr(parent, IWritersKeywords.CPU_RL78);
		isrWriter.setGenerateDefineCategory(true);
		counterHwWriter = new SectionWriterKernelCounterHw(parent, IWritersKeywords.CPU_RL78, EE_RL78_SYSTEM_TIMER_HANDLER);
		counterHwWriter.setAllowSystemTimerPriority(true);
		counterHwWriter.setGenerateIsr2Defines(isrWriter);
	}

	/**
	 * This method parse the data, prepares all buffers for
	 * <code>.c/.h/.s/.asm</code> files and store them inside a
	 * {@link OilWriterBuffer OilWriterBuffer}. If there are more than one cpu,
	 * uses one {@link OilWriterBuffer OilWriterBuffer}for each cpu.
	 * 
	 * @param vt
	 *            all data (cannot be null)
	 * 
	 * @return one {@link OilWriterBuffer OilWriterBuffer}for each cpu.
	 * 
	 * @throws OilCodeWriterException
	 *             if there are some errors
	 */
	public IOilWriterBuffer[] internalWrite(IVarTree vt)
			throws OilCodeWriterException {
		parent.check(vt);

		return writeEE_rl78_CPU();
	}
	
	/**
	 * This method add new keywords enabled by this writer.
	 */
	public void updateKeywords(ArrayList<String> keywords, String[] rtosPrefix) {
		
	}

	/**
	 * This method makes some changes to computed Oil Object
	 * @throws OilCodeWriterException if some ISR's attribute is missing 
	 */
	public void updateObjects() throws OilCodeWriterException {
		
		{
			isrWriter.updateObjects();
			counterHwWriter.updateObjects();
		}

		final IOilObjectList[] oilObjects = parent.getOilObjects();
		
		for (int currentRtosId = 0; currentRtosId < oilObjects.length; currentRtosId++) {
			final IOilObjectList ool = oilObjects[currentRtosId];
			

			{
				/***********************************************************************
				 * 
				 * EE OPTS
				 *  
				 **********************************************************************/
		        // store all older values (if there are)
		        ISimpleGenRes sgrCpu = ool.getList(IOilObjectList.OS).get(0);
		        ArrayList<String> tmp_eeopts = new ArrayList<String>();
		        if (sgrCpu.containsProperty(ISimpleGenResKeywords.OS_CPU_EE_OPTS)) {
		        	String[] old = (String[]) sgrCpu.getObject(ISimpleGenResKeywords.OS_CPU_EE_OPTS);
		        	tmp_eeopts.addAll(Arrays.asList(old));
		        }
				
				tmp_eeopts.add(EEOPT__RL78_CPU);
				
				{ // Compiler
					boolean useE2Studio = false;
					boolean useCSplus = false;
					ArrayList<String> paths = new ArrayList<String>();
		        	List<String> all = parent.getCpuDataEnum(ool, "COMPILER_TYPE", paths);
					String tmp1 = all.size() == 0? null : all.get(0);
					if (tmp1 == null) {
						tmp1 = RenesasConstants.SGRK__CCRL_COMPILER__;
			            tmp_eeopts.add(EEOPT__CCRL_COMPILER__);
					} else {
						String compilerPath = paths.get(0);
						
						boolean needMemoryFar = false;
						if (RenesasConstants.SGRK__CCRL_COMPILER__.equals(tmp1)) {
							needMemoryFar = true;
				            tmp_eeopts.add(EEOPT__CCRL_COMPILER__);
				            
							String e2studio = CommonUtils.getFirstChildEnumType(vt, compilerPath + S + "E2STUDIO");
							if ("TRUE".equalsIgnoreCase(e2studio)) {
								tmp_eeopts.add("EE_E2STUDIO__");
								useE2Studio = true;
							}
							
						} else if (RenesasConstants.SGRK__CA78K0R_COMPILER__.equals(tmp1)) {
							needMemoryFar = true;
				            tmp_eeopts.add(EEOPT__CA78K0R_COMPILER__);
							
							String csplus = CommonUtils.getFirstChildEnumType(vt, compilerPath + S + "CSPLUS");
							if ("TRUE".equalsIgnoreCase(csplus)) {
								tmp_eeopts.add("EE_CSPLUS__");
								useCSplus = true;
							}
						}
						
						if (needMemoryFar) {
							String memoryModel = CommonUtils.getFirstChildEnumType(vt, compilerPath + S + "MEMORY_MODEL");
							if ("SMALL".equalsIgnoreCase(memoryModel)) {
								tmp_eeopts.add("EE_RL78_SMALL__");
							} else {
								tmp_eeopts.add("EE_RL78_MEDIUM__");
							}
							String farData = CommonUtils.getFirstChildEnumType(vt, compilerPath + S + "FAR_DATA");
							if ("TRUE".equalsIgnoreCase(farData)) {
								tmp_eeopts.add("EE_RL78_FAR__");
							}
						}
					}
					
						
					sgrCpu.setProperty(RenesasConstants.SGRK__RENESAS_COMPILER_TYPE__, tmp1);
					sgrCpu.setProperty(RenesasConstants.SGRK__RENESAS_USE_E2STUDIO__, "" +useE2Studio);
					sgrCpu.setProperty(RenesasConstants.SGRK__RENESAS_USE_CSPLUS__, "" +useCSplus);
				}

			
				// store ee_opts
				sgrCpu.setObject(ISimpleGenResKeywords.OS_CPU_EE_OPTS, tmp_eeopts.toArray(new String[tmp_eeopts.size()]));
						
				/***********************************************************************
				 * 
				 * System stack size
				 *  
				 **********************************************************************/
				{
					String[] stack_size = parent.getCpuDataValue(ool, "SYS_STACK_SIZE");
					if (stack_size != null && stack_size.length>0 && stack_size[0] != null) {
						
						boolean valid = false;
						int value = -1;
						try {
							value = Integer.decode(stack_size[0]);
							valid = true;
						} catch (NumberFormatException e) {
							Messages.sendWarningNl("Invalid value for System stack size : " + stack_size[0]);
						}
						
						if (valid && value <0) {
							Messages.sendWarningNl("System stack size cannot be negative (" + value + ")");
						} else {
							
							sgrCpu.setProperty(SGR_OS_CPU_SYS_STACK_SIZE, ""+value);
						}
						
					}

				}
			}
			
		}
		
        LinkedHashSet<String> tmp_common_eeopts = new LinkedHashSet<String>();
		checkMcu(tmp_common_eeopts); // note that mcu must done after board 
	
		for (int currentRtosId = 0; currentRtosId < oilObjects.length; currentRtosId ++) { 
			IOilObjectList ool = oilObjects[currentRtosId];
			
			/* COMMON VARIABLES */
			ISimpleGenRes sgrCpu = (ISimpleGenRes) ool.getList(IOilObjectList.OS).get(0);
			sgrCpu.setObject(ISimpleGenResKeywords.OS_CPU_COMMON_EE_OPTS, tmp_common_eeopts.toArray(new String[tmp_common_eeopts.size()]));			
			
			// updateStacks (needed by orti)... not the best solution....
			handleStacks(currentRtosId, ool);
		}

	}
	
	/**
	 * Write configuration's files for Renesas RL78
	 * 
	 * @return buffers with configuration for Renesas RL78
	 * 
	 * @throws OilCodeWriterException
	 *             if there are some problems
	 */
	protected IOilWriterBuffer[] writeEE_rl78_CPU()
			throws OilCodeWriterException {

		final IOilObjectList[] oilObjects = parent.getOilObjects();		
		IOilWriterBuffer[] answer = new IOilWriterBuffer[oilObjects.length];

		final int rtosNumber = oilObjects.length;


		/***********************************************************************
		 * prepare and write buffers for All OS
		 **********************************************************************/
		for (int currentRtosId = 0; currentRtosId < rtosNumber; currentRtosId++) {
		
			IOilWriterBuffer cpuBuffs = new OilWriterBuffer();
			answer[currentRtosId] = cpuBuffs;
	
	
			// ------------- Requirement --------------------
			StringBuffer sbInithal_c = cpuBuffs.get(FILE_EE_CFG_C);
//			StringBuffer sbInithal_h = cpuBuffs.get(FILE_EE_CFG_H);
			
			final IOilObjectList ool = oilObjects[currentRtosId];
	
	
			// ------------- Compute --------------------
	
			sbInithal_c.append("\n#include \"ee.h\"\n");
	
//			/***********************************************************************
//			 * SYSTEM STACK SIZE
//			 **********************************************************************/
//			sbInithal_h.append(indent1 + getCommentWriter(ool, FileTypes.H).writerSingleLineComment("System stack size") + 
//						indent1 + "#define EE_ISTACK_SIZE     " + 
//							( ErikaEnterpriseWriter.checkOrDefault(AbstractRtosWriter.getOsProperty(ool, SGR_OS_CPU_SYS_STACK_SIZE),
//									DEFAULT_SYS_STACK_SIZE))
//						+ "\n\n");
			
			
			/***********************************************************************
			 * OTHER STACKs
			 **********************************************************************/
			sbInithal_c.append(handleStacks(currentRtosId, ool));
			
	
			
			//  ------------- MORE FILES ---------------
			
			
			// isr
			isrWriter.writeIsr(currentRtosId, ool, cpuBuffs);
			counterHwWriter.writeCounterHw(currentRtosId, ool, cpuBuffs);
			
			// makefile
			prepareMakeFile(ool);
		
		}
		
		return answer;
	}


	protected StringBuffer handleStacks(int currentRtosId, final IOilObjectList ool) throws OilCodeWriterException  {
		String stackType = parent.getStackType(); // MULTI or MONO
		StringBuffer sbInithal_c = new StringBuffer();

		if (getOsProperty(ool, SGRK_OS_CPU_DATA_PREFIX) == null) {
			return sbInithal_c;
		}
		final ICommentWriter commentWriterC = getCommentWriter(ool, FileTypes.C);
		
		List<ISimpleGenRes> taskNames = ool.getList(IOilObjectList.TASK);
		List<ISimpleGenRes> osApplications = ool.getList(IOilObjectList.OSAPPLICATION);
		boolean binaryDistr = parent.checkKeyword(IEEWriterKeywords.DEF__EE_USE_BINARY_DISTRIBUTION__);
		
		final String cpu_type = checkOrDefault(getOsProperty(ool, ISimpleGenResKeywords.OS_CPU_TYPE), IWritersKeywords.CPU_RL78);
		final String mcu_type;
		{
			String tmp = getOsProperty(ool, SGR_OS_MCU_MODEL);
			mcu_type = tmp == null ? "" : " " + tmp;
		}

		/*
		 * Define a string for each MAX_OBJECT_NUMBER (OBJECT=task, RESOURCE, ...).
		 * Binary distribution uses the suffix RTD_. 
		 */
		final String MAX_TASK = (binaryDistr ? "RTD_" : "EE_") + "MAX_TASK";
		final boolean isFp = parent.checkKeyword(IWritersKeywords.FP);
		final String stack_offset = isFp ? "" : " - RL78_INIT_TOS_OFFSET";

		String pre = "";
		String post = "";
		final String indent = IWritersKeywords.INDENT;
		
		EEStackData sys_stack = new EEStackData(0,
				new long[] { Long.decode(checkOrDefault(getOsProperty(ool, SGR_OS_CPU_SYS_STACK_SIZE), "" + DEFAULT_SYS_STACK_SIZE))},
				new long[] {0},
				new String[] {" (int)&EE_rl78_sys_stack "}, true);

		
		/***********************************************************************
		 * MULTI STACK
		 **********************************************************************/
		if (DEF__MULTI_STACK__.equals(stackType)) {
			ArrayList<EEStackData> stackTmp = new ArrayList<EEStackData>();
//			final String DELTA = "-2";

			// ------------- Buffers --------------------
			/* A buffer about stack  */
			StringBuffer sbStack = new StringBuffer();

			/* A buffer about declarations of stacks  */
			StringBuffer sbStackDecl = new StringBuffer();
			StringBuffer sbStackDeclSize = new StringBuffer();
//			final boolean usePragma = parent.checkPragma(currentRtosId);
			LinkedHashMap<String, StringBuffer> sbStackPragma = new LinkedHashMap<String, StringBuffer>();
//			StringBuffer stackPatternFill = new StringBuffer();
			
			sbInithal_c.append(commentWriterC
					.writerBanner("Stack definition for "+mcu_type+", "+cpu_type.toLowerCase()));
			
			ITreeInterface ti = vt.newTreeInterface();


			
			int[] irqSize = null;
			if (parent.checkKeyword(DEF__IRQ_STACK_NEEDED__)) {
				/***************************************************************
				 * IRQ_STACK
				 **************************************************************/
				final List<String> currentCpuPrefixes = AbstractRtosWriter.getOsProperties(ool, SGRK_OS_CPU_DATA_PREFIX);
				for (String currentCpuPrefix: currentCpuPrefixes) {
					if (irqSize != null) {
						break;
					}
					
					String[] child = new String[1];
					String type = CommonUtils
							.getFirstChildEnumType(vt, currentCpuPrefix
									+ "MULTI_STACK", child);
	
					if ("TRUE".equalsIgnoreCase(type)) {
						String prefixIRQ = currentCpuPrefix
							+ "MULTI_STACK" + VARIANT_ELIST+child[0] + PARAMETER_LIST
							+ "IRQ_STACK";
						boolean ok = "TRUE".equalsIgnoreCase(CommonUtils
						.getFirstChildEnumType(vt, prefixIRQ, child));
						
						if (ok) {
							
							prefixIRQ += VARIANT_ELIST + child[0] +PARAMETER_LIST;
							irqSize = new int[1];
							{ // get data for IRQ STACK ...
								String path[] = { "SYS_SIZE" };
	
								for (int i = 0; i < path.length; i++) {
									String tmp = null;
									IVariable var = ti.getValue(prefixIRQ + path[i]
											+ VALUE_VALUE);
									if (var != null && var.get() != null) {
										tmp = var.toString();
									}
									if (tmp == null)
										throw new RuntimeException(
												ERR_CPU_TYPE + " : Expected " + path[i]);
	
									// check for value
									try {
										// ... store them inside the irqSize vector
										irqSize[0] = (Integer.decode("" + tmp))
												.intValue();
										// ... and increase the memory requirement
	//									stackEnd += irqSize[0];
									} catch (Exception e) {
										throw new RuntimeException(
												ERR_CPU_TYPE + " : Wrong int" + path[i]
														+ ", value = " + tmp + ")");
									}
								}
							}
						}
					}
				}
			}

			/*
			 * elStack contains all data about stack, for current rtos and its
			 * tasks.
			 * 
			 * tList and tListN are used to identify all tasks (theirs name and
			 * system id).
			 * 
			 * elStack accepts the list of task's names (tList) to compute all
			 * required stack and theirs size/position
			 */
			//EEStacks elStack = new EEStacks(parent, ool, irqSize);
			EEStacks elStack = new EEStacks(parent, ool, null);
			elStack.setDummyStackPolicy(elStack.FORCE_ALWAYS | elStack.FORCE_FIRST);
			ArrayList<String> tList = new ArrayList<String>();
			ArrayList<String> tListN = new ArrayList<String>();

			// use a macro to ensure alignment
			final int STACK_UNIT = 2;
			
			 
			{
				/***************************************************************
				 * STACK FOR EACH TASK
				 **************************************************************/
				
				// add the dummy task
				tListN.add("");
				tList.add(IWritersKeywords.dummyName);
				
				// fill data for each task
				for (Iterator<ISimpleGenRes> iter = taskNames.iterator(); iter.hasNext();) {

					ISimpleGenRes sgr = (ISimpleGenRes) iter.next();
					tList.add(sgr.getName());
					tListN.add(sgr.getString(ISimpleGenResKeywords.TASK_SYS_ID));
				}

				// fill data for each shared stack, related to OS applications
				for (Iterator<ISimpleGenRes> iter = osApplications.iterator(); iter.hasNext();) {

					ISimpleGenRes sgr = (ISimpleGenRes) iter.next();
					tList.add(EEStacks.APPLICATION_SHARED_PREFIX+ sgr.getName());
					tListN.add(" ");

					tList.add(EEStacks.APPLICATION_IRQ_PREFIX+ sgr.getName());
					tListN.add("");
					
					sgr.setObject(SGR_OS_APPL_SHARED_STACK_ID, new Integer((tList.size()-1)));
					sgr.setObject(EEStacks.STACK_BASE_NAME_PREFIX, STACK_BASE_NAME);
				}
				// compute total stack size and add it to memory requirement
//				int offset[][] = elStack.taskOffsets((String[]) tList
//						.toArray(new String[0]));
//				stackEnd += offset[offset.length - 1][0]; // tot sys
			}

			
			{
				/***************************************************************
				 * PREPARE BUFFERS
				 **************************************************************/

				pre = "";
				post = "";

				/* get the link between a task and its stack. */
				int pos[] = elStack.taskStackLink(tList
						.toArray(new String[1]));
				/* get the size of each stack. */
				int size[][] = elStack.stackSize(tList
						.toArray(new String[1]));
				/* get the memory section id of each stack. */
				String memoryId[] = elStack.stackMemoryId(tList
						.toArray(new String[1]));
				
				/* descrStack contains a description for each stack. */ 
				String[] descrStack = new String[size.length];
				sbStack.append(indent
						+ "const EE_UREG EE_std_thread_tos["+MAX_TASK+"+1] = {\n");
				
			 // fill data for each shared stack, related to OS applications
				for (Iterator<ISimpleGenRes> iter = osApplications.iterator(); iter.hasNext();) {

					ISimpleGenRes sgr = (ISimpleGenRes) iter.next();
					Integer index = (Integer) sgr.getObject(SGR_OS_APPL_SHARED_STACK_ID);

					sgr.setProperty(ISimpleGenResKeywords.OS_APPL_SHARED_STACK_ID,"" + pos[index.intValue()]);
					
				}
					
				// DESCRIPTIONS
				
				for (int j = 0; j < pos.length; j++) {
					String stack_name = tList.get(j);
					if (!stack_name.startsWith(EEStacks.APPLICATION_SHARED_PREFIX) 
							&& !stack_name.startsWith(EEStacks.APPLICATION_IRQ_PREFIX)) {
						
						sbStack.append(pre + post + indent + indent + +pos[j]+"U");
						// set new values for "post" and "pre"
						post = " /* " + stack_name + "*/\n";
						pre = ",\t";
					}

					/*
					 * add the name of current task to the description of the /
					 * right stack. Check also if there is already something or
					 * not, infact in the second case append the new description
					 * to the old one
					 */ 
					final String tid;
					if (stack_name.startsWith(EEStacks.APPLICATION_SHARED_PREFIX)) {
						tid = "shared stack " +stack_name.substring(EEStacks.APPLICATION_SHARED_PREFIX.length()); 
					} else if (stack_name.startsWith(EEStacks.APPLICATION_IRQ_PREFIX)) {
						tid = "ISR stack for " +stack_name.substring(EEStacks.APPLICATION_IRQ_PREFIX.length()); 
					} else {
						tid = "Task " +tListN.get(j)+ " (" + stack_name + ")";
					}
					descrStack[pos[j]] = (descrStack[pos[j]] == null) ?
							// The first description
							(tid)
							:
							// other descriptions
							(descrStack[pos[j]] + ", " + tid); // others
				}

				// close sbStack
				sbStack.append(" \t" + post + indent + "};\n\n");

				pre = "";
				post = "";

				
				// USED BY ORTI
				stackTmp.add(sys_stack);
				

//				 DECLARE STACK SIZE && STACK (ARRAY)
				for (int j = 1; j < size.length; j++) {
				    long value = size[j][0];
			    	final String memId = memoryId[j];
				    value  = (value + (value%STACK_UNIT)) / STACK_UNIT; // 
					sbStackDeclSize.append(indent1 + "#define STACK_"+j+"_SIZE "+value+" " + commentWriterC.writerSingleLineComment("size = "+size[j][0]+" bytes"));
			    	
					final String decl = "static int " +
//							( usePragma ? "" :
//								(memId == null ? "EE_STACK_ATTRIB " : "EE_STACK_ATTRIB_NAME("+memId+") ")
//							) +
								STACK_BASE_NAME+j+"[STACK_"+j+"_SIZE];\t/* "+descrStack[j]+" */";
			    	
//				    if (usePragma) {
//				    	addPragmaSections(sbStackPragma, decl, memId);
//				    } else {
				    	sbStackDecl.append(indent1 + decl + "\n");
//				    }
					
					// USED BY ORTI
					stackTmp.add(new EEStackData(j, new long[] {size[j][0]}, new long[] {size[j][0]},
							new String[] {" (int)(&"+STACK_BASE_NAME+j+")"}, true)); // DELTA
				}

				
				int tos_size = size.length - osApplications.size();
				
				// open system tos
				sbStack.append(indent
						+ "struct EE_TOS EE_rl78_system_tos["+ErikaEnterpriseWriter.addVectorSizeDefine(ool, "EE_rl78_system_tos", tos_size)
						+ "] = {\n");

				/*
				 * For each stack prepare the configuration's vectors and
				 * descriptions
				 */
				for (int j = 0; j < tos_size; j++) {
				    
			        String value = j == 0 ? "{NULL}" : "{(EE_ADDR)(&"+STACK_BASE_NAME+j+"[STACK_"+j+"_SIZE"+stack_offset+"])}"; // DELTA

					sbStack.append(pre
							+ post
							+ indent
							+ indent
							+ value);

					// set new values for size
					pre = ",";
					post = "\t/* "+descrStack[j]+" */\n";
				}

				// complete the stack's buffer
				sbStack.append(" " + post + indent + "};\n\n" + indent
						+ "EE_UREG EE_rl78_active_tos = 0U; /* dummy */\n\n");

				{ // if required, init also the irq stack
					if (irqSize != null) {
						int j = size.length;
					    long value = irqSize[0];
					    
					    value  = (value + (value%STACK_UNIT)) / STACK_UNIT;
						sbStackDeclSize.append(indent1 + "#define STACK_"+j+"_SIZE "+value+ " " + commentWriterC.writerSingleLineComment("size = "+irqSize[0]+" bytes"));
						
						final String decl = "static int "+
								//( usePragma ? "" : "EE_STACK_ATTRIB ")+
								STACK_BASE_NAME+j+"[STACK_"+j+"_SIZE];\t/* irq stack */";
//					    if (usePragma) {
//					    	addPragmaSections(sbStackPragma, decl, null);
//					    } else {
					    	sbStackDecl.append(indent1 + decl + "\n");
//					    }

						sbStack
								.append(indent+"/* stack used only by IRQ handlers */\n"
										+ indent+"struct EE_TOS EE_rl78_IRQ_tos;	/* MIRSA-2012 Rule 8.4 */\n"
										+ indent+"struct EE_TOS EE_rl78_IRQ_tos = {\n"
										+ indent+indent+"(EE_ADDR)(&"+STACK_BASE_NAME+j+"[STACK_"+j+"_SIZE])\n" // DELTA
										+ indent+"};\n\n");
						
						// REQUIRED By ORTI's STACK
						int eesdID = stackTmp.size();
						stackTmp.add(new EEStackData(eesdID, new long[] {irqSize[0]}, new long[] {irqSize[0]},
								new String[] {" (int)(&"+STACK_BASE_NAME+j+")"}, true)); // DELTA

						ISimpleGenRes sgrCpu = ool.getList(IOilObjectList.OS).get(0);
						sgrCpu.setProperty(ISimpleGenResKeywords.OS_IRQ_STACK_ID, ""+eesdID);
					}
				}

				
//			    stackPatternFill.append("#endif\n");
				{// ORTI : Store link between task and stack
					int j = 1;
					for (Iterator<ISimpleGenRes> iter = taskNames.iterator(); iter.hasNext(); j++) {
						ISimpleGenRes sgr = iter.next();
						sgr.setObject(SGRK_TASK_STACK, stackTmp.get(pos[j]));
					}
					ISimpleGenRes sgrCpu = ool.getList(IOilObjectList.OS).get(0);
					sgrCpu.setObject(SGRK_OS_STACK_LIST, stackTmp.toArray(new EEStackData[0]));
					sgrCpu.setObject(SGRK_OS_STACK_VECTOR_NAME, "EE_rl78_system_tos");
				}

			}		

			// add stack sizes
			sbInithal_c.append(sbStackDeclSize+"\n");

			//  add pragma sections
//			closePragmaSections(sbStackPragma);
			for (StringBuffer buff: sbStackPragma.values()) {
				sbInithal_c.append(buff+"\n\n");
			}

			// add other stack declarations
			sbInithal_c.append(sbStackDecl + "\n" +
			        sbStack
//			        +stackPatternFill
			        );

		} else {
			
			ISimpleGenRes sgrCpu = ool.getList(IOilObjectList.OS).get(0);
			sgrCpu.setObject(SGRK_OS_STACK_LIST, new EEStackData[] {sys_stack});
		}

		return sbInithal_c;
	}

	/*
	 * Parse and write the MCU Section
	 */
	private void checkMcu(LinkedHashSet<String> tmp_common_eeopts) throws OilCodeWriterException {
		
		final IOilObjectList[] oilObjects = parent.getOilObjects();
		
		String mcu_model = getMcuType(parent.getVt(), oilObjects);
		
		boolean s3option = false;
		
		ArrayList<String> mcu_ee_opt = null;
		if (MCU_R5F104LE.equals(mcu_model)) {
			mcu_ee_opt = new ArrayList<String>();
			mcu_ee_opt.add(EEOPT__MCU_R5F10XXX);
			mcu_ee_opt.add(EEOPT__MCU_R5F104XX);
			mcu_ee_opt.add(EEOPT__MCU_R5F104LE);
			s3option = true;
//			mcu_ee_opt.add(EEOPT__EE_RL78_S3); -> only for CCRL
			
		} else if (MCU_R5F10BMG.equals(mcu_model)) {
			mcu_ee_opt = new ArrayList<String>();
			mcu_ee_opt.add(EEOPT__MCU_R5F10XXX);
			mcu_ee_opt.add(EEOPT__MCU_R5F10BXX);
			mcu_ee_opt.add(EEOPT__MCU_R5F10BMG);
			s3option = true;
//			mcu_ee_opt.add(EEOPT__EE_RL78_S3); -> only for CCRL
			
		} else if (MCU_R5F10PPJ.equals(mcu_model)) {
			mcu_ee_opt = new ArrayList<String>();
			mcu_ee_opt.add(EEOPT__MCU_R5F10XXX);
			mcu_ee_opt.add(EEOPT__MCU_R5F10PXX);
			mcu_ee_opt.add(EEOPT__MCU_R5F10PPJ);
			s3option = true;
//			mcu_ee_opt.add(EEOPT__EE_RL78_S3); -> only for CCRL
			
		} else if (MCU_R5F10AGF.equals(mcu_model)) {
			mcu_ee_opt = new ArrayList<String>();
			mcu_ee_opt.add(EEOPT__MCU_R5F10XXX);
			mcu_ee_opt.add(EEOPT__MCU_R5F10AXX);
			mcu_ee_opt.add(EEOPT__MCU_R5F10AGF);
			s3option = true;
//			mcu_ee_opt.add(EEOPT__EE_RL78_S3); -> only for CCRL
			
		} else if (MCU_R5F10PLG.equals(mcu_model)) {
			mcu_ee_opt = new ArrayList<String>();
			mcu_ee_opt.add(EEOPT__MCU_R5F10XXX);
			mcu_ee_opt.add(EEOPT__MCU_R5F10PXX);
			mcu_ee_opt.add(EEOPT__MCU_R5F10PLG);
			s3option = true;
//			mcu_ee_opt.add(EEOPT__EE_RL78_S3); -> only for CCRL
			
		} else if (MCU_R5F10PLJ.equals(mcu_model)) {
			mcu_ee_opt = new ArrayList<String>();
			mcu_ee_opt.add(EEOPT__MCU_R5F10XXX);
			mcu_ee_opt.add(EEOPT__MCU_R5F10PXX);
			mcu_ee_opt.add(EEOPT__MCU_R5F10PLJ);
			s3option = true;
//			mcu_ee_opt.add(EEOPT__EE_RL78_S3); -> only for CCRL
		} else {
			Messages.sendWarningNl("Unsupported MCU");
		}
		
		if (mcu_ee_opt != null) {
			tmp_common_eeopts.addAll(mcu_ee_opt);				
		}

			
		if (mcu_model != null) {
			for (IOilObjectList ool: oilObjects) { 
				ISimpleGenRes sgrCpu = ool.getList(IOilObjectList.OS).get(0);
				sgrCpu.setProperty(SGR_OS_MCU_MODEL, mcu_model);
				
				if (s3option) {
					String compiler_type = AbstractRtosWriter.getOsProperty(ool, RenesasConstants.SGRK__RENESAS_COMPILER_TYPE__);
			        if (RenesasConstants.SGRK__CCRL_COMPILER__.equalsIgnoreCase(compiler_type)) {
			        	ArrayList<String> tmp_eeopts = new ArrayList<String>();
				        if (sgrCpu.containsProperty(ISimpleGenResKeywords.OS_CPU_EE_OPTS)) {
				        	String[] old = (String[]) sgrCpu.getObject(ISimpleGenResKeywords.OS_CPU_EE_OPTS);
				        	tmp_eeopts.addAll(Arrays.asList(old));
				        }
				        tmp_eeopts.add(EEOPT__EE_RL78_S3);
						// store ee_opts
						sgrCpu.setObject(ISimpleGenResKeywords.OS_CPU_EE_OPTS, tmp_eeopts.toArray(new String[tmp_eeopts.size()]));
			        }
				}
			}
		}
	}
	
	/**
	 * @param mcu_model
	 * @param ool
	 * @return
	 */
	public static String getMcuType(IVarTree vt, IOilObjectList[] oilObjects) {
		String mcu_model = null;
		
		for (IOilObjectList ool : oilObjects) {

			if (mcu_model == null) {
				/***********************************************************************
				 * get values
				 **********************************************************************/
				ArrayList<String> childPaths = new ArrayList<String>();
				List<String> childFound = ErikaEnterpriseWriter.getRtosCommonChildType(vt, ool, "MCU_DATA", childPaths);

				for (int index = 0; index<childFound.size(); index++) {
					if (mcu_model == null) {
						String mcu_type = childFound.get(index);
						
						if (R5F10_MCU.equals(mcu_type)) {
							// ... and compete it 
							String currentMcuPrefix = childPaths.get(index) + PARAMETER_LIST + "MODEL";
			
							mcu_model = CommonUtils.getFirstChildEnumType(vt, currentMcuPrefix, null);
						}
						
					}
				}
			}
		}
		return mcu_model;
	}
	
	
	void prepareMakeFile(IOilObjectList ool) {
		final ICommentWriter commentWriterMf = getCommentWriter(ool, FileTypes.MAKEFILE);
		
		
		final String cpu_type = checkOrDefault(getOsProperty(ool, ISimpleGenResKeywords.OS_CPU_TYPE), IWritersKeywords.CPU_RL78);
		final String mcu_type;
		{
			String tmp = getOsProperty(ool, SGR_OS_MCU_MODEL);
			mcu_type = tmp == null ? "" : " " + tmp;
		}
		
		/***********************************************************************
         * 
         * MAKE FILE
         *  
         **********************************************************************/
		OsType wrapper = HostOsUtils.common.getTarget();

		    StringBuffer sbMakefile_variables = new StringBuffer(commentWriterMf.writerBanner("Renesas "+cpu_type));
		    
		    {	// PATHs
	        	HashMap<String, ?> options = parent.getOptions();

		        String outputDir = "Debug";
		        String appBase = "..";
		        
		    	if (options.containsKey(IWritersKeywords.WRITER_OUTPUT_DIR_SET)) {
					outputDir = (String) options.get(IWritersKeywords.WRITER_LAST_OUTPUT_DIR);
					outputDir = (String) options.get(IWritersKeywords.WRITER_FS_PATH_OUTPUT_DIR);
					
					appBase = (String) options.get(IWritersKeywords.WRITER_WS_OUTPUT_projectbase_DIR);//"..";
					if (options.containsKey(IWritersKeywords.WRITER_WS_PATH_OUTPUT_DIR)) {
						outputDir = (String) options.get(IWritersKeywords.WRITER_WS_PATH_OUTPUT_DIR);
					}
				}
		    	IPath p = new Path(outputDir);
		    	if (p.isAbsolute()) {
		    		outputDir = wrapper.wrapPath(outputDir);
		    	}
		    	
		    	
		        sbMakefile_variables.append(
		        		CommonUtils.addMakefileDefinesInclude() +
		                "APPBASE := " + appBase + "\n" +
		                "OUTBASE := " + outputDir + "\n\n"
		                
		        );
		        
		        String compiler_type = AbstractRtosWriter.getOsProperty(ool, RenesasConstants.SGRK__RENESAS_COMPILER_TYPE__);
		        if (RenesasConstants.SGRK__CCRL_COMPILER__.equalsIgnoreCase(compiler_type)) {
			        String gcc = RenesasConstants.DEFAULT_RL78_CONF_CCRL_CC;
			    	if (options.containsKey(RenesasConstants.PREF_RL78_CCRL_CC_PATH)) {
						String tmp = (String) options.get(RenesasConstants.PREF_RL78_CCRL_CC_PATH);
						if (tmp.length()>0) gcc = tmp;
					} 
		    		sbMakefile_variables.append( CommonUtils.compilerMakefileDefines(gcc, "CCRL_ROOT", wrapper) );
		    		
		        } else if (RenesasConstants.SGRK__CA78K0R_COMPILER__.equalsIgnoreCase(compiler_type)) {
			        String gcc = RenesasConstants.DEFAULT_RL78_CONF_CA78K0R_CC;
			    	if (options.containsKey(RenesasConstants.PREF_RL78_CA78K0R_CC_PATH)) {
						String tmp = (String) options.get(RenesasConstants.PREF_RL78_CA78K0R_CC_PATH);
						if (tmp.length()>0) gcc = tmp;
					} 
		    		sbMakefile_variables.append( CommonUtils.compilerMakefileDefines(gcc, "CA78K0R_ROOT", wrapper) );
		        } 		        
		        {
			        String enableE2Studio = AbstractRtosWriter.getOsProperty(ool, RenesasConstants.SGRK__RENESAS_USE_E2STUDIO__);
			        if ( Boolean.parseBoolean(enableE2Studio)) {
				        String e2StudioPath = "";
				    	if (options.containsKey(RenesasConstants.PREF_RL78_E2STUDIO_PATH)) {
							String tmp = (String) options.get(RenesasConstants.PREF_RL78_E2STUDIO_PATH);
							if (tmp.length()>0) e2StudioPath = tmp;
						} 
			    		sbMakefile_variables.append( CommonUtils.compilerMakefileDefines(e2StudioPath, "E2STUDIO_ROOT", wrapper) );
			        } 
		        }
		        
		        {
			        String enableCsPlus = AbstractRtosWriter.getOsProperty(ool, RenesasConstants.SGRK__RENESAS_USE_CSPLUS__);
			        if ( Boolean.parseBoolean(enableCsPlus)) {
				        String csPlusPath = "";
				    	if (options.containsKey(RenesasConstants.PREF_RL78_CSPLUS_PATH)) {
							String tmp = (String) options.get(RenesasConstants.PREF_RL78_CSPLUS_PATH);
							if (tmp.length()>0) csPlusPath = tmp;
						} 
			    		sbMakefile_variables.append( CommonUtils.compilerMakefileDefines(csPlusPath, "CSPLUS_ROOT", wrapper) );
			        } 
		        }
		    }

			ISimpleGenRes sgrCpu = ool.getList(IOilObjectList.OS).get(0);
            sgrCpu.setProperty(SGRK__MAKEFILE_EXTENTIONS__, sbMakefile_variables.toString());

	}
	
	/**
	 * This metod takes an array and returns the first element, or null if the
	 * array is a zero-length array or if the first element is empty ("" or
	 * null)
	 */
	static String clean(String[] arr) {
		String answer = null;
		if (arr != null && arr.length>0) {
			if (arr[0].trim().length()>0) {
				answer= arr[0].trim();
			}
		}
		return answer;
	}

}
