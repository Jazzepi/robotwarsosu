package game;

import lexer.TextFile;
import lexer.Token.TokenType;
import java.util.*;

/**
 * A decision engine navigates through the source code of a Robot Wars script that has been compiled into VMC.
 * The primary purpose of the decision engine is to produce GameOrders for the Robot Wars {@link GameWorld} to
 * execute on behalf of the robots controlled by said scripts. Each robot has its own decision engine, which is
 * impregnated with code that has been compiled into VMC (Virtual Machine Code). The decision engine contains LITTLE TO NO error checking.
 * <P>Infinite loops, calling a RETURN statement outside of a subroutine, using the same subroutine name more than once can all cause
 * unrecoverable errors in the DecisionEngine's process.
 * <P>A cap on infinite loops, called MAXEXECUTIONCYCLE is the number of times the decision engine will process through the VMC to find a game order. If the 
 * MAXEXECUTIONCYCLE = 100, then the DecisionEngine can process roughly 100 IF loops before terminating and returning a HALT game order which
 * indicates that either the end of the VMC has been reached, or the MAXEXECUTIONCYCLE has been. 
 */
public class DecisionEngine {

	/**
	 * Number of instructions the Decision Engine will process to find a game order before giving up and returning a HALT command indicating that the robot's
	 * script is most likely stuck in an infinite loop.
	 */ 
	private int MAXEXECUTIONCYCLE = 1000;
	/**
	 * Compiled VMC created from source code, lineralized for execution
	 */
	private TextFile executionCode;
	/**
	 * Line location in the VMC of the spot just below the SUBROUTINE declartion
	 */
	private HashMap<String, Integer> subroutineLocation = new HashMap<String, Integer>();
	/**
	 *  Map of strings -> values where strings are variable symbols 
	 */
	private HashMap<String, Integer> symbolTable = new HashMap<String, Integer>();
	/**
	 * Call list for 
	 */
	private ArrayList<String> parameterCallList = new ArrayList<String>();
	/**
	 * Value returned by a SUBROUTINE call
	 */
	private Integer retValue;
	/**
	 * Location to return to after a RETURN call
	 */
	private int returnLocation;
	/**
	 * Spot to store returning value from a function call
	 */
	private String storeGameValueHere;

	/**
	 * Constructor for a decision engine. Processes all the SUBROUTINES into a storage class. Sets execution to begin just below MAIN
	 * @param executionCode source code compiled int VMC code
	 */
	public DecisionEngine(TextFile executionCode)
	{
		this.executionCode = executionCode;
		this.executionCode.reset();
		collectSubroutines(this.executionCode); //Have removed MAIN, at the line after it, begin processing
	}

