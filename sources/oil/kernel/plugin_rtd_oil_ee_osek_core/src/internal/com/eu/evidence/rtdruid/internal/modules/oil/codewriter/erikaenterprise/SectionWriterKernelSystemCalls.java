package com.eu.evidence.rtdruid.internal.modules.oil.codewriter.erikaenterprise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.eu.evidence.rtdruid.internal.modules.oil.exceptions.OilCodeWriterException;
import com.eu.evidence.rtdruid.internal.modules.oil.keywords.ISimpleGenResKeywords;
import com.eu.evidence.rtdruid.internal.modules.oil.keywords.IWritersKeywords;
import com.eu.evidence.rtdruid.modules.oil.abstractions.IOilObjectList;
import com.eu.evidence.rtdruid.modules.oil.abstractions.IOilWriterBuffer;
import com.eu.evidence.rtdruid.modules.oil.abstractions.ISimpleGenRes;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.AbstractRtosWriter;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.OilWriterBuffer;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.SWCategoryManager;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.SectionWriter;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.comments.FileTypes;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.comments.ICommentWriter;
import com.eu.evidence.rtdruid.modules.oil.erikaenterprise.constants.IEEWriterKeywords;
import com.eu.evidence.rtdruid.modules.oil.erikaenterprise.interfaces.IExtractKeywordsExtentions;
import com.eu.evidence.rtdruid.modules.oil.erikaenterprise.interfaces.IGetEEOPTExtentions;
import com.eu.evidence.rtdruid.vartree.IVarTree;

