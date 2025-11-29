🌲 Forest Escape – Java Swing Game

Forest Escape is a 2D survival and puzzle game built using Java Swing, where the player explores a forest, collects mushrooms, avoids wolves, and uses power-ups to survive.
The game features 10 handcrafted levels, fog-of-war visibility, enemy AI, SQLite-backed highscores, and a complete GUI with menus, dialogs, and keyboard controls.

This project demonstrates strong skills in Java OOP, Swing UI, file parsing, game state design, animation timers, and automated testing.

🎮 Gameplay Overview

Move using W A S D

Collect all mushrooms to clear a level

Avoid wolves (they move and change direction like basic AI)

Fog reduces visibility radius

Use power-ups:

SPEED – move twice as fast as wolves

INVISIBILITY – wolves cannot detect you

LIFE – get an extra life

Respawn at the campfire

Game automatically saves your score into SQLite

🧩 Features
✔ Core Mechanics

Player movement & collision

Wolf movement with direction + fallback behavior

Fog-of-war visibility

Power-up system with timed expiration

Multi-wolf support

Campfire respawn

Win/lose screen + auto-restart

✔ File-based Level System

10 levels in /levels directory

Loaded from .txt grid (20×20)

Each tile represented by a single character

Easy to extend & modify

✔ Persistent Highscores (SQLite)

Automatically created DB: forest_escape.db

Saves:

Player name

Level name

Mushrooms collected

Time

Date

✔ Swing GUI

Level selector

Start button

Menubar: New Game, Highscores, How to Play, About

Exit confirmation

Dynamic window title containing:

Lives

Mushrooms

Power-ups

Time

✔ Testing (JUnit 4)

Test suite covers:

Level loader

Movement logic

Obstacle detection

Wolf behavior

Database operations

🧱 Project Structure
forest-escape/
│
├── src/forestgame/
│     ├── MainFrame.java
│     ├── GamePanel.java
│     ├── GameMap.java
│     ├── GameState.java
│     ├── Player.java
│     ├── Wolf.java
│     ├── Direction.java
│     ├── TileType.java
│     ├── LevelLoader.java
│     ├── LoadedLevel.java
│     ├── HighscoreDatabase.java
│     ├── HighscoreEntry.java
│     │
│     └── (any helper classes)
│
├── levels/
│     ├── level1.txt
│     ├── level2.txt
│     ├── ...
│     └── level10.txt
│
├── test/
│     └── ForestGameTest.java
│
├── forest_escape.db
└── README.md

🗺️ Level File Format

Each level is a 20×20 grid stored as plain text.

Char	Meaning
P	Player start
W	Wolf
C	Campfire (respawn point)
M	Mushroom
T / #	Tree (block)
R	Rock
B	Bush
S	Speed power-up
I	Invisibility power-up
L	Extra life
.	Ground

Example:

####################
#C....M.....S......#
#....R....B........#
#..................#
#.........M........#
#..................#
#..R...............#
#..................#
#.........W........#
#..................#
#..................#
#..................#
#..................#
#..................#
#..................#
#..................#
#..................#
#P.................#
#..................#
####################

📐 UML Diagram

(Place your image or PlantUML file here)

Example placeholder:

/uml/forest_escape_uml.png

▶️ How to Run
Using NetBeans, IntelliJ, or Eclipse

Import project as a Java Application Project

Add libraries:

sqlite-jdbc.jar

junit-4.13.2.jar

hamcrest-core-1.3.jar

Ensure /levels folder is in the project root

Run:
MainFrame.java

Command Line
javac -cp ".;sqlite-jdbc.jar" forestgame/*.java
java -cp ".;sqlite-jdbc.jar" forestgame.MainFrame

👤 Author

Toghrul Hasanli
Java Developer / Student Project
📧 toghrulhasanli2@gmail.com