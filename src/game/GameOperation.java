package game;

import java.util.ArrayList;

/**
 * Class that defines an in game command. Conatins its parameters and it's call ID.
 * @author Michael Pinnegar
 *
 */
public class GameOperation {
	
	/**
	 * Call ID
	 */
	private String commandName;
	/**
	 * Parameters
	 */
	private ArrayList<String> parameters = new ArrayList<String>();
	
	/**
	 * Creates a game operation object with a deep copy of the commandName and parameters
	 * @param commandName
	 * @param parameters
	 */
	public GameOperation(String commandName, ArrayList<String> parameters)
	{
		this.commandName = commandName.substring(0);
		for(String element: parameters)
		{
			parameters.add(element.substring(0));
		}
	}
	
	/**
	 * Creates a game operation with no parameters and a deep copy of the commandName
	 * @param commandName
	 */
	public GameOperation(String commandName)
	{
		this.commandName = commandName.substring(0);
	}
	
	/**
	 * Default constructor for a game operation
	 */
	public GameOperation()
	{
		
	}
	
	/**
	 * @return the command's ID 
	 */
	public String getName()
	{
		return commandName;
	}
	
	/**
	 * @return Returns an array representing the parameters of 
	 */
	public String[] getParameterList()
	{
		String[] flag = new String[parameters.size()];
		int i = 0;
		for(String element:parameters)
		{
			flag[i] = element;
			i++;
		}
		return flag;
	}
	/**
	 * Setter for game command id
	 * @param newName 
	 */
	public void setName(String newName)
	{
		commandName = newName;
	}
	
	/**
	 * Adds a parameter in on the end of the parameter list
	 * @param parameterName parameter to be added
	 */
	public void addParameter(String parameterName)
	{
		parameters.add(parameterName);
	}
}
