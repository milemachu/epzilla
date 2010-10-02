@echo on
start rmiregistry
java -classpath %CD%\target\epZilla-1.0.0.jar;%CD%\lib\jstm.jar;%CD%\lib\javasysmon-0.3.1.jar -Djava.security.policy=%CD%\policy.txt -Djava.rmi.server.codebase=file:%CD%\target\epZilla-1.0.0.jar org.epzilla.accumulator.Main
