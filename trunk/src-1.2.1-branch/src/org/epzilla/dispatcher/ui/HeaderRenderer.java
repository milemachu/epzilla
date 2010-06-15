package org.epzilla.dispatcher.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;

class HeaderRenderer
    extends DefaultTableCellRenderer
{
    public HeaderRenderer()
    {
        setHorizontalAlignment(SwingConstants.CENTER);
        setOpaque(true);

        // This call is needed because DefaultTableCellRenderer calls setBorder()
        // in its constructor, which is executed after updateUI()
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    }

    public void updateUI()
    {
        super.updateUI();
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));
    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                       boolean selected, boolean focused, int row, int column)
    {
        JTableHeader h = table != null ? table.getTableHeader() : null;

        if (h != null)
        {
            setEnabled(h.isEnabled());
            setComponentOrientation(h.getComponentOrientation());

            setForeground(Color.white);
            setBackground(Color.black);
            setFont(h.getFont());
        }
        else
        {
            /* Use sensible values instead of random leftover values from the last call */
            setEnabled(true);
            setComponentOrientation(ComponentOrientation.UNKNOWN);

            setForeground(Color.white);
            setBackground(Color.black);
            setFont(UIManager.getFont("TableHeader.font"));
        }

        setValue(value);

        return this;
    }
}
