/**
 * 
 */
package com.eblackwelder.physx.renderable;

import java.util.Collection;

/**
 * @author Ethan
 *
 */
public interface RenderableCollection extends Renderable {

	public Collection<Renderable> getComponents();
}
