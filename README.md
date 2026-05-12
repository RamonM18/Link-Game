Zelda-Style Game
A top-down adventure game inspired by The Legend of Zelda, built three times in different languages as a learning exercise to understand how the same game logic translates across languages and environments.
Versions
Java
The original version. Uses MVC (Model-View-Controller) architecture to separate game logic, rendering, and input handling. No external libraries required.
To run:
javac *.java
java Main
JavaScript (HTML5 Canvas)
A browser-based port of the Java version. Runs directly in the browser — no install needed. Uses an HTML5 canvas and a drag-and-drop JSON map loader to place sprites.
To run: Open index.html in a browser.
Python (Pygame)
The most feature-complete version, built last. Adds Cuccos (chickens) that bounce around the map and attack Link if provoked too many times — a reference to the classic Zelda mechanic.
To run:
pip install pygame
python main.py
Gameplay

Move Link with the arrow keys
Throw a boomerang with the spacebar
Open treasure chests to collect rupees
Hit Cuccos with the boomerang too many times and they'll swarm you
Press E to enter edit mode and place trees, chests, and Cuccos on the map
Press S to save the map, L to load it, C to clear it
