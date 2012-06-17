/**
 * 
 */
package com.eblackwelder.graphx;

import java.awt.Container;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.TimerTask;

class RepaintTask extends TimerTask {
	private final Reference<Container> ref;
	
	public RepaintTask(Container c) {
		this.ref = new WeakReference<Container>(c);
	}

	@Override public void run() {
		Container c = ref.get();
		if (c != null) {
			c.repaint();
		}
	}
}