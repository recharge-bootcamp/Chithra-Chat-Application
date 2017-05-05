package com.paypal.client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class LoginScreen extends JDialog {

	/**
	 * @author Chithra
	 * Login window for Let's Chat application
	 */
	private static final long serialVersionUID = 1L;
	public static final String STATUS_ONLINE = "online";
	private JTextField userNameField;
	private JButton submitButton;
	private JButton cancelButton;
	private JLabel userNameLabel;
	public HashMap<String, String> userStatusMap;

	public static void main(String[] args) {
		LoginScreen login = new LoginScreen();
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				login.createAndShowGui();

			}
		});

	}

	private void createAndShowGui() {

		JPanel panel = new JPanel();

		// Add Username textfield and label
		userNameLabel = new JLabel("Username");
		userNameLabel.setBounds(10, 10, 80, 25);
		panel.add(userNameLabel);

		userNameField = new JTextField();
		userNameField.setPreferredSize(new Dimension(120, 30));
		userNameField.setBounds(100, 100, 200, 25);
		panel.add(userNameField);

		submitButton = new JButton("Submit");
		panel.add(submitButton);
		cancelButton = new JButton("Cancel");
		panel.add(cancelButton);

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int confirm = JOptionPane.showConfirmDialog(panel, "Are you sure you want to exit?", "Alert",
						JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					dispose();
				} else if (confirm == JOptionPane.NO_OPTION) {
					return;
				}
			}
		});

		submitButton.addActionListener(new ActionListener() {

			private ChatWindow cw;

			@Override
			public void actionPerformed(ActionEvent e) {
				String username = userNameField.getText();
				if (username.equals("")) {
					JOptionPane.showMessageDialog(panel, "Please enter username", "Alert", JOptionPane.OK_OPTION);
					// dispose();
					return;
				}
//				userStatusMap = new HashMap<String, String>();
//				if (userStatusMap.containsKey(username)) {
//					JOptionPane.showInternalMessageDialog(panel,
//							"This username already exists. Please choose a unique name.");
//				} else {
//					userStatusMap.put(username, STATUS_ONLINE);
//				}

				cw = new ChatWindow(username);
				cw.initializeSocket();
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						cw.createAndShowGui();
					}

				});
				cw.callSwingWorker();
				dispose();
			}
		});

		add(panel);
		setSize(250, 120);
		setTitle("Login to LetsChat");

		setVisible(true);
		ImageIcon icon = new ImageIcon(getClass().getResource("chatIcon.png"));
		setIconImage(icon.getImage());
		setDefaultLookAndFeelDecorated(true);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

	};

}
