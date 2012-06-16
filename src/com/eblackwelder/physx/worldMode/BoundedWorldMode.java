/**
 * 
 */
package com.eblackwelder.physx.worldMode;

import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import com.eblackwelder.physx.World;
import com.eblackwelder.physx.object.Collidable;
import com.eblackwelder.physx.object.Mass;

/**
 * @author Ethan
 *
 */
public class BoundedWorldMode implements WorldMode {

	public static enum WALL {
		NORTH(1),
		SOUTH(1 << 1),
		EAST(1 << 2),
		WEST(1 << 3);
		
		private WALL(int modee) {
			this.mode = modee;
		}

		final int mode;
		
		public boolean matches(int mode) {
			return (mode & this.mode) != 0;
		}
		public static int allWalls() {
			return getMode(WALL.values());
		}
		public static int getMode(WALL... walls) {
			int mode = 0;
			for (WALL wall : walls) {
				mode |= wall.mode;
			}
			return mode;
		}
	}
	
	private final double efficiency; //loss of energy due to friction, etc (0.9 is a fair example)

	private final int wallMode;
	
	/**
	 * @param efficiency Percent collision "efficiency" (should be between 0 and 1).
	 * <code>(1 - efficiency)</code> is the velocity loss due to friction when colliding with the world's "walls".
	 */
	public BoundedWorldMode(int wallMode, double efficiency) {
		this.efficiency = efficiency;
		this.wallMode = wallMode;
	}

	public BoundedWorldMode(WALL... walls) {
		this(WALL.getMode(walls), 1.0);
	}
	
	public BoundedWorldMode(double efficiency) {
		this(WALL.allWalls(), efficiency);
	}
	
	public BoundedWorldMode() {
		this(WALL.allWalls(), 1.0);
	}
	
	protected boolean bounds(WALL wall) {
		return wall.matches(wallMode);
	}
	
	@Override
	public void handleOutOfBounds(Mass m, World w) {
		Rectangle2D bounds = m.getBounds();
		double halfWidth = bounds.getWidth() / 2.0;
		double halfHeight = bounds.getHeight() / 2.0;
		
		Dimension worldSize = w.getPreferredSize();
		double worldWidth = worldSize.getWidth() / 2.0;
		double worldHeight = worldSize.getHeight() / 2.0;

		Point2D position = m.getPosition();
		double x = position.getX();
		double y = position.getY();
		double left   = x - halfWidth;
		double right  = x + halfWidth;
		double top    = y + halfHeight;
		double bottom = y - halfHeight;

		Point2D velocity = m.getVelocity();
		double vx = velocity.getX();
		double vy = velocity.getY();
		
		boolean reflectX = false;
		boolean reflectY = false;
		double deltaX = 0;
		double deltaY = 0;
		
		double elasticity = (m instanceof Collidable) ? Collidable.class.cast(m).getElasticity() : 1.0;
		
		//specific wall collisions
		if (left < -worldWidth && bounds(WALL.WEST)) { //left
			reflectX = vx < 0;
		} else if (worldWidth < right && bounds(WALL.EAST)) { //right
			reflectX = vx > 0;
		}
		
		if (bottom < -worldHeight && bounds(WALL.SOUTH)) { //bottom
			reflectY =  vy < 0;
		} else if (top > worldHeight && bounds(WALL.NORTH)) { //top
			reflectY =  vy > 0;
		}
		
		//only collide once (won't get trapped half-way through the wall)! :)
		if (reflectX) {
			vx *= -elasticity * efficiency;
			vy *= efficiency;
		}
		if (reflectY) {
			vx *= efficiency;
			vy *= -elasticity * efficiency;
		}
		
		m.getPosition().add(new Point2D.Double(deltaX, deltaY));
		m.setVelocity(new Point2D.Double(vx, vy));	
		
		//TODO : add Normal forces
	}
}
