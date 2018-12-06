import java.util.ArrayList;

public class Quick_Game{
  
  //For Quick game mode: Words between 2 and 4 letters long. 
  //The user has until one turn after the head, body, left and right arm, 
  //left and right leg are added to guess the word. That is 7 chances. 
  
  //TO DO:
  //- Make static letter index if correct
  //   - Make sure that letter is in an index that is actually the same as a word
  //   - Account for a letter being in multiple indexes
  //- Make ai check for static letters/indexes
  //- RETHINK LOGIC??? :(
  
  
  //Constants////////////////////////////////////////////////////////////////////////////////////////
  //Scoring Constants
  final static String GAME_TYPE = "Quick Play";
  final static int WON = 5;
  final static int LOST = 3;
  final static int LESS_THAN_3 = 20;
  final static int LESS_THAN_4 = 10;
  final static int LESS_THAN_5 = 5;
  
  final static int CORRECT_CONSONANT = 10;
  final static int CORRECT_VOWEL = 15;
  final static int CORRECT_SPECIAL = 30;
  
  //Guessing Constants
  final static int NUMBER_OF_GUESSES = 7;
  final static int GUESSES_MINUS_1 = 6;
  final static int GUESSES_MINUS_2 = 5;
  final static int GUESSES_MINUS_3 = 4;
  final static int GUESSES_MINUS_4 = 3;
  final static int GUESSES_MINUS_5 = 2;
  final static int GUESSES_MINUS_6 = 1;
  final static int ZERO_GUESSES = 0;
  
  //Word Length Constants
  final static int WORD_LENGTH_MIN = 2;
  final static int WORD_LENGTH_MAX = 4;
  
  final static String WORD_HOLDER = "_";
  
//__Hangman Display Constants________________________________________________________________________
  String[][] hangmanDisplay = {
    {" ", " ", " ", " ", " ", "_", "_", "_", " ", " "},
    {" ", " ", " ", " ", "|", " ", " ", " ", "|", " "},
    {" ", " ", " ", " ", "|", " ", " ", " ", "^", " "},
    {" ", " ", " ", " ", "|", " ", " ", " ", " ", " "},
    {" ", " ", " ", " ", "|", " ", " ", " ", " ", " "},
    {" ", " ", " ", " ", "|", " ", " ", " ", " ", " "},
    {" ", " ", " ", " ", "|", " ", " ", " ", " ", " "},
    {" ", " ", " ", " ", "|", " ", " ", " ", " ", " "},
    {"_", "_", "_", "_", "|", "_", "_", "_", "_", "_"},
  };
  
  String[] hangman1 = {" ", " ", " ", " ", "|", " ", " ", " ", "O", " "};//will switch with the 3rd index row
  String[] hangman2 = {" ", " ", " ", " ", "|", " ", " ", " ", "|", " "};//will switch with the 4th index row
  String[] hangman3 = {" ", " ", " ", " ", "|", " ", " ", "/", "|", " "};//will switch with the 4th index row
  String[] hangman4 = {" ", " ", " ", " ", "|", " ", " ", "/", "|", "\\"};//will switch with the 4th index row
  String[] hangman5 = {" ", " ", " ", " ", "|", " ", " ", "/ ", " ", " "};//will switch with the 5th index row
  String[] hangman6 = {" ", " ", " ", " ", "|", " ", " ", "/ ", "\\", ""};//will switch with the 5th index row
  
//__guessed Array, consonant array, vowel array, special letters array_______________________________
  String[] vowelArray = {"A", "E", "I", "O", "U"};
  String[] consonantArray = {"B", "C", "D", "F", "G", "H", "I", "J", "K", "L", "M", "N", "P", "R", "S", "T", "V", "W"};
  String[] specialLettersArray = {"Q", "X", "Y", "Z"};
  String[] guessedArray = new String[NUMBER_OF_GUESSES];
  String[] validLetters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
  
//__FIELDS___________________________________________________________________________________________
  
  private boolean correctGuess;
  private boolean correctVowel;
  private boolean correctConsonant;
  private boolean correctSpecial;
  private int score;
  // private int theme;
  private int guessChances;
  private int round;
  private String guess;
  private String userName;
  private boolean complete;
  private int size = IR4.getRandomNumber(WORD_LENGTH_MIN, WORD_LENGTH_MAX);
  
  
  
  
  ArrayList<String> list = new ArrayList<>();
  String[] wordArray = new String[size];
  boolean[] indexFilled = new boolean[size];
  
//__CONSTRUCTOR______________________________________________________________________________________
  Quick_Game(int t, String name){
    
    correctGuess = false;
    correctVowel = false;
    correctConsonant = false;
    correctSpecial = false;
    score = 0;
    //theme = t;
    guessChances = NUMBER_OF_GUESSES;//will minus down with every guess
    round = 0;
    guess = " ";
    userName = name;
    complete = false;   
    
    initializeGuessedArray();
    initializeThemeArray();
    //clean word array list
    removeWords();
    
    
    
  }//end default constructor
  
  
  
