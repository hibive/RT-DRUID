/**
 * 13/gen/2012
 */
package com.eu.evidence.rtdruid.test.vartree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.junit.Test;

import com.eu.evidence.rtdruid.vartree.IVarTree;
import com.eu.evidence.rtdruid.vartree.VarTreeUtil;
import com.eu.evidence.rtdruid.vartree.VarTreeUtil.VarTreeCreator;

/**
 *
 * @author Nicola Serreli
 * @since 2.0
 *
 */
public class VarTreeUtilTest {


	private void compareVtLike(IVarTree vt1, IVarTree vt2) {
		assertNotNull(vt1);
		assertNotNull(vt2);
		assertNotSame(vt1, vt2);
		
		assertNotNull(vt1.getResourceSet());
		assertNotNull(vt2.getResourceSet());

		assertNotSame(vt1.getResourceSet(), vt2.getResourceSet());
		
		assertNotNull(vt1.getAdapterFactory());
		assertNotNull(vt2.getAdapterFactory());
		assertSame(vt1.getAdapterFactory().getClass(), vt2.getAdapterFactory().getClass());
	}

	@Test
	public void testVarTreeUtilNewInstance() {
		compareVtLike(VarTreeUtil.newVarTree(), VarTreeUtil.newVarTree());
		compareVtLike(VarTreeUtil.newVarTree(new BasicCommandStack()), VarTreeUtil.newVarTree(new BasicCommandStack()));
		compareVtLike(VarTreeUtil.newVarTree(), VarTreeUtil.newVarTree(new BasicCommandStack()));
	}
	
	@Test
	public void testMultipleInstance() {
		
		{
			VarTreeCreator vtc = new VarTreeUtil.VarTreeCreator();
			IVarTree vt1 = vtc.istantiateVarTree();
			IVarTree vt2 = vtc.istantiateVarTree();
			compareVtLike(vt1, vt2);
		}

		{
			VarTreeCreator vtc = new VarTreeUtil.VarTreeCreator();
			CommandStack cs = new BasicCommandStack();
			vtc.setCommandStack(cs);
			IVarTree vt1 = vtc.istantiateVarTree();
			IVarTree vt2 = vtc.istantiateVarTree();
			compareVtLike(vt1, vt2);
			CommandStack cs2 = new BasicCommandStack();
			vtc.setCommandStack(cs2);
			IVarTree vt3 = vtc.istantiateVarTree();

			assertSame(cs, vt1.getCommandStack());
			assertSame(cs, vt2.getCommandStack());
			assertSame(cs2, vt3.getCommandStack());
		}

		{
			VarTreeCreator vtc = new VarTreeUtil.VarTreeCreator();
			AdapterFactory af = new ComposedAdapterFactory();
			vtc.setAdapterFactory(af);
			IVarTree vt1 = vtc.istantiateVarTree();
			IVarTree vt2 = vtc.istantiateVarTree();
			compareVtLike(vt1, vt2);
			AdapterFactory af2 = new ComposedAdapterFactory();
			vtc.setAdapterFactory(af2);
			IVarTree vt3 = vtc.istantiateVarTree();

			assertSame(af, vt1.getAdapterFactory());
			assertSame(af, vt2.getAdapterFactory());
			assertSame(af2, vt3.getAdapterFactory());

			assertNotSame(vt1.getCommandStack(), vt2.getCommandStack());
			assertNotSame(vt1.getCommandStack(), vt3.getCommandStack());
			assertNotSame(vt2.getCommandStack(), vt3.getCommandStack());
		}

		{
			VarTreeCreator vtc = new VarTreeUtil.VarTreeCreator();
			EPackage epkg1 = EcoreFactory.eINSTANCE.createEPackage();
			epkg1.setNsURI("someting");
			vtc.setEpkg(epkg1);
			IVarTree vt1 = vtc.istantiateVarTree();
			IVarTree vt2 = vtc.istantiateVarTree();
			compareVtLike(vt1, vt2);
			EPackage epkg2 = EcoreFactory.eINSTANCE.createEPackage();
			epkg2.setNsURI("someting");
			vtc.setEpkg(epkg2);
			IVarTree vt3 = vtc.istantiateVarTree();

			assertSame(epkg1, vt1.getResourceSet().getPackageRegistry().get("someting"));
			assertSame(epkg1, vt2.getResourceSet().getPackageRegistry().get("someting"));
			assertSame(epkg2, vt3.getResourceSet().getPackageRegistry().get("someting"));

			assertNotSame(vt1.getCommandStack(), vt2.getCommandStack());
			assertNotSame(vt1.getCommandStack(), vt3.getCommandStack());
			assertNotSame(vt2.getCommandStack(), vt3.getCommandStack());
		}

		{
			CommandStack cs = new BasicCommandStack();
			AdapterFactory af = new ComposedAdapterFactory();
			EPackage epkg = EcoreFactory.eINSTANCE.createEPackage();
			epkg.setNsURI("someting");

			IVarTree[] vts = new IVarTree[6];
			
			vts[0] = (new VarTreeUtil.VarTreeCreator()).setCommandStack(cs).setAdapterFactory(af).setEpkg(epkg).istantiateVarTree();
			vts[1] = (new VarTreeUtil.VarTreeCreator()).setCommandStack(cs).setEpkg(epkg).setAdapterFactory(af).istantiateVarTree();
			vts[2] = (new VarTreeUtil.VarTreeCreator()).setAdapterFactory(af).setCommandStack(cs).setEpkg(epkg).istantiateVarTree();
			vts[3] = (new VarTreeUtil.VarTreeCreator()).setAdapterFactory(af).setEpkg(epkg).setCommandStack(cs).istantiateVarTree();
			vts[4] = (new VarTreeUtil.VarTreeCreator()).setEpkg(epkg).setCommandStack(cs).setAdapterFactory(af).istantiateVarTree();
			vts[5] = (new VarTreeUtil.VarTreeCreator()).setEpkg(epkg).setAdapterFactory(af).setCommandStack(cs).istantiateVarTree();

			for (IVarTree vt : vts) {
				
				assertSame(cs, vt.getCommandStack());
				assertSame(epkg, vt.getResourceSet().getPackageRegistry().get("someting"));
				assertSame(af, vt.getAdapterFactory());
			}
			
			
		}

	}

