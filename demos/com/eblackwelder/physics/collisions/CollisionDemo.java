package com.eblackwelder.physics.collisions;

import java.awt.Color;

import javax.swing.JFrame;

import com.eblackwelder.math.Position;
import com.eblackwelder.math.Vector2D;
import com.eblackwelder.math.Velocity;
import com.eblackwelder.physics.AbstractDemo;
import com.eblackwelder.physics.PhysicsMain;
import com.eblackwelder.physics.updatables.ForceResetter;
import com.eblackwelder.world.World;
import com.eblackwelder.world.driver.WorldDriverBase;
import com.eblackwelder.world.ui.UIWorldDriver;

public class CollisionDemo extends AbstractDemo {

	@Override
	public WorldDriverBase createDriver(JFrame parent) {
		UIWorldDriver driver = new UIWorldDriver(getTitle());
		driver.setRendererForType(new CollidableRenderer(), CollidableObject.collisionType);
		
		CollidableObject ball1 = new CollidableObject(10, 1, Color.BLUE, 20);
		CollidableObject ball2 = new CollidableObject(10, 1, Color.BLUE, 20);
		ball1.setPosition(new Position(0, 0));
		ball2.setPosition(new Position(widthOption.getValue(), heightOption.getValue()));
		Vector2D v = ball1.getPosition().minus(ball2.getPosition());
		double speed = v.getMagnitude() / 3;
		ball1.setVelocity(new Velocity(ball2.getPosition(), speed));
		ball2.setVelocity(new Velocity(v, speed));
		
		World world = driver.getWorld();
		world.addObject(new ForceResetter());
		world.addObject(ball1);
		world.addObject(ball2);
		return driver;
	}

	@Override
	public String getTitle() {
		return "Collisions";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
			"Trying out a better collision detection algo.",	
		};
	}

	public static void main(String[] args) {
		PhysicsMain.runDemo(CollisionDemo.class.getName());
	}
}
