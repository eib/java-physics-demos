/**
 * 
 */
package com.eblackwelder.graphx.renderable;

import java.util.Collection;

/**
 * @author Ethan
 *
 */
public interface RenderableCollection extends Renderable {

	public Collection<Renderable> getComponents();
}
