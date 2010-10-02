@echo on
start cluster.bat
start rmiregistry
java -classpath %CD%\target\epZilla-1.0.0.jar -Djava.security.policy=%CD%\policy.txt -Djava.rmi.server.codebase=file:%CD%\target\epZilla-1.0.0.jar org.epzilla.daemon.services.DaemonStartup
pause