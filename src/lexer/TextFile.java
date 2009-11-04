package lexer;

import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

import lexer.Token.TokenType;

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

	public String getCharacter()
	{
		String character;
		while(!isEndOfFile() && posPointer > (body.get(rowPointer).length()-1))
		{
			rowPointer++;
			posPointer = 0;
		}

		if(!isEndOfFile())
		{
			posPointer++;
		}


		if(!isEndOfFile())
		{
			posPointer--;
			character = body.get(rowPointer).substring(posPointer, posPointer+1);
			posPointer++;
		}
		else
		{
			character = "";
		}

		return character; 
	}

	public Token getToken()
	{
		String rVal = body.get(rowPointer); //Get the string out of the array located at the rowPointer
		rVal = rVal.substring(posPointer); //Get the portion of the string to the right of the posPointer
		for(int i = rowPointer+1; i < body.size(); i++)
			rVal = rVal + body.get(i);

		String returnTokText = null;
		TokenType returnTokType = null;
		StringTokenizer st = new StringTokenizer(rVal," \t;(){}=!<>+-/*%",true);
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
			if(temp.matches("=") && lookAhead == null) //This is the last token, so it must just be the = symbol
			{
				returnTokText = temp;
				returnTokType = TokenType.SYMBOL;
			}
			else if(temp.matches("=") && lookAhead != null) //This is not the last token, so something must come after it
			{
				if(lookAhead.matches("="))//Check to see if you actually found == instead of just =
				{
					returnTokText = temp.concat(lookAhead);
					returnTokType = TokenType.SYMBOL;
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
		else if(temp.matches("!") ||temp.matches("<") ||temp.matches(">")) //Must be a condition
		{
			if(temp.matches("!"))//Must have = after it, or it's an error
			{
				if(lookAhead.matches("="))
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

			if(temp.matches("<") || temp.matches(">"))
			{
				if(lookAhead == null || !lookAhead.matches("="))
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

		posPointer += returnTokText.length();

		while(body.get(rowPointer).length() < posPointer) //While the pos pointer is greater than the size of the current row, take out the pos pointer 
		{
			posPointer -= body.get(rowPointer).length();
			rowPointer++;	
			}
//		System.out.println("ROW:"+rowPointer);
//		System.out.println("POS:"+posPointer);
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
	 * Inserts a line of text at the specified line in the textfile. Bumps the line of text that was there down one line.
	 * @param entry	Should not be longer than the number of rows in the textfile
	 */
	public void input(String entry)
	{
		body.add(entry);
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
		else //If the file isn't empty, and the marker isn't at the last spot in the file, then you must not be at EoF
		{
			return false;
		}
	}

}
