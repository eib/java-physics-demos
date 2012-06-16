package com.eblackwelder.physx;

import javax.swing.JFrame;

public class DemoUtils {

	public static void alwaysOnTop(JFrame frame) {
		try {
			frame.setAlwaysOnTop(true);
		} catch (SecurityException e) {
			System.out.println("Can't make the window \"" + frame.getTitle() + "\" always on top.");
		}
	}

}
