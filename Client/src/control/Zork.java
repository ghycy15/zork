package control;

import gui.zorkGUI;

import javax.swing.JFrame;

import client.ZorkClient;

public class Zork {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
<<<<<<< HEAD
		
		/*  test for client */
		//  ZorkClient.register("some", "other");
		 
		
=======

		/*
		 * test for client ZorkClient.login("some", "other");
		 */

>>>>>>> gun
		/*
		 * test for S3
		 */

		JFrame gui = new zorkGUI();
		gui.setVisible(true);
		gui.setFocusable(false);
		gui.setSize(1000, 600);

	}

}
