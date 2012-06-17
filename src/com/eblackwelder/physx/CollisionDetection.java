/**
 * 
 */
package com.eblackwelder.physx;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import com.eblackwelder.math.Vector2D;
import com.eblackwelder.world.model.Collidable;
import com.eblackwelder.world.model.Mass;

/**
 * @author Ethan
 * Something of an elegant hack. :)
 */
public class CollisionDetection implements Physics {
	
	/**
	 * All Affected objects from the previous round.
	 */
	private List<Mass> lastAffected = new ArrayList<Mass>(); //initialize to prevent NPE on first round
	/**
	 * All Affected objects from the current round.
	 * The order of Objects is the same as <code>currentCollisions</code>.
	 */
	private List<Mass> currentAffected = new ArrayList<Mass>();
	/**
	 * A 2-D array, storing pair-wise collisions from the previous round.
	 * Indices are the same as those in <code>lastAffected</code>.
	 * If two objects with indices i and j collided last round, then both <code>lastCollisions[i][j]</code>
	 * and <code>lastCollisions[j][i]</code> will be true (for simplicity's sake). False otherwise.
	 */
	private boolean[][] lastCollisions;
	/**
	 * A 2-D array, storing pair-wise collisions from the current round, as detected.
	 * Indices are the same as those in <code>currentAffected</code> (indices may change between rounds).
	 */
	private boolean[][] currentCollisions;

	private double elasticity;
	
	public CollisionDetection() {
		this(1.0); //"perfect" collisions
	}
	
	public CollisionDetection(double elasticity) {
		this.elasticity = elasticity;
	}

	@Override
	public void startNext(List<Mass> affected) {
		lastAffected = currentAffected;
		lastCollisions = currentCollisions;
		currentAffected = affected;
		currentCollisions = new boolean[affected.size()][affected.size()];
	}

	@Override
	public void apply(Mass a1, Mass a2, Rectangle2D b1, Rectangle2D b2) {
		boolean collision = isCollision(a1, a2, b1, b2);
		if (collision) {
			boolean wasCollision = false;
			int i1 = lastAffected.indexOf(a1);
			int i2 = lastAffected.indexOf(a2);
			wasCollision = i1 != -1 && i2 != -1 && lastCollisions[i1][i2];
			if (!wasCollision) { //a "new" collision
				collide(a1, a2);
			}
			int j1 = currentAffected.indexOf(a1);
			int j2 = currentAffected.indexOf(a2);
			currentCollisions[j1][j2] = collision;
			currentCollisions[j2][j1] = collision;
		}
	}

	protected boolean isCollision(Mass a1, Mass a2, Rectangle2D b1, Rectangle2D b2) {
		boolean collision = b1.intersects(b2);
		return collision;
	}

	protected void collide(Mass a1, Mass a2) {
		Vector2D v1 = new Vector2D(a1.getVelocity());
		Vector2D v2 = new Vector2D(a2.getVelocity());
		
		//scaling by the ratio of the Object masses and by the corresponding elasticity
		double m1 = a1.getMass();
		double m2 = a2.getMass();
		
		double scale2 = m2 / m1 * elasticity;
		double scale1 = m1 / m2 * elasticity;
		if (Collidable.class.isInstance(m1)) {
			scale2 *= Collidable.class.cast(a1).getElasticity();
		}
		if (Collidable.class.isInstance(m2)) {
			scale1 *= Collidable.class.cast(a2).getElasticity();
		}
		v2.scale(scale2);
		v1.scale(scale1);
		
		//simply swapping the directions is "close" enough ATM. //TODO : Newtonian collisions
		a1.setVelocity(v2);
		a2.setVelocity(v1);
	}

	@Override
	public boolean affects(Mass a, Rectangle2D bounds) {
		return true;
	}

}