  //__RUN GAME METHOD__________________________________________________________________________________
  public void runGame(){ 
    
    initializeIndexFilledArray();
    initializeWordArray();
    displayGameIntro();
    
    while(guessChances > 0 && (!complete)){   
      
      displayHangman();
      displayWordArray();
      //displayChances(); //For testing: The hangman display shows current status of guesses good enough.
      displayGuessedArray();
      displayScore();
      round++;
      
      setGuess(); 
      
      
      correctGuess = theHangman();//this is the AI control method. Will tell the computer if the guess is correct or not.
      
      if(correctGuess){
        checkGuess();//this checks for consonant/vowel/special letter status
        calculateScore(); //this method uses the four booleans (correct, vowel, consonant, special) to calculate score
        //displayScore(); //for Testing
        
        
      }else{
        guessChances--;
        updateHangman();//This updates the Hang Man display
        //displayScore(); //For testing
      }//This should make it so the guessChances only goes down on incorrect guesses.
      
      guessedArray[round-1] = guess;

      complete = checkCompletion();
      
      
    }//end guessChances while loop
    
    calculateFinalScore();
    if(guessChances == 0){ displayLost();}
    if(complete){displayWon();}
  

    // Save score and other stats.
    ProjectFileIO_v2.saveStats(userName, score);

    //return to main menu.
    
    
  }//end runGame
  
//__THEME ARRAY MANIPULATION METHODS_________________________________________________________________
  
  public void initializeThemeArray(){
    list.add("APP");
    list.add("SNAP");
    list.add("RAID");
    list.add("CAP");
    list.add("TOO");
    list.add("ZOO");
    list.add("TAPE");
    list.add("MAIN");
    list.add("TAME");
    list.add("LOSE");
    list.add("LAME");
    list.add("BAIT");
    list.add("NEW");
    list.add("NEWS");
    list.add("TO");
    list.add("AT");
    list.add("CAT");
    list.add("TAP");
    list.add("NO");
    list.add("YES");
    list.add("ZAP");
    list.add("BAIN");
    list.add("BANE");
    list.add("PUT");
    list.add("PUTT");
    list.add("BUT");
    list.add("BUTT");
  }//end initializeThemeArray
  
  
//__SETTERS__________________________________________________________________________________________
  public void setGuess(){
    String g = IR4.getString("Please enter your guess.");
    g = g.toUpperCase();
    while(validGuess(g)){
      System.err.println("That is not a valid guess. \nPlease enter a single letter you have not previously guessed.");
      g = IR4.getString("Please enter your guess.");
    }
    guess = g;
  }//end setGuess
  
  private boolean validGuess(String g){
    //true = invalid!
    boolean valid = false;
    boolean length = checkGuessLength(g);//false = too long or too short
    boolean validLetter = checkValidLetters(g);//false = not a valid letter
    boolean notGuessed = checkPreviouslyGuessed(g);//true = guessed
    
    if(!length){
      valid = true;
    }
    if(!validLetter){
      valid = true;
    }
    if(notGuessed){
      valid = true;
    }
    
    System.out.println("Invalid guess?: " + valid); //for testing
    return valid;
  }//end validGuess
  
  private boolean checkGuessLength(String g){
    boolean valid = false;
    if(g.length() > 1){ valid = false;}
    else{valid = true;}  
    System.out.println("Length?: " + valid);//for testing
    return valid;
  }//end checkGuessLength
  
  private boolean checkValidLetters(String g){
    //If the guess is a valid letter, return true
    //If the guess is not a valid letter, return false
    boolean valid = false;
    
    for(int i = 0; i < validLetters.length; i++){
      if(validLetters[i].equals(g)){
        valid = true;
      }
    }
    System.out.println("Valid Letter?: " + valid);//for testing
    return valid;
  }//end checkValidLetters
  
  private boolean checkPreviouslyGuessed(String g){
    boolean valid = false;
    for(int x = 0; x < guessedArray.length; x++){
        if(g.equals(guessedArray[x])){
          valid = true;
        }
      }
    System.out.println("Previously guessed?: " + valid);//for testing
    return valid; 
  }//end checkPreviouslyGuessed
  
//__GETTERS__________________________________________________________________________________________    
  public String getGuess(){
    return guess;
  }//end getGuess
  
