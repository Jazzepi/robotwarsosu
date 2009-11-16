package game;

import java.util.ArrayList;

public class GameCommand {
	
	private String commandName;
	private ArrayList<String> parameters = new ArrayList<String>();
	
	GameCommand(String commandName, ArrayList<String> parameters)
	{
		this.commandName = commandName;
		for(String element: parameters)
		{
			parameters.add(element.substring(0));
		}
	}
	
	GameCommand(String commandName)
	{
		this.commandName = commandName;
	}
	
	GameCommand()
	{
		
	}
	
	String getName()
	{
		return commandName;
	}
	
	String[] getParameterList()
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
	
	void setName(String newName)
	{
		commandName = newName;
	}
	
	void addParameter(String parameterName)
	{
		parameters.add(parameterName);
	}
}
