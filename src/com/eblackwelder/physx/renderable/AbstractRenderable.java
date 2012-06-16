package com.eblackwelder.physx.renderable;

public abstract class AbstractRenderable implements Renderable {

	protected final int layer;

	public AbstractRenderable() {
		this(Renderable.DEFAULT_LAYER);
	}
	public AbstractRenderable(int layerNumber) {
		this.layer = layerNumber;
	}

	@Override
	public int getLayer() {
		return layer;
	}

}