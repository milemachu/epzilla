package org.epzilla.dispatcher.ui;

import javax.swing.*;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.Enumeration;

public class EpzillaTable extends JPanel {

    public EpzillaTable() {
        EpzillaDataModel mdm = new EpzillaDataModel();
        this.setLayout(new CustomGridLayout(new String[]{"100%"}, new String[]{"100%"}));
        JTable jt = new JTable(mdm);
        jt.setModel(mdm);
        jt.setBackground(Color.black);
        jt.setForeground(Color.white);
        jt.setDefaultRenderer(Object.class, new TableRenderer());
        jt.setDefaultRenderer(Integer.class, new TableRenderer());
        jt.setDefaultRenderer(Long.class, new TableRenderer());
        jt.setDefaultRenderer(String.class, new TableRenderer());
//        jt.setBounds(new Rectangle(713, 340, 281, 170));
        jt.setDragEnabled(false);

      

        JScrollPane jsp = new JScrollPane(jt);
        jt.setFillsViewportHeight(true);
        jsp.setBackground(Color.black);
//        jt.setBounds(0,0,300,90);

//        TableColumnModel tcm = jt.getColumnModel();
//        Enumeration e = tcm.getColumns();
//        while (e.hasMoreElements()) {
//            TableColumn tc = (TableColumn) e.nextElement();
//            tc.setMinWidth(80);
//            tc.setPreferredWidth(80);
//            tc.setMaxWidth(80);
//        }
//        jsp.setBounds(new Rectangle(713, 340, 281, 170));
                                     super.setBackground(Color.black);
        super.add(jsp);
    }

}
