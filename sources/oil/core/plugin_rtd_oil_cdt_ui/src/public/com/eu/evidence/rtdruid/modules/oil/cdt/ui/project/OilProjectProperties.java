/*
 * Created on 21-giu-2005
 *
 * $Id: OilProjectProperties.java,v 1.3 2007/03/09 09:12:41 durin Exp $
 */
package com.eu.evidence.rtdruid.modules.oil.cdt.ui.project;

/**
 * 
 * TODO : Commentare && Tradurre
 * 
 * @author Nicola Serreli
 */


import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.PropertyPage;

import com.eu.evidence.rtdruid.desk.RtdruidLog;
import com.eu.evidence.rtdruid.modules.oil.cdt.ui.Rtd_oil_cdt_Plugin;
import com.eu.evidence.rtdruid.modules.oil.cdt.ui.builder.OilBuilder;
import com.eu.evidence.rtdruid.modules.oil.codewriter.common.CommonUtils;
import com.eu.evidence.rtdruid.ui.common.OneResourceSelectionDialog;
import com.eu.evidence.rtdruid.vartree.data.init.RTD_XMI_Factory;

public class OilProjectProperties extends PropertyPage {

    /**
     * Current project
     */
    private IProject project;
    /**
     * Contains the Config File Name
     */
    private String configFile = "";;
    private Text text;
    /** 
     * Contains a string that describes an error inside the Config File name, 
     * or null if it's all ok.
     */
    private String configFileError = null;
    private int problemType = super.NONE;
    
	public OilProjectProperties() {
		super();
	}
	
	protected void init() {
	    // nothing to do if project is null
	    if (project == null) {
	        return;
	    }
	    
	    // init Config File
	    Map attributes = OilBuilder.getParameters(project);
	    if (attributes != null && attributes.containsKey(OilBuilder.ATTR_CONFIG_FILE)) {
	        configFile = (String) attributes.get(OilBuilder.ATTR_CONFIG_FILE);
	    }
	}
	
		
	protected Control createContents(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE | SWT.FLAT);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.makeColumnsEqualWidth = false;
        layout.marginHeight = 10;
        layout.marginWidth = 10;
		layout.horizontalSpacing = 5;
		layout.verticalSpacing = 5;
		composite.setLayout(layout);

		IProject project = getProject();
		if (!project.isOpen()) {
			contentForClosedProject(composite);
		} else {
			contentForCProject(composite);
		}

