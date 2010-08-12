/***************************************************************************************************
Copyright 2009 - 2010 Harshana Eranga Martin, Dishan Metihakwala, Rajeev Sampath, Chathura Randika

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
****************************************************************************************************/
package org.epzilla.clusterNode.loadAnalyzer;

import com.jezhumble.javasysmon.CpuTimes;
import com.jezhumble.javasysmon.JavaSysMon;
import org.epzilla.clusterNode.NodeController;
import org.epzilla.clusterNode.dataManager.PerformanceInfoManager;
import org.epzilla.clusterNode.userInterface.NodeUIController;

import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: May 15, 2010
 * Time: 6:30:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class CpuMemAnalyzer {
    private static int cpuUsage;
    private static int memUsage;
    private static int totalMemory;
    private static int freeMemory;
    private static int INITIAL_DELAY = 100;
    private static int UPDATING_DELAY = 1000;

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
                NodeUIController.clearPerformanceInfo();
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
                    NodeUIController.appendTextToPerformanceInfo("CPU Usage Average :" + temp + " %");
                    PerformanceInfoManager.removePerformanceInfoFromList(NodeController.getThisIP());
                    PerformanceInfoManager.addPerformanceInfo(NodeController.getThisIP(), temp, String.valueOf((mem * 100 / mon.physical().getTotalBytes())), "0");
                }
                NodeUIController.appendTextToPerformanceInfo("CPU Usage :" + val * 100 + " %");
                long mem = (mon.physical().getTotalBytes() - mon.physical().getFreeBytes());
                totalMemory = (int) ((mon.physical().getTotalBytes()) / 1048576);
                freeMemory = (int) ((mon.physical().getFreeBytes()) / 1048576);
                cpuUsage = (int) (val * 100);
                memUsage = (int) (mem * 100 / mon.physical().getTotalBytes());
                NodeUIController.appendTextToPerformanceInfo("Memory Usage :" + mem / 1048576 + " MB");
                NodeUIController.appendTextToPerformanceInfo("Memory Usage Percentage: " + (mem * 100 / mon.physical().getTotalBytes()) + " %");
                oldTime = newTime;
            }
        }, INITIAL_DELAY,UPDATING_DELAY);

    }

    public static int getCpuUsage() {
        return cpuUsage;
    }

    public static int getTotalMemory() {
        return totalMemory;
    }

    public static int getFreeMemory() {
        return freeMemory;
    }

    public static int getMemUsage() {
        return memUsage;
    }
}
