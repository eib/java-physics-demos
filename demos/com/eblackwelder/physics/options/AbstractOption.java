package com.eblackwelder.physics.options;

public abstract class AbstractOption<E> implements Option<E> {

	private final String title;

	public AbstractOption(String title) {
		this.title = title;
	}

	@Override
	public String getText() {
		return title;
	}

}