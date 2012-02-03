package com.eu.evidence.templates.interfaces;

public interface ITemplatePathProvider {

	/**
	 * 
	 * @return true if this provider is dynamic and the result my change during
	 *         the execution. This means that this provider is called every time
	 *         it is needed and not only once at start-up.
	 */
	boolean isDynamic();

	/**
	 * 
	 * @return a list of path in the fileSystem where can be found one or more
	 *         templates. Each path is scan recursively, looking for all
	 *         templates (if a template is found, no sub-directory is checked)
	 */
	String[] getPaths();
}
