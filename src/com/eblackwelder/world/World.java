package com.eblackwelder.world;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.eblackwelder.graphx.renderable.Renderable;
import com.eblackwelder.graphx.renderable.RenderableCollection;
import com.eblackwelder.physx.Physics;
import com.eblackwelder.physx.effects.Effect;
import com.eblackwelder.world.model.Mass;
import com.eblackwelder.world.model.Updatable;
import com.eblackwelder.world.modes.WorldMode;

public class World implements Renderable {

	private final double speedFactor;
	private int numFrames = 0;
	private long totalMillisElapsed = 0;
	protected static World singleton;
	protected final Rectangle2D bounds;
	protected final Dimension preferredSize;
	private final WorldMode mode;
	
	private final Map<Integer, Collection<Renderable>> layers = new TreeMap<Integer, Collection<Renderable>>();
	private final Collection<Updatable> updatables = new HashSet<Updatable>();
	private final List<Mass>      affected = new ArrayList<Mass>();
	private final Collection<Object>    objectsToRemove = new ArrayList<Object>();
	private final Collection<Object>    objectsToAdd = new ArrayList<Object>();
	private final Collection<Effect>    effects = new ArrayList<Effect>();
	private final Collection<Physics>   physics = new ArrayList<Physics>();
	
	/** Converts Java's Component coordinate system to Cartesian coordinates (oriented and centered). */
	protected final AffineTransform baseRenderTransform;
	protected final AffineTransform reverseTransform;

	public static World getSingleton() {
		return World.singleton;
	}

	public static World newInstance(int width, int height, WorldMode mode) {
		singleton = new World(width, height, mode);
		return singleton;
	}
	public static World newInstance(Dimension size, WorldMode mode) {
		singleton = new World(size, mode, 1.0);
		return singleton;
	}
	
	public static World newInstance(Dimension size, WorldMode mode, double speedFactor) {
		singleton = new World(size, mode, speedFactor);
		return singleton;
	}

	private World(int width, int height, WorldMode mode) {
		this(new Dimension(width, height), mode, 1.0);
	}
	
	private World(Dimension preferredSize, WorldMode mode, double speedFactor) {
		this.mode = mode;
		this.preferredSize = preferredSize;
		this.speedFactor = speedFactor;
		double width = preferredSize.getWidth();
		double height = preferredSize.getHeight();
		double offsetX = width / 2.0;
		double offsetY = height / 2.0;
		this.bounds = new Rectangle2D.Double(-offsetX, -offsetY, width, height);
		
		this.baseRenderTransform = AffineTransform.getTranslateInstance(offsetX, offsetY);
		baseRenderTransform.scale(1, -1);
		
		this.reverseTransform = AffineTransform.getScaleInstance(1, -1);
		reverseTransform.translate(-offsetX, -offsetY);
	}

	public void reverseTransform(Graphics2D g2) {
		g2.transform(reverseTransform);
	}
	
	public Rectangle2D getBounds() {
		return bounds;
	}

	@Override
	public Rectangle2D getBounds(Point2D position) {
		return bounds;
	}
	
	@Override
	public int getLayer() {
		return Renderable.WORLD_LAYER;
	}

	public boolean isOutOfBounds(Rectangle2D shape) {
		return !this.bounds.contains(shape);
	}

	public Dimension getPreferredSize() {
		return preferredSize;
	}

	/**
	 * Performs a few things:
	 * <ol>
	 * <li>adds and removes pending Objects. See {@link #add(Object)} and {@link #remove(Object)},</li>
	 * <li>applies (global) forces,</li>
	 * <li>collision-detection,</li>
	 * <li>calls {@link Updatable#update(long)} (on all Updatables),</li>
	 * <li>collision-detection,</li>
	 * </ol>
	 * @param millisElapsed The milli-seconds that have elapsed since the last update.
	 */
	public void update(long millisElapsed) {
		numFrames++;
		totalMillisElapsed += millisElapsed;
		millisElapsed = totalMillisElapsed / numFrames;
		removeObjects();
		addObjects();
		applyForces();
		applyPhysics();
		updateObjects((long) (millisElapsed * speedFactor));
		checkBounds();
	}

	private void applyForces() {
		for (Effect f : effects) {
			for (Mass a : affected) {
				Rectangle2D bounds = a.getBounds();
				if (f.isAffected(a, bounds)) {
					f.apply(a, bounds);
				}
			}
		}
	}

