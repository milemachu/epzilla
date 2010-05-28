package stm;

import generatedModel.ImageInfo;
import generatedModel.ImagesObjectModel;
import jstm.core.*;
import jstm.misc.Log;
import jstm.transports.clientserver.Server;
import jstm.transports.clientserver.socket.AutoSocket;
import jstm.transports.clientserver.socket.SocketServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.net.InetAddress;

/**
 * Created by IntelliJ IDEA.
 * User: Dishan
 * Date: May 28, 2010
 * Time: 11:24:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class RunAsServer {
    private static Share share;
    private static Server server;
    private JPanel imagePanel;
    private int imageIndex;

    public void start() {
        JFrame frame = buildWindow();
        frame.setVisible(true);
        try {
            InetAddress inetAddress;
            inetAddress = InetAddress.getLocalHost();
            String ipAddress = inetAddress.getHostAddress();
            // Register the object model specific to this application.

            Site.getLocal().registerObjectModel(new ImagesObjectModel());

            server = new SocketServer(444);

            server.start();

            // Once connected, retrieve the Group that represents the server and its
            // clients

            Group serverAndClientsSites = server.getServerAndClients();


            // Open a share in this group is there is none yet

            if (serverAndClientsSites.getOpenShares().size() == 0) {
                Transaction transaction = Site.getLocal().startTransaction();
                serverAndClientsSites.getOpenShares().add(new Share());
                transaction.commit();
            }

            share = (Share) serverAndClientsSites.getOpenShares().toArray()[0];

            // Register a listener on the share to be notified when an ImageInfo is
            // shared. When this happen, add an image to the UI.

            share.addListener(new TransactedSet.Listener<TransactedObject>() {

                public void onAdded(Transaction transaction, TransactedObject object) {
                    if (object instanceof ImageInfo)
                        addImage((ImageInfo) object);
                }

                public void onRemoved(Transaction transaction, TransactedObject object) {
                }
            });

            // The share might already contain images, show them

            for (TransactedObject o : share)
                if (o instanceof ImageInfo)
                    addImage((ImageInfo) o);


            //If no images has been loaded, load them
            if (share.isEmpty()) {
                loadImages();
            }

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


    }

    private void loadImages() {
        if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
            Site.getLocal().allowThread();
            for (int i = 0; i < 9; i++) {
                Transaction transaction = Site.getLocal().startTransaction();

                ImageInfo info = new ImageInfo();
                imageIndex = i;
                info.setUrl("earth" + imageIndex++ + ".jpg");
                if (i < 3) {
                    int top = (i * 137) + 10;
                    int left = 10;
                    info.setTop(top);
                    info.setLeft(left);
                } else if (i < 6) {
                    int top = ((i - 3) * 137) + 10;
                    int left = 209;
                    info.setTop(top);
                    info.setLeft(left);

                } else if (i < 9) {
                    int top = ((i - 6) * 137) + 10;
                    int left = 408;
                    info.setTop(top);
                    info.setLeft(left);

                }
                share.add(info);

                transaction.commit();
            }
        }
    }

    private JFrame buildWindow() {
        JFrame frame = new JFrame("Epzilla STM demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // A panel for images

        imagePanel = new JPanel();
        imagePanel.setLayout(null);
        imagePanel.setPreferredSize(new Dimension(1024, 768));
        panel.add(imagePanel);

        // A panel for logs

        final TextArea tf = new TextArea();
        tf.setBackground(Color.BLACK);
        tf.setForeground(Color.GREEN);
        tf.setEditable(false);
        panel.add(tf);

        // Write JSTM logs to the text area

        Log.add(new Log() {

            @Override
            public void onWrite(String message) {
                tf.append(message + "\n\r");
                tf.setCaretPosition(tf.getText().length());
            }
        });

        // Window

        frame.getContentPane().add(panel);
        frame.setSize(1024, 768);

        return frame;
    }

    /**
     * Creates an image corresponding to an ImageInfo, and adds listeners to the
     * image and the ImageInfo to be notified of events on both sides.
     */
    private void addImage(final ImageInfo info) {
        Icon icon = new ImageIcon("images/" + info.getUrl());
        final JLabel label1 = new JLabel(icon);
        imagePanel.add(label1);
        imagePanel.setComponentZOrder(label1, 0);
        label1.setSize(icon.getIconWidth(), icon.getIconHeight());
        label1.setLocation(info.getLeft(), info.getTop());

        // Listen to the ImageInfo changes, and move image to new position.

        info.addListener(new FieldListener() {

            public void onChange(Transaction transaction, int i) {
                if (i == ImageInfo.LEFT_INDEX || i == ImageInfo.TOP_INDEX)
                    label1.setLocation(info.getLeft(), info.getTop());
            }
        });

        // Listen to mouse and change ImageInfo when image is dragged.

        Listener listener = new Listener(info);
        label1.addMouseListener(listener);
        label1.addMouseMotionListener(listener);
    }

    /**
     * Listens mouse events. If an image is dragged, update the fields of the
     * corresponding ImageInfo.
     */
    private class Listener extends MouseAdapter implements MouseMotionListener {

        private final ImageInfo _info;

        private long _lastUpdate;

        private int _left, _top;

        private boolean _dragging;

        public Listener(ImageInfo info) {
            _info = info;
        }

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            _left = e.getX();
            _top = e.getY();
            _dragging = true;
        }

        public void mouseDragged(MouseEvent e) {
            long now = System.currentTimeMillis();

            if (_dragging && now > _lastUpdate + 30) {
                if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
                    _lastUpdate = now;

                    Site.getLocal().allowThread();

                    Transaction transaction = Site.getLocal().startTransaction();

                    _info.setLeft(e.getX() + ((JLabel) e.getSource()).getX() - _left);
                    _info.setTop(e.getY() + ((JLabel) e.getSource()).getY() - _top);

                    // Updating fields asynchronously with beginCommit instead
                    // of commit makes moves smoother.
                    transaction.beginCommit(null);
                }
            }
        }

        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            _dragging = false;
        }
    }
}
