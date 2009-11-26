package game;

import java.util.ArrayList;

/**
 * GameWorld contains all the elements necessary to run a single game of Robot Wars. The game world tracks the position of the robots on the game grid,
 * verifies the validity of actions issued (two robots can not occupy the same spot), tracks points per player, keeps track of the current turn, and otherwise maintains
 * all the states necessary for a robust gaming environment.  
 * @author Michael Pinnegar
 *
 */
public class GameWorld {

	/**
	 * Inner class used to store x,y coordinates as a pair
	 * @author Michael Pinnegar
	 *
	 */
	private class Coordinate
	{
		/**
		 * Coordinates x and y
		 */
		private int x,y;

		/**
		 * Comparison equals comparison for two coordinates. If they're both the same, returns true
		 * @param aThat Second coordinate pair to compare to the original
		 * @return If they're both the same, returns true
		 */
		public boolean equals(Coordinate aThat) {
			if(this.getX() == aThat.getX() && this.getX() == aThat.getX())
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		/**
		 * Constructor with known x, y to store in the pair
		 * @param x x to be stored
		 * @param y y to be stored
		 */
		Coordinate(int x,int y)
		{
			this.x = x;
			this.y = y;
		}

		/**
		 * 
		 * @return x value of xy pair
		 */
		int getX()
		{
			return x;
		}

		/**
		 * 
		 * @return y value of xy pair
		 */
		int getY()
		{
			return y;
		}

		/**
		 * Sets the x value of the xy pair to x
		 * @param x
		 */
		void setX(int x)
		{
			this.x = x;
		}
		
		/**
		 * Sets the y value of the xy pair to y
		 * @param y
		 */
		void setY(int y)
		{
			this.y = y;
		}
	}

	/**
	 * Number of players in the game
	 */
	private int numberOfPlayers;
	/**
	 * size of the x dimension of the gaming board
	 */
	private int xSize;
	/**
	 * size of the y dimension of the gaming board
	 */
	private int ySize;
	/**
	 * Default board x size constant
	 */
	private final int DEFAULTBOARDSIZEX = 8;
	/**
	 * Default board y size constant
	 */
	private final int DEFAULTBOARDSIZEY = 8;
	/**
	 * Default turn count.
	 */
	private final int DEFAULTMAXTURNCOUNT = 50;
	private int turnCounter, maxTurnCount;
	/**
	 * Position of each player, up to 4, on the board
	 */
	private Coordinate[] playerPositions = new Coordinate[4];
	/**
	 * Robot for each player
	 */
	private Robot[] playerRobots = new Robot[4];
	/**
	 * Decision engine for each player
	 */
	private DecisionEngine[] playerScripts = new DecisionEngine[4];

	/**
	 * Creates a game world with a defined size and a number of turns till the game ends
	 * @param boardSizeX X size
	 * @param boardSizeY Y size
	 * @param maxTurnCount number of turns till the game ends
	 */
	GameWorld(int boardSizeX, int boardSizeY, int maxTurnCount)
	{
		xSize = boardSizeX;
		ySize = boardSizeY;
		this.maxTurnCount = maxTurnCount;
	}

	/**
	 * Creates a default game world
	 */
	public GameWorld()
	{
		xSize = DEFAULTBOARDSIZEX;
		ySize = DEFAULTBOARDSIZEY;
		maxTurnCount = DEFAULTMAXTURNCOUNT;
	}
	
