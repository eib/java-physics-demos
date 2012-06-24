package com.eblackwelder.physics.cursor;

import java.awt.Color;
import java.awt.event.KeyListener;
import java.beans.EventHandler;

import com.eblackwelder.graphics.cursor.CursorRenderer;
import com.eblackwelder.world.ui.UIWorldDriver;

public class KineticCursorDriver extends UIWorldDriver {

	private final KineticCursor cursor;
	
	public KineticCursorDriver(KineticCursor cursor) {
		super("Cursor");
		this.cursor = cursor;
		
		gridPanel.setBackground(Color.WHITE);
		gridPanel.setRendererForType(new CursorRenderer(), KineticCursor.cursorType);
		world.addObject(cursor);
		
		frame.addKeyListener(EventHandler.create(KeyListener.class, this, "keyPressed", "keyCode", "keyPressed"));
		frame.addKeyListener(EventHandler.create(KeyListener.class, this, "keyReleased", "keyCode", "keyReleased"));
	}

	public void keyPressed(int keyCode) {
		boolean toggle = true;
		cursor.movement.toggleDirectionForKey(keyCode, toggle);
	}

	public void keyReleased(int keyCode) {
		boolean toggle = false;
		cursor.movement.toggleDirectionForKey(keyCode, toggle);
	}
	
	@Override
	protected boolean needsRepaint() {
		return cursor.movement.isMoving() ||
		       cursor.getAcceleration().isAlmostZero(0.0001) ||
		       cursor.getVelocity().isAlmostZero(0.0001);
	}
}
