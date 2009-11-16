package game;

import java.util.ArrayList;

public class GameCommand {
	
	private String commandName;
	private ArrayList<String> parameters = new ArrayList<String>();
	
	public GameCommand(String commandName, ArrayList<String> parameters)
	{
		this.commandName = commandName;
		for(String element: parameters)
		{
			parameters.add(element.substring(0));
		}
	}
	
	public GameCommand(String commandName)
	{
		this.commandName = commandName;
	}
	
	public GameCommand()
	{
		
	}
	
	public String getName()
	{
		return commandName;
	}
	
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
	
	public void setName(String newName)
	{
		commandName = newName;
	}
	
	public void addParameter(String parameterName)
	{
		parameters.add(parameterName);
	}
}
