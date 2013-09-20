package gui;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import javax.swing.filechooser.FileFilter;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

import util.S3Util;

import client.ZorkClient;

import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Pattern;

public class zorkGUI extends JFrame implements ActionListener {

	public static int confirmed = 0;
	protected String superUserName;
	public JPanel content = new JPanel();
	private BorderLayout bl = new BorderLayout();
	private JLabel image, text, titleLabel; // contains the image to display
			// private JScrollPane pane; //scrollable pane that holds the image
	private JMenuBar menuBar; // the menubar
	private JMenu registerAndLogin, saveAndLoad;// map;
	private JMenuItem register, login, newGame, saveGame, loadGame, createGame;// saveMap,loadMap;
	private JButton dataButton[] = new JButton[10];
	private int guiRefresh = 0;
	private JScrollPane sp;
	private JTextArea gameProcessField;
	private JTextField inputField;
	// private JFileChooser fc; //file chooser to choose file

	// Variable for Register
	private JLabel hintLabel = new JLabel("Zork Register");
	private JPanel totalRegister = new JPanel(new GridLayout(8, 1));
	private JTextField nameText = new JTextField(20);
	private JPasswordField passwordText = new JPasswordField(20);
	private JPasswordField reEnterPasswordText = new JPasswordField(20);
	private JButton reg = new JButton("Register");
	private JButton reset = new JButton("Reset");
	private JButton cancel = new JButton("Cancel");

	// Variable for Login
	private JLabel loginHintLabel = new JLabel("Zork Login");
	private JPanel totalLogin = new JPanel(new GridLayout(8, 1));
	private JTextField loginNameText = new JTextField(20);
	private JPasswordField loginPasswordText = new JPasswordField(20);
	private JButton loginLogin = new JButton("Login");
	private JButton loginReset = new JButton("Reset");
	private JButton loginCancel = new JButton("Cancel");
	
	// create
    private JFileChooser fileChooser;
    private JRadioButton setPrivate, setPublic;
    private JTextField fileNameField;
    private JButton submitFile;
    private JPanel createPanel,renamePanel,radioPanel;
    private ButtonGroup createButtonGroup;
    private File mapToUpload = null;
    
    // game play
	private JPanel loadGamePanel = new JPanel(new GridLayout(8, 1));
	private JPanel playPanel;

