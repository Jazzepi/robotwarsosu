package game;

public class GameWorld {

	private class Coordinate
	{
		private int x,y;

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

		Coordinate(int x,int y)
		{
			this.x = x;
			this.y = y;
		}

		int getX()
		{
			return x;
		}

		int getY()
		{
			return y;
		}

		void setX(int x)
		{
			this.x = x;
		}

		void setY(int y)
		{
			this.y = y;
		}
	}


	private int numberOfPlayers, xSize, ySize;
	private final int DEFAULTBOARDSIZEX = 8;
	private final int DEFAULTBOARDSIZEY = 8;
	private final int DEFAULTMAXTURNCOUNT = 50;
	private int turnCounter, maxTurnCount;
	private Coordinate[] playerPositions = new Coordinate[4];
	private Robot[] playerRobots = new Robot[4];
	private DecisionEngine[] playerScripts = new DecisionEngine[4];

	GameWorld(int boardSizeX, int boardSizeY, int maxTurnCount)
	{
		xSize = boardSizeX;
		ySize = boardSizeY;
		this.maxTurnCount = maxTurnCount;
	}

	GameWorld()
	{
		xSize = DEFAULTBOARDSIZEX;
		ySize = DEFAULTBOARDSIZEY;
		maxTurnCount = DEFAULTMAXTURNCOUNT;
	}

	void addRobot(Robot robot, DecisionEngine script)
	{
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
