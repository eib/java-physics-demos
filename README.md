java-physics-demos
==================
A re-usable 2D physics and rendering engine.

Also contains a number of built-in physics "effects" and several demos.


Physics Engine
==============
The physics engine has support for "global" and "interaction" effects.
It updates in time with the rendering engine. 

Global effects:
* Gravity
* Gas/liquid drag (resistance to movement)
* Electric field (on charged particles)

Interaction effects:
* Collisions (with varying elasticity)
* Coulomb's law (interactions between charged particles)
* Buoyancy (when objects are inside another shape)


Rendering Engine
===============
The rendering engine has support for displaying objects
with one or more of the following properties:
* Position (may still be subject to velocity/acceleration)
* Shape
* Color
* Text
* Layer (for shadows or overlays)


Demos
=====
Built-in simulations:
* Gravity
* Collision detection
* Buoyancy
* Coulomb's law
* Charged particles in an electric field
* A sandbox to mix collisions, velocity, mass, and/or gravity.
* Piston: a "piston" interacting with tiny particles (a "gas") in an enclosed container.
* Pendulum: still a work-in-progress.

All simulations include configuration panels to change the properties of the system.


License
=======
MIT license: http://eib.mit-license.org/