  public int getScore(){
    return score;
  }//end getScore
  
//__METHODS__________________________________________________________________________________________
  
  //__DISPLAY METHODS________________________________________________________________________________
  private static void displayGameIntro(){
    System.out.println("*******************************************************************");
    System.out.println("       Welcome to the"+ GAME_TYPE +" Mode of The Honest Hangman!");
    System.out.println("   The rules are simple: When prompted, guess a single letter.");
    System.out.println("    You have "+NUMBER_OF_GUESSES+" chances to guess the word.");
    System.out.println("              Goal: Don't let the hangman hang.");
    System.out.println("*******************************************************************\n");
  }//end displayShortGameIntro
  
  private void displayHangman(){
    for(int r = 0; r<hangmanDisplay.length;r++){
      for(int c = 0; c<hangmanDisplay[0].length;c++){
        System.out.print(hangmanDisplay[r][c]);
      }
      System.out.println();
    }
  }//end displayHangMan
  
  private void displayScore(){
    System.out.println("Score: "+ score);
  }//end displayScore
  
  private void displayWordArray(){
    for(int i = 0; i < wordArray.length; i++){
      System.out.print(" " + wordArray[i] + " ");
    }
    System.out.println(" ");
  }//end displayWordArray
  
  private void displayChances(){
    System.out.println("Total Guesses Left:" + guessChances);
  }//end displayChances
  
  private void displayWon(){
    System.out.println("Congratulations! You won!");
    System.out.println("You scored a total of " + score + " points.");
  }//end displayWon
  
  private void displayLost(){
    System.out.println("And the hangman is hung. You have lost.");
    System.out.println("You scored a total of " + score + " points.");
  }//end displayLost
  
  private void displayGuessedArray(){
    System.out.print("Previously Guessed: ");
    for(int i = 0; i < guessedArray.length; i++){
      if(!(guessedArray[i].equals(WORD_HOLDER))){
        System.out.print(guessedArray[i] + " ");
      }
    }
    System.out.println(" ");
  }//end displayGuessedArray
  
//__THE HANGMAN______________________________________________________________________________________
  private boolean theHangman(){
    boolean valid = false;
    int index = 0;
    int count = 0;
    int highest = 0;
    boolean found = checkForFound();//checkForFound sees if the letter is even in any words.
    //System.out.println(found); // for testing
    
    if(found){
      for(int i = 0; i < size; i++){
        //System.out.println("Index" + i);//for testing
        count = countForIndex(i);
        if(count > highest && (!indexFilled[i])){
          highest = count;
          index = i;
        }
        else if(count == highest && (!indexFilled[i])){
          int num = IR4.getRandomNumber(0,1);
          if(num == 0){
            highest = count;
            index = i;
          }
        }
      }
      if(index != 9){
        wordArray[index] = guess;
        indexFilled[index] = true;
        //System.out.println(wordArray[index]+ " "+ index);//for testing
        removeWords(guess, index);
        //displayWords();//for testing
        valid = true;
      }
    }else{
      valid = false;
    }
    
    return valid;
  }//end theHangMan
  
  
  private boolean checkForFound(){
    boolean check = false;
    
    for(int x = 0; x < list.size(); x++){
      for(int i = 0; i < size; i++){
        
        if(guess.equals((list.get(x).charAt(i))+"")){
          check = true;
          //System.out.println(check);//for testing
        }
        //System.out.println(list.get(x).charAt(i));//for testing
      }
      
    }
    return check;
  }//end checkForFound
  
  private int countForIndex(int i){
    int count = 0;
    String word;
    
    for(int x = 0; x < list.size(); x++){
      word = list.get(x);
      //System.out.println(word);//for testing
      //System.out.println(word.charAt(i));// for testing
      //System.out.println(guess.equals((word.charAt(i))+""));//for testing
      if(guess.equals((word.charAt(i))+"")){
        count++;
      }
    }
    
    
    //System.out.println(count);//for testing
    return count;
  }//end countForIndex
  
  
  
  
  
  
//__CHECK GUESS______________________________________________________________________________________
  public void checkGuess(){
    correctConsonant = false;
    correctVowel = false;
    correctSpecial = false;
    
    checkForConsonant();
    if(!correctConsonant){
      checkForVowel();
    }
    if(!correctConsonant && !correctVowel){
      checkForSpecial();
    }
  }//end checkGuess 
  
  private void checkForVowel(){
    for(int i = 0; i<vowelArray.length;i++){
      if(guess.equals(vowelArray[i])){correctVowel = true;
      }
    }
  }//end checkForVowel
  
  private void checkForConsonant(){
    for(int j = 0; j<consonantArray.length;j++){
      if(guess.equals(consonantArray[j])){correctConsonant = true;
      }
    }
  }//end checkForConsonant
  
