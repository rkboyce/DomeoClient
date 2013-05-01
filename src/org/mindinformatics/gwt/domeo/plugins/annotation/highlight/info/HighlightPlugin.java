package org.mindinformatics.gwt.domeo.plugins.annotation.highlight.info;

import org.mindinformatics.gwt.domeo.client.ui.annotation.plugins.APlugin;

/**
 * @author Paolo Ciccarese <paolo.ciccarese@gmail.com>
 */
public class HighlightPlugin extends APlugin {
	
	public static final String VERSION = "0.1";
	public static final String TYPE = "Annotation";
	public static final String SUB_TYPE = "Highlight";
	public static final String PLUGIN = HighlightPlugin.class.getName().substring(0, HighlightPlugin.class.getName().indexOf(".info"));
	
	private static HighlightPlugin instance;
	private HighlightPlugin() {}
	
	public static HighlightPlugin getInstance() {
		if(instance==null) instance = new HighlightPlugin();
		return instance;
	}
	
	@Override
	public String getPluginName() {
		return PLUGIN;
	}

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public String getSubType() {
		return SUB_TYPE;
	}

	@Override
	public Boolean getMandatory() {
		return true;
	}

	@Override
	public String getVersion() {
		return VERSION;
	}
}
