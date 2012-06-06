package com.eu.evidence.rtdruid.internal.modules.oil.cortex;


class Mcu_Model {
	public static final String SEPARATOR = "[\t ]";
	public static final String EMPTY = "?";

	
	
		public final String name;
		public final String model;
		public final String ee_opt;
		public final String linkerScript;
		public final String include_c;
		public final String include_s;
		public final String startup;
		Mcu_Model(String name, String model, String ee_opt, String linker, String inc_c, String inc_s, String startup) {
			this.name = name;
			this.model = model;
			this.ee_opt = ee_opt;
			this.linkerScript = linker;
			this.include_c = inc_c;
			this.include_s = inc_s;
			this.startup = startup;
		}
		
		Mcu_Model(String row) {
			String[] values = row.split(SEPARATOR); 
			
			// not valid !!!
			if (values.length!=5) {
				throw new RuntimeException("Illegal row\n" + (new String(row)));
			}
//			#OIL/MODEL	LINKERSCRIPT	INCL_C	INCL_S	STARTUP	

			this.name   = values[0];
			this.model  = name.startsWith("CORTEX") ?	name.substring(5) : name;
			this.ee_opt = "__" + model + "__ __"+ name +"__";
			this.linkerScript = clean(values[1]);
			this.include_c  = clean(values[2]);
			this.include_s  = clean(values[3]);
			this.startup  = clean(values[4]);
		}

		protected String clean(String s) {
			s = s.trim();
			return EMPTY.equals(s) ? null : s;
		}
	}