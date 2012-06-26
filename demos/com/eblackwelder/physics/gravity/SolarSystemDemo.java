package com.eblackwelder.physics.gravity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.beans.EventHandler;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.eblackwelder.physics.AbstractDemo;
import com.eblackwelder.physics.PhysicsMain;
import com.eblackwelder.physics.options.BooleanOption;
import com.eblackwelder.physics.options.DoubleRangeOption;
import com.eblackwelder.physics.options.Option;
import com.eblackwelder.world.World;
import com.eblackwelder.world.WorldObject;
import com.eblackwelder.world.driver.WorldDriverBase;
import com.eblackwelder.world.model.impl.AbstractWorldObject;
import com.eblackwelder.world.ui.IGraphicsContext;
import com.eblackwelder.world.ui.Renderer;
import com.eblackwelder.world.ui.UIWorldDriver;

public class SolarSystemDemo extends AbstractDemo {

	private final Option<Double> yearsPerSecond = new DoubleRangeOption("Earth years per second", 0, 10, 0.3, .1);
	private final Option<Double> mercurySizePixels = new DoubleRangeOption("Mercury size (pixels)", 0.1, 10.0, 1.0, .1);
	private final Option<Boolean> useImages = new BooleanOption("Render with images", true);

	private PlanetRenderer renderer;
	private OrbitOverlay overlay;
	
	@Override
	public List<Option<?>> getConfigurationOptions() {
		List<Option<?>> options = super.getConfigurationOptions();
		options.add(yearsPerSecond);
		options.add(mercurySizePixels);
		options.add(useImages);
		return options;
	}

	@Override
	public String getTitle() {
		return "Solar System";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
			"This demo shows off our very own solar system.",
			" ",
			"1. Only the 8 \"official\" planets are rendered.",
			"2. Planet sizes are proportional to one another, minimum 1 pixel each.",
			"   (The sun is always a few white pixels in the center of the system.)",
			"3. The distances between planets are proportional to one another.",
			"4. Orbit periods are proportional (you control how many Earth years elapse per second).",
			" ",
			" ",
			"Images courtesy of NASA/SDO and the AIA, EVE, and HMI science teams.",
			" ",
		};
	}

	@Override
	public WorldDriverBase createDriver(JFrame parent) {
		UIWorldDriver driver = new UIWorldDriver(getTitle());
		super.configureWorld(driver);
		World world = driver.getWorld();
		
		List<OrbitingObject> planets = createPlanets();
		Map<String, BufferedImage> planetImages = createImages(planets);
		this.renderer = new PlanetRenderer(planetImages);
		this.overlay = new OrbitOverlay(planets);
		
		//space (the final frontier):
		driver.setBackgroundColor(Color.BLACK);
		
		//the planets
		world.addObjects(planets);
		
		//overlay (for statistics)
		world.addObject(overlay);

		driver.setRendererForType(renderer, OrbitingObject.planetType);
		driver.getFrame().addMouseWheelListener(EventHandler.create(MouseWheelListener.class, this, "mouseWheelMoved", "unitsToScroll"));
		return driver;
	}
	
	int unitsScrolled = 0;
	double scalePerUnit = -0.1;
	double maxZoom = 50;
	double minZoom = 0.1;
	public void mouseWheelMoved(int unitsToScroll) {
		unitsScrolled += unitsToScroll;
		double power = (double) unitsScrolled * scalePerUnit;
		double zoom = Math.pow(10, power);
		zoom = Math.min(Math.max(zoom, minZoom), maxZoom);
		System.out.println("scrolled " + unitsToScroll + ", zoom=" + zoom);
		renderer.setZoom(zoom);
		overlay.setZoom(zoom);
	}

	private Map<String, BufferedImage> createImages(List<OrbitingObject> planets) {
		Map<String, BufferedImage> planetImages = new HashMap<String, BufferedImage>();
		if (useImages.getValue()) {
			for (OrbitingObject planet : planets) {
				String name = planet.getName();
				String resourceName = "resources/" + name + ".transparent.png";
				BufferedImage bufferedImage = null;
				InputStream is = getClass().getResourceAsStream(resourceName);
				if (is != null) {
					try {
						bufferedImage = ImageIO.read(is);
					} catch (IOException e) {}
				}
				if (bufferedImage != null) {
					planetImages.put(name, bufferedImage);
				} else {
					System.out.println("Could not load image for planet " + name);
				}
			}
		}
		return planetImages;
	}
	
	protected List<OrbitingObject> createPlanets() {
		Dimension size = super.getSize();
		double yearsPerMilli = yearsPerSecond.getValue() / 1000.0;
		double width = Math.max(size.getWidth(), size.getHeight());
		double radiusScale = mercurySizePixels.getValue() / Planet.Mercury.getRadius();
		final double distanceKmScale = width / 2.0 / (Planet.Neptune.getDistanceFromSunKm() + radiusScale * Planet.Neptune.getRadiusKm());
		
		List<OrbitingObject> planetObjects = new ArrayList<OrbitingObject>();
		planetObjects.add(new OrbitingObject("Sol", Color.YELLOW, 0, 2.0, 1, 0));
		for (Planet p : Planet.values()) {
			OrbitingObject orbit = new OrbitingObject(p, yearsPerMilli, radiusScale, distanceKmScale);
			planetObjects.add(orbit);
		}
		return planetObjects;
	}
	
	public static void main(String[] args) {
		PhysicsMain.runDemo(SolarSystemDemo.class.getName());
	}
	
	/**
	 * Internal class for overlaying statistics and other fun stuff.
	 * @author Ethan
	 */
	static class OrbitOverlay extends AbstractWorldObject implements Renderer {
		private final List<OrbitingObject> planets;
		private final Font font = Font.getFont("Arial");
		private final int lineHeight = 14; //px
		private final int insets = 20; //px
		
		private double zoom = 1.0;
		
		public OrbitOverlay(List<OrbitingObject> planets) {
			super("Orbit Overlay");
			this.planets = planets;
		}
		
		public void setZoom(double zoom) {
			this.zoom = zoom;
		}

		@Override
		public void render(WorldObject self, IGraphicsContext context) {
			Graphics2D overlayLayer = context.getOverlayLayer();
			overlayLayer.setFont(font);

			int xInset = insets; //px
			int yInset = insets; //px
		
			for (OrbitingObject planet : planets) {
				yInset += lineHeight;
				overlayLayer.setColor(planet.getColor());
				double yearsElapsed = planet.getElapsedYears();
				String text = yearsElapsed > 0.0 ?
						String.format("%s: Year %.2f", planet.getName(), yearsElapsed) :
						planet.getName();
				overlayLayer.drawString(text, xInset, yInset);
			}
			
			String text = String.format("Zoom: %.1f", zoom);
			overlayLayer.setColor(Color.WHITE);
			overlayLayer.drawString(text, insets, context.getCanvasSize().height - insets - lineHeight);
		}
	}
}
