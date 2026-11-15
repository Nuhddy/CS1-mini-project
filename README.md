# CS1-mini-project

## Program description

The following is a java CLI game where a player has to clean up an oil spill at sea.

The game is played on a grid. The player controls a VESSEL(V) with starting position (4,0) that cleans all the adjacent cells from OIL(O). The player can also place BARRIER(B) which stop the oil from drifting with the current.

The current direction changes with a 25% probability it turns left, 25% it turns right and a 50% probability it keeps the direction.

The game ends when there is no more OIL(O) at sea. At the end of the game, the OIL(O) cells cleaned by the player is displayed as a score.

## Program structure

The program contains the following files:

- Cell.java
    - Contains code for the Cell object that the playing board is comprised of. Each cell can have any Status from the following: CLEAN, OIL, SHORE, BARRIER, VESSEL, ROCK, OILEDSHORE.
- Grid.java 
    - Contains code for the grid object, which contains a 2D array of cells as well as methods to manipulate the individual cells the array is comprised of. Additionally, it contains methods to generate the playingoard.
- Game.java
    - Contains code for the game object, which contains the game logic and graphics.
- OilSpillResponse.java
    - Main file from which the game plays from.


## Tutorial

To download and play the game, head to releases and download the OILSPILLRESPONSE.ZIP file. Extract it using your preferred compression utility.

Navigate to the folder with the extracted games code and run the following:

```java OilSpillResponse.java```



