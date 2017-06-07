package com.singland.console.mbean;

import java.util.List;
import java.util.Map;

public interface KeepalivedLifecycleMBean {
	
	public List<Map<String, String>> listKeepalived(String jolokiaUrl);
	
	public void stopKeepalived();
	
	public void startKeepalived();
	
	public void startKeepalivedIfNecessary();

}
