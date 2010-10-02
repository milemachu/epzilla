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
import javax.swing.border.Border;
import java.awt.*;
import java.io.Serializable;

public class CustomGridLayout
        implements LayoutManager, Serializable {

    private String cols[];
    private String rows[];
    private int widths[];
    private int hights[];
    private int minWidth;
    private int minHight;
    private boolean rtl;
    private int hGap;
    private int vGap;
    private boolean useHGapForBorders;
    private boolean useVGapForBorders;
    private int ng;
    private int eg;
    private int wg;
    private int sg;

    public CustomGridLayout(String widths[], String heights[]) {
        this(widths, heights, 0, 0, true, true);
    }

    public CustomGridLayout(String widths[], String heights[], int hGap, int vGap) {
        this(widths, heights, hGap, vGap, true, true);
    }

    public CustomGridLayout(String widths[], String heights[], int hGap, int vGap, boolean useHGapForBorders, boolean useVGapForBorders) {
        this.widths = null;
        hights = null;
        ng = 0;
        eg = 0;
        wg = 0;
        sg = 0;
        cols = widths;
        rows = heights;
        this.hGap = hGap;
        this.vGap = vGap;
        this.useHGapForBorders = useHGapForBorders;
        this.useVGapForBorders = useVGapForBorders;
    }

    public void addLayoutComponent(String s, Component component) {
    }

    public void removeLayoutComponent(Component component) {
    }

    public Dimension preferredLayoutSize(Container parent) {
        recalculate(parent.getWidth(), parent.getHeight(), parent);
        return new Dimension(minWidth + eg + wg, minHight + ng + sg);
    }

    public Dimension minimumLayoutSize(Container parent) {
        recalculate(parent.getWidth(), parent.getHeight(), parent);
        return new Dimension(minWidth, minHight);
    }

    public void layoutContainer(Container target) {
        int nmembers = target.getComponentCount();
        int startX = 0;
        int col = 0;
        int row = 0;
        try {
            Border border = ((JComponent) target).getBorder();
            Insets insets = border.getBorderInsets((JComponent) target);
            ng = insets.top;
            eg = insets.left;
            wg = insets.right;
            sg = insets.bottom;
        }
        catch (Exception e) {
        }
        int startY;
        if (useVGapForBorders) {
            startY = vGap + ng;
        } else {
            startY = ng;
        }
        int totalWidth = target.getWidth() - eg - wg;
        rtl = !target.getComponentOrientation().isLeftToRight();
        recalculate(totalWidth, target.getHeight() - ng - sg, target);
        try {
            for (int i = 0; i < nmembers; i++) {
                Component m = target.getComponent(i);
                col = i % cols.length;
                row = i / cols.length;
                if (col == 0) {
                    if (rtl) {
                        startX = eg;
                    } else {
                        startX = wg;
                    }
                    if (useHGapForBorders) {
                        startX += hGap;
                    }
                    if (row > 0) {
                        startY += vGap + hights[row - 1];
                    }
                }
                if (rtl) {
                    m.setBounds(target.getWidth() - startX - widths[col], startY, widths[col], hights[row]);
                } else {
                    m.setBounds(startX, startY, widths[col], hights[row]);
                }
                startX += widths[col] + hGap;
            }

        } catch (Exception ex) {
//            Logger.error(ex);
        }
    }

    private void recalculate(int targetWidth, int targetHight, Container target) {
        try {
            Border border = ((JComponent) target).getBorder();
            Insets insets = border.getBorderInsets((JComponent) target);
            ng = insets.top;
            eg = insets.left;
            wg = insets.right;
            sg = insets.bottom;
        }
        catch (Exception e) {
        }
        if (widths == null) {
            widths = new int[cols.length];
        }
        minWidth = 0;
        for (int i = 0; i < cols.length; i++) {
            if (cols[i].endsWith("%")) {
                continue;
            }
            if (cols[i].equals("0")) {
                for (int r = 0; r < rows.length; r++) {
                    Component m = target.getComponent(r * cols.length + i);
                    widths[i] = Math.max((int) m.getPreferredSize().getWidth(), widths[i]);
                    m = null;
                }

                minWidth += widths[i];
            } else {
                widths[i] = Integer.parseInt(cols[i]);
                minWidth += widths[i];
            }
        }

        if (useHGapForBorders) {
            minWidth += hGap * (cols.length + 1);
        } else {
            minWidth += hGap * (cols.length - 1);
        }
        int remainingWidth = targetWidth - minWidth;
        if (remainingWidth > 0) {
            for (int i = 0; i < cols.length; i++) {
                if (cols[i].endsWith("%")) {
                    widths[i] = (remainingWidth * Integer.parseInt(cols[i].split("\\%")[0])) / 100;
                }
            }

        }
        if (hights == null) {
            hights = new int[rows.length];
        }
        minHight = 0;
        for (int i = 0; i < rows.length; i++) {
            if (rows[i].endsWith("%")) {
                continue;
            }
            if (rows[i].equals("0")) {
                for (int c = 0; c < cols.length; c++) {
                    Component m = target.getComponent(c * rows.length + i);
                    hights[i] = Math.max((int) m.getPreferredSize().getHeight(), hights[i]);
                    m = null;
                }

                minHight += hights[i];
            } else {
                hights[i] = Integer.parseInt(rows[i]);
                minHight += hights[i];
            }
        }

        if (useVGapForBorders) {
            minHight += vGap * (rows.length + 1);
        } else {
            minHight += vGap * (rows.length - 1);
        }
        int remainingHight = targetHight - minHight;
        if (remainingHight > 0) {
            for (int i = 0; i < rows.length; i++) {
                if (rows[i].endsWith("%")) {
                    hights[i] = (remainingHight * Integer.parseInt(rows[i].split("\\%")[0])) / 100;
                }
            }

        }
    }
}
