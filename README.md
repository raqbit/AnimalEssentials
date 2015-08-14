# AnimalEssentials
All the essentials for your animals.

----

### To-Do

1. Ability to click the entity name in chat, after issuing /ae find <name>, to teleport to it.
2. (XP-System)
3. Animal Protect
4. Fix cancelling of all tasks of the plugin (aka only cancel the task of the current command)
5. Define amount of kills (eg. /ae kill 6)
6. Add WG support to prevent people from spawning animals in areas that are not their's.

### Done
1. /ae \<home|sethome|listhomes|delhome|edithome\> ([AnimalTeleport] (https://github.com/JustRamon/AnimalTeleport) functionality with new look and commands.)
2. /ae reload (Reload the configuration file.)
3. /ae help (Show the help menu.)
4. /ae name \<name\> (Name the right-clicked animal.)
5. /ae tp \<home|player\> (Teleport the right-clicked animal to a home or player.)
6. /ae find \<name\> (Find all animals by name.)
7. /ae kill (Kills the right-clicked animal.)
8. Config option to only show own animals with /ae find
9. /ae heal
10. /ae owner (Shows the owner of the animal.)
11. Invincibility when teleporting to player
12. /ae tame (Tames the the right-clicked animal.)
13. Replace "a(n)" with the correct thing
14. /ae spawn (With GUI.)
15. Make multiple players able to issue the same command at a time (List!)
16. Particles for /ae kill | /ae spawn | /ae name

-----

### Bugs
1. /ae help | /ae | /ae \<anything\> <--- Faulty output if issued through console
2. Multiple /ae commands can be issues at the same time
3. Stacktrace when clicking outside of inventory
4. When doing particle commands, the particles will go to the animal's location on execution. If the animal moves during the animation, the particles will not follow the animal.
5. /ae tp does not work correctly with multiple players.
6. A player is a null mob instead of a player.