package com.eu.evidence.rtdruid.internal.modules.oil.cortex;

class Mcu_Model {
	public static final String SEPARATOR = "[\t ]";
	public static final String EMPTY = "?";
	public static final String EEOPT_SEPARATOR = "@";

	
		public final String useCustom_prefix;
	
		public final String name;
		public final String model;
		public final String ee_opt;
		public final String linkerScript;
		public final String include_c;
		public final String include_s;
		public final String startup;
		Mcu_Model(String name, String model, String ee_opt, String linker, String inc_c, String inc_s, String startup) {
			this(name, model, ee_opt, linker, inc_c, inc_s, startup, "");
		}
		Mcu_Model(String name, String model, String ee_opt, String linker, String inc_c, String inc_s, String startup, String custom_prefix) {
			this.name = name;
			this.model = model;
			this.ee_opt = ee_opt;
			this.linkerScript = linker;
			this.include_c = inc_c;
			this.include_s = inc_s;
			this.startup = startup;
			this.useCustom_prefix = custom_prefix;
		}
		
		Mcu_Model(String row) {
			this(row, null);
		}
		Mcu_Model(String full_row, String custom_prefix) {
			StringBuffer tmp_ee_opt = new StringBuffer();
			this.useCustom_prefix = custom_prefix;
			
			String[] semi_row = full_row.split(EEOPT_SEPARATOR);
			
			// not valid !!!
			if (semi_row.length>2) {
				throw new RuntimeException("Illegal row\n" + (new String(full_row)));
			}
			String row = semi_row[0];
			String[] values = row.split(SEPARATOR); 
			
			// not valid !!!
			if (values.length!=5) {
				throw new RuntimeException("Illegal row\n" + (new String(row)));
			}
//			#OIL/MODEL	LINKERSCRIPT	INCL_C	INCL_S	STARTUP  @ EEOPTs

			String prefix = useCustom_prefix == null ? "__" : useCustom_prefix;

			this.name   = values[0];
			this.model  = name.startsWith("CORTEX") ?	name.substring(5) : name;
			tmp_ee_opt.append( prefix + model + "__ " + prefix + name +"__" );
			this.linkerScript = clean(values[1]);
			this.include_c  = clean(values[2]);
			this.include_s  = clean(values[3]);
			this.startup  = clean(values[4]);
			
			if (semi_row.length==2) {
				String[] values_eeopt = semi_row[1].split(SEPARATOR);
				for (String s: values_eeopt) {
					if (s.trim().length()>0) {
						tmp_ee_opt.append( " " + s.trim());
					}
				}
			}
			
			this.ee_opt = tmp_ee_opt.toString();
		}

		protected String clean(String s) {
			s = s.trim();
			return EMPTY.equals(s) ? null : s;
		}
	}