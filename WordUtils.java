
/**
 *	Provides utilities for word games:
 *	1. gets a list of only letters from user
 *	2. finds all words in the dictionary that match a list of letters
 *	3. prints an array of words to the screen in table format
 *	4. finds the word from an array of words with the highest score
 *		according to a table
 *
 *	Uses the FileUtils and Prompt classes.
 *	
 *	@author Sanvitti Shah REMOVE LATERRR AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
 *	@since	10/27/26
 */

import java.util.Scanner;
 
public class WordUtils
{
	private String[] words;		// the dictionary of words
	
	// File containing dictionary of almost 100,000 words.
	private final String WORD_FILE = "wordList.txt";
	
	/* Constructor */
	public WordUtils() 
	{
		loadWords();
	}
	
	/**	Load all of the dictionary of about 100,000 words from a file */
	private void loadWords ()
	{
		//open the dictionary file
		Scanner input = FileUtils.openToRead(WORD_FILE);

		String[] wordList = new String[100000];
		int wordsIndex = 0;

		while(input.hasNext())
		{
			wordList[wordsIndex] = input.next();
			wordsIndex++;
		}

		//resize the words array
		words = new String[wordsIndex];
		for(int a = 0; a < wordsIndex; a++)
		{
			words[a] = wordList[a];
		}

		input.close(); //if not closed, stays open for entire program and at end it will automatically close
		//if not closed explicitly inside program, file ends up incomplete
	}
	
	/**	Determine if string is only letters.
	 *	@param letters		the string of letters to check
	 *	@return				true if string is only letters; false otherwise
	 */
	public boolean isLetters(String letters)
	{
		for(int a = 0; a < letters.length(); a++)
			if(!Character.isLetter(letters.charAt(a))) //if not a letter
				return false;
		//makes it through everything (is a letter)
		return true;
	}
	
	/**	Find all words that can be formed by a list of letters.
	 *	The returned array is exact size of word list.
	 *  @param letters	string containing list of letters
	 *  @return			array of all words found.
	 */
	public String [] findAllWords (String letters)
	{
		String [] result = new String [10000];
		int resultIndex = 0;
		
		for(int a = 0; a < words.length; a++)
			if(hasMatchLetters(words[a], letters))
			{
				result[resultIndex] = words[a];
				resultIndex++;
			}
		
		String [] list = new String[resultIndex];
		for(int a = 0; a < resultIndex; a++)
		{
			list[a] = result[a];
		}

		return list;
	}
	
	/**	Determines if a word can be formed by a list of letters.
	 *  @param word		the word to be tested.
	 *  @param letters  a string containing the list of letters
	 *  @return   		true if word can be formed, false otherwise
	 */
	private boolean hasMatchLetters (String word, String letters)
	{
		for(int a = 0; a < word.length(); a++)
		{
			char c = word.charAt(a);
			if(letters.indexOf(c) > -1) //original string doesn't change
			{
				letters = letters.substring(0, letters.indexOf(c))
					+ letters.substring(letters.indexOf(c) + 1); //if c is first letter -> empty string (is ok) same for if c is last letter
			}
			else //character is not there
				return false;
		}

		return true; //already went through whole word and found all the characters
	}
	
	/**	Print the words found to the screen.
	 *  @param words	array containing the words to be printed
	 */
	public void printWords (String [] wordList)
	{
		for (int a = 0; a < wordList.length; a++)
		{
			System.out.printf("%-15s", wordList[a]);
			if ((a + 1) % 5 == 0) System.out.println();
		}
		System.out.println();
	}
	
	/**	Finds the highest scoring word according to Scrabble rules.
	 *
	 *  @param word  		An array of words to check
	 *  @param scoreTable	An array of 26 integer scores in letter order
	 *  @return   			The word with the highest score
	 */
	public String bestWord(String[] wordList, int[] scoreTable)
	{
    	if(wordList.length == 0)
        	return "";

    	String best = wordList[0];
    	int highestScore = getScore(best, scoreTable);

    	for(int a = 1; a < wordList.length; a++) {
        	int currentScore = getScore(wordList[a], scoreTable);

        	if(currentScore > highestScore) {
            	best = wordList[a];
            	highestScore = currentScore;
        	}
    	}

    	return best;
	}
	
	/**	Calculates the score of a word according to Scrabble rules.
	 *
	 *  @param word			The word to score
	 *  @param scoreTable	An array of 26 integer scores in letter order
	 *  @return				The integer score of the word
	 */
	public int getScore (String word, int [] scoreTable)
	{
		int totalScore = 0; 

		for(int a = 0; a < word.length(); a++) {
			int index = word.charAt(a) - 'a';
			totalScore += scoreTable[index];
		}
		return totalScore;
	}
	
	/***************************************************************/
	/************************** Testing ****************************/
	/***************************************************************/
	public static void main (String [] args)
	{
		WordUtils wu = new WordUtils();
		wu.run();
	}
	
	public void run() {
		// Score table in alphabetic order according to Scrabble
		int [] scoreTable = {1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
		String letters = "";
		String prompt = "Please enter a list of letters, from 3 to 12 letters long, " +
						"without spaces (q to quit)";
		System.out.println();
		do {
			// get string from 3 to 12 letters long
			do
			{
				letters = Prompt.getString(prompt);
			} while ((! isLetters(letters) || letters.length() < 3 || letters.length() > 12) 
							&& ! letters.equalsIgnoreCase("q"));
			if (! letters.equalsIgnoreCase("q")) {
				String [] words = findAllWords(letters);
				System.out.println();
				printWords(words);
			
				String best = bestWord(words,scoreTable);
				System.out.println("\nHighest scoring word: " + best + "\nScore = " 
									+ getScore(best, scoreTable) + "\n");
			}
		} while (! letters.equalsIgnoreCase("q"));
		System.out.println();
	}
}