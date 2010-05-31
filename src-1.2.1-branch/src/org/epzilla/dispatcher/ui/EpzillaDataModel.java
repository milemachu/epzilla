package org.epzilla.dispatcher.ui;

import javax.swing.table.AbstractTableModel;

class EpzillaDataModel extends AbstractTableModel {
    String[] headers = {"Cluster Id", "CPU (%)", "Memory (%)"};
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
