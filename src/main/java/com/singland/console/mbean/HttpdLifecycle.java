package com.singland.console.mbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.singland.console.mbean.utils.ShellCmdHelper;

public class HttpdLifecycle implements HttpdLifecycleMBean {

	@Override
	public List<Map<String, String>> listHttpd(String jolokiaUrl) {
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		
		List<String> list1 = getHttpdInfo();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("httpdName", list1.get(0));
		map1.put("host", ShellCmdHelper.getHost());
		map1.put("port", list1.get(1));
		map1.put("status", list1.get(2));
		map1.put("jolokiaUrl", jolokiaUrl);
		mapList.add(map1);
		
		return mapList;
	}

	@Override
	public void stopHttpd() {
		ShellCmdHelper.startStopProcess("stop", "httpd");
	}

	@Override
	public void startHttpd() {
		ShellCmdHelper.startStopProcess("start", "httpd");
	}
	
	private List<String> getHttpdInfo() {
		List<String> list = new ArrayList<String>();
		list.add("httpd");
		list.add("9090");
		list.add(ShellCmdHelper.getProcessStatus("httpd"));
		return list;
	}

	@Override
	public void startHttpdIfNecessary() {
		String status = ShellCmdHelper.getProcessStatus("httpd");
		if ("STOPPED".equalsIgnoreCase(status)) {
			startHttpd();
		}
		
	}

}