	/**
	 * Stores the location of all the subroutines into a map. 
	 * @param executionCode source code compiled int VMC code
	 */
	private void collectSubroutines(TextFile executionCode) {
		boolean stillProcessing = true;

		while(stillProcessing)
		{
			String current = executionCode.getLine();
			StringTokenizer st = new StringTokenizer(current," (),",true);
			String temp = st.nextToken();

			if(temp.equals("SUBROUTINE"))
			{
				st.nextToken(); //Clear out space
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

	/**
	 * @return Retrieves the next reachable Game Command from the execution code. If none can be reached in 1000 cycles, or the end of the VMC is reached
	 * returns halt.
	 */
	public GameOperation getNextGameCommand()
	{
		int counter = 0;
		boolean haveGameCommand = false;
		GameOperation extractedGameCommand = null;

		while(!haveGameCommand && counter < MAXEXECUTIONCYCLE)
		{
			if(executionCode.isEndOfFile())
			{
				extractedGameCommand = new GameOperation("halt");
				return extractedGameCommand;
			}

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
				executionCode.setRow(returnLocation-1); //Return to the location where execution is suppose to begin
				symbolTable.put(storeGameValueHere, retValue); //Put the value that was calculated during the subroutine into the VAR <variable> following the CALL command
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
				parameterCallList.clear();
				st.nextToken(); // space
				String routineToJumpTo = st.nextToken(); //Routine that we want to jump to
				returnLocation = executionCode.getReport() + 2; //Place we want to return to
				int lineToJumpTo = subroutineLocation.get(routineToJumpTo); //Line just below the SUBROUTINE entry
				int lineToGrabInfoFrom = lineToJumpTo-1; //Line of the SUBROUTINE entry
				st.nextToken();// (
				String decider = st.nextToken(); //Potential first item in the 

				while(!decider.equals(")"))
				{
					parameterCallList.add(decider); //First parameter
					decider = st.nextToken(); //either , or )
					if(decider.equals(","))
					{
						decider = st.nextToken();
					}
				}

				//Parameter call list now stores all the variables that will be transferred into the subroutine call
				//Grab the VAR just below it
				StringTokenizer smallTokenizer = new StringTokenizer(executionCode.getLine()," (),",true);
				smallTokenizer.nextToken(); //VAR
				smallTokenizer.nextToken(); // Space
				storeGameValueHere = smallTokenizer.nextToken(); //Variable ID of the variable that I want to store what I get from the subroutine call into

				//Jump to the subroutine call SUBROUTINE header line
				executionCode.setRow(lineToGrabInfoFrom);
				//Get that line of code
				String callLineToken = executionCode.getLine(); 
				//Put it into a tokenizer
				StringTokenizer tokenizedCallLine = new StringTokenizer(callLineToken," (),",true);
				//Read the parameter values out, and push the values into the subroutine's variables
				tokenizedCallLine.nextToken(); //SUBROUTINE
				tokenizedCallLine.nextToken(); // space
				tokenizedCallLine.nextToken(); // subroutine ID
				tokenizedCallLine.nextToken(); // space
				tokenizedCallLine.nextToken(); // (
				String currentCallTok = tokenizedCallLine.nextToken(); // either a parameter or )

				ArrayList<String> destinationParameters = new ArrayList<String>(); // List of parameters at the destination

				while (!currentCallTok.equals(")"))
				{
					destinationParameters.add(currentCallTok);
					currentCallTok = tokenizedCallLine.nextToken();
					if(currentCallTok.equals(","))
					{
						currentCallTok = tokenizedCallLine.nextToken();
					}
				}

				if(destinationParameters.size() == parameterCallList.size())
				{
					for(int i = 0; i < destinationParameters.size(); i++)
					{
						symbolTable.put(destinationParameters.get(i), symbolTable.get(parameterCallList.get(i)));
					}
				}
				else
				{
					System.out.println("RUNTIME ERROR: Call to subroutine " + routineToJumpTo + " has too few, or too many parameters.");
				}
			}
			else if(TokenType.matchesToken(TokenType.GAMEFUNCTION,token))
			{
				//This function has not been completely implemented. A hook into the gameworld needs to be created to process these gamefunctions
				//To return meaningful values. Right now gamefunctions all return the value of 1

				String gameFuctionCommand = token; //Pull off the game function
				st.nextToken(); //Pull off the (
				ArrayList<String> gameFunctionArguements = new ArrayList<String>();
				token = st.nextToken(); //Prime loop with either ) or a variable

				while (!token.equals(")"))
				{
					gameFunctionArguements.add(token);
					token = st.nextToken();
					if(token.equals(","))
					{
						token = st.nextToken();
					}
				}

				//gameFunctionArguements now stores all the variables that will be transferred into the gamefunction call
				//Grab the VAR just below it
				StringTokenizer smallTokenizer = new StringTokenizer(executionCode.getLine()," (),",true);
				smallTokenizer.nextToken(); //VAR
				smallTokenizer.nextToken(); // Space
				storeGameValueHere = smallTokenizer.nextToken(); //Variable ID of the variable that I want to store what I get from the gamefunction call into
				symbolTable.put(storeGameValueHere, GameWorld.processGameFunction(gameFunctionArguements, gameFuctionCommand));

			}
			else if(TokenType.matchesToken(TokenType.GAMEORDER, token))
			{
				extractedGameCommand = new GameOperation(token);
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

				String jumpAmount = st.nextToken(); // Space
				jumpAmount = st.nextToken(); //
				executionCode.addToRow(Integer.parseInt(jumpAmount)- 1);
			}
			counter++;
		}

		return extractedGameCommand;
	}
}
