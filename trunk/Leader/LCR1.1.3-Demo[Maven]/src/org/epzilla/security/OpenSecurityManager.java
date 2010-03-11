package org.epzilla.security;

import java.rmi.RMISecurityManager;
import java.security.Permission;

public class OpenSecurityManager extends RMISecurityManager {
	   public void checkConnect(String host, int port) {
	   }
	   
	   public void checkConnect(String host, int port, Object context) {
	   }
	   
	   public void checkPermission(Permission perm) {
	   }
	   
	   public void checkAccept(String host, int port) {
	   }

}