	public zorkGUI() {

		/**
		 * These are for Register
		 * **/

		JPanel regHint = new JPanel();
		regHint.add(hintLabel);
		totalRegister.add(regHint);

		for (int i = 0; i < 10; ++i) {
			dataButton[i] = new JButton();
			dataButton[i].addActionListener(this);
		}
		JPanel loginName = new JPanel();
		JLabel name = new JLabel("User Name");
		loginName.add(name);
		loginName.add(nameText);
		totalRegister.add(loginName);

		JPanel loginPassword = new JPanel();
		JLabel password = new JLabel("Password");
		loginPassword.add(password);
		loginPassword.add(passwordText);
		totalRegister.add(loginPassword);

		JPanel rePasswordPanel = new JPanel();
		JLabel rePasswordLabel = new JLabel("Confirm");
		rePasswordPanel.add(rePasswordLabel);
		rePasswordPanel.add(reEnterPasswordText);
		totalRegister.add(rePasswordPanel);

		JPanel buttons = new JPanel();
		reg.addActionListener(this);
		buttons.add(reg);
		reset.addActionListener(this);
		buttons.add(reset);
		totalRegister.add(buttons);

		totalRegister.setVisible(true);

		/**
		 * These are for login
		 * **/
		JPanel logHint = new JPanel();
		logHint.add(loginHintLabel);
		totalLogin.add(logHint);

		JPanel loginLoginName = new JPanel();
		JLabel loginUserName = new JLabel("User Name");
		loginLoginName.add(loginUserName);
		loginLoginName.add(loginNameText);
		totalLogin.add(loginLoginName);

		JPanel loginPasswordPanel = new JPanel();
		JLabel loginPasswordLabel = new JLabel("Password");
		loginPasswordPanel.add(loginPasswordLabel);
		loginPasswordPanel.add(loginPasswordText);
		totalLogin.add(loginPasswordPanel);

		JPanel loginButtons = new JPanel();
		loginLogin.addActionListener(this);
		loginButtons.add(loginLogin);
		loginReset.addActionListener(this);
		loginButtons.add(loginReset);
		totalLogin.add(loginButtons);

		playPanel = new JPanel();
		playPanel.setLayout(new BorderLayout());

		gameProcessField = new JTextArea();
		gameProcessField.setEditable(false);
		DefaultCaret caret = (DefaultCaret) gameProcessField.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		sp = new JScrollPane(gameProcessField);
		playPanel.add(new Label("Game process:"), BorderLayout.NORTH);
		playPanel.add(sp, BorderLayout.CENTER);

		inputField = new JTextField();
		playPanel.add(inputField, BorderLayout.SOUTH);
        inputField.addActionListener(this);
		/**
		 * These are for main GUI
		 * **/

		content.setLayout(bl);
		// initialize file chooser
		// fc = new JFileChooser();
		// fr.setFocusable(false);
		// initialize the display window
		// image=new JLabel();
		// pane=new JScrollPane();
		text = new JLabel("Console Output: ");

		// content.add(text,BorderLayout.SOUTH);

		menuBar = new JMenuBar();

		registerAndLogin = new JMenu("Login");
		menuBar.add(registerAndLogin);
		saveAndLoad = new JMenu("Archive");
		menuBar.add(saveAndLoad);

		register = new JMenuItem("Register");
		register.addActionListener(this);
		login = new JMenuItem("Login");
		login.addActionListener(this);
		registerAndLogin.add(register);
		registerAndLogin.add(login);

		newGame = new JMenuItem("New Game");
		newGame.addActionListener(this);
		saveAndLoad.add(newGame);

		saveGame = new JMenuItem("Save Game");
		saveGame.addActionListener(this);
		loadGame = new JMenuItem("Load Game");
		loadGame.addActionListener(this);
		saveAndLoad.add(saveGame);
		saveAndLoad.add(loadGame);

		createGame = new JMenuItem("Create Game");
		createGame.addActionListener(this);
		saveAndLoad.add(createGame);

		content.add(menuBar, BorderLayout.NORTH);

		saveAndLoad.setEnabled(false);

		setContentPane(content);
		setTitle("Zork>>");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();

	}

