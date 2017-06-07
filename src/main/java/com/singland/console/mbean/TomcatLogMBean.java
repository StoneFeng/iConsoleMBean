package com.singland.console.mbean;

import java.util.List;

import com.singland.console.vo.TomcatLogFileVo;

public interface TomcatLogMBean {
	
	public List<TomcatLogFileVo> listTomcatLogFiles(String subTomcatName);
	
	public String printSingleLogFile(String path);
	
	public void clearLog(String path);
	
	public void deleteLog(String path);

}
