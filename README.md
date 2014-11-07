## Simulation tool

The files in the folders are part of my Honours project during my final year at the University of Edinburgh.  My project involved both programming and analysis, however I have only included the code for the tool I built, and thus the results for my analysis are not included.
My project concerned the well-known problem in decision/game theory of the Prisoner's Dilemma, and analysing this in a more spatial context than as a 1-1 symmetric game.  Part of the project was to build a tool, use it to generate these spatial games and analyse the results of simulations.  This tool heavily uses the Swing library for Java, as it provides a user interface for the user to submit and interact with the game they create.  It also uses an open source library called Jung which visualises the graph.

### Usage

In order to use this tool, you will have to compile it using the Java compiler, as all parts of the code is written in Java.  You will also need the open source library called JUNG, and if you are compiling the files using command line, you will have to set up the classpath for the library.  You will find JUNG at jung.sourceforge.net.
After successful compilation, in the tool folder use the command line:

* java MainMenu

which will bring up the main menu interface.  Instructions on how to proceed to use this tool is provided in the instruction.pdf file which is included.