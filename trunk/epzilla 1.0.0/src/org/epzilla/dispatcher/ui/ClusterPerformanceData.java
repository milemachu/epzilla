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
package org.epzilla.dispatcher.ui;

import org.epzilla.dispatcher.controlers.DispatcherUIController;

import java.util.Collections;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: May 31, 2010
 * Time: 11:04:03 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClusterPerformanceData {

    private static ClusterPerformanceData instance = new ClusterPerformanceData();
    Vector<Data> vec = null;

    public static ClusterPerformanceData getInstance() {
        return instance;
    }

    private ClusterPerformanceData() {
        vec = new Vector();
//        this.addData(90, 18, 44);
//        this.addData(71, 90, 20);
//
//        this.addData(105, 70, 5);
//        this.addData(10, 12, 65);

    }


    public int getSize() {
        return this.vec.size();
    }

    public synchronized void addData(int clusterId, int cpuUsage, int memoryUsage) {
        System.out.println("cluster performance data recieved:" + clusterId + ", " + cpuUsage);
        Data d = null;
        for (Data data : vec) {
            if (data.getClusterId() == clusterId) {
                d = data;
                break;
            }
        }
        if (d == null) {
            d = new Data();
            d.setClusterId(clusterId);
            vec.add(d);
            Collections.sort(vec);
        }
        d.setCpuUsage(cpuUsage);
        d.setMemoryUsage(memoryUsage);
        DispatcherUIController.updateUI();
    }

    public Data getData(int index) {
        return this.vec.get(index);
    }

    public synchronized Data getDataForCluster(int clusterId) {
        for (Data data : this.vec) {
            if (data.getClusterId() == clusterId) {
                return data;
            }
        }
        return null;
    }


    public synchronized void removeData(int clusterId) {
        Data d = null;
        for (Data data : vec) {
            if (data.getClusterId() == clusterId) {
                d = data;
                break;
            }
        }
        if (d != null) {
            vec.remove(d);
        }
    }


    public static class Data implements Comparable {
        int clusterId;
        int cpu;
        int memory;
        int cpuUsage;
        int memoryUsage;
        long lastUpdated;

        public int getClusterId() {
            return clusterId;
        }

        public void setClusterId(int clusterId) {
            this.clusterId = clusterId;
        }

        public int getCpu() {
            return cpu;
        }

        public void setCpu(int cpu) {
            this.cpu = cpu;
        }

        public int getMemory() {
            return memory;
        }

        public void setMemory(int memory) {
            this.memory = memory;
        }

        public int getCpuUsage() {
            return cpuUsage;
        }

        public void setCpuUsage(int cpuUsage) {
            this.cpuUsage = cpuUsage;
        }

        public int getMemoryUsage() {
            return memoryUsage;
        }

        public void setMemoryUsage(int memoryUsage) {
            this.memoryUsage = memoryUsage;
        }

        public long getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(long lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        @Override
        public int compareTo(Object o) {
            Data d = (Data) o;
            if (this.clusterId < d.clusterId) {
                return -1;
            } else if (this.clusterId == d.clusterId) {
                return 0;
            }
            return 1;
        }
    }
}
