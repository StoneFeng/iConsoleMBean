package com.singland.console.mbean.utils;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.singland.console.mbean.HttpdLifecycle;
import com.singland.console.mbean.KeepalivedLifecycle;
import com.singland.console.mbean.TomcatLifecycle;
import com.singland.console.mbean.TomcatLog;

public class MbeanRegister extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LoggerFactory.getLogger(MbeanRegister.class);
	private static final String IESB_MBEAN_TOMCAT_LOG = "iesb.mbean:type=TomcatLog";
	private static final String IESB_MBEAN_TOMCAT_LIFECYCLE = "iesb.mbean:type=TomcatLifecycle";
	private static final String IESB_MBEAN_HTTPD_LIFECYCLE = "iesb.mbean:type=HttpdLifecycle";
	private static final String IESB_MBEAN_KEEPALIVED_LIFECYCLE = "iesb.mbean:type=KeepalivedLifecycle";

	@Override
	public void init() throws ServletException {
		super.init();
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer(); 
		try {
			ObjectName tomcatLog = new ObjectName(IESB_MBEAN_TOMCAT_LOG); 
	        mbs.registerMBean(new TomcatLog(), tomcatLog);
	        LOGGER.debug("MBean iesb.mbean:type=TomcatLog registered");
	        
	        ObjectName tomcatLifecycle = new ObjectName(IESB_MBEAN_TOMCAT_LIFECYCLE);
	        mbs.registerMBean(new TomcatLifecycle(), tomcatLifecycle);
	        LOGGER.debug("MBean iesb.mbean:type=TomcatLifecycle registered");
	        
	        ObjectName httpdLifecycle = new ObjectName(IESB_MBEAN_HTTPD_LIFECYCLE);
	        mbs.registerMBean(new HttpdLifecycle(), httpdLifecycle);
	        LOGGER.debug("MBean iesb.mbean:type=HttpdLifecycle registered");
	        
	        ObjectName keepalivedLifecycle = new ObjectName(IESB_MBEAN_KEEPALIVED_LIFECYCLE);
	        mbs.registerMBean(new KeepalivedLifecycle(), keepalivedLifecycle);
	        LOGGER.debug("MBean iesb.mbean:type=KeepalivedLifecycle registered");
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (InstanceAlreadyExistsException e) {
			LOGGER.error("Ignore this exception deliberately", e);
		} catch (MBeanRegistrationException e) {
			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		try {
			ObjectName tomcatLog = new ObjectName(IESB_MBEAN_TOMCAT_LOG);
			mbs.unregisterMBean(tomcatLog);
			LOGGER.debug("MBean iesb.mbean:type=TomcatLog unregistered");
			
			ObjectName tomcatLifecycle = new ObjectName(IESB_MBEAN_TOMCAT_LIFECYCLE);
			mbs.unregisterMBean(tomcatLifecycle);
			LOGGER.debug("MBean iesb.mbean:type=TomcatLifecycle unregistered");
			
			ObjectName httpdLifecycle = new ObjectName(IESB_MBEAN_HTTPD_LIFECYCLE);
			mbs.unregisterMBean(httpdLifecycle);
			LOGGER.debug("MBean iesb.mbean:type=HttpdLifecycle unregistered");
			
			ObjectName keepalivedLifecycle = new ObjectName(IESB_MBEAN_KEEPALIVED_LIFECYCLE);
			mbs.unregisterMBean(keepalivedLifecycle);
			LOGGER.debug("MBean iesb.mbean:type=KeepalivedLifecycle unregistered");
		} catch (MalformedObjectNameException e) {
			e.printStackTrace();
		} catch (MBeanRegistrationException e) {
			e.printStackTrace();
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
		}
	}

}
