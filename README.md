# Horse Race Simulator

This project simulates horse races. It has a graphical user interface (GUI) using Swing, featuring customisable races and horses, live statistics updates, and virtual betting capabilities.

## Installation

First, clone the repository to your local machine:

```
git clone https://github.com/BKuliesis/HorseRaceSimulator.git
cd HorseRaceSimulator
```

## Dependencies

This project requires Java JDK 11 or higher. Ensure that your Java JDK installation is properly configured on your PATH to use the `javac` and `java` commands from any terminal.

## Running the Application

1. Open your terminal in the Horse-Race-Simulator directory.
2. Compile the Java files:

     ```
     javac Main.java
     ```

3. Execute the compiled class:

     ```
     java Main
     ```

This will compile the `Main.java` file and execute the compiled class.

## Usage

The Horse Race Simulator utilises a single-window graphical user interface to manage and observe horse races:

1. **Track and Race Customisation**: The user starts by customising the number of tracks and the length of each track for the races.

2. **Horses Customisation**: Before the races begin, the user can customise their horses by selecting names, colours, and saddles.

3. **Racing and Betting Interface**: On the race screen, users may choose to place bets on whether a horse will finish the race, with odds provided for each horse. All bets must be placed before the race starts.

4. **Race Animation**: When the race starts, an animation displays the horses moving along the track.

5. **Post-Race**: After each race finished, the statistics of each horse are immediately updated to reflect the outcomes and the user is informed on the results of any bets which have been placed. Users can then click a button to move on to the next race if there are more races.

6. **Results**: Once all races are complete, a results button allows the user to view the final standings and outcomes based on a points system. The user has the option to repeat the races with the same settings or return to the customisation stage to make adjustments to the tracks and horses.

## Contributing

If you wish to contribute to this project, please fork the repository and submit a pull request.

## Contact

For questions or support, please contact Benas Kuliesis at bkuliesis@gmail.com.