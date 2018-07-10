package de.ngloader.multiinventory.config;

import java.util.Map;

public class MultiInventoryConfig {

	public String kickReason;

	public Boolean autoSave;
	public Integer autoSaveDelay;
	public Integer autoSaveQueueDelay;

	public String defaultInventoryId;
	public String defaultTemplateId;

	public Map<String, MultiInventoryWorldConfig> worlds;
}