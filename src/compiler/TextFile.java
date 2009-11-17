package compiler;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

import compiler.Token.TokenType;


/**
 * Wrapper class that is used to pull a text file from hard disk into memory. The TextFile class
 * gives functionality to that text file in memory so that it can be accessed and written to.
 * 
 * @author Michael Pinnegar
 *
 */
public class TextFile {


	/** The main storage body for each string that represents the text file in memory*/
	private ArrayList<String> body;
	/** The horizontal depth into a string of the body array*/ 
	private int posPointer;
	/** The vertical depth down the rows of strings in the body array*/
	private int rowPointer;


	/**
	 * Returns the line number that is currently ready to be accessed.
	 * @return	line number
	 */
	public Integer getReport()
	{
		return rowPointer;
	}

	/**
	 * Constructor for TextFile
	 * 
	 * @param inputFilename Name of the file to be wrapped by the TextFile class
	 * @throws IOException Unable to access file
	 */
	public TextFile(String inputFilename) throws IOException
	{
		body = new ArrayList<String>();
		posPointer = 0;
		rowPointer = 0;

		BufferedReader in = new BufferedReader(new FileReader(inputFilename));

		while (in.ready()) //While not end of file, take in another line
		{
			String currentInput = in.readLine();
			body.add(currentInput);
		}

		in.close();
	}
	/**
	 * Constructs an empty textFile
	 */
	public TextFile()
	{
		body = new ArrayList<String>();
		posPointer = 0;
		rowPointer = 0;
	}

	/**
	 * Removes all comments from a textFile object.
	 */
	public void stripCommentsAndWhiteSpace()
	{
		ArrayList<String> tmpBody = new ArrayList<String>();

		int line = 0;
		while(tmpBody.size() < body.size())
		{
			String original = body.get(line);
			int pos = original.indexOf('#');

			if (pos > -1)//a comment exists in the original line
			{
				original = original.substring(0, pos); //Remove the comment and everything after it
			}

			original = original.trim(); //Remove whitespace
			if(original.isEmpty()) //Tack on a space if the line is empty
			{
				original += " ";
			}
			tmpBody.add(original);
			line++;
		}
		body = tmpBody;
	}

	/**
	 * Inserts a line of text at the specified row
	 * @param row	specified row
	 * @param input	line of text
	 */
	public void insertLine(int row, String input)
	{
		body.add(row, input);
		rowPointer += 1;
	}

	/**
	 * Returns the next non-whitespace {@link Token} from the file. The token is still there, but the lookAhead boolean
	 * determines of the internal pointer is incremented past it.
	 * 
	 * @param lookAhead If true, token is left in the textfile
	 * @return next non-whitespace token .Null if the textfile is empty.
	 */
	public Token getNonWSToken(boolean lookAhead)
	{
		Token flag = null;

		
		if(lookAhead) //If I want to leave the tokens in there, then I want to pull out all the WS till I find a real token 
		{
			boolean gotNonWSToken = false;
			
			while(!gotNonWSToken)
			{
				flag = getToken(true);
				if(flag != null && flag.getType() == TokenType.WS)
				{//If this one is safe to pull out, get rid of it
					flag = getToken(false);
				}
				else
				{//Otherwise you've already got a token that you want, return it
					gotNonWSToken = true;
				}
			}
		}
		else 
		{
			flag = getToken(false);
			while (flag != null && flag.getType() == TokenType.WS)
			{
				flag = getToken(false);
			}		
		}

		return flag;
	}
	
