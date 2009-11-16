package game;

import lexer.TextFile;
import lexer.Token.TokenType;

import java.util.HashMap;
import java.util.StringTokenizer;
public class DecisionEngine {

	private int MAXEXECUTIONCYCLE = 1000;
	private TextFile executionCode;
	private HashMap<String, Integer> subroutineLocation = new HashMap<String, Integer>();
	private HashMap<String, Integer> symbolTable = new HashMap<String, Integer>();
	private Integer retValue;

	DecisionEngine(TextFile executionCode)
	{
		this.executionCode = executionCode;
		this.executionCode.reset();
		collectSubroutines(this.executionCode); //Have removed MAIN, at the line after it, begin processing
	}

	private void collectSubroutines(TextFile executionCode) {
		boolean stillProcessing = true;

		while(stillProcessing)
		{
			String current = executionCode.getLine();
			StringTokenizer st = new StringTokenizer(current," (),",true);
			String temp = st.nextToken();

			if(temp.equals("SUBROUTINE"))
			{
				temp = st.nextToken();

				if (subroutineLocation.containsKey(temp))
				{
					System.out.println("RUNTIME ERROR: The subroutine name " + temp + " has been used twice.");
				}
				else
				{
					subroutineLocation.put(temp, executionCode.getReport());
				}
			}
			else if(temp.equals("MAIN"))
			{
				stillProcessing = false;
			}
		}
	}

