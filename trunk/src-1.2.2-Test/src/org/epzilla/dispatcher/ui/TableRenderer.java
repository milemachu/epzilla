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
        final String txt = value.toString();

        JLabel jl = null;
        final String colname = table.getColumnName(column);

        try {
            if (colname.equals(EpzillaDataModel.headers[EpzillaDataModel.CPU]) || colname.equals(EpzillaDataModel.headers[EpzillaDataModel.MEMORY])) {
                final int i = (Integer) value;
                Color back = null;
                if (i < 25) {
                    back = Color.GREEN;
                } else if (i < 50) {
                    back = Color.yellow;
                } else if (i < 75) {
                    back = Color.orange;
                } else {
                    back = Color.red;
                }
                final Color fc = back;
                jl = new JLabel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);    //To change body of overridden methods use File | Settings | File Templates.
                        g.setColor(fc);
                        int ww = (int) ((this.getWidth()) * (i / 100.0d));
                        g.fillRect(0, 0, ww, this.getHeight());
                        g.setColor(Color.black);
                        g.fillRect(ww, 0, this.getWidth() - ww, this.getHeight());
                        g.setColor(Color.black);
                        g.drawString(txt, 2, 12);


                    }
                };

//                 jl.setBackground(back);
            } else {
                jl = new JLabel() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);    //To change body of overridden methods use File | Settings | File Templates.
                        g.setColor(Color.black);
                        g.fillRect(0, 0, this.getWidth(), this.getHeight());
                        g.setColor(Color.white);
                        g.drawString(txt, 3, 12);
                    }
                };
            }
            jl.setOpaque(true);
        } catch (Exception e) {
            Logger.error("error in cell renderer", e);
        }
        return jl;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
