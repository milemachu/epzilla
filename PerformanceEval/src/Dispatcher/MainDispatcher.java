//package Dispatcher;
//
//import java.awt.Button;
//import java.awt.TextArea;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.io.IOException;
//import java.util.Random;
//import java.util.Set;
//import java.util.TimerTask;
//import generated.ServerInfo;
//import generated.ServerInfoObjectModel;
//import javax.swing.BoxLayout;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import jstm.core.FieldListener;
//import jstm.core.Group;
//import jstm.core.Share;
//import jstm.core.Site;
//import jstm.core.TransactedList;
//import jstm.core.TransactedObject;
//import jstm.core.TransactedSet;
//import jstm.core.Transaction;
//import jstm.core.Transaction.AbortedException;
//import jstm.misc.Log;
//import jstm.transports.clientserver.ConnectionInfo;
//import jstm.transports.clientserver.Server;
//import jstm.transports.clientserver.socket.SocketClient;
//import jstm.transports.clientserver.socket.SocketServer;
//
//public class MainDispatcher {
//
//	private Share share;
//	private TransactedList<String> commonItems;
//	TextArea commonList;
//	TextArea processedList;
//	TextArea tf;
//	Button addItemsBtn;
//	Button processItemsBtn;
//	Button startServerBtn;
//	Button startClientBtn;
//
//	public static void main(String[] args) {
//		MainDispatcher main = new MainDispatcher();
//		main.run();
//	}
//
//	private void run() {
//		tf = new TextArea();
//		JFrame frame = buildWindow();
//		frame.setVisible(true);
//		Site.getLocal().registerObjectModel(new ServerInfoObjectModel());
//	}
//
//	private void addList(final TransactedList<String> info) {
//
////		commonList.setText("");
//		commonItems = info;
////		for (int j = 0; j < info.size(); j++) {
////			commonList.append(info.get(j) + "\n");
////		}
//
//		info.addListener(new FieldListener() {
//			public void onChange(Transaction transaction, int i) {
//				// commonList.setText("");
////				for (int j = 0; j < info.size(); j++) {
////					// commonList.append(info.get(j) + "\n");
////				}
//			}
//		});
//
//	}
//
//	private void addInfo(final ServerInfo info) {
//		if (info.getHasProcessingStarted() == 1)
//			addItemsBtn.setVisible(false);
//
//		if (info.getHasProcessingStarted() == 0)
//			addItemsBtn.setVisible(true);
//
//		if (info.getHasServerStarted() == 1)
//			startClientBtn.setVisible(true);
//
//		info.addListener(new FieldListener() {
//
//			public void onChange(Transaction transaction, int i) {
//
//				if (i == ServerInfo.HASPROCESSINGSTARTED_INDEX) {
//					if (info.getHasServerStarted() == 1)
//						startClientBtn.setVisible(true);
//					if (info.getHasProcessingStarted() == 1)
//						addItemsBtn.setVisible(false);
//					if (info.getHasProcessingStarted() == 0)
//						addItemsBtn.setVisible(true);
//				}
//			}
//		});
//
//	}
//
//	private JFrame buildWindow() {
//		JFrame frame = new JFrame("Epzilla Dispatcher Test");
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setResizable(false);
//
//		JPanel panel = new JPanel();
//		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//
//		startServerBtn = new Button("Start Server");
//		startServerBtn.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				tf.append("Starting server" + "\n\r");
//
//				try {
//					Server server = new SocketServer(4444);
//					server.start();
//
//					tf
//							.append("Attaching a share to sites group: server and clients" + "\n\r");
//
//					share = new Share();
//
//					tf.append("Waiting For Clients" + "\n\r");
//					// Once connected, retrieve the Group that represents the
//					// server and its
//					// clients
//
//					Group serverAndClientsSites = server.getServerAndClients();
//
//					// Open a share in this group is there is none yet
//
//					if (serverAndClientsSites.getOpenShares().size() == 0) {
//						Transaction transaction = Site.getLocal()
//								.startTransaction();
//						server.getServerAndClients().getOpenShares().add(share);
//						transaction.commit();
//					}
//
//					share = (Share) serverAndClientsSites.getOpenShares()
//							.toArray()[0];
//
//					share
//							.addListener(new TransactedSet.Listener<TransactedObject>() {
//
//								public void onAdded(Transaction transaction,
//										TransactedObject object) {
//									if (object instanceof ServerInfo)
//										addInfo((ServerInfo) object);
//
//									if (object instanceof TransactedList<?>)
//										addList((TransactedList<String>) object);
//
//								}
//
//								public void onRemoved(Transaction transaction,
//										TransactedObject object) {
//								}
//							});
//
//					// The share might already contain images, show them
//
//					for (TransactedObject o : share) {
//						if (o instanceof ServerInfo)
//							addInfo((ServerInfo) o);
//
//						if (o instanceof TransactedList<?>)
//							addList((TransactedList<String>) o);
//
//					}
//
//				} catch (AbortedException e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//				} catch (IOException e3) {
//					// TODO Auto-generated catch block
//					e3.printStackTrace();
//				}
//			}
//		});
//		panel.add(startServerBtn);
//
//		startClientBtn = new Button("Start Client");
//		startClientBtn.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				tf.append("Starting Client" + "\n\r");
//
//				try {
//					SocketClient client = new SocketClient("localhost", 4444);
//					ConnectionInfo connection = client.connect();
//
//					tf.append("Connected to server: "
//							+ connection.getServer().toString()+ "\n\r");
//
//					share = new Share();
//
//					tf.append("Number of Open Shares: "
//							+ String.valueOf(connection.getServerAndClients()
//									.getOpenShares().size())+ "\n\r");
//					// Once connected, retrieve the Group that represents the
//					// server and its
//					// clients
//
//					Set<Share> shares = connection.getServerAndClients()
//							.getOpenShares();
//
//					// Open a share in this group is there is none yet
//
//					share = (Share) shares.toArray()[0];
//
//					share
//							.addListener(new TransactedSet.Listener<TransactedObject>() {
//
//								public void onAdded(Transaction transaction,
//										TransactedObject object) {
//									if (object instanceof ServerInfo)
//										addInfo((ServerInfo) object);
//
//									if (object instanceof TransactedList<?>)
//										addList((TransactedList<String>) object);
//
//								}
//
//								public void onRemoved(Transaction transaction,
//										TransactedObject object) {
//								}
//							});
//
//					// The share might already contain images, show them
//
//					for (TransactedObject o : share) {
//						if (o instanceof ServerInfo)
//							addInfo((ServerInfo) o);
//
//						if (o instanceof TransactedList<?>)
//							addList((TransactedList<String>) o);
//
//					}
//
//				} catch (AbortedException e2) {
//					// TODO Auto-generated catch block
//					e2.printStackTrace();
//				} catch (IOException e3) {
//					// TODO Auto-generated catch block
//					e3.printStackTrace();
//				}
//			}
//		});
//		panel.add(startClientBtn);
//
//		addItemsBtn = new Button("Add Items");
//		addItemsBtn.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (Site.getLocal().getPendingCommitCount() < Site.MAX_PENDING_COMMIT_COUNT) {
//					Site.getLocal().allowThread();
//					Transaction transaction = Site.getLocal()
//							.startTransaction();
//					ServerInfo info = new ServerInfo();
//					info.setHasProcessingStarted(1);
//					commonItems = new TransactedList<String>();
//					share.add(commonItems);
//					share.add(info);
//					transaction.commit();
//					addCommonItems();
//				}
//			}
//		});
//
//		panel.add(addItemsBtn);
//
//		processItemsBtn = new Button("Process Items");
//
//		processItemsBtn.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				processItems();
//			}
//		});
//
//		panel.add(processItemsBtn);
//
//		commonList = new TextArea();
//		panel.add(commonList);
//
//		processedList = new TextArea();
//		panel.add(processedList);
//
//		// A panel for logs
//
//		tf.setEditable(false);
//		panel.add(tf);
//
//		// Write JSTM logs to the text area
//
//		Log.add(new Log() {
//
//			@Override
//			public void onWrite(String message) {
//				tf.append(message + "\n\r");
//				tf.setCaretPosition(tf.getText().length());
//			}
//		});
//
//		// Window
//
//		frame.getContentPane().add(panel);
//		frame.setSize(350, 700);
//		return frame;
//	}
//
//	private void addCommonItems() {
//		final java.util.Timer timer1 = new java.util.Timer();
//		final Random gen = new Random();
//		gen.setSeed(1000);
//		timer1.schedule(new TimerTask() {
//			int count = 0;
//
//			@Override
//			public void run() {
//				if (commonItems != null) {
//					String item = String.valueOf(gen.nextInt());
//					commonItems.add(item);
//					commonList.append(item + "\n");
//					count++;
//
//					if (count == 300)
//						timer1.cancel();
//				}
//			}
//		}, 1000, 50);
//	}
//
//	private void processItems() {
//		java.util.Timer timer2 = new java.util.Timer();
//
//		timer2.schedule(new TimerTask() {
//			@Override
//			public void run() {
//				try {
//					if (commonItems != null) {
//						if (commonItems.size() > 0) {
//							TransactedList<String> list = commonItems;
//							if (!list.isEmpty()) {
//								String item = list.get(0);
//								if (item != null) {
//									boolean state = commonItems.remove(item);
//									if (state) {
//										processedList.append(item + "\n");
//									}
//								}
//							}
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}, 1000, 100);
//
//	}
//
//}
