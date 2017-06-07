package com.singland.console.mbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.singland.console.mbean.utils.ShellCmdHelper;

public class TomcatLifecycle implements TomcatLifecycleMBean {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TomcatLifecycle.class);

	@Override
	public List<Map<String, String>> listAllTomcats(String jolokiaUrl) {
		
		LOGGER.error("iEsbMBean.listAllTomcats");
		
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		
		List<String> list1 = getTomcat1Info();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("tomcatName", list1.get(0));
		map1.put("host", ShellCmdHelper.getHost());
		map1.put("port", list1.get(1));
		map1.put("status", list1.get(2));
		map1.put("jolokiaUrl", jolokiaUrl);
		mapList.add(map1);
		
		List<String> list2 = getTomcat2Info();
		Map<String, String> map2 = new HashMap<String, String>();
		map2.put("tomcatName", list2.get(0));
		map2.put("host", ShellCmdHelper.getHost());
		map2.put("port", list2.get(1));
		map2.put("status", list2.get(2));
		map2.put("jolokiaUrl", jolokiaUrl);
		mapList.add(map2);
		
		List<String> list3 = getTomcat3Info();
		Map<String, String> map3 = new HashMap<String, String>();
		map3.put("tomcatName", list3.get(0));
		map3.put("host", ShellCmdHelper.getHost());
		map3.put("port", list3.get(1));
		map3.put("status", list3.get(2));
		map3.put("jolokiaUrl", jolokiaUrl);
		mapList.add(map3);
		
		return mapList;
	}
	
	private List<String> getTomcat1Info() {
		List<String> list = new ArrayList<String>();
		list.add("tomcat1");
		list.add("8080");
		list.add(ShellCmdHelper.getProcessStatus("tomcat1"));
		return list;
	}
	
	private List<String> getTomcat2Info() {
		List<String> list = new ArrayList<String>();
		list.add("tomcat2");
		list.add("8081");
		list.add(ShellCmdHelper.getProcessStatus("tomcat2"));
		return list;
	}
	
	private List<String> getTomcat3Info() {
		List<String> list = new ArrayList<String>();
		list.add("tomcat3");
		list.add("8082");
		list.add(ShellCmdHelper.getProcessStatus("tomcat3"));
		return list;
	}
	
	@Override
	public void stopTomcat(String tomcat) {
		ShellCmdHelper.startStopProcess("stop", tomcat);
	}

	@Override
	public void startTomcat(String tomcat) {
		ShellCmdHelper.startStopProcess("start", tomcat);
	}

	@Override
	public void startTomcatIfNecessary(String tomcat) {
		String status = ShellCmdHelper.getProcessStatus(tomcat);
		if ("STOPPED".equalsIgnoreCase(status)) {
			startTomcat(tomcat);
		}
	}

}
