/**
 *  A simple version of the Scrabble game where the user plays against the computer.
 *
 *  @author	Sanvitti Shah REMOVE LATER AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAa
 *  @since	Nov 9, 2026
 */

public class Scrapple {
	
	public int [] scores = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10,
					 		1, 1, 1, 1, 4, 4, 8, 4, 10};
	private String tilesRemaining = 
					"AAAAAAAAAABBCCDDDDEEEEEEEEEEEEEFFGGGHHIIIIIIIII" +
					"JKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ";
	private final int NUMTILES = 8;			// the number of tiles per player
	private final int MIN_WORD_LENGTH = 4;	// minimum of 4 characters
	private String playerTiles = ""; //stores current letter tiles that are currently in players hand
	private String computerTiles = ""; //stores current letter tiles that computer has in its "hand"
	private int playerScore = 0; //stores the total points that the player gets throughout the game
	private int computerScore = 0; //stores the total points that the computer scores throughout the game
	private WordUtils wordUtils; //reference to WordUtils for dictionary lookups, scoring, and finding valid words


	//constructor  to initialize the WordUtils object when a Scrapple game object is created
	public Scrapple() {
		wordUtils = new WordUtils();
	}
	
	public static void main(String [] args) {
		Scrapple sjr = new Scrapple();
		sjr.run();
	}
	
	//runs Scrapple game by initiating the introduction screen and also starts the main gameplay loop
	public void run() {
		printIntroduction(); //call printIntroduction to display the intro
		//call the main loop
		playGame(); 
	}

	//controls the actual gameplay loop of Scrapple:
	//player's and computer's turns, scoring, refilling tiles, when to end the game
	public void playGame() {
		//randomly choose 8 (NUMTILES) from the pool for the player
		playerTiles = selectTiles(NUMTILES).toLowerCase();

		//same for computer's hand
		computerTiles = selectTiles(NUMTILES).toLowerCase();

		boolean playing = true; //boolean to control gameplay

		//main gameplay loop
		while(playing) {

			//PLAYER TURN

			//CHECK if player has any valid words left that can be made with their tiles
			String [] playerPossibleWords = wordUtils.findAllWords(playerTiles);
			if(playerPossibleWords.length == 0) {
				System.out.println("\nYou have no valid words left that can be made from your tiles. Game Over!");
				playing = false;
			}

			boolean validWordEntered = false;
			String playerWord = "";

			//inner loop to continuously prompt player until a valid word enterd
			while(playing && !validWordEntered) 
			{
				printTilesRemaining();
				System.out.println("Your current tiles: ");
				printSpacedTiles(playerTiles);
				System.out.println("Current Player Score: " + playerScore);
				System.out.println("Current Computer Score: " + computerScore);
				System.out.println();

				//prompt player for a word
				playerWord = Prompt.getString("Enter a word (or 'q' to quit): ");

				//allow player to quit
				if(playerWord.equalsIgnoreCase("q")) {
					System.out.println("\nThanks for playing Scrapple!");
					playing = false;
				}

				//test 1: check word length
				else if(playerWord.length() < MIN_WORD_LENGTH || playerWord.length() > NUMTILES) {
					System.out.println("\nInvalid word length! Must be between " + MIN_WORD_LENGTH + " and " + NUMTILES + " letters. Please try again.\n");
				}

				//test 2: check if letters are in player's hand
				else if(!isLettersInTiles(playerWord, playerTiles)) {
					System.out.println("\nYou used letters you don't have in your hand! Please try again.\n");
				}

				else {
					validWordEntered = true; //word passed length and tile checks
				}
			} 

			//if player successfully entered a valid word (and didn't quit) (STILL PLAYING), process dictionary, score, tiles
			if(playing) {
				int points = 0; 

				//test 3: check if word exists in dictionary
				if(isWordInDictionary(playerWord)) {
					points = getWordScore(playerWord);
					System.out.println("-> You played '" + playerWord + "' for " + points + " points!");
				}
				else {
					System.out.println("\n'" + playerWord + "' is not found in the dictionary! Word Score = 0 points.");
				}
				playerScore += points;

				//remove played letters from hand and refill back up to NUMTILES from pool
				playerTiles = removeWordFromTile(playerTiles, playerWord);
				playerTiles += selectTiles(playerWord.length()).toLowerCase(); //add back what was taken out

				//check if player has run out of tiles/pool is empty and hand can't be filled
				if(playerTiles.length() < MIN_WORD_LENGTH) {
					System.out.println("\nNot enough tiles left in the pool to continue. Game Over!");
					playing = false;
				}
			}

			//COMPUTER TURN (only runs if player turn didn't end the game)
			if(playing) {
				System.out.println("\n---------------------------------------------\n");

				String [] compWords = wordUtils.findAllWords(computerTiles); //computer finds all valid words from its current tiles

				if(compWords.length == 0) { 
					System.out.println("Computer has no valid words left to play.");
					playing = false;
				}
				else {
					String compWord = wordUtils.bestWord(compWords, scores); //computer finds best word
					int compPoints = getWordScore(compWord); //get word score 
					computerScore += compPoints; //add it to total computer score

					System.out.println("Computer played: '" + compWord + "' for " + compPoints + " points.");

					//remove played letters from computer hand and refill
					computerTiles = removeWordFromTile(computerTiles, compWord);
					computerTiles += selectTiles(compWord.length()).toLowerCase();
				}
				System.out.println("\n============================================-\n");
			}
		}

		//PRINT FINAL SCORES
		System.out.println("\n----FINAL SCORES----");
		System.out.println("Player Score: " + playerScore);
		System.out.println("Computer Score: " + computerScore);
		if(playerScore > computerScore) 
			System.out.println("Congratulations, you beat the computer!");
		else if(computerScore > playerScore) 
			System.out.println("The computer wins this time!");
		else 
			System.out.println("It's a tie!");
	}

	/**
	 *  Print the introduction screen for Scrapple.
	 */
	public void printIntroduction() {
		System.out.print(" _______     _______     ______     ______    ");
		System.out.println(" ______    ______   __          _______");
		System.out.print("/\\   ___\\   /\\  ____\\   /\\  == \\   /\\  __ \\   ");
		System.out.println("/\\  == \\  /\\  == \\ /\\ \\        /\\  ____\\");
		System.out.print("\\ \\___   \\  \\ \\ \\____   \\ \\  __<   \\ \\  __ \\  ");
		System.out.println("\\ \\  _-/  \\ \\  _-/ \\ \\ \\_____  \\ \\  __\\");
		System.out.print(" \\/\\______\\  \\ \\______\\  \\ \\_\\ \\_\\  \\ \\_\\ \\_\\ ");
		System.out.println(" \\ \\_\\     \\ \\_\\    \\ \\______\\  \\ \\______\\");
		System.out.print("  \\/______/   \\/______/   \\/_/ /_/   \\/_/\\/_/ ");
		System.out.println("  \\/_/      \\/_/     \\/______/   \\/______/ TM");
		System.out.println();
		System.out.print("This game is a modified version of Scrabble. ");
		System.out.println("The game starts with a pool of letter tiles, with");
		System.out.println("the following group of 100 tiles:\n");
		
		for (int i = 0; i < tilesRemaining.length(); i ++) {
			System.out.printf("%c ", tilesRemaining.charAt(i));
			if (i == 49) System.out.println();
		}
		System.out.println("\n");
		System.out.printf("The game starts with %d tiles being chosen at ran", NUMTILES);
		System.out.println("dom to fill the player's hand. The player must");
		System.out.printf("then create a valid word, with a length from 4 to %d ", NUMTILES);
		System.out.println("letters, from the tiles in his/her hand. The");
		System.out.print("\"word\" entered by the player is then checked. It is first ");
		System.out.println("checked for length, then checked to make ");
		System.out.print("sure it is made up of letters from the letters in the ");
		System.out.println("current hand, and then it is checked against");
		System.out.print("the word text file. If any of these tests fail, the game");
		System.out.println(" terminates. If the word is valid, points");
		System.out.print("are added to the player's score according to the following table ");
		System.out.println("(These scores are taken from the");
		System.out.println("game of Scrabble):");
		
		// Print line of letter scores
		char c = 'A';
		for (int i = 0; i < 26; i++) {
			System.out.printf("%3c", c);
			c = (char)(c + 1);
		}
		System.out.println();
		for (int i = 0; i < scores.length; i++) System.out.printf("%3d", scores[i]);
		System.out.println("\n");
		
		System.out.print("The score is doubled (BONUS) if the word has consecutive double ");
		System.out.println("letters (e.g. ball).\n");
		
		System.out.print("Once the player's score has been updated, more tiles are ");
		System.out.println("chosen at random from the remaining pool");
		System.out.printf("of letters, to fill the player's hand to %d letters. ", NUMTILES);
		System.out.println("The player again creates a word, and the");
		System.out.print("process continues. The game ends when the player enters an ");
		System.out.println("invalid word, or the letters in the");
		System.out.println("pool and player's hand run out. Ready? Let's play!\n");
		
		Prompt.getString("HIT ENTER on the keyboard to continue:");

	}

	//selects a specified number of random letter tiles from the remaining pool 
	//removes them from the pool, and returns them
	//parameter: n -> number of random tiles to select
	//return A string that contains the randomly selected tiles
	private String selectTiles(int n) {
		//String var that will store the randomly selected tiles
		String selected = "";

		//loop n times and make sure tilesRemaining.length() is > 0 (so that we dont selected more than what is left in the pool)
		for(int i = 0; i < n && tilesRemaining.length() > 0; i++) {
			//generate a random valid integer index in the current bound of tilesRemaining string
			int index = (int)(Math.random() * tilesRemaining.length());

			//add the character at that random index to the selected string
			selected += tilesRemaining.charAt(index);

			//remove the chosen character
			tilesRemaining = tilesRemaining.substring(0, index) + tilesRemaining.substring(index + 1);
		}
		return selected;
	}
	
	//prints remaining letter tiles that are currently availible in the pool (formatted)
	private void printTilesRemaining() {
		//print header message
		System.out.println("Here are the tiles remaining in the pool of letters: ");
		
		//loop through each character in the remaining tiles string 
		for(int i = 0; i < tilesRemaining.length(); i++) {
			//print current character with a space after it
			System.out.printf("%c ", tilesRemaining.charAt(i));

			//format output to print new line after every 40 characters
			if((i + 1) % 40 == 0)
				System.out.println();
		}
		System.out.println(); //print new line at end for formatting
	}

	//prints set of player or computer tiles (formatted) 
	//parameter: tiles -> string of letters representing a hand
	private void printSpacedTiles(String tiles) {
		//loop through each character in the tiles string
		for(int i = 0; i < tiles.length(); i++) {
			//print the letter with a space after
			System.out.print(tiles.charAt(i) + " ");
		}
		System.out.println(); //print new line at end for formatting
	}

	//checks if a given word can actually be formed using the availble letters in the player's hand
	//parameter: tiles -> string of tiles in the hand
	//parameter: word -> word being checked
	//return true if the word CAN be formed using the hand and false if not
	private boolean isLettersInTiles(String word, String tiles) {
		//create a temp copy of the tiles string so we don't already change the actual hand
		String tempTiles = tiles; 

		//loop through each character in the word
		for(int i = 0; i < word.length(); i++) {
			char c = word.charAt(i); //get the current char

			//find index of the char in the temp tiles
			int idx = tempTiles.indexOf(c);

			//If char is found in tiles pool
			if(idx > -1) {
				//remove that character instance from our tiles copy (so that user can't use one tile twice in a word)
				tempTiles = tempTiles.substring(0, idx) + tempTiles.substring(idx + 1);
			}
			else {
				//if any of the characters in the word are not found return false
				return false; 
			}
		}
		//all characters in word were succuessfully matched (word CAN be made)
		return true;
	}

	//removes letters of a successfully played word from a hand of tiles (both computer and player) 
	//parameter: tiles -> current string hand of tiles
	//parameter: word -> the word that was played and needs to be removed
	//return -> updated hand string with the used letters removed
	private String removeWordFromTile(String tiles, String word) {
		//create temp copy of tiles hand
		String tempTiles = tiles;

		//loop through each character of word that was played
		for(int i = 0; i < word.length(); i++) {
			char c = word.charAt(i); //get current char

			//find character in temp tiles string copy
			int idx = tempTiles.indexOf(c);

			//if found remove that character from the temp hand
			if(idx != -1) {
				tempTiles = tempTiles.substring(0, idx) + tempTiles.substring(idx + 1);
			}
		}
		return tempTiles; //return updated hand with letters of played word removed
	}

	//checks if a word exists in the dictionary (wordList.txt) using WordUtils
	//parameter: word -> word being checked
	//return true if the word exists in the dictionary and false if not
	private boolean isWordInDictionary(String word) {
		//find all dictionary matches for the given word using WordUtils
		String [] dictionary = wordUtils.findAllWords(word.toLowerCase());

		//loop through results to see if an exact match is found
		for(int i = 0; i < dictionary.length; i++) {
			if(dictionary[i].equalsIgnoreCase(word)) {
				return true;
			}
		}
		//word not found in dictionary
		return false;
	}

	//checks if word contains double consecutive letters (ball, ballet)
	//gives double points
	//parameter: word -> word being checked
	//return true if consecutive double letters are found and false if not
	private boolean hasDoubleLetters(String word) {
		//loop through the word up until 2nd to last character (to avoid out of bound error)
		for(int i = 0; i < word.length() - 1; i++) {
			//compare the current character with the next character
			if(word.charAt(i) == word.charAt(i + 1)) {
				//found consecutive matching double letters
				return true;
			}
		}
		//no consecutive matching double letters found
		return false; 
	}

	//calculate the total score for a given word based on the letter using WordUtils 
	//doubles score if it has consecutvive double letters
	//parameter: word -> word being scored
	//return -> final score for the word
	private int getWordScore(String word) {
		//get score of word from WordUtils using scores array
		int score = wordUtils.getScore(word, scores);

		//if word has consecutive double letters, double the score
		if(hasDoubleLetters(word)) {
			score *= 2;
		}
		return score; //return final score
	}
}