package org.epzilla.dispatcher.loadAnalyzer;

import com.jezhumble.javasysmon.CpuTimes;
import com.jezhumble.javasysmon.JavaSysMon;
import org.epzilla.dispatcher.dataManager.NodeVariables;
import org.epzilla.dispatcher.dataManager.PerformanceInfoManager;

import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: Jun 12, 2010
 * Time: 9:57:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class CpuMemAnalyzer {
    private static int cpuUsage;
    private static int memUsage;
    private static int totalMemory;
    private static int freeMemory;

    public static void Initialize() {
        final JavaSysMon mon = new JavaSysMon();


//        NodeUIController.appendTextToMachineInfo("Number of CPUs :" + mon.numCpus());
//        NodeUIController.appendTextToMachineInfo("CPU Speed :" + mon.cpuFrequencyInHz() / 1000000 + " MHz");
//        NodeUIController.appendTextToMachineInfo("OS :" + mon.osName());
//        NodeUIController.appendTextToMachineInfo("Total Memory :" + mon.physical().getTotalBytes() / 1048576 + " MB");

        final java.util.Timer timer1 = new java.util.Timer();
        timer1.schedule(new TimerTask() {
            CpuTimes oldTime = mon.cpuTimes();
            float[] usageCache = new float[100];
            int count = 0;

            @Override
            public void run() {
                CpuTimes newTime = mon.cpuTimes();
                float val = newTime.getCpuUsage(oldTime);
                usageCache[count] = val;
//                NodeUIController.clearPerformanceInfo();
                if (count < (usageCache.length - 1)) {
                    count++;
                } else {
                    count = 0;
                    int sum = 0;
                    for (int i = 0; i < usageCache.length; i++) {
                        sum = sum + (int) (usageCache[i] * 100);
                    }
                    String temp = String.valueOf(sum / usageCache.length);
                    long mem = (mon.physical().getTotalBytes() - mon.physical().getFreeBytes());
//                    NodeUIController.appendTextToPerformanceInfo("CPU Usage Average :" + temp + " %");
                    PerformanceInfoManager.removePerformanceInfoFromList(NodeVariables.getNodeIP());
                    PerformanceInfoManager.addPerformanceInfo(NodeVariables.getNodeIP(), temp, String.valueOf((mem * 100 / mon.physical().getTotalBytes())), "0");
                }
//                NodeUIController.appendTextToPerformanceInfo("CPU Usage :" + val * 100 + " %");
                long mem = (mon.physical().getTotalBytes() - mon.physical().getFreeBytes());
                totalMemory = (int) mon.physical().getTotalBytes();
                freeMemory = (int)mon.physical().getFreeBytes();
//                NodeUIController.appendTextToPerformanceInfo("Memory Usage :" + mem / 1048576 + " MB");
//                NodeUIController.appendTextToPerformanceInfo("Memory Usage Percentage: " + (mem * 100 / mon.physical().getTotalBytes()) + " %");
                cpuUsage = (int) (val*100);
                memUsage = (int) (mem * 100 / mon.physical().getTotalBytes());
                oldTime = newTime;
            }
        }, 1000, 1000);

    }

    public static int getCpuUsage() {
        return cpuUsage;
    }
    public static int getTotalMemory(){
        return  totalMemory;
    }
    public static int getFreeMemory(){
        return freeMemory;
    }
    public static int getMemUsage() {
        return memUsage;
    }

    public static void setMemUsage(int memUsage) {
        CpuMemAnalyzer.memUsage = memUsage;
    }
}
