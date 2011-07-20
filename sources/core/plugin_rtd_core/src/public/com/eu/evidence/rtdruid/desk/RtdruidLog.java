/*
 * Created on May 9, 2005
 *
 * $Id: RtdruidLog.java,v 1.3 2008/01/24 15:57:10 durin Exp $
 */
package com.eu.evidence.rtdruid.desk;

import com.eu.evidence.rtdruid.Rtd_corePlugin;


/**
 *
 * This class is a wrapper to Eclipse Log  
 *
 * @author Nicola Serreli
 */
public class RtdruidLog {
	
	private static boolean FINAL = false; 

	/** This method logs a String as an Exception */
	public static void log(String txt) {
		if (txt == null) {
			txt = "";
		}
		RtdruidLog.log(new RuntimeException(txt));
	}

	/** This method logs a String with an Exception */
	public static void log(String txt, Throwable e) {
		if (e == null) {
			log(txt);
			return;
		}
		if (txt == null)
			txt = "null";
		RtdruidLog.log(new RuntimeException(txt, e));
		return;
	}

	/**
     * This method works like "e.printStackTrace()" during testing and software
     * prodution, and does nothing in final version (FINAL == true).
     */
	public static void showDebug(Throwable e) {
		
		if (!FINAL) {
			// print the stack trace
			System.err.print(">>> ");
			e.printStackTrace();
			Rtd_corePlugin.log(e);
			System.err.println("<<<");
		}
	}

	/**
     * This method works like "e.printStackTrace()" during testing and software
     * prodution, and does nothing in final version (FINAL == true).
     */
	public static void showDebug(String txt) {
		
		if (!FINAL) {
			// print the stack trace
			System.err.print(">>> " + txt + "<<<");
		}
	}

	/** This method logs Exceptions and Errors */
	public static void log(Throwable e) {
		
		if (FINAL) {

			// clear stack Trace of this Exception and all its causes !!
			for (Throwable tmp = e; tmp !=null; tmp = tmp.getCause()) {
				tmp.setStackTrace(new StackTraceElement[] {});
			}
			
		} else {
			// print the stack trace
			e.printStackTrace();
		}

		Rtd_corePlugin.log(e);
	}
}