	/**
	 * Fetches the next {@link Token} from the textFile. Will return white space tokens. If lookAhead is true, the textfile's internal pointers are left unchanged, allowing the same token to be retrieved multiple times.
	 * 
	 * @param isLookAhead If true, an internal pointer is not incremented past the token.
	 * @return The next token in the text file. Null if the textfile is empty.
	 */
	public Token getToken(boolean isLookAhead)
	{

		if(isEndOfFile())
		{
			System.out.println("ERROR: Ran out of tokens.");
			return null;
		}
		
		String rVal = body.get(rowPointer); //Get the string out of the array located at the rowPointer
		rVal = rVal.substring(posPointer); //Get the portion of the string to the right of the posPointer
		for(int i = rowPointer+1; i < body.size(); i++)
			rVal = rVal + body.get(i);

		String returnTokText = null;
		TokenType returnTokType = null;
		StringTokenizer st = new StringTokenizer(rVal," \t;(){}=!<>+-,/*%",true);
		String temp = st.nextToken();
		String lookAhead = null;

		if(st.hasMoreTokens()) //Grab the lookahead token if there is one left
		{
			lookAhead = st.nextToken();
		}


		if(TokenType.matchesToken(TokenType.WS, temp))
		{
			returnTokText = temp;
			returnTokType = TokenType.WS;
		}
		else if(TokenType.matchesToken(TokenType.KEYWORD, temp))
		{//Keywords 
			returnTokText = temp;
			returnTokType = TokenType.KEYWORD;
		}
		else if(TokenType.matchesToken(TokenType.OPERATOR, temp))
		{
			returnTokText = temp;
			returnTokType = TokenType.OPERATOR;
		}
		else if(TokenType.matchesToken(TokenType.GAMEFUNCTION, temp))
		{
			returnTokText = temp;
			returnTokType = TokenType.GAMEFUNCTION;
		}
		else if(TokenType.matchesToken(TokenType.GAMEORDER, temp))
		{
			returnTokText = temp;
			returnTokType = TokenType.GAMEORDER;
		}
		else if(TokenType.matchesToken(TokenType.SYMBOL, temp))
		{
			if(temp.equals("=") && lookAhead == null) //This is the last token, so it must just be the = symbol
			{
				returnTokText = temp;
				returnTokType = TokenType.SYMBOL;
			}
			else if(temp.equals("=") && lookAhead != null) //This is not the last token, so something must come after it
			{
				if(lookAhead.equals("="))//Check to see if you actually found == instead of just =
				{
					returnTokText = temp.concat(lookAhead);
					returnTokType = TokenType.CONDITION;
				}
				else //Wasn't ==, so it must just be the symbol operator for assignment
				{
					returnTokText = temp;
					returnTokType = TokenType.SYMBOL;
				}
			}
			else //Is one of the other symbols in the symbol type that is only one length long
			{
				returnTokText = temp;
				returnTokType = TokenType.SYMBOL;
			}

		}
		else if(temp.equals("!") ||temp.equals("<") ||temp.equals(">")) //Must be a condition
		{
			if(temp.equals("!"))//Must have = after it, or it's an error
			{
				if(lookAhead != null && lookAhead.equals("="))
				{
					returnTokText = temp + lookAhead;
					returnTokType = TokenType.CONDITION;
				}
				else //Is error, no token can begin with ! and not have = after it
				{
					returnTokText = temp;
					returnTokType = TokenType.ERROR;
				}
			}

			if(temp.equals("<") || temp.equals(">"))
			{
				if(lookAhead == null || !lookAhead.equals("="))
				{
					returnTokText = temp;
					returnTokType = TokenType.CONDITION;
				}
				else
				{
					returnTokText = temp + lookAhead;
					returnTokType = TokenType.CONDITION; 
				}
			}
		}
		else if(TokenType.matchesToken(TokenType.DIGITS, temp)) //If it is a series of digits only
		{
			returnTokText = temp;
			returnTokType = TokenType.DIGITS;
		}
		else if(TokenType.matchesToken(TokenType.IDENTIFIER, temp)) //If it is a properly formed identifier
		{
			returnTokText = temp;
			returnTokType = TokenType.IDENTIFIER;
		}
		else //If it doesn't fit any of the above classifications, it must be an error
		{
			returnTokText = temp;
			returnTokType = TokenType.ERROR;
		}

		if(!isLookAhead)
		{//If it's not look ahead, then we want to increment the pointer
			posPointer += returnTokText.length();

			while(body.get(rowPointer).length() < posPointer) //While the pos pointer is greater than the size of the current row, take out the pos pointer 
			{
				posPointer -= body.get(rowPointer).length();
				rowPointer++;	
			}			
		}


		return new Token(returnTokType, returnTokText);

	}

	/**
	 * Returns the remaining text from the pointer's position to the end of the line. Increments the pointer to the next line afterwards.
	 * 
	 * @return Returns the remaining text to the end of the line without any carriage return. 
	 */
	public String getLine()
	{
		String rVal = body.get(rowPointer); //Get the string out of the array located at the rowPointer
		rVal = rVal.substring(posPointer); //Get the portion of the string to the right of the posPointer

		rowPointer++;
		posPointer = 0;

		return rVal;
	}
	
	/**
	 * Adds some amount to the row counter.
	 * @param amount
	 */
	public void addToRow(int amount)
	{
		rowPointer += amount;
		posPointer = 0;
	}
	
	/**
	 * Set the row counter to a position indicated by amount.
	 * @param amount
	 */
	public void setRow(int position)
	{
		rowPointer = position;
		posPointer = 0;
	}


	/** 
	 *	Writes the textfile to disk, line by line, starting from the first line regardless of the pointer's position.
	 * @param outputFilename	Name given to the file to be written to disk
	 * @throws IOException	Can not write file to disk
	 */
	public void write(String outputFilename) throws IOException
	{
		int i = 0;

		PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outputFilename)));
		while(i < body.size()) //Print out each line in the body representation of the text file
		{
			out.println(body.get(i));
			i++;
		}
		out.close();
	}

	/**
	 * Creates a new line of text at the end of the textfile.
	 * @param entry	Text to be appended to the end of the textfile
	 */
	public void input(String entry)
	{
		body.add(entry);
		rowPointer++;
	}

	/**
	 * Prints the textfile line by line to the console.
	 */
	public void display()
	{
		int i = 0;

		while(i < body.size())
		{
			System.out.println(body.get(i));
			i++;
		}
	}

	/**
	 * Moves the pointer back to the very beginning of the textfile
	 */
	public void reset()
	{
		posPointer = 0;
		rowPointer = 0;
	}

	/**
	 * Checks to see if the pointer is pointing at the end of file
	 * @return True if the pointer is at the last line, last spot of the file
	 */
	public boolean isEndOfFile()
	{
		if (body.size() == 0 || (body.size() == 1 && body.get(0).equals(""))) //If file is empty, you're automatically at EoF
		{
			return true;
		}
		else if(body.size() - 1 == rowPointer  && body.get(rowPointer).length() == posPointer) //If the file isn't empty, is the horizontal marker too far?
		{
			return true;
		}
		else if(rowPointer > body.size() - 1 )
		{
			return true;
		}
		else //If the file isn't empty, and the marker isn't at the last spot in the file, then you must not be at EoF
		{
			return false;
		}
	}

}
