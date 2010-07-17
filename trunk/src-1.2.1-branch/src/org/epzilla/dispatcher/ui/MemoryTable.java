package org.epzilla.dispatcher.ui;

import org.epzilla.dispatcher.dataManager.EventsCounter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.TimerTask;

import static java.awt.Color.*;

/**
 * Created by IntelliJ IDEA.
 * This is class shows the Event dispatch rate of the Dispatcher in a graph.
 */
public class MemoryTable extends JPanel {

    public MemoryTable() {
        setLayout(new BorderLayout());
        DisplayPanel displayPanel = new DisplayPanel();
        add(displayPanel);
    }
    /*
    draw the graph to displat the Event dispatch rate in the user interface
     */
    public class DisplayPanel extends JPanel {

        public Thread runner = null;
        public long time = 500;
        private int w, h;
        private int cInc;
        private int points[];
        private int nums;
        private int aH, dH;
        private String name;
        private BufferedImage image;
        private Graphics2D graphics;
        private Font myFont = new Font("Calibri", Font.PLAIN, 11);
        private Runtime runtime = Runtime.getRuntime();
        private Rectangle rect1 = new Rectangle();
        private Rectangle2D rect2 = new Rectangle2D.Float();
        private Rectangle2D rect3 = new Rectangle2D.Float();
        private Line2D gLine = new Line2D.Float();
        private Color gColor = new Color(46, 139, 87);
        private Color plotColor = new Color(0, 100, 0);
        java.util.Timer timer = new java.util.Timer();
        private int UPDATE_SERVICE_RUNNING_TIME = 500;
        private int INITIAL_START_TIME = 3000;


        public DisplayPanel() {
            setBackground(BLACK);
            if (runner == null)
                initProcess();
        }
       /*
       periodically take the event rates and repaint the graph
        */
        public void initProcess() {
            timer.scheduleAtFixedRate(new TimerTask() {
                public void run() {

                    Dimension d = getSize();
                    if (d.width != w || d.height != h) {
                        w = d.width;
                        h = d.height;
                        image = (BufferedImage) createImage(w, h);
                        graphics = image.createGraphics();
                        graphics.setFont(myFont);
                        FontMetrics fontMatrics = graphics.getFontMetrics(myFont);
                        aH = fontMatrics.getAscent();
                        dH = fontMatrics.getDescent();
                    }
                    repaint();
                }
            }, INITIAL_START_TIME, UPDATE_SERVICE_RUNNING_TIME);
        }
        /*
        draw the graph
         */
        public void paint(Graphics g) {

            if (graphics == null) {
                return;
            }

            graphics.setBackground(getBackground());
            graphics.clearRect(0, 0, w, h);

            float currentRate = EventsCounter.getEventDispatchRate();
            float maxRate = EventsCounter.getMaxRate();
            float free = maxRate - currentRate;

            graphics.setColor(GREEN);
            name = String.valueOf(((int) (maxRate - free)))
                    + " (Evt/sec)";
            graphics.drawString(name, 4, h - dH);

            float ssH = aH + dH;
            float remainingHeight = h - (ssH * 2) - 0.5f;
            float blockHeight = remainingHeight / 10;
            float blockWidth = 20.0f;

            graphics.setColor(plotColor);
            int MemUsage = (int) ((free / maxRate) * 10);
            int i = 0;
            for (; i < MemUsage; i++) {
                rect2.setRect(5, ssH + i * blockHeight,
                        blockWidth, blockHeight - 1);
                graphics.fill(rect2);
            }

            graphics.setColor(GREEN);
            for (; i < 10; i++) {
                rect3.setRect(5, ssH + i * blockHeight,
                        blockWidth, blockHeight - 1);
                graphics.fill(rect3);
            }

            graphics.setColor(gColor);
            int graphX = 30;
            int graphY = (int) ssH;
            int graphW = w - graphX - 5;
            int graphH = (int) remainingHeight;
            rect1.setRect(graphX, graphY, graphW, graphH);
            graphics.draw(rect1);

            int graphRow = graphH / 10;


            for (int j = graphY; j <= graphH + graphY; j += graphRow) {
                gLine.setLine(graphX, j, graphX + graphW, j);
                graphics.draw(gLine);
            }

            int graphColumn = graphW / 15;

            if (cInc == 0) {
                cInc = graphColumn;
            }

            for (int j = graphX + cInc; j < graphW + graphX; j += graphColumn) {
                gLine.setLine(j, graphY, j, graphY + graphH);
                graphics.draw(gLine);
            }

            --cInc;

            if (points == null) {
                points = new int[graphW];
                nums = 0;
            } else if (points.length != graphW) {
                int tmp[];
                if (nums < graphW) {
                    tmp = new int[nums];
                    System.arraycopy(points, 0, tmp, 0, tmp.length);
                } else {
                    tmp = new int[graphW];
                    System.arraycopy(points, points.length - tmp.length, tmp, 0, tmp.length);
                    nums = tmp.length - 2;
                }
                points = new int[graphW];
                System.arraycopy(tmp, 0, points, 0, tmp.length);
            } else {
                graphics.setColor(YELLOW);
                points[nums] = (int) (graphY + graphH * (free / maxRate));
                for (int j = graphX + graphW - nums, k = 0; k < nums; k++, j++) {
                    if (k != 0) {
                        if (points[k] != points[k - 1]) {
                            graphics.drawLine(j - 1, points[k - 1], j, points[k]);
                        } else {
                            graphics.fillRect(j, points[k], 1, 1);
                        }
                    }
                }
                if (nums + 2 == points.length) {
                    System.arraycopy(points, 1, points, 0, nums - 1);
                    --nums;
                } else {
                    nums++;
                }
            }
            g.drawImage(image, 0, 0, this);
        }
    }
}

