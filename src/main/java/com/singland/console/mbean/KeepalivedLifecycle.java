package com.singland.console.mbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.singland.console.mbean.utils.ShellCmdHelper;

public class KeepalivedLifecycle implements KeepalivedLifecycleMBean {

	@Override
	public List<Map<String, String>> listKeepalived(String jolokiaUrl) {
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		
		List<String> list1 = getKeepalivedInfo();
		Map<String, String> map1 = new HashMap<String, String>();
		map1.put("name", list1.get(0));
		map1.put("host", ShellCmdHelper.getHost());
		map1.put("status", list1.get(1));
		map1.put("jolokiaUrl", jolokiaUrl);
		mapList.add(map1);
		
		return mapList;
	}

	@Override
	public void stopKeepalived() {
		ShellCmdHelper.startStopProcess("stop", "keepalived");
	}

	@Override
	public void startKeepalived() {
		ShellCmdHelper.startStopProcess("start", "keepalived");
	}
	
	private List<String> getKeepalivedInfo() {
		List<String> list = new ArrayList<String>();
		list.add("keepalived");
		list.add(ShellCmdHelper.getProcessStatus("keepalived"));
		return list;
	}

	@Override
	public void startKeepalivedIfNecessary() {
		String status = ShellCmdHelper.getProcessStatus("keepalived");
		if ("STOPPED".equalsIgnoreCase(status)) {
			startKeepalived();
		}
		
	}

}
