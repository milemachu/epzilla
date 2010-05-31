package org.epzilla.dispatcher.ui;

import org.epzilla.util.Logger;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: May 31, 2010
 * Time: 2:02:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class TableRenderer implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel jl = new JLabel();
        jl.setText(value.toString());
//        jl.setHorizontalAlignment(JLabel.CENTER);
        try {
            if (column == EpzillaDataModel.CPU || column == EpzillaDataModel.MEMORY) {
                int i = (Integer) value;
                Color back = null;
                if (i < 25) {
                    System.out.println("settin green");
                    back = Color.GREEN;
                } else if (i < 50) {
                    back = Color.yellow;
                } else if (i < 75) {
                    back = Color.orange;
                } else {
                    back = Color.red;
                }
                 jl.setBackground(back);
            }
        } catch (Exception e) {
            Logger.error("error in cell renderer", e);
        }
        jl.setOpaque(true);
        return jl;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
