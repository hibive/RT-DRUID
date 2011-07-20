/*
 * Created on 19/feb/2010
 *
 * $Id: PIC32Configurator.java,v 1.4 2008/03/26 18:23:55 durin Exp $
 */
package com.eu.evidence.rtdruid.modules.oil.dspic.ui.preferencepages;



import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.eu.evidence.modules.oil.dspic.constants.PicConstants;
import com.eu.evidence.rtdruid.modules.oil.ee.ui.preferencepages.IOPPConstants;
import com.eu.evidence.rtdruid.ui.preferencepages.AbstractPage;

/**
 * This class implements a preference page about PIC32 Configuration.
 * 
 * @author Nicola Serreli
 */
public class PIC32Configurator extends AbstractPage {

	private Text paramGcc = null;
    private Text paramAsm = null;
    
	/**
	 * (non-Javadoc) Method declared on PreferencePage
	 */
	protected Control createContents(Composite parent) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent, IOPPConstants.OS_CONF_CONTEXT);

		//composite_tab << parent
		Composite composite_tab = createComposite(parent, 3);
		composite_tab.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL));

		createLabel(composite_tab, "Gcc path", 1); //$NON-NLS-1$
		paramGcc = createTextField(composite_tab); //$NON-NLS-1$
		Button gccButton = createPushButton(composite_tab, "Browse"); //$NON-NLS-1$
		gccButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {	work(e);	}
			public void widgetDefaultSelected(SelectionEvent e) {	work(e);	}
			protected void work(SelectionEvent e) {
				DirectoryDialog dia = new DirectoryDialog(getShell());
				String path = dia.open();
				if (path!=null) {
					paramGcc.setText(path);
				}
			}
		});
		
		createLabel(composite_tab, "Asm path", 1); //$NON-NLS-1$
		paramAsm = createTextField(composite_tab); //$NON-NLS-1$
		Button asmButton = createPushButton(composite_tab, "Browse"); //$NON-NLS-1$
		asmButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {	work(e);	}
			public void widgetDefaultSelected(SelectionEvent e) {	work(e);	}
			protected void work(SelectionEvent e) {
				DirectoryDialog dia = new DirectoryDialog(getShell());
				String path = dia.open();
				if (path!=null) {
					paramAsm.setText(path);
				}
			}
		});

		
		initializeValues();

		//font = null;
		return new Composite(parent, SWT.NULL);
	}

	
	//--------------------------------------------------
	
	/**
	 * (non-Javadoc) Method declared on SelectionListener
	 */
	public void widgetDefaultSelected(SelectionEvent event) {
		widgetSelected(event);
	}

	/**
	 * (non-Javadoc) Method declared on SelectionListener
	 */
	public void widgetSelected(SelectionEvent event) {
	}
	
	// --------------------------------------------------
	
	/*
	 * (non-Javadoc) Method declared on IWorkbenchPreferencePage
	 */
	public void init(IWorkbench workbench) {
	}

	/**
	 * Initializes states of the controls using default values in the preference
	 * store.
	 */
	private void initializeDefaults() {
		paramAsm.setText(PicConstants.DEFAULT_PIC32_CONF_ASM);
		paramGcc.setText(PicConstants.DEFAULT_PIC32_CONF_GCC);

		enableOk();
	}

	/**
	 * Initializes states of the controls from the preference store.
	 */
	private void initializeValues() {
		IPreferenceStore store = getPreferenceStore();

		String asm = store.contains(Options.PIC32_CONF_ASM) ?
				store.getString(Options.PIC32_CONF_ASM) 
				: PicConstants.DEFAULT_PIC32_CONF_ASM;

		paramAsm.setText(asm);
		
		String gcc = store.contains(Options.PIC32_CONF_GCC) ?
				store.getString(Options.PIC32_CONF_GCC) 
				: PicConstants.DEFAULT_PIC32_CONF_GCC;
		paramGcc.setText(gcc);

		enableOk();
	}

	
    /**
     * Check all possible errors and then  enable next button if it's all ok
     * or disable it and print an error message if there is at least one error   
     */
	protected void enableOk() {
		
		// disable previous Error and warning messages
        setMessage(null, PIC32Configurator.WARNING);
        setMessage(null, PIC32Configurator.ERROR);
	}

	/*
	 * (non-Javadoc) Method declared on PreferencePage
	 */
	protected void performDefaults() {
		super.performDefaults();
		initializeDefaults();
	}

	/**
	 * Stores the values of the controls back to the preference store.
	 */
	protected void storeValues() {
		IPreferenceStore store = getPreferenceStore();

		store.setValue(Options.PIC32_CONF_ASM, paramAsm.getText());
		store.setValue(Options.PIC32_CONF_GCC, paramGcc.getText());
	}

}