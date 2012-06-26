java-physics-demos
==================
A cross-platform 2D physics engine and physics simulations. Built with WorldEngine.

https://github.com/eib/java-physics-demos
https://github.com/eib/java-world-engine


Instructions
============
How to run these demos:
* Download an "Executable JAR": https://github.com/eib/java-physics-demos/downloads
* Make sure Java 1.6 (or greater) is installed on your machine. (Type "java -version" into command prompt/terminal/...)
* Windows and OS X users can double-click on the Executable JAR file to run the suite of demos.
   Otherwise, it can be run via command-line:

        java -jar PhysicsDemo-vX.YY.jar


Physics Engine
==============
The physics engine has built-in support for a number of real-world forces.
It can also support any number of user-definable forces.

Global forces:
* Gravity
* Gas/liquid drag (resistance to movement)
* Electric field (on charged particles)

Pair-wise forces:
* Collisions (with varying elasticity)
* Coulomb's law (interactions between charged particles)
* Buoyancy (when objects are overlapping a liquid)


Demos
=====
All simulations include configuration panels to change the properties of the system.

Version 0.04
------------
Built-in simulations:
* Pendulum
* Solar system
* Coulomb's Law
* Charge particles in an electric field

Version 0.02
------------
These simulations were built on a custom graphics/world engine and may not
have been ported to WorldEngine, yet. (They are still available on the downloads page.)
* Gravity
* Collision detection
* Buoyancy
* A sandbox to mix collisions, velocity, mass, and/or gravity.
* Piston: a "piston" interacting with tiny particles (a "gas") in an enclosed container.


License
=======
MIT license: http://eib.mit-license.org/