package compiler;

import game.DecisionEngine;
import game.GameOperation;
import game.GameWorld;

import java.io.*;
import java.util.ArrayList;

public class Start {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
		ArrayList<TextFile> uncompiledHolster = new ArrayList<TextFile>();
		ArrayList<Routine> transitionHolster = new ArrayList<Routine>();
		ArrayList<TextFile> compiledHolster = new ArrayList<TextFile>();

		for(String element: args)
		{
			try {
				uncompiledHolster.add(new TextFile(element));
			} catch (IOException e) {
				System.out.println("I/O error " + e + " prevented sourcecode file " + element + " from being compiled.");
			}
		}

		for(TextFile element: uncompiledHolster)
		{
			element.stripCommentsAndWhiteSpace();
			element.reset();
			transitionHolster.add(new Routine(element));
		}

		int i = 0;
		for(Routine element: transitionHolster)
		{
			System.out.println();
			System.out.println("Compiling file " + args[i]);
			System.out.println();
			System.out.println("***************************");
			System.out.println();
			compiledHolster.add(element.compile());
			i++;
		}



		System.out.print("Would you like to write the compiled files? [Y/N]");
		String input = "";
		try {
			input = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		i=0;
		if(input.startsWith("Yes") || input.startsWith("yes") || input.startsWith("y") || input.startsWith("Y"))
		{
			for(TextFile element: compiledHolster)
			{
				System.out.println();
				System.out.println("Writing compiled VMC for file " + args[i]);
				System.out.println();
				System.out.println("***************************");
				System.out.println();
				try {
					element.write(args[i] + ".o");
				} catch (IOException e) {
					System.out.println("Failed to write VMC for file "  + args[i]);
				}
				i++;
			}
		}

		System.out.print("Would you like to display the compiled files? [Y/N]");
		input = "";
		try {
			input = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		i=0;
		if(input.startsWith("Yes") || input.startsWith("yes") || input.startsWith("y") || input.startsWith("Y"))
		{
			for(TextFile element: compiledHolster)
			{
				System.out.println();
				System.out.println("Displaying compiled VMC for file " + args[i]);
				System.out.println();
				System.out.println("***************************");
				System.out.println();
				element.display();
				i++;
			}
		}



		System.out.println("");
		System.out.print("Would you like to test the compiled files? [Y/N]");
		input = "";
		try {
			input = bufferedReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (input.startsWith("Yes") || input.startsWith("yes") || input.startsWith("y") || input.startsWith("Y")) {

			ArrayList<DecisionEngine> engineHolster = new ArrayList<DecisionEngine>();
			GameWorld globalGameworld = new GameWorld();
			for(TextFile element: compiledHolster)
			{
				engineHolster.add(new DecisionEngine(element));
			}
			
			for(DecisionEngine element: engineHolster)
			{
				element.getReferenceToGameworld(globalGameworld);
			}
			int numberCounter = 1;
			for(DecisionEngine element: engineHolster)
			{
				System.out.print("How many game commands for program number "+ numberCounter +" would you like to fetch? [Enter a number < 1000 and > 0]");
				numberCounter++;
				input = "";
				try {
					input = bufferedReader.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}

				int max = Integer.parseInt(input);
				
				for(int j = 0; j<max;j++)
				{
					GameOperation temp = element.getNextGameCommand();
					
					System.out.print("Command #" +j+ ": " + temp.getName() + "(");
					String[] list = temp.getParameterList();
					boolean isFirstParam = true;
					for(String element1 : list)
					{
						if(isFirstParam)
						{
							System.out.print(element1);
							isFirstParam = false;
						}
						else
						{
							System.out.print("," + element1);
						}
					}
					System.out.println(")");
				}

			}
		}
	}
}
