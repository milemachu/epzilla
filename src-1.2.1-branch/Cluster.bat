@echo on
start rmiregistry
java -classpath %CD%\target\DispatcherJSTM-1.0.0.jar;%CD%\lib\jstm.jar;%CD%\lib\javasysmon-0.3.1.jar;%CD%\lib\LeaderElection-1.0.0.jar -Djava.security.policy=%CD%\lib\policy.txt -Djava.rmi.server.codebase=file:%CD%\target\DispatcherJSTM-1.0.0.jar -Djava.rmi.server.codebase=file:%CD%\lib\LeaderElection-1.0.0.jar org.epzilla.clusterNode.leaderReg.Main
pause