	/**
	 * Adds a robot to the game world in one of the four corners, and initializes the decision engine inside of the game world
	 * @param robot Robot to be added
	 * @param script VMC compiled script to run the robot
	 */
	void addRobot(Robot robot, DecisionEngine script)
	{
		script.getReferenceToGameworld(this);
		
		if(numberOfPlayers == 0)
		{
			playerPositions[numberOfPlayers] = new Coordinate(0,0);
			playerRobots[numberOfPlayers] = robot;
			playerScripts[numberOfPlayers] = script;
			numberOfPlayers++;
		}
		else if(numberOfPlayers == 1)
		{
			playerPositions[numberOfPlayers] = new Coordinate(xSize - 1,ySize - 1);
			playerRobots[numberOfPlayers] = robot;
			playerScripts[numberOfPlayers] = script;
			numberOfPlayers++;
		}
		else if(numberOfPlayers == 2)
		{
			playerPositions[numberOfPlayers] = new Coordinate(0,ySize - 1);
			playerRobots[numberOfPlayers] = robot;
			playerScripts[numberOfPlayers] = script;
			numberOfPlayers++;
		}
		else if(numberOfPlayers == 3)//Number of players == 4
		{
			playerPositions[numberOfPlayers] = new Coordinate(xSize - 1,0);
			playerRobots[numberOfPlayers] = robot;
			playerScripts[numberOfPlayers] = script;
			numberOfPlayers++;
		}
		else
		{
			System.out.println("ERROR: Already have four players. Robot " + robot.getName() + " was not added.");
		}
	}

	/**
	 * Moves the given player's robot one spot in the given direction. Returns true if the robot was moved, false if the robot was not moved. Will not move the robot if it is trying to move into another robot or a wall.
	 * @param playerNumber Player who's robot is to be moved
	 * @param moveTo Direction robot is to be moved
	 * @return Whether or not the robot was successfully moved
	 */
	boolean moveRobot(int playerNumber, CardinalDirections moveTo)
	{
		int i = 0;

		Coordinate spotToMoveTo = calcNewCoordinatesOneSpotAway(playerPositions[playerNumber], moveTo);

		if(spotToMoveTo == null)
		{
			return false;
		}

		while(i < 4 && playerPositions[i] != null)
		{
			if(playerPositions[i].equals(spotToMoveTo))
			{
				return false;
			}
			i++;
		}

		playerPositions[playerNumber] = spotToMoveTo;
		
		return true;
	}

	/**
	 * Takes in a game function, it's parameters, and returns an integer representing the state of the game world.
	 * 
	 * @param parameters Parameter list for the game world 
	 * @param gameFunctionCallID Name of the game function to be called
	 * @return Value representing the current 
	 */
	int processGameFunction(ArrayList<String> parameters, String gameFunctionCallID)
	{
		return 1;
	}
	
	/**
	 * Tells which player is at a given location. Returns -1 if there is no player there.
	 * @param location Location to be checked for a player
	 * @return 0-3 if there is a player, -1 if there is not
	 */
	int whatPlayerIsIn(Coordinate location)
	{
		for(int i = 0;i < 4; i++)
		{
			if(playerPositions[i].equals(location))
			{
				return i;
			}
		}
		
		return -1;
	}

	/**
	 * Helper function that calculates the x-y position of a spot in a given direction
	 * @param currentPos Base position that the new one will be calculated from
	 * @param moveTo Direction that the new position will be in relative to the base position
	 * @return Coordinates with XY representing the new spot. Null if that spot is illegal.
	 */
	private Coordinate calcNewCoordinatesOneSpotAway(Coordinate currentPos, CardinalDirections moveTo)
	{
		Coordinate flag = new Coordinate(currentPos.getX(), currentPos.getY());

		switch(moveTo)
		{
		case East:
			if(flag.getX() + 1 >= xSize) //If it would put you out of bounds, return a null to indicate that you can't go that way
			{
				flag = null;
			}
			else //Else just increment x by 1
			{
				flag.setX(flag.getX()+1);
			}
			break;
		case West:
			if(flag.getX() - 1 < 0) //If it would put you out of bounds, return a null to indicate that you can't go that way
			{
				flag = null;
			}
			else //Else just decrement x by 1
			{
				flag.setX(flag.getX()-1);
			}
			break;
		case North:
			if(flag.getY() + 1 >= ySize) //If it would put you out of bounds, return a null to indicate that you can't go that way
			{
				flag = null;
			}
			else //Else just increment y by 1
			{
				flag.setY(flag.getY()+1);
			}
			break;
		case South:
			if(flag.getY() - 1 < 0) //If it would put you out of bounds, return a null to indicate that you can't go that way
			{
				flag = null;
			}
			else //Else just decrement y by 1
			{
				flag.setY(flag.getY()-1);
			}
			break;
		}

		return flag;
	}
}
