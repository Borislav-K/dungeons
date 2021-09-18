# Dungeons
A tiny MMORPG/MOBA that supports up to 9 players who can fight with minions and each other,equip weapons, learn spells, collect treasures and trade with each other.

## Prerequisites
* Java (15 or later version)
* Maven

## Building the client and server
* Start a terminal session in the root directory of the project
* Execute `mvn clean install`
## Running the server
* Within a terminal session in the root directory of the project, execute `mvn exec:java -pl DungeonsServer`
## Running a client
* Within a terminal session in the root directory of the project, execute `mvn exec:java -pl DungeonsClient`

## How to play
You can move around the map with the W,A,S,D keys or with the arrows.\
The main keybindings are the following:
* **F** to fight another actor at your position (player/minion)
* **T** to pick up a treasure at your position
* **1-9** to use items from the inventory
* **SHIFT+1-9** to throw an item from the inventory
* **F1-F9** to trade an item from the inventory to another player (both players must be at the same position)

* Players are labeled with the numbers 1-9 on the map. Minions' level varies from level 1 to 5. Making unsuccessful attempts until learning how a level X minion works is part of the design :)
