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

import javax.swing.*;
import java.awt.*;

public class EpzillaTable extends JPanel {

    public EpzillaTable() {
        EpzillaDataModel mdm = new EpzillaDataModel();
        this.setLayout(new CustomGridLayout(new String[]{"100%"}, new String[]{"100%"}));
        JTable jt = new JTable(mdm);
        jt.setModel(mdm);
        jt.setBackground(Color.black);
//        jt.getTableHeader().setBackground(Color.black);
//        jt.getTableHeader().setForeground(Color.white);

        jt.setForeground(Color.white);
        jt.setDefaultRenderer(Object.class, new TableRenderer());
        jt.setDefaultRenderer(Integer.class, new TableRenderer());
        jt.setDefaultRenderer(Long.class, new TableRenderer());
        jt.setDefaultRenderer(String.class, new TableRenderer());
//        jt.setDefaultRenderer(JTableHeader.class, new HeaderRenderer());
        jt.getTableHeader().setDefaultRenderer(new HeaderRenderer());
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
