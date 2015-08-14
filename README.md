# AnimalEssentials
All the essentials for your animals.

----

SOFTWARE USED UNDER THE [MIT-LICENSE](https://github.com/JustRamon/AnimalEssentials/blob/master/MIT-LICENSE.md):
- ["fanciful" by mkremins](https://github.com/mkremins/fanciful)

----

### To-Do

1. Define amount of kills (eg. /ae kill 6)
2. Add WG support to prevent people from spawning animals in areas that are not their's.
3. Animal Protect
4. Money System
5. /ae clone (Clones the right-clicked animal)
6. /aetp x y z (Teleport command used to have find teleport permissions)

### Done
1. /ae \<home|sethome|listhomes|delhome|edithome\> ([AnimalTeleport](https://github.com/JustRamon/AnimalTeleport) functionality with new look and commands.)
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
17. Ability to click the coordinates in chat, after issuing /ae find <name>, to teleport to the animal.
18. Update Checker

-----

### Bugs
1. /ae help | /ae | /ae \<anything\> <--- Faulty output if issued through console
2. Multiple /ae commands can be issued at the same time
3. Stacktrace when clicking outside of inventory
4. When doing particle commands, the particles will go to the animal's location on execution. If the animal moves during the animation, the particles will not follow the animal.
5. /ae tp does not work correctly with multiple players.
6. Fix cancelling of all tasks of the plugin (aka only cancel the task of the current command)

### Fixed
1. A player is a null mob instead of a player.
2. Space names cannot be found or named

---