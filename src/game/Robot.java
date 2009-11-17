package game;

/**
 * Robot designed by a player. Unfinished, but has slots for different styles of items, a name, the player's number in the game.
 * @author Michael Pinnegar
 *
 */
public class Robot {
	private Item leftHandItem, rightHandItem, backPackItem, armorItem, helmetItem, miscItem, miscItem2;
	private String name;
	private int playerDesignation;
	
	Robot()
	{

	}

	public String getName() {
		return name;
	}
}