	@Test
	public void testVarTreeCreatorChain() {
		VarTreeCreator vtc = new VarTreeUtil.VarTreeCreator();
		assertSame(vtc, vtc.setAdapterFactory(null));
		assertSame(vtc, vtc.setCommandStack(null));
		assertSame(vtc, vtc.setEpkg(null));
		assertSame(null, VarTreeCreator.addEPackage(null, null));
		
		assertSame(vtc, vtc.setAdapterFactory(new ComposedAdapterFactory()));
		assertSame(vtc, vtc.setCommandStack(new BasicCommandStack()));
		assertSame(vtc, vtc.setEpkg(EcoreFactory.eINSTANCE.createEPackage()));
		assertSame(null, VarTreeCreator.addEPackage(null, EcoreFactory.eINSTANCE.createEPackage()));
	}

	@Test(expected=NullPointerException.class)
	public void testVarTreeCreatorAddNullEPackage() {
		EPackage epkg = EcoreFactory.eINSTANCE.createEPackage();
		epkg.setNsURI("someting");
		VarTreeCreator.addEPackage(null, epkg);
	}

	@Test
	public void testVarTreeCreatorAddEPackage() {

		{
			ResourceSet res = new ResourceSetImpl();
			EditingDomain ed = new AdapterFactoryEditingDomain(new ComposedAdapterFactory(), new BasicCommandStack(), res);
			final int size = res.getPackageRegistry().keySet().size();
			assertSame(ed, VarTreeCreator.addEPackage(ed, null));
			assertEquals(size, res.getPackageRegistry().keySet().size());
		}
		
		{
			ResourceSet res = new ResourceSetImpl();
			EditingDomain ed = new AdapterFactoryEditingDomain(new ComposedAdapterFactory(), new BasicCommandStack(), res);
			final int size = res.getPackageRegistry().keySet().size();
			assertSame(ed, VarTreeCreator.addEPackage(ed, EcoreFactory.eINSTANCE.createEPackage()));
			assertEquals(size, res.getPackageRegistry().keySet().size());
		}

		{
			ResourceSet res = new ResourceSetImpl();
			EditingDomain ed = new AdapterFactoryEditingDomain(new ComposedAdapterFactory(), new BasicCommandStack(), res);
			final int size = res.getPackageRegistry().keySet().size();
			EPackage epkg = EcoreFactory.eINSTANCE.createEPackage();
			epkg.setNsURI("someting");
			assertSame(ed, VarTreeCreator.addEPackage(ed, epkg));
			assertEquals(size+1, res.getPackageRegistry().keySet().size());
			assertSame(epkg, res.getPackageRegistry().get("someting"));
			EPackage epkg2 = EcoreFactory.eINSTANCE.createEPackage();
			epkg2.setNsURI("someting");
			assertNotSame(epkg, epkg2);
			assertSame(ed, VarTreeCreator.addEPackage(ed, epkg2));
			assertEquals(size+1, res.getPackageRegistry().keySet().size());
			assertSame(epkg2, res.getPackageRegistry().get("someting"));
		}

	}

}
