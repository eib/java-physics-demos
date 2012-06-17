package com.eblackwelder.graphx;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Timer;

import javax.swing.JPanel;

import com.eblackwelder.world.World;


@SuppressWarnings("serial")
public class RenderPanel extends JPanel {

	private final Timer timer = new Timer("RenderPanel Repaint Timer");
	private final World world;
	private long lastPaintMillis = 0;
	private double framesPerSecond = 0;
	private boolean isStarted = false;
	private boolean paused = false;
	private final int repaintMillis;
	
	public RenderPanel(World world) {
		this(world, 20);
	}
	public RenderPanel(World world, int repaintMillis) {
		super.setBackground(new Color(0xEE, 0xEE, 0xEE));
		super.setPreferredSize(world.getPreferredSize());
		this.world = world;
		this.repaintMillis = repaintMillis;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (!paused) {
			if (!isStarted) {
				init();
				isStarted = true;
			}
			long millis = System.currentTimeMillis();
			long delta = millis - lastPaintMillis;
			
			world.update(delta);
			world.render((Graphics2D) g);
	
			//1 frame / delta ms * 1000 ms / 1s = 1000 / delta = X fps
			framesPerSecond = (delta == 0) ? Double.POSITIVE_INFINITY : 1000.0 / delta;
			lastPaintMillis = millis;
		}
	}

	private void init() {
		timer.schedule(new RepaintTask(this), repaintMillis, repaintMillis);
		lastPaintMillis = System.currentTimeMillis();
		isStarted = true;
	}
	
	public void pause() {
		paused = true;
		isStarted = false;
		timer.cancel();
	}
	
	public double getFramesPerSecond() {
		return framesPerSecond;
	}
}
