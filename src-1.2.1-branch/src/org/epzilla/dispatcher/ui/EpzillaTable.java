package org.epzilla.dispatcher.ui;

import javax.swing.*;
import java.awt.*;

public class EpzillaTable extends JPanel {

    public EpzillaTable() {
        EpzillaDataModel mdm = new EpzillaDataModel();

        JTable jt = new JTable(mdm);
        jt.setModel(mdm);
        jt.setFillsViewportHeight(true);
//        jt.setBounds(new Rectangle(713, 340, 281, 170));

        JScrollPane jsp = new JScrollPane(jt);
//        jsp.setBounds(new Rectangle(713, 340, 281, 170));

        super.add(jsp);
    }

}