  private void checkForSpecial(){
    for(int k = 0; k<specialLettersArray.length;k++){
      if(guess.equals(specialLettersArray[k])){ correctSpecial = true;
      }
    }
  }//end checkForSpecial
  
  private boolean checkCompletion(){
    boolean valid = true;
    int checkCounter = 0;
    for(int i = 0; i < wordArray.length; i++){
      if(wordArray[i].equals(WORD_HOLDER)){
        valid = true;
      }else{
        checkCounter++;
      }
    }
    //System.out.println(checkCounter); //for testing
    if(checkCounter == size){
      return true;
    }else{
      return false;
    }
  }//end checkCompletion
  
//__CALCULATE SCORE__________________________________________________________________________________
  private void calculateScore(){
    if(correctVowel){ score = score + CORRECT_VOWEL;}
    
    else if(correctConsonant){ score = score + CORRECT_CONSONANT;}
    
    else if(correctSpecial){ score = score + CORRECT_SPECIAL;}
    
  }//end calculateScore
  
  private void calculateFinalScore(){
    if(complete){ 
      score = score + WON;
      if(round < 3){
        score = score + LESS_THAN_3;
      }
      if(round < 4){
        score = score + LESS_THAN_4;
      }
      if(round < 5){
        score = score + LESS_THAN_5;
      }
    }
    
    else{ score = score + LOST;}
  }//end calculateFinalScore
  
//__UPDATING DISPLAYS________________________________________________________________________________
  private void updateHangman(){
    if(!correctGuess){
      
      if(guessChances == GUESSES_MINUS_1){
        //read hangman1 into the 3rd index row of hangmanDisplay
        int x = 3;
        for(int c = 0; c < hangmanDisplay[0].length; c++){
          hangmanDisplay[x][c] = hangman1[c];
        }
      }
      if(guessChances == GUESSES_MINUS_2){
        //read hangman2 into the 4th index row of hangmanDisplay
        int r = 4;
        for(int c = 0; c < hangmanDisplay[0].length; c++){
          hangmanDisplay[r][c] = hangman2[c];
        }
      }
      if(guessChances == GUESSES_MINUS_3){
        //read hangman3 into the 4th index row of hangmanDisplay
        int r = 4;
        for(int c = 0; c < hangmanDisplay[0].length; c++){
          hangmanDisplay[r][c] = hangman3[c];
        }
      }
      if(guessChances == GUESSES_MINUS_4){
        //read hangmand4 into the 4th index row of hangmanDisplay
        int r = 4;
        for(int c = 0; c < hangmanDisplay[0].length; c++){
          hangmanDisplay[r][c] = hangman4[c];
        }
      }
      if(guessChances == GUESSES_MINUS_5){
        //read hangman5 into the 5th index row of hangmanDisplay
        int r = 5;
        for(int c = 0; c < hangmanDisplay[0].length; c++){
          hangmanDisplay[r][c] = hangman5[c];
        }
      }
      if(guessChances == GUESSES_MINUS_6){
        //read hangman6 into the 5th index row of hangmanDisplay
        int r = 5;
        for(int c = 0; c < hangmanDisplay[0].length; c++){
          hangmanDisplay[r][c] = hangman6[c];
        }
      }
    }//update according to guessChances counter. Remember: It's backwards. 7 is good, 0 is bad.}
  }//end updateHangman
  
//__INITIALIZE ARRAYS________________________________________________________________________________
  private void initializeWordArray(){
    for(int i = 0; i < wordArray.length;i++){
      wordArray[i] = "_";
    }
  }//end initializeWordArray
  
  private void displayWords(){
    for(int i = 0; i < list.size(); i++){
      System.out.println(list.get(i));
    }
  }//end displayWords
  
  private void initializeGuessedArray(){
    for(int i = 0; i < guessedArray.length; i++){
      guessedArray[i] = WORD_HOLDER;
    }
  }//end initializeGuessedArray
  
  private void removeWords(){
    for(int i = list.size()-1; i >= 0; i--){
      if(list.get(i).length() > size || list.get(i).length() < size){
        list.remove(i);
      }
    }
  }//end removeWords
  
  private void removeWords(String guess, int index){
    boolean valid;
    for(int x = list.size()-1; x >= 0; x--){
      if(guess.equals((list.get(x).charAt(index)+""))){
        valid = true;
      }else{
        list.remove(x);
      }
    }
  }//end removeWords(String, int)
  
  private void initializeIndexFilledArray(){
    for(int i = 0; i < indexFilled.length; i++){
      indexFilled[i] = false;
    }
  }//end initializeIndexFilledArray
  
  
}//end Quick Game