public class SectionWriterKernelSystemCalls extends SectionWriter
	implements IEEWriterKeywords, 
	IExtractKeywordsExtentions, IGetEEOPTExtentions {

	protected final static String indent1 = IWritersKeywords.INDENT;
	protected final static String indent2 = IWritersKeywords.INDENT + IWritersKeywords.INDENT;
	
	protected final static String white_spaces = "                                                    ";
	protected final static int id_def_lenght = 40;
	
	protected final static String[] EE_OS_INTERRUPT_IDs = {
		"EnableAllInterrupts",
		"DisableAllInterrupts",
		"ResumeAllInterrupts",
		"SuspendAllInterrupts",
		"ResumeOSInterrupts",
		"SuspendOSInterrupts",
		
	};
	
	protected final static String[] EE_OS_SERVICES_IDs = {
		"ActivateTask",
		"TerminateTask",
		"ChainTask",
		"Schedule",
		"ForceSchedule",
		"GetTaskID",
		"GetTaskState",
		
		"GetActiveApplicationMode",
		"StartOS",
		"ShutdownOS"
	};


	protected final static String[] EE_ISR2_IDs = {
		"ThreadNotTerminated",
		"TerminateISR2"
	};

	
	protected final static String[] EE_RESOURCES_IDs = { 
			"GetResource",
			"ReleaseResource"
	};
	
	
	protected final static String[] EE_ECC1_2_IDs = {
		"SetEvent",
		"ClearEvent",
		"GetEvent",
		"WaitEvent"
	};
	
	
	protected final static String[] EE_ALARMS_IDs = {
		"GetAlarmBase",
		"GetAlarm",
		"SetRelAlarm",
		"SetAbsAlarm",
		"CancelAlarm",
		"IncrementCounter"
	};

	
	protected final static String[] EE_SEM_IDs = {
		"InitSem",
		"WaitSem",
		"TryWaitSem",
		"PostSem",
		"GetValueSem"
	};
		
		
				
	
	
	
	/** The Erika Enterprise Writer that call this section writer */
	protected final ErikaEnterpriseWriter parent;
	
	/**
	 * 
	 */
	public SectionWriterKernelSystemCalls() {
		this(null);
	}
	
	/**
	 * 
	 */
	public SectionWriterKernelSystemCalls(ErikaEnterpriseWriter parent) {
		super("OSEK_SYS_CALLS",
				new String[] {}, //
				new String[] {},
					SWCategoryManager.defaultInstance.newCategory(SWCategoryManager.TYPE_KERNEL, IWritersKeywords.KERNEL_MEMORY_PROTECTION_PRIORITY+10));
		
		this.parent = parent;
	}
	
	@Override
	protected boolean enabled() {
		return (Collections.binarySearch(keywords, IWritersKeywords.KERNEL_SYS_CALL) > 0);
	}
	
	@Override
	protected IOilWriterBuffer[] internalWrite(IVarTree vt)
			throws OilCodeWriterException {
		
		IOilObjectList[] oilObjects = parent.getOilObjects();
		IOilWriterBuffer[] answer = new IOilWriterBuffer[oilObjects.length];
		for (int rtosId = 0; rtosId < oilObjects.length; rtosId++) {
		
			answer[rtosId] = new OilWriterBuffer();
			final IOilObjectList ool = parent.getOilObjects()[rtosId];
			
			addSysCalls(ool, answer[rtosId], rtosId);
		}
		
		return answer;
	}
	
	
	@SuppressWarnings("unchecked")
	protected void addSysCalls(IOilObjectList ool, IOilWriterBuffer answer, int rtosId) {
		List<Integer> requiredOilObjects = (List<Integer>) AbstractRtosWriter.getOsObject(ool, SGRK__FORCE_ARRAYS_LIST__);

		final ICommentWriter commentWriterH = getCommentWriter(ool, FileTypes.H);
		final ICommentWriter commentWriterC = getCommentWriter(ool, FileTypes.C);

		StringBuffer ee_c_buffer = answer.get(FILE_EE_CFG_C);
		ee_c_buffer.append(commentWriterC.writerBanner("System Calls") + 
				"const EE_FADDR EE_syscall_table[EE_SYSCALL_NR] = {\n");
				
		StringBuffer ids = new StringBuffer();
//		StringBuffer shortNames = new StringBuffer();
//		ee_asm_buffer.append("#ifndef __EE_ASM_H__\n" +
//				"#define __EE_ASM_H__\n\n");
		
		
		// this counter is the ID of the next sys call
		int counter = 0;
//		final String end = ",\n\n";
		
		
		// OS Services
		for (String s: EE_OS_INTERRUPT_IDs) {
			ids.append("#define EE_ID_"+s+ (s.length()<40 ? white_spaces.substring(0,40-s.length()) :"") + (counter <10 ? " " : "") + counter +"\n");
			ee_c_buffer.append(indent1+"(EE_FADDR)&EE_as_"+s+",\n");
			counter ++;
		}
		int interrupts_last_id = counter -1;
		
		// OS Services
		for (String s: EE_OS_SERVICES_IDs) {
			ids.append("#define EE_ID_"+s+ (s.length()<40 ? white_spaces.substring(0,40-s.length()) :"") + (counter <10 ? " " : "") + counter +"\n");
			ee_c_buffer.append(indent1+"(EE_FADDR)&EE_oo_"+s+",\n");
			counter ++;
		}
		
		{
			boolean isr2_enabled = false;
			for (ISimpleGenRes isr :  ool.getList(IOilObjectList.ISR)) {
				if (isr.containsProperty(ISimpleGenResKeywords.ISR_CATEGORY) && "2".equals(isr.getString(ISimpleGenResKeywords.ISR_CATEGORY))) {
					isr2_enabled = true;
					break;
				}
			}
			
			if (isr2_enabled) {
				for (String s: EE_ISR2_IDs) {
					ids.append("#define EE_ID_"+s+ (s.length()<40 ? white_spaces.substring(0,40-s.length()) :"") + (counter <10 ? " " : "") + counter +"\n");
					ee_c_buffer.append(indent1+"(EE_FADDR)&EE_as_"+s+",\n");
					counter ++;
				}	
			}
			
			
		}
		
		{
		
			Integer sections = (Integer) AbstractRtosWriter.getOsObject(ool, OsekOrtiConstants.OS_CPU_ORTI_ENABLED_SECTIONS);
			if (sections != null && (sections.intValue() & OsekOrtiConstants.EE_ORTI_OS) != 0) {
					String s="ORTI_ext_set_service";
					ids.append("#define EE_ID_"+s+ (s.length()<40 ? white_spaces.substring(0,40-s.length()) :"") + (counter <10 ? " " : "") + counter +"\n");
				ee_c_buffer.append(indent1+"(EE_FADDR)&EE_as_ORTI_set_service,\n");
					counter ++;
				}
			}
		
		// Resources
		if (ool.getList(IOilObjectList.RESOURCE).size() > 0
				|| requiredOilObjects.contains(new Integer(IOilObjectList.RESOURCE)))  {

			for (String s: EE_RESOURCES_IDs) {
				ids.append("#define EE_ID_"+s+ (s.length()<40 ? white_spaces.substring(0,40-s.length()) :"") + (counter <10 ? " " : "") + counter +"\n");
				ee_c_buffer.append(indent1+"(EE_FADDR)&EE_oo_"+s+",\n");
				counter ++;
			}
		}
		
		// Alarm
		if ( ool.getList(IOilObjectList.ALARM).size() > 0 
				|| requiredOilObjects.contains(new Integer(IOilObjectList.ALARM)))  {

			for (String s: EE_ALARMS_IDs) {
				ids.append("#define EE_ID_"+s+ (s.length()<40 ? white_spaces.substring(0,40-s.length()) :"") + (counter <10 ? " " : "") + counter +"\n");
				ee_c_buffer.append(indent1+"(EE_FADDR)&EE_oo_"+s+",\n");
				counter ++;
			}
		}
		
		// Events
		if (parent.checkKeyword(IWritersKeywords.OSEK_ECC1) 
				|| parent.checkKeyword(IWritersKeywords.OSEK_ECC2))  {
			if (ool.getList(IOilObjectList.EVENT).size() > 0 
					|| requiredOilObjects.contains(new Integer(IOilObjectList.EVENT))) { 
	
				for (String s: EE_ECC1_2_IDs) {
					ids.append("#define EE_ID_"+s+ (s.length()<40 ? white_spaces.substring(0,40-s.length()) :"") + (counter <10 ? " " : "") + counter +"\n");
					ee_c_buffer.append(indent1+"(EE_FADDR)&EE_oo_"+s+",\n");
					counter ++;
				}
			}
		}

		
		// Semaphores
		{
			String[] eeOpts = parent.extractEE_Opts(EE_OPT_ALL, rtosId);
			boolean enabled = false;
			for (String s: eeOpts) {
				if ("__OO_SEM__".equals(s)) {
					enabled = true;
					break;
				}
			}

			if (enabled) {
				for (String s: EE_SEM_IDs) {
					ids.append("#define EE_ID_"+s+ (s.length()<40 ? white_spaces.substring(0,40-s.length()) :"") + (counter <10 ? " " : "") + counter +"\n");
					ee_c_buffer.append(indent1+"(EE_FADDR)&EE_oo_"+s+",\n");
					counter ++;
				}
			}
		}

		int max_sys_serviceId = counter;

		// Trusted Functions
		{
			List<ISimpleGenRes> os_appls = ool.getList(IOilObjectList.OSAPPLICATION);
			for (ISimpleGenRes appl: os_appls) {
				if (appl.containsProperty(IEEWriterKeywords.OS_APPLICATION_TRUSTED) 
						&& "true".equalsIgnoreCase(appl.getString(IEEWriterKeywords.OS_APPLICATION_TRUSTED))) {
					
					List<String> functions = appl.containsProperty(IEEWriterKeywords.OS_APPLICATION_TRUSTED_FUNCTIONS) ?
							(List<String>) appl.getObject(IEEWriterKeywords.OS_APPLICATION_TRUSTED_FUNCTIONS) : new ArrayList<String>();
					
					if (functions.size()>0) {
						ee_c_buffer.insert(0, "#include \"trusted.h\"\n");
					}
							
					for (String s: functions) {
						ids.append("#define EE_ID_TRUSTED_"+s+ (s.length()<40 ? white_spaces.substring(0,32-s.length()) :"") + (counter <10 ? " " : "") + counter +"\n");
						ee_c_buffer.append(indent1+"(EE_FADDR)&TRUSTED_"+s+",\n");
						counter ++;
					}
					
				}
			}
			
		}

		final StringBuffer ee_buffer = answer.get(FILE_EE_CFG_H);
		ee_buffer.append(commentWriterH.writerBanner("System Calls")
				+ ids.toString() + "\n"
				+ "#define EE_ID_interrupts_last                         "+ (interrupts_last_id <10 ? " " : "") + interrupts_last_id +"\n"
				+ "#define EE_MAX_SYS_SERVICEID                          " + (max_sys_serviceId <10 ? " " : "") + max_sys_serviceId + "\n"
				+ "#define EE_SYSCALL_NR                                 " + (counter <10 ? " " : "") + (counter)+ "\n");

		ee_c_buffer.append("};\n\n");
	}
	


	@Override
	public List<String> getEEOpt(int type, int rtosId) {
		List<String> answer = new ArrayList<String>();
		// nothing
		return answer;
	}

	@Override
	public void updateKeywords(ArrayList<String> keywords, String[] rtosPrefix)
			throws OilCodeWriterException {
		// TODO Auto-generated method stub
		
	}

}
