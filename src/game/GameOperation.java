package game;

import java.util.ArrayList;

public class GameOperation {
	
	private String commandName;
	private ArrayList<String> parameters = new ArrayList<String>();
	
	public GameOperation(String commandName, ArrayList<String> parameters)
	{
		this.commandName = commandName;
		for(String element: parameters)
		{
			parameters.add(element.substring(0));
		}
	}
	
	public GameOperation(String commandName)
	{
		this.commandName = commandName;
	}
	
	public GameOperation()
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
