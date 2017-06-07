package com.singland.console.mbean;

import java.util.List;
import java.util.Map;

public interface HttpdLifecycleMBean {
	
	public List<Map<String, String>> listHttpd(String jolokiaUrl);
	
	public void stopHttpd();
	
	public void startHttpd();
	
	public void startHttpdIfNecessary();

}
