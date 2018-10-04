package de.chrissx.ytmnychat;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client {
	
	private static JFrame frame;
	private static JTextArea chat;
	private static JTextField eingabe;
	private static JButton senden;
	private static JScrollPane scroller;
	private static Socket client;
	private static final int port = 555;
	private static String ip = "localhost";
	private static String msg;
	private static PrintWriter writer;
	private static OutputStream out;
	private static InputStream in;
	private static BufferedReader reader;
	private static boolean isButtonPressed = false;

	public static void main(String[] args) {
		
		initWindow();
		
		senden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				msg = senden.getText();
				isButtonPressed = true;
			}});
		
		initSocket();
		
		loop();
	}
	
	private static void readMessage() {
		String s = null;
		
		try {
			while((s = reader.readLine()) != null) {
				chat.append(s);
			}
		} catch (IOException e) {
			chat.append("Problem reading messages(send to chrissx):\n" + e.toString() + "\n" + e.getMessage() + "\n");
		}
	}
	
	private static void sendMessage() {
		writer.write(msg + "\n");
		writer.flush();
	}
	
	private static void loop() {
		while(true) {
			if(!isButtonPressed) {
				try {
					readMessage();
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					chat.append("Problem pausing thread(send to chrissx):\n" + e.toString() + "\n" + e.getMessage() + "\n");
				}
			}else {
				sendMessage();
			}
		}
	}
	
	private static void initSocket() {
			eingabe.setText("ENTER ADDRESS HERE");
			
			while(!isButtonPressed) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					chat.append("Problem pausing thread(send to chrissx):\n" + e.toString() + "\n" + e.getMessage() + "\n");
				}
			}
			ip = msg;
			isButtonPressed = false;
			try {
				client = new Socket(ip, port);
			} catch (Exception e) {
				chat.append("Problem initializing WebSocket(send to chrissx):\n" + e.toString() + "\n" + e.getMessage() + "\n");
			}
			
			try {
				out = client.getOutputStream();
			} catch (IOException e) {
				chat.append("Problem initializing Output-Stream(send to chrissx):\n" + e.toString() + "\n" + e.getMessage() + "\n");
			}
			writer = new PrintWriter(out);
			
			try {
				in = client.getInputStream();
			} catch (IOException e) {
				chat.append("Problem initializing Input-Stream(send to chrissx):\n" + e.toString() + "\n" + e.getMessage() + "\n");
			}
			reader = new BufferedReader(new InputStreamReader(in));
	}
	
	private static void initWindow() {
		frame = new JFrame("YTMny-internal Chat");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chat = new JTextArea();
		eingabe = new JTextField();
		senden = new JButton();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(1280, 720);
		frame.setMaximumSize(new Dimension(1280, 720));
		frame.setMinimumSize(new Dimension(1280, 720));
		frame.setPreferredSize(new Dimension(1280, 720));
		frame.setFocusable(true);
		chat.setFocusable(false);
		chat.setBounds(0, 0, 1180, 670);
		chat.setSize(1250, 670);
		chat.setMaximumSize(new Dimension(1250, 670));
		chat.setMinimumSize(new Dimension(1250, 670));
		chat.setPreferredSize(new Dimension(1250, 670));
		eingabe.setBounds(0, 670, 1180, 25);
		eingabe.setSize(1180, 25);
		eingabe.setMaximumSize(new Dimension(1180, 25));
		eingabe.setMinimumSize(new Dimension(1180, 25));
		eingabe.setPreferredSize(new Dimension(1180, 25));
		senden.setBounds(1180, 670, 100, 25);
		senden.setSize(100, 25);
		senden.setMaximumSize(new Dimension(100, 25));
		senden.setMinimumSize(new Dimension(100, 25));
		senden.setPreferredSize(new Dimension(100, 25));
		senden.setText("SENDEN");
		scroller = new JScrollPane(chat);
		scroller.setSize(25, 670);
		scroller.setBounds(1250, 0, 25, 670);
		scroller.setPreferredSize(new Dimension(25, 670));
		scroller.setMinimumSize(new Dimension(25, 670));
		scroller.setMaximumSize(new Dimension(25, 670));
		frame.setLayout(null);
		frame.add(chat);
		frame.add(scroller);
		frame.add(eingabe);
		frame.add(senden);
		frame.pack();
	}

}