		return composite;
	}

	private void contentForCProject(Composite parent) {
        /*
         * Prepare all compontens
         */
		Group current = new Group(parent, SWT.NONE);
		{
		    current.setText("Configuration file");
    		GridLayout layout = new GridLayout();
    		layout.numColumns = 2;
    		layout.makeColumnsEqualWidth = false;
	        layout.marginHeight = 10;
	        layout.marginWidth = 10;
    		layout.horizontalSpacing = 5;
    		layout.verticalSpacing = 5;
    		current.setLayout(layout);
    		current.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
		

		// Label
		Label label = new Label(current, SWT.LEFT);
		{
    		label.setText("File name");
			GridData data = new GridData();
			data.horizontalAlignment = GridData.FILL;
			data.horizontalSpan = 2;
    		label.setLayoutData(data);
		}

		// Text
		text = new Text(current, SWT.SINGLE | SWT.BORDER);
		{
		    text.setText(configFile);
    		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL));
    		
    		text.addModifyListener(new ModifyListener() {
                public void modifyText(ModifyEvent e) {
                    if (e.getSource() == text) {
                    	
                    	final String newName = text.getText();
                    	
                    	problemType = OilProjectProperties.super.NONE;
                        /*
                         * Check if config file is valid
                         */
                    	configFileError = CommonUtils.checkConfigFileName(newName);
                    	
                    	if (configFileError != null) {
                    		problemType = OilProjectProperties.super.ERROR;
                    	} else {
                    	
	                    	/*
	                    	 * Check if exist 
	                    	 */
	                    	IResource res = getProject().findMember(new Path(newName));
	                    	if (res == null) {
	                    		
	                    		configFileError = "Specified file doesn't exist";
	                    		problemType = OilProjectProperties.super.WARNING;
	                    		
	                    	} else if (res.getType() != IResource.FILE){
	                    		configFileError = "Specified configuration file identifies a folder";
	                    		problemType = OilProjectProperties.super.ERROR;
	                    		
	                    	} else {
	                    		configFile = newName;
	                    	}
                    	}
	                        
                        // update the status of this page 
                        enableOk();
                    }
                }
            });
		}
		
		Button browseForConfFileButton;
		browseForConfFileButton = new Button(current,SWT.PUSH);
		browseForConfFileButton.setText(
				"Browse" /*IDEWorkbenchMessages.getString("WizardImportPage.browse2")*/);
				//Rtdruid_module_oil_niosPlugin.getString("OilProjectProperties.common.Browse"));
		browseForConfFileButton.setLayoutData(new GridData());
		browseForConfFileButton.addSelectionListener(new SelectionListener() {
			/* (non-Javadoc)
			 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent)
			 */
			public void widgetDefaultSelected(SelectionEvent e) {
				handleBinaryBrowseButtonSelected();
			}
			
			public void widgetSelected(SelectionEvent evt) {
				handleBinaryBrowseButtonSelected();
			}
		});


		
		// Label
		label = new Label(parent, SWT.LEFT);
		{
    		label.setText("");
    		label.setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL | GridData.GRAB_HORIZONTAL));
		}
	}

	/**
	 * Show a dialog that lets the user select an oil file, from current project
	 */
	protected void handleBinaryBrowseButtonSelected() {
		
		ElementTreeSelectionDialog dialog = new OneResourceSelectionDialog(
				getShell(),
				Rtd_oil_cdt_Plugin.getString("OilProjectProperties.ConfFile_Selection"),
				Rtd_oil_cdt_Plugin.getFormattedString(
						"OilProjectProperties.Choose_configuration_file_for_NAME", project.getName()),
				getProject(),
				OneResourceSelectionDialog.getStandardValidator(Rtd_oil_cdt_Plugin.PLUGIN_ID, RTD_XMI_Factory.getAllImportTypes())
			).getDialog();

		if (dialog.open() == Window.CANCEL) {
			return;
		}

		Object[] results = dialog.getResult();

		try {
			text.setText(((IResource) results[0]).getProjectRelativePath().toString());
		} catch (Exception ex) {
			/* Why ?? */
			RtdruidLog.log(ex);
		}

	}

	
	
	private void contentForClosedProject(Composite parent) {
		//Label label = new Label(parent, SWT.LEFT);
		//label.setText("Label text");

		noDefaultAndApplyButton();
	}

    /**
     * Check all possible errors and then  enable next button if it's all ok
     * or disable it and print an error message if there is at least one error   
     */
	protected void enableOk() {
		
		// disable previous Error and warning messages
        setMessage("", super.WARNING);
        setMessage("", super.ERROR);

		
		String messg = null;
		
		/* 
		 * check all strings ...
		 */
		
		// ... config ...
		if (configFileError != null) {
		    messg = configFileError;
		}
        
		/*
		 * Update "next" button and "Error message"
		 */
		
		// enable next only if it's all ok
		setValid(problemType != super.ERROR);
    	
        // print error message 
        setMessage(messg, problemType);
	}

	public boolean performOk() {

	    Map attributes = OilBuilder.getParameters(project);
	    if (attributes != null && attributes.containsKey(OilBuilder.ATTR_CONFIG_FILE)) {
	        attributes.put(OilBuilder.ATTR_CONFIG_FILE, configFile);
	        OilBuilder.setParameters(project, attributes);
	    }
	    
		return true;
	}
	
	/* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
    protected void performDefaults() {
        super.performDefaults();
        
        init();
        if (text != null) {
            text.setText(configFile);
        }
    }

	public IProject getProject() {
		Object element = getElement();
		if (element instanceof IProject) {
			return (IProject) element;
		}
		return null;
	}
	
	/* (non-Javadoc)
     * @see org.eclipse.ui.dialogs.PropertyPage#setElement(org.eclipse.core.runtime.IAdaptable)
     */
    public void setElement(IAdaptable element) {
        // TODO Auto-generated method stub
        super.setElement(element);
		if (element instanceof IProject) {
			project = (IProject) element;
		}
        
        init();
    }

}