	private void applyPhysics() {
		List<Rectangle2D> orderedBounds = new ArrayList<Rectangle2D>(affected.size()); //only calculate bounds once
		List<Mass> copy = Collections.unmodifiableList(new ArrayList<Mass>(affected));
		for (int ii = 0; ii < affected.size(); ii++) {
			Mass a = affected.get(ii);
			Rectangle2D bounds = a.getBounds();
			orderedBounds.add(bounds);
		}
		for (Physics p : physics) {
			p.startNext(copy);
			for (int ii = 0; ii < affected.size(); ii++) {
				Mass a1 = affected.get(ii);
				Rectangle2D b1 = orderedBounds.get(ii);
				if (p.affects(a1, b1)) {
					for (int jj = ii + 1; jj < affected.size(); jj++) { //notice "ii+1"--we only do "apply" _once_ per pair
						Mass a2 = affected.get(jj);
						Rectangle2D b2 = orderedBounds.get(jj);
						if (p.affects(a2, b2)) {
							p.apply(a1, a2, b1, b2);
						}
					}
				}
			}
		}
	}

	private void updateObjects(long millisElapsed) {
		for (Updatable u : updatables) {
			u.update(millisElapsed);
		}
	}

	private void checkBounds() {
		for (Mass m : affected) {
			if (isOutOfBounds(m.getBounds())) {
				mode.handleOutOfBounds(m, this);
			}
		}
	}

	/**
	 * Draws all Renderables in their appropriate layer.
	 */
	public void render(Graphics2D g2) {
		AffineTransform previousTransform = g2.getTransform();
		for (Collection<Renderable> renderables : layers.values()) {
			for (Renderable r : renderables) {
				g2.transform(baseRenderTransform);
				r.render(g2);
				g2.setTransform(previousTransform);
			}
		}
	}
	
	/**
	 * The Object will be added just before the next update
	 * @param o -- Be it Force, Collidable, Renderable, or Updatable
	 */
	public void add(Object o) {
		objectsToAdd.add(o);	
	}

	/**
	 * The Object will be removed just before the next update
	 * @param o -- Be it Force, Collidable, Renderable, or Updatable
	 */
	public void remove(Object o) {
		objectsToRemove.add(o);
	}
	
	private void addRenderable(Renderable r) {
		int layerNumber = r.getLayer();
		Collection<Renderable> layer;
		if (layers.containsKey(layerNumber)) {
			layer = layers.get(layerNumber);
		} else {
			layer = new HashSet<Renderable>();
			layers.put(layerNumber, layer);
		}
		layer.add(r);
	}

	private void addRenderableCollection(RenderableCollection c) {
		for (Renderable r : c.getComponents()) {
			addRenderable(r);
		}
	}

	private void removeRenderable(Object o) {
		if (Renderable.class.isInstance(o)) {
			Renderable r = Renderable.class.cast(o);
			int layerNumber = r.getLayer();
			if (layers.containsKey(layerNumber)) {
				Collection<Renderable> layer = layers.get(layerNumber);
				layer.remove(r);
			}
		}
	}
	
	private void removeRenderableCollection(Object o) {
		if (RenderableCollection.class.isInstance(o)) {
			RenderableCollection c = (RenderableCollection) o;
			for (Renderable r : c.getComponents()) {
				removeRenderable(r);
			}
		}
	}

	private void addObjects() {
		for (Object o : objectsToAdd) {
			if (Updatable.class.isInstance(o)) {
				updatables.add((Updatable) o);
			}
			if (Mass.class.isInstance(o)) {
				affected.add((Mass) o);
			}
			if (Effect.class.isInstance(o)) {
				effects.add((Effect) o);
			}
			if (Physics.class.isInstance(o)) {
				physics.add((Physics) o);
			}
			if (RenderableCollection.class.isInstance(o)) {
				addRenderableCollection((RenderableCollection) o);
			} else if (Renderable.class.isInstance(o)) {
				addRenderable((Renderable) o);
			}
		}
		objectsToAdd.clear();
	}

	private void removeObjects() {
		for (Object o : objectsToRemove) {
			updatables.remove(o);
			affected.remove(o);
			effects.remove(o);
			physics.remove(o);
			removeRenderable(o);
			removeRenderableCollection(o);
		}
		objectsToRemove.clear();
	}

}