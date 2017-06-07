package com.singland.console.mbean;

import java.util.List;
import java.util.Map;

public interface TomcatLifecycleMBean {
	
	public List<Map<String, String>> listAllTomcats(String jolokiaUrl);
	
	public void stopTomcat(String tomcat);
	
	public void startTomcat(String tomcat);
	
	public void startTomcatIfNecessary(String tomcat);

}