	public void actionPerformed(ActionEvent e) {

		// Handle Register Process
		if (e.getSource() == reg) {
			String userName = nameText.getText();
			String userPassword = String.valueOf(passwordText.getPassword());
			String reEnterPassword = String.valueOf(reEnterPasswordText
					.getPassword());
			Pattern p = Pattern.compile("[^a-z0-9]", Pattern.CASE_INSENSITIVE);

			if (userName.equals("")) {
				hintLabel.setText("Invalid User Name");
			} else if (p.matcher(userName).find()) {
				hintLabel.setText("User Name Cannot Contain Special Character");
			} else if (userName.length() < 4) {
				hintLabel.setText("User name too short");
			} else if (userName.length() > 20) {
				hintLabel.setText("User name too long");
			} else if (userPassword.equals("")) {
				hintLabel.setText("Please type password");
			} else if (reEnterPassword.equals("")) {
				hintLabel.setText("Password cannot be empty");
			} else if (reEnterPassword.equals(userPassword) == false) {
				hintLabel.setText("Please verify two passwords");
			} else {
				// Check if User already exist
				boolean flag = false;
				// User user=new User(userName,userPassword);
				// User userCheck=User.check(user);
				// if(userCheck!=null){
				// System.out.println("Search Result:"+userCheck+" already exit.");
				// flag=true;
				// }
				flag = ZorkClient.register(userName, userPassword);
				superUserName = userName;
				if (flag == false) {// Exist
					hintLabel.setText("Hint: User [" + userName
							+ "] alreay exist.");
				} else {
					// //Store User Info
					// FileOutputStream file=null;
					// try {
					// file = new FileOutputStream("login.txt",true);
					// }catch(IOException i)
					// {i.printStackTrace();}
					// try {
					// file.write((user.toString()).getBytes());
					// } catch (IOException ioe)
					// {
					// ioe.printStackTrace();
					// }
					// try {
					// file.close();
					// } catch (IOException ie) {
					// ie.printStackTrace();
					// }
					hintLabel.setText("Hint: User [" + userName
							+ "] registered successfully.");
				}
			}
		}
		// Handle Reset Request
		else if (e.getSource() == reset) {
			// zorkGUI.confirmed=1;
			hintLabel.setText("Zork Resgister");
			nameText.setText("");
			passwordText.setText("");
			reEnterPasswordText.setText("");
		}
		// Handle Cancel
		else if (e.getSource() == cancel) {
			// Return to waiting screen
			// map.setEnabled(true);
			System.exit(1);
		}
		/** END OF HANDLING FOR REGISTER **/

		// Handle Login Process
		if (e.getSource() == loginLogin) {
			String luserName = loginNameText.getText();
			String luserPassword = String.valueOf(loginPasswordText
					.getPassword());
			if ("".equals(luserName)) {
				loginHintLabel.setText("Invalid User Name");
			} else if ("".equals(luserPassword)) {
				loginHintLabel.setText("Invalid Password");
			} else {
				boolean flag;// if registered
				// User luser=new User(luserName,luserPassword);
				// User userChecked=User.check(luser);
				// if(userChecked!=null){
				// flag=true;
				// }
				// else{
				// flag=false;
				// }
				flag = ZorkClient.login(luserName, luserPassword);
				superUserName = luserName;
				if (flag == false) {
					loginHintLabel.setText("Hint: User [" + luserName
							+ "] does not exist or the password is wrong.");
				} else {
					// Verify user info
					// if(flag==true &&
					// luserPassword.equals(userChecked.getPassword())){
					loginHintLabel.setText("Hint: User [" + luserName
							+ "] Logged In !");
					saveAndLoad.setEnabled(true);
					registerAndLogin.setEnabled(false);
					// after logged in

					// }
					// else{
					// loginHintLabel.setText("Hint: User ["+luserName+"] Wrong Password");
					// }
				}
			}
		}
		// Reset
		else if (e.getSource() == loginReset) {
			loginHintLabel.setText("Zork Login");
			loginNameText.setText("");
			loginPasswordText.setText("");
		}
		// Cancel
		else if (e.getSource() == loginCancel) {
			System.exit(1);
		}

		/** END OF HANDLING FOR LOGIN **/

		if (e.getSource() == register) {

			if (guiRefresh == 1 && bl.getLayoutComponent(BorderLayout.CENTER)!=null) {
				content.remove(bl.getLayoutComponent(BorderLayout.CENTER));
			}
			content.add(totalRegister, BorderLayout.CENTER);
			guiRefresh = 1;

		}
		if (e.getSource() == login) {

			if (guiRefresh == 1 && bl.getLayoutComponent(BorderLayout.CENTER)!=null) {
				content.remove(bl.getLayoutComponent(BorderLayout.CENTER));
			}
			content.add(totalLogin, BorderLayout.CENTER);
			guiRefresh = 1;

		}

		if (e.getSource() == newGame) {

			if (guiRefresh == 1 && bl.getLayoutComponent(BorderLayout.CENTER)!=null) {
				content.remove(bl.getLayoutComponent(BorderLayout.CENTER));

			}
			S3Util s3Util = new S3Util();
			List<String> maps= s3Util.getMapsList(superUserName);
			// content.add(totalRegister,BorderLayout.CENTER);
			Iterator<String> mapsIter = maps.iterator();
			int i = 0;
			while(i<10 && mapsIter.hasNext()){
				System.out.println(mapsIter.next());
				
			}
			guiRefresh = 1;

		}
		if (e.getSource() == saveGame) {

			if (guiRefresh == 1 && bl.getLayoutComponent(BorderLayout.CENTER)!=null) {
				content.remove(bl.getLayoutComponent(BorderLayout.CENTER));
			}

			// content.add(totalLogin,BorderLayout.CENTER);
			guiRefresh = 1;

		}
		if (e.getSource() == loadGame) {

			if (guiRefresh == 1 && bl.getLayoutComponent(BorderLayout.CENTER)!=null) {
				content.remove(bl.getLayoutComponent(BorderLayout.CENTER));
			}

			// ZorkClient.saveData(superUserName,1, "some data");
			Map<Integer, String> dataSlot = ZorkClient.getData(superUserName);
			for (int i = 0; i < 10; ++i) {
				System.out.println(dataSlot.get(i));
				dataButton[i].setText(dataSlot.get(i));
				loadGamePanel.add(dataButton[i]);

			}

			content.add(loadGamePanel, BorderLayout.CENTER);
			guiRefresh = 1;

		}
		if (e.getSource() == createGame) {

			if (guiRefresh == 1 && bl.getLayoutComponent(BorderLayout.CENTER)!=null) {
				content.remove(bl.getLayoutComponent(BorderLayout.CENTER));
			}

			
			fileChooser = new JFileChooser("Please choose a map to upload (xml format)");
			FileFilter fliter = new FileFilter() {
				 
			    public String getDescription() {
			        return "XML Documents (*.xml)";
			    }
			 
			    public boolean accept(File f) {
			        if (f.isDirectory()) {
			            return true;
			        } else {
			            return f.getName().toLowerCase().endsWith(".xml");
			        }
			    }
			};
			fileChooser.addChoosableFileFilter(fliter);
			int returnVal = fileChooser.showOpenDialog(content);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
            	mapToUpload = fileChooser.getSelectedFile();
            }
            if(mapToUpload != null)	{
                setPrivate = new JRadioButton("private");            
                setPublic = new JRadioButton("public");
                setPublic.setSelected(true);
                
                fileNameField = new JTextField(mapToUpload.getName(),30);
                submitFile = new JButton("Submit");
                submitFile.addActionListener(this);
                createPanel = new JPanel(new BorderLayout());
                createButtonGroup = new ButtonGroup();
                createButtonGroup.add(setPrivate);
                createButtonGroup.add(setPublic);
                
                renamePanel = new JPanel(new FlowLayout());
                renamePanel.add(new Label("MapName"));
                renamePanel.add(fileNameField);
                
                radioPanel = new JPanel(new FlowLayout());
                radioPanel.add(new Label("Do you want to make the map private or public?"));
                radioPanel.add(setPublic);
                radioPanel.add(setPrivate);
                createPanel.add(renamePanel,BorderLayout.NORTH);
                createPanel.add(radioPanel,BorderLayout.CENTER);
                createPanel.add(submitFile,BorderLayout.SOUTH);
                
                content.add(createPanel, BorderLayout.CENTER);
            	
            }

            
			guiRefresh = 1;

		}
		
		if (e.getSource() == submitFile){
			S3Util s3Util = new S3Util();
			if(fileNameField.getText().equals("")){
				JOptionPane.showMessageDialog(this,"The map name can not be empty");
			}
			if(setPrivate.isSelected()){
				s3Util.S3MapUpload(mapToUpload, fileNameField.getText(), superUserName, false);
			}
			else{
				s3Util.S3MapUpload(mapToUpload, fileNameField.getText(), superUserName, true);	
			}
		}
		
		if(e.getSource() == inputField){
			System.out.print(inputField.getText());
			
			gameProcessField.append(inputField.getText() + "\n");
			inputField.setText("");
			
			
		}

		for (int i = 0; i < 10; i++) {
			if (e.getSource() == dataButton[i]) {
				if (guiRefresh == 1 && bl.getLayoutComponent(BorderLayout.CENTER)!=null) {
					content.remove(bl.getLayoutComponent(BorderLayout.CENTER));
				}
				content.add(playPanel, BorderLayout.CENTER);
				gameProcessField.append("hello\n");
				guiRefresh = 1;

			}

		}
		content.revalidate();
		content.repaint();

	}

}