	GameCommand getNextGameCommand()
	{
		int counter = 0;
		boolean haveGameCommand = false;
		GameCommand extractedGameCommand = null;

		while(!haveGameCommand && counter < MAXEXECUTIONCYCLE)
		{
			String current = executionCode.getLine();
			StringTokenizer st = new StringTokenizer(current," (),",true);
			String token = st.nextToken();
			if(token.equals("LEFTEXPRESSION"))
			{
				boolean haveNotFinishedLeftEXP = true;
				String leftVariableToBeStored =null;

				while(haveNotFinishedLeftEXP)
				{
					current = executionCode.getLine();
					StringTokenizer stTemp = new StringTokenizer(current," (),+-/%*",true);
					leftVariableToBeStored = stTemp.nextToken(); //Should be #something
					stTemp.nextToken(); //will be space
					stTemp.nextToken(); //will be =
					stTemp.nextToken(); //will be space
					String firstRightSide = stTemp.nextToken(); //Could be #3 or 3 or x

					if(TokenType.matchesToken(TokenType.DIGITS, firstRightSide)) //Is a literal
					{
						symbolTable.put(leftVariableToBeStored, Integer.parseInt(firstRightSide)); //Stick the new value into the symbol table
					}
					else //Is a variable in the symbol table, and there might be more
					{
						if(st.hasMoreTokens()) //There is an Operator and then something else
						{
							stTemp.nextToken(); //will be space
							String operator = st.nextToken();
							stTemp.nextToken(); //will be space
							String secondRightSide = st.nextToken();
							if(operator.equals("+"))
							{
								symbolTable.put(leftVariableToBeStored, (symbolTable.get(firstRightSide) + symbolTable.get(secondRightSide)));
							}
							else if(operator.equals("-"))
							{
								symbolTable.put(leftVariableToBeStored, (symbolTable.get(firstRightSide) - symbolTable.get(secondRightSide)));
							}
							else if(operator.equals("*"))
							{
								symbolTable.put(leftVariableToBeStored, (symbolTable.get(firstRightSide) * symbolTable.get(secondRightSide)));
							}
							else if(operator.equals("%"))
							{
								symbolTable.put(leftVariableToBeStored, (symbolTable.get(firstRightSide) % symbolTable.get(secondRightSide)));
							}
							else// operator is /
							{
								symbolTable.put(leftVariableToBeStored, (symbolTable.get(firstRightSide) / symbolTable.get(secondRightSide)));
							}
						}
						else
						{
							symbolTable.put(leftVariableToBeStored, symbolTable.get(firstRightSide));
						}
					}

					current = executionCode.getLine();

					if(current.equals("RIGHTEXPRESSION"))
					{
						haveNotFinishedLeftEXP = false;
					}
					else
					{
						executionCode.addToRow(-1);
					}
				}

				//String rightVariableToBeStored;
				boolean haveNotFinishedRightEXP = true;
				String rightVariableToBeStored = null;

				while(haveNotFinishedRightEXP)
				{
					current = executionCode.getLine();
					StringTokenizer stTemp = new StringTokenizer(current," (),+-/%*",true);
					rightVariableToBeStored = stTemp.nextToken(); //Should be #something
					stTemp.nextToken(); //will be space
					stTemp.nextToken(); //will be =
					stTemp.nextToken(); //will be space
					String firstRightSide = stTemp.nextToken(); //Could be #3 or 3 or x
					if(TokenType.matchesToken(TokenType.DIGITS, firstRightSide)) //Is a literal
					{
						symbolTable.put(rightVariableToBeStored, Integer.parseInt(firstRightSide)); //Stick the new value into the symbol table
					}
					else //Is a variable in the symbol table, and there might be more
					{
						if(st.hasMoreTokens()) //There is an Operator and then something else
						{
							stTemp.nextToken(); //will be space
							String operator = st.nextToken();
							stTemp.nextToken(); //will be space
							String secondRightSide = st.nextToken(); 
							if(operator.equals("+"))
							{
								symbolTable.put(rightVariableToBeStored, (symbolTable.get(firstRightSide) + symbolTable.get(secondRightSide)));
							}
							else if(operator.equals("-"))
							{
								symbolTable.put(rightVariableToBeStored, (symbolTable.get(firstRightSide) - symbolTable.get(secondRightSide)));
							}
							else if(operator.equals("*"))
							{
								symbolTable.put(rightVariableToBeStored, (symbolTable.get(firstRightSide) * symbolTable.get(secondRightSide)));
							}
							else if(operator.equals("%"))
							{
								symbolTable.put(rightVariableToBeStored, (symbolTable.get(firstRightSide) % symbolTable.get(secondRightSide)));
							}
							else// operator is /
							{
								symbolTable.put(rightVariableToBeStored, (symbolTable.get(firstRightSide) / symbolTable.get(secondRightSide)));
							}
						}
						else
						{
							symbolTable.put(rightVariableToBeStored, symbolTable.get(firstRightSide));
						}
					}

					current = executionCode.getLine();

					if(current.startsWith("IF") || current.startsWith("WHILE"))
					{
						haveNotFinishedRightEXP = false;
					}

					executionCode.addToRow(-1);
				}

				current = executionCode.getLine();
				st = new StringTokenizer(current," (),+-/%*",true);
				st.nextToken(); //Should be IFELSE, IF, or WHILE
				st.nextToken(); //Should be space
				String jumpCondition = st.nextToken(); //Should be CONDITION
				st.nextToken(); //Should be space
				st.nextToken(); //Should be JUMP
				st.nextToken(); //Should be space
				String jumpAmount = st.nextToken(); //Should be JUMP amount

				if(jumpCondition.equals("=="))
				{
					if(symbolTable.get(leftVariableToBeStored) == symbolTable.get(rightVariableToBeStored))
					{
						executionCode.addToRow(Integer.parseInt(jumpAmount)-1);
					}
				}
				else if(jumpCondition.equals("!="))
				{
					if(symbolTable.get(leftVariableToBeStored) != symbolTable.get(rightVariableToBeStored))
					{
						executionCode.addToRow(Integer.parseInt(jumpAmount)-1);
					}
				}
				else if(jumpCondition.equals("<="))
				{
					if(symbolTable.get(leftVariableToBeStored) <= symbolTable.get(rightVariableToBeStored))
					{
						executionCode.addToRow(Integer.parseInt(jumpAmount)-1);						
					}
				}
				else if(jumpCondition.equals(">="))
				{
					if(symbolTable.get(leftVariableToBeStored) >= symbolTable.get(rightVariableToBeStored))
					{
						executionCode.addToRow(Integer.parseInt(jumpAmount)-1);
					}
				}
				else if(jumpCondition.equals("<"))
				{
					if(symbolTable.get(leftVariableToBeStored) < symbolTable.get(rightVariableToBeStored))
					{
						executionCode.addToRow(Integer.parseInt(jumpAmount)-1);
					}
				}
				else if(jumpCondition.equals(">"))
				{
					if(symbolTable.get(leftVariableToBeStored) > symbolTable.get(rightVariableToBeStored))
					{
						executionCode.addToRow(Integer.parseInt(jumpAmount)-1);
					}
				}

			}
			else if(token.equals("RETURN"))
			{
				st.nextToken(); //Gets a space
				token = st.nextToken(); //Gets the variable to return 
				retValue = symbolTable.get(token); //Put the value from the variable into the return register
			}
			else if(token.startsWith("#"))
			{
				
				boolean haveFoundVar = false;
				String thisLine;
				String runningTotalVariable = token;
				st.nextToken(); //Space
				st.nextToken(); //=
				st.nextToken(); //Space
				String firstRightSide = st.nextToken(); //Either a literal, or a variable
				
				if(TokenType.matchesToken(TokenType.DIGITS, firstRightSide))
				{
					symbolTable.put(runningTotalVariable, Integer.parseInt(firstRightSide));
				}
				else
				{
					symbolTable.put(runningTotalVariable, symbolTable.get(firstRightSide));
				}
				
				while(!haveFoundVar)
				{
					thisLine = executionCode.getLine();
					StringTokenizer tokenizedLine = new StringTokenizer(thisLine," (),+-*/%",true);
					String expToken = tokenizedLine.nextToken();
					if(expToken.equals("VAR"))
					{
						haveFoundVar = true;
						tokenizedLine.nextToken(); //Get rid of space
						String variableToStoreInto = tokenizedLine.nextToken(); //Variable I want to store the expression into
						symbolTable.put(variableToStoreInto, symbolTable.get(runningTotalVariable));
					}
					else
					{
						runningTotalVariable = expToken; //Store left sided variable for later use
						expToken = tokenizedLine.nextToken(); //Space
						expToken = tokenizedLine.nextToken(); //=
						expToken = tokenizedLine.nextToken(); //Space
						firstRightSide = tokenizedLine.nextToken(); //#3, 3, or variable
						
						if(tokenizedLine.hasMoreTokens())
						{
							tokenizedLine.nextToken(); //will be space
							String operator = tokenizedLine.nextToken();
							tokenizedLine.nextToken(); //will be space
							String secondRightSide = tokenizedLine.nextToken();
							if(operator.equals("+"))
							{
								symbolTable.put(runningTotalVariable, (symbolTable.get(firstRightSide) + symbolTable.get(secondRightSide)));
							}
							else if(operator.equals("-"))
							{
								symbolTable.put(runningTotalVariable, (symbolTable.get(firstRightSide) - symbolTable.get(secondRightSide)));
							}
							else if(operator.equals("*"))
							{
								symbolTable.put(runningTotalVariable, (symbolTable.get(firstRightSide) * symbolTable.get(secondRightSide)));
							}
							else if(operator.equals("%"))
							{
								symbolTable.put(runningTotalVariable, (symbolTable.get(firstRightSide) % symbolTable.get(secondRightSide)));
							}
							else// operator is /
							{
								symbolTable.put(runningTotalVariable, (symbolTable.get(firstRightSide) / symbolTable.get(secondRightSide)));
							}

						}
						else //No more tokens, either a literal or a symbol
						{
							if(TokenType.matchesToken(TokenType.DIGITS, firstRightSide))
							{
								symbolTable.put(runningTotalVariable, Integer.parseInt(firstRightSide));
							}
							else
							{
								symbolTable.put(runningTotalVariable, symbolTable.get(firstRightSide));
							}
						}
					}
				}
			}
			else if(token.equals("CALL"))
			{

			}
			else if(TokenType.matchesToken(TokenType.GAMEFUNCTION,token))
			{

			}
			else if(TokenType.matchesToken(TokenType.GAMEORDER, token))
			{
				extractedGameCommand = new GameCommand(token);
				boolean firstTimeThrough = true;
				token = st.nextToken(); // (
				token = st.nextToken(); // either ) or a parameter

				while(!token.equals(")"))
				{
					if(firstTimeThrough)
					{
						firstTimeThrough = false;
						extractedGameCommand.addParameter(token);
					}
					else //Must be a , variable pair, , is already in token
					{
						extractedGameCommand.addParameter(st.nextToken());
					}

					token = st.nextToken();
				}
				haveGameCommand = true;
			}
			else if(token.equals("JUMP"))
			{
				String jumpAmount = st.nextToken();
				executionCode.addToRow(Integer.parseInt(jumpAmount)- 1);
			}

			counter++;
		}

		return extractedGameCommand;
	}
}
