package com.paypal.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingWorker;

public class ChatWindow extends JFrame {

	/**
	 * This is a swing class which defines the UI of the chat application window
	 */

	private JPanel panel;
	private JTextField myTextField;
	private JTextArea textArea;
	private JScrollPane scrollPane;
	public String sendToServer;
	private String userNameText;
	private JList<String> usersList;
	// private JMenuItem menuItem;

	private static final long serialVersionUID = 1L;
	public static final String ME_TEXT = "Me: ";
	public static final String IMAGE_FILE_PATH = "C:\\Users\\Bhargav\\workspace\\ChatWithMe\\src\\com\\paypal\\util\\chatIcon.png";
	private DefaultListModel<String> usersListModel;
	Socket connection;

	 public static void main(String[] args) {
	 ChatWindow frame = new ChatWindow("");
	 frame.createAndShowGui();
	 }

	public ChatWindow(String userName) throws HeadlessException {

		setTitle("Welcome " + userName + "!");
		this.userNameText = userName;
		
		
	}
	
	public void initializeSocket(){
		try {
			connection = new Socket(InetAddress.getLocalHost(), 3000);
			PrintWriter pw = new PrintWriter(connection.getOutputStream(), true);
			System.out.println("Sending user name"+getUserNameText());
			pw.println("#"+getUserNameText());
		} catch (UnknownHostException e) {
			System.out.println("Error resolving host.."+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("An exception occured while establishing connection"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void callSwingWorker(){
		SendAndReceiveWorker task = new SendAndReceiveWorker();
		task.execute();
	}

	public String getUserNameText() {
		return userNameText;
	}

	public void setUserNameText(String userNameText) {
		this.userNameText = userNameText;
	}

	void createAndShowGui() {

		panel = new JPanel();
		myTextField = new JTextField();
		textArea = new JTextArea();
		usersList = new JList<String>();
		usersListModel = new DefaultListModel<String>();

		textArea.setMargin(new Insets(5, 5, 5, 5));
		textArea.setEditable(false);
		usersListModel.addElement(getUserNameText());
		
		
		usersList.setBackground(Color.WHITE);
		usersList.setModel(usersListModel);
		usersList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    JScrollPane usersScrollPane = new JScrollPane(usersList);
	    usersScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	    usersScrollPane.setPreferredSize(new Dimension(120, 50));

	    panel.setLayout(new GridLayout(1,1));
		scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		textArea.setBounds(1, 2, 60, 100);
		add(scrollPane, BorderLayout.CENTER);
		add(usersScrollPane, BorderLayout.EAST);
		add(myTextField, BorderLayout.SOUTH);


			myTextField.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					sendToServer =myTextField.getText();
					myTextField.setText("");
					textArea.append("\n");
					textArea.append(ME_TEXT + sendToServer);
					textArea.append("\n");
					
					try {
						PrintWriter output = new PrintWriter(connection.getOutputStream(), true);
						output.println(sendToServer);
					} catch (IOException e1) {
						System.out.println("Error sending data to other user"+e1.getMessage());
						e1.printStackTrace();
					}
					
				}
				
			}
		});

		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				int option = JOptionPane.showConfirmDialog(getContentPane(), "Are you sure you want to exit My Chat?",
						"Alert", JOptionPane.YES_NO_OPTION);
				if (option == JOptionPane.YES_OPTION) {
					dispose();
				}
			}
		});
		
		ImageIcon icon = new ImageIcon(getClass().getResource("chatIcon.png"));
		setIconImage(icon.getImage());
		
		setDefaultLookAndFeelDecorated(true);
		setLocationRelativeTo(null);
		setSize(400, 400);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setVisible(true);

	}


	class SendAndReceiveWorker extends SwingWorker<List<String>, String> {


		@Override
		protected List<String> doInBackground() throws Exception {
			BufferedReader input;
			List<String> dataList = new ArrayList<String>();

			input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			String fromServerUser;

			while ((fromServerUser = input.readLine())!=null) {
				publish(fromServerUser);
				dataList.add(fromServerUser);
				if (fromServerUser.equalsIgnoreCase("EXIT")) {
					break;
				}
			}
			input.close();
			connection.close();
			return dataList;
		}
		
		
		@Override
		protected void process(List<String> list) {
			for (String data : list) {
				textArea.append("\nFrom Server: " + data);
			}
		}
		
		public Socket getConnection(){
			return connection;
		}

		
	}
	
	public String getInputText(){
		return sendToServer;
	}

	
		


}
