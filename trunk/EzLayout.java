import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Rajeev
 * Date: Feb 21, 2010
 * Time: 01:15:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class EzLayout implements LayoutManager {

    // ** experimental **

    // EpZillaLayout by EpZilla Team.
    // http://www.epzilla.net

    // this layout can be customized using simple strings as arguments.
    // u can construct an entire ui with a single layout. no need to include components inside JPanels.
    // just set this layout to the JFrame and then add all the buttons, text boxes etc etc.

    // released under Apache License 2.0

    // code has to be refactored later.

    String[] vertical = null;
    String[][] rows = null;

    int minWidth = 0;
    int minHeight = 0;

    int[][] xyWidthHeight = null;

    /**
     * use the following notation to configure the layout.
     *
     * @param heightOfEachRow        eg: 10, 30%,25 ,70% -> values with % mark refers the portion of the remainig part.
     *                               no problem with spaces. leave spaces as u want. but remember to write single values (eg: 50% or 10) without having spaces
     *                               inside it.
     * @param widthOfEachCellInARow each element in this array should refer to a single row.
     *                               each row should be written like: "50%, 10f, 20, 30%f, 20%"
     *                               the cells with letter 'f' are filled with components.
     *                               others are left blank.
     *                               u can also use "L" - this means the entire row will be left blank
     *                               this is similar to having gaps between two rows.
     */
    public EzLayout(String heightOfEachRow, String[] widthOfEachCellInARow) {
        String[] t = heightOfEachRow.split(",");
        vertical = new String[t.length];
        for (int i = 0; i < t.length; i++) {
            this.vertical[i] = t[i].trim();
        }

        rows = new String[widthOfEachCellInARow.length][];
        for (int i = 0; i < widthOfEachCellInARow.length; i++) {
            t = widthOfEachCellInARow[i].split(",");
            rows[i] = new String[t.length];
            for (int j = 0; j < t.length; j++) {
                rows[i][j] = t[j].trim();
                if (rows[i][j].equalsIgnoreCase("L")) {
                     rows[i][j] = "100%";
                }
            }
        }
    }

    // no need to implement.
    public void addLayoutComponent(String name, Component comp) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    // no need to implement.
    public void removeLayoutComponent(Component comp) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public Dimension preferredLayoutSize(Container parent) {
        Dimension d = new Dimension();
        this.configSize(parent);
        Insets i = parent.getInsets();
        d.setSize(this.minWidth + i.left + i.right, this.minHeight + i.bottom + i.top);
        return d;
    }

    public Dimension minimumLayoutSize(Container parent) {
        Dimension d = new Dimension();
        this.configSize(parent);
        d.setSize(this.minWidth, this.minHeight);
        return d;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void layoutContainer(Container parent) {

        this.configSize(parent);
        int count = parent.getComponentCount();

        for (int i = 0; i < count; i++) {
            Component c = parent.getComponent(i);
            c.setBounds(xyWidthHeight[i][0], xyWidthHeight[i][1], xyWidthHeight[i][2], xyWidthHeight[i][3]);
//            System.out.println(xyWidthHeight[i][0] + ", " + xyWidthHeight[i][1] + ", " + xyWidthHeight[i][2] + ", " + xyWidthHeight[i][3]);
        }
    }

    private void configSize(Container parent) {
        int ph = parent.getHeight();
        int pw = parent.getWidth();

        int remVertical = 0;

        minHeight = 0;
        minWidth = 0;
//        String[] hs = this.vertical.split(",");
        for (String item : this.vertical) {
            item = item.trim();
            if (!item.contains("%")) {
                minHeight += Integer.parseInt(item);
            }
        }

        if (minHeight < ph) {
            remVertical = ph - minHeight;
            minHeight = ph;
        }

        int temp = 0;
        int[] minWidths = new int[this.vertical.length];
        int ii = 0;
        for (String[] item : this.rows) {
            temp = 0;
            String c = null;
            for (String cell : item) {
                if (!cell.contains("%")) {
//                    c = cell.trim();
                    c = cell;
                    if (c.contains("f")) {
                        c = c.substring(0, c.length() - 1);
                    }
                    temp += Integer.parseInt(c);
                }
            }
            minWidths[ii] = temp;
            ii++;
            if (minWidth < temp) {
                minWidth = temp;
            }
        }

        if (minWidth < pw) {
            minWidth = pw;
        }

        // todo - row / col calc.
        int remHor = 0;

        ArrayList<int[]> list = new ArrayList<int[]>();

        int tempY = 0;
        int curX = 0;
        int curY = 0;
        for (int i = 0; i < this.vertical.length; i++) {
//            int tempheight = 0;
//            String[] tempRows = this.rows[i];
            String tv = this.vertical[i];
            if (tv.contains("%")) {
                tv = tv.substring(0, tv.length() - 1);
                tempY = (Integer.parseInt(tv) * remVertical) / 100;
            } else {
                tempY = Integer.parseInt(tv);
            }

            curX = 0;
            remHor = minWidth - minWidths[i];

//            System.out.println("minwidth : " + minWidth);
//            System.out.println("remhor : " + remHor);

            for (int j = 0; j < this.rows[i].length; j++) {
                int tempX = 0;
                String temps = rows[i][j];
                boolean fill = false;
                if (temps.contains("f")) {
                    fill = true;
                    temps = temps.replace("f", "");
                }
                if (temps.contains("%")) {
                    temps = temps.replace("%", "");
                    tempX = (Integer.parseInt(temps) * remHor) / 100;
                } else {
                    tempX = Integer.parseInt(temps);
                }
                if (fill) {
                    int[] d = new int[4];
                    d[0] = curX;
                    d[1] = curY;
                    d[2] = tempX;
                    d[3] = tempY;
                    list.add(d);
//                    System.out.println("cury:" + curY);
                }
                curX += tempX;
//                System.out.println(i + " tempX:" + tempX + "  curX:" + curX);
            }

            curY += tempY;

        }
        this.xyWidthHeight = new int[list.size()][4];
        for (int i = 0; i < list.size(); i++) {
            int[] dt = list.get(i);
            for (int j = 0; j < 4; j++) {
                xyWidthHeight[i][j] = dt[j];
            }
        }
    }
}
