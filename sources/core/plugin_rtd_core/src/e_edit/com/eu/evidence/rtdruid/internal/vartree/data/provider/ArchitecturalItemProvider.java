/**
 * <copyright>
 * </copyright>
 *
 * $Id: ArchitecturalItemProvider.java,v 1.1 2005/09/28 15:22:29 durin Exp $
 */
package com.eu.evidence.rtdruid.internal.vartree.data.provider;




import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;

import com.eu.evidence.rtdruid.DataEditPlugin;
import com.eu.evidence.rtdruid.vartree.data.Architectural;
import com.eu.evidence.rtdruid.vartree.data.DataFactory;
import com.eu.evidence.rtdruid.vartree.data.DataPackage;
import com.eu.evidence.rtdruid.vartree.data.init.ObjectWithIDItemProvider;

/**
 * This is the item provider adpater for a {@link rtdruid.vartree.data.Architectural} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ArchitecturalItemProvider
	extends ObjectWithIDItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ArchitecturalItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public List getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

		}
		return itemPropertyDescriptors;
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Collection getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(DataPackage.eINSTANCE.getArchitectural_BusList());
			childrenFeatures.add(DataPackage.eINSTANCE.getArchitectural_EcuList());
			childrenFeatures.add(DataPackage.eINSTANCE.getArchitectural_FrameList());
			childrenFeatures.add(DataPackage.eINSTANCE.getArchitectural_MutexList());
			childrenFeatures.add(DataPackage.eINSTANCE.getArchitectural_ResourceList());
			childrenFeatures.add(DataPackage.eINSTANCE.getArchitectural_SignalList());
			childrenFeatures.add(DataPackage.eINSTANCE.getArchitectural_TaskList());
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}


	/**
	 * This returns Architectural.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object getImage(Object object) {
		return getResourceLocator().getImage("full/obj16/Architectural");
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getText(Object object) {
		return getString("_UI_Architectural_type");
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(Architectural.class)) {
			case DataPackage.ARCHITECTURAL__BUS_LIST:
			case DataPackage.ARCHITECTURAL__ECU_LIST:
			case DataPackage.ARCHITECTURAL__FRAME_LIST:
			case DataPackage.ARCHITECTURAL__MUTEX_LIST:
			case DataPackage.ARCHITECTURAL__RESOURCE_LIST:
			case DataPackage.ARCHITECTURAL__SIGNAL_LIST:
			case DataPackage.ARCHITECTURAL__TASK_LIST:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds to the collection of {@link org.eclipse.emf.edit.command.CommandParameter}s
	 * describing all of the children that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void collectNewChildDescriptors(Collection newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);

		newChildDescriptors.add
			(createChildParameter
				(DataPackage.eINSTANCE.getArchitectural_BusList(),
				 DataFactory.eINSTANCE.createBus()));

		newChildDescriptors.add
			(createChildParameter
				(DataPackage.eINSTANCE.getArchitectural_EcuList(),
				 DataFactory.eINSTANCE.createEcu()));

		newChildDescriptors.add
			(createChildParameter
				(DataPackage.eINSTANCE.getArchitectural_FrameList(),
				 DataFactory.eINSTANCE.createFrame()));

		newChildDescriptors.add
			(createChildParameter
				(DataPackage.eINSTANCE.getArchitectural_MutexList(),
				 DataFactory.eINSTANCE.createMutex()));

		newChildDescriptors.add
			(createChildParameter
				(DataPackage.eINSTANCE.getArchitectural_ResourceList(),
				 DataFactory.eINSTANCE.createResource()));

		newChildDescriptors.add
			(createChildParameter
				(DataPackage.eINSTANCE.getArchitectural_SignalList(),
				 DataFactory.eINSTANCE.createSignal()));

		newChildDescriptors.add
			(createChildParameter
				(DataPackage.eINSTANCE.getArchitectural_TaskList(),
				 DataFactory.eINSTANCE.createTask()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ResourceLocator getResourceLocator() {
		return DataEditPlugin.INSTANCE;
	}
}
