# Dungeons
A tiny MMORPG that supports up to 9 players who can fight with minions and each other,equip weapons, learn spells, collect treasures and trade with each other.
## Building the client and server
Execute `mvn clean install` in the root directory of the project 
## Running the server
`mvn exec:java -pl DungeonsServer`
## Running a client
`mvn exec:java -pl DungeonsClient`

## How to play
You can move around the map with the W,A,S,D keys or with the arrows.\
The main keybindings are the following:
* **F** to fight another actor at your position (player/minion)
* **T** to pick up a treasure at your position
* **1-9** to use items from the inventory
* **SHIFT+1-9** to throw an item from the inventory
* **F1-F9** to trade an item from the inventory to another player (both players must be at the same position)
