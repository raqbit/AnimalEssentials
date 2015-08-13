# AnimalEssentials
All the essentials for your animals.

----

### To-Do

1. Ability to click the entity name in chat, after issuing /ae find <name>, to teleport to it.
2. Particles for /ae name | /ae kill
3. (XP-System)
4. Animal Protect
5. Fix cancelling of all tasks of the plugin (aka only cancel the task of the current command)
6. Define amount of kills (eg. /ae kill 6)

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

-----

### Bugs
1. /ae help | /ae | /ae <anything> <--- Faulty output if issued through console
2. Multiple /ae commands can be issues at the same time
3. Stacktrace when clicking outside of inventory
4. When doing /ae tp [home], the particles will go to the animal's location on execution. If the animal moves during the animation, the particles will not follow the animal.