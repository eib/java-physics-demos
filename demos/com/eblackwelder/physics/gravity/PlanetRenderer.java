package com.eblackwelder.physics.gravity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import com.eblackwelder.math.Position;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.ui.IGraphicsContext;
import com.eblackwelder.world.ui.Renderer;

public class PlanetRenderer implements Renderer {

	private final Map<String, BufferedImage> planetImages;
	private final Map<String, Image> scaledImages = new HashMap<String, Image>();
	
	private double zoom = 1;
	
	public PlanetRenderer(Map<String, BufferedImage> planetImages) {
		this.planetImages = planetImages;
	}

	public void setZoom(double zoom) {
		if (this.zoom != zoom) {
			scaledImages.clear();
		}
		this.zoom = zoom;
	}

	@Override
	public void render(WorldObject object, IGraphicsContext context) {
		OrbitingObject planet = (OrbitingObject) object;
		Position p = planet.getPosition();
		Dimension size = context.getCanvasSize();
		
		Graphics2D mainLayer = context.getMainLayer();
		
		mainLayer.translate(size.width / 2, size.height / 2); //center in window
		mainLayer.translate(p.x * zoom, -p.y * zoom);
		
		String name = planet.getName();
		double zoomRadius = planet.getRadius() * zoom;
		if (zoomRadius > 3.0 && planetImages.containsKey(name)) {
			Image image = getScaledImage(name, zoomRadius);
			int leftX = (int) (-image.getWidth(null) / 2.0);
			int topY = (int) (-image.getHeight(null) / 2.0);
			mainLayer.setColor(Color.BLACK);
			mainLayer.setBackground(Color.BLACK);
			mainLayer.drawImage(image, leftX, topY, null);
		} else {
			int radius = (int) (planet.getRadius() * zoom);
			mainLayer.setColor(planet.getColor());
			mainLayer.fillOval(-radius, -radius, radius * 2, radius * 2);
		}
		mainLayer.translate(-p.x * zoom, p.y * zoom);
		mainLayer.translate(-size.width / 2, -size.height / 2);
	}
	
	protected Image getScaledImage(String key, double radius) {
		Image scaledImage;
		if (!scaledImages.containsKey(key)) {
			BufferedImage bufferedImage = planetImages.get(key);
			scaledImage = scaleToRadius(bufferedImage, radius);
			scaledImages.put(key, scaledImage);
		} else {
			scaledImage = scaledImages.get(key);
		}
		return scaledImage;
	}

	private Image scaleToRadius(BufferedImage bufferedImage, double radius) {
		double diameter = radius * 2.0;
		int width;
		int height;
		 //scale to the smaller dimension (assume the larger dimension will have rings)
		if (bufferedImage.getWidth() < bufferedImage.getHeight()) {
			width = (int) Math.ceil(diameter);
			height = (int) Math.ceil(diameter * bufferedImage.getHeight() / bufferedImage.getWidth());
		} else {
			height = (int) Math.ceil(diameter);
			width = (int) Math.ceil(diameter * bufferedImage.getWidth() / bufferedImage.getHeight());
		}
		Image image = bufferedImage.getScaledInstance(width, height, 0);
		
		return image;
	}

}
