package org.epzilla.clusterNode.loadAnalyzer;
import com.jezhumble.javasysmon.JavaSysMon;
import com.jezhumble.javasysmon.CpuTimes;

import java.util.TimerTask;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: May 15, 2010
 * Time: 6:30:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class CpuMemAnalyzer {
    
    public static void Initialize()
    {
        final JavaSysMon mon = new JavaSysMon();
               System.out.println("Number of CPUs :" + mon.numCpus());
               System.out.println("Speed :" + mon.cpuFrequencyInHz() / 1000000 + " MHz");
               System.out.println("OS :" + mon.osName());
               System.out.println("Total Memory :" + mon.physical().getTotalBytes() / 1048576 + " MB");

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

                       if (count < (usageCache.length-1)) {
                           count++;
                       } else {
                           count = 0;
                           int sum = 0;
                           for (int i = 0; i < usageCache.length; i++) {
                               sum = sum + (int) (usageCache[i]*100);
                           }
                           System.out.println("CPU Usage Average :" + sum / usageCache.length + " %");
                       }
                       System.out.println("CPU Usage :" + val * 100 + " %");
                       long mem = (mon.physical().getTotalBytes() - mon.physical().getFreeBytes());
                       System.out.println("Memory Usage :" + mem / 1048576 + " MB");
                       System.out.println("Memory Usage Percentage: " + (mem * 100 / mon.physical().getTotalBytes()) + " %");
                       oldTime = newTime;
                   }
               }, 100, 1000);

    }




}
