package com.singland.console.vo;

import java.util.Comparator;

public class TomcatLogFileVo implements Comparator<TomcatLogFileVo> {
	
	private String logName;
	private String absolutePath;
	private String logSize;
	
	public String getLogSize() { 
		return logSize;
	}
	public void setLogSize(String logSize) {
		this.logSize = logSize;
	}
	public String getLogName() {
		return logName;
	}
	public void setLogName(String logName) {
		this.logName = logName;
	}
	public String getAbsolutePath() {
		return absolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}
	@Override
	public int compare(TomcatLogFileVo arg0, TomcatLogFileVo arg1) {
		return arg0.getLogName().toLowerCase().compareTo(arg1.getLogName().toLowerCase());
	}

}
