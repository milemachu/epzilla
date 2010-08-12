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

import javax.swing.table.AbstractTableModel;

class EpzillaDataModel extends AbstractTableModel {
    public static final String[] headers = {"Cluster Id", "CPU (%)", "Memory (%)"};
    public static int CPU = 1;
    public static int MEMORY = 2;
    
    public EpzillaDataModel() {

    }

    public int getRowCount() {
        return ClusterPerformanceData.getInstance().getSize();
    }

    public int getColumnCount() {
        return headers.length;
    }

    public String getColumnName(int c) {
        return headers[c];
    }


    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
            case 1:
            case 2:
                return Integer.class;
            case 3:
                return Long.class;
        }
        return super.getColumnClass(columnIndex);    //To change body of overridden methods use File | Settings | File Templates.
    }


    public Object getValueAt(int r, int c) {
        ClusterPerformanceData.Data d = ClusterPerformanceData.getInstance().getData(r);
        switch (c) {
            case 0:
                return d.getClusterId();
            case 1:
                return d.getCpuUsage();
            case 2:
                return d.getMemoryUsage();
            case 3:
                return d.getLastUpdated();
        }
        return "<no entry>";
    }

}
