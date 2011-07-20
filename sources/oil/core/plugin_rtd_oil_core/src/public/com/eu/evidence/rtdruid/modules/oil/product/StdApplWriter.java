package com.eu.evidence.rtdruid.modules.oil.product;

import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

import com.eu.evidence.rtdruid.internal.modules.oil.workers.Logger;
import com.eu.evidence.rtdruid.internal.modules.oil.workers.StdOutLogger;
import com.eu.evidence.rtdruid.internal.modules.oil.workers.WorkerOilConfWriter;

public class StdApplWriter implements IApplication {

	@Override
	public Object start(IApplicationContext context) throws Exception {
		Object o = context.getArguments().get(IApplicationContext.APPLICATION_ARGS);
		String[] args = new String[0];
		if (o instanceof String[]) {
			args = (String[]) o;
//			for (String s : (String[]) o) {
//				System.out.println(s);
//			}
		}
		Logger log = new StdOutLogger();

		try {
			ParameterParser pt = new ParameterParser();
			
			WorkerOilConfWriter worker = pt.parseWriter(args);
			log = worker.getLogger();
			worker.execute();
		} catch (IllegalArgumentException e) {
			log.log(e.getMessage());
			return new Integer(-1);
		} catch (Throwable e) {
			log.log(e.getMessage());
//			e.printStackTrace();
			return new Integer(-1);
		}
		return new Integer(0);
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}
}
