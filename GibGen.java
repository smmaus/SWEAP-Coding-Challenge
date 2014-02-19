/**
 * Suzanne Maus
 * GE Capital Coding Challenge
 * Software Engineering Apprenticeship 
 * Presenting the "Gibberish Generator"
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *                ABOUT
 * The user can pass a string of text to
 * the Gibberish Generator, and it will
 * return each word scrambled, except for
 * the first and last letter.
 * Exceptions include punctuation marks,
 * numbers, and abbreviations. 
 * 
 *            Give it a try!
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 *              11/9/13
 */

import java.util.Scanner;
import java.util.StringTokenizer;
import java.lang.StringBuilder;
import java.util.ArrayList;
import java.util.List;

/**
 * TESTING: Updated SWEAP code for the Git Quest!
 * UPDATE: Number of characters scrambled.
 * FOURTH COMMIT FOURTH COMMIT/Testing for Release branch
 * UPDATE2: This UPDATE!
 *
 * FINAL UPDATE 2/19/14
 *
 */

public class GibGen {
  
  private Scanner input;
  private StringTokenizer st;
  private StringBuilder sb;
  
  private char firstLetter;
  private String body;
  private char lastLetter;
  
  //UPDATED PORTION
  private int chScrambled;
  
  public GibGen() {
    input = new Scanner(System.in);
  }
  
  //Runs the "Gibberish Generator"
  public static void main(String[] args) {
    (new GibGen()).start();
  }
  
  /**Function: welcomes the user and then asks 
    * for a string of text, which it then passes 
    * to gibberate() one word at a time.
    */
  public void start() {
    System.out.println("\nWelcome to the Gibberish Generator!");
    String text = getString("\nEnter text.\n").trim();
    String gibText = "";
    
    //UPDATED PORTION
    chScrambled = 0;
    
    /**Each word becomes a token, which is then
      * scrambled one at a time, and then
      * added to one long result.
      */
    st = new StringTokenizer(text);
    String temp = "";
    while (st.hasMoreTokens()) {
      temp = gibberate(st.nextToken());
      if(gibText.isEmpty())
        gibText = gibText + temp;
      else
        gibText = gibText + " " + temp;
    }
    
    System.out.println("\nHere you go!\n\n" + gibText + 
    		"\n\nNumber of characters scrambled: " + chScrambled); 
  }
  
  /**Function: finds exception tokens, such as punctuation
    * and numbers, and seperates them from normal chars.
    * Then, it scrambles the remaining legal characters,
    * and re-inserts the exception tokens.
    * ~~~~~~~~~~~~See "Exceptions" in the ABOUT~~~~~~~~~~
    *      @require: s != null
    */
  public String gibberate(String s) {
    int size = s.length();
    String result = "";
    
    //UPDATED PORTION
    chScrambled = chScrambled + s.length();
    
    //If the word is only 3 chars of less, scrambling not needed.
    if(size <= 3) 
      return s;
    
    //If the word is at least 4 chars long, scrambling proceeds.
    else {
      List<Character> legalLetters = new ArrayList<Character>();
      List<ExcPos> exceptions = new ArrayList<ExcPos>();
      
      String temp = "";
      String t = "";
      int count = 0;
      
      /**LOOP 1 ~ Goes through entire text and divides
        * legal characters and illegal characters.
        * See the two ArrayLists above.
        */
      for(char c: s.toCharArray()){
        if(isExceptionToken(c))
          exceptions.add(new ExcPos(c, count));
        else 
          legalLetters.add(c);
        count++;
      }
      
    /**LOOP 2 ~ Forms a new string of all the legal
      * characters collected in legalLetters.
      */
    for(char c: legalLetters)
      temp = temp + c;
    
    //Resets the value of "size" for the new temp value.
    size = temp.length();
      
      //Saves the first letter
      t = temp.substring(0, 1);
      firstLetter = t.charAt(0);
      
      //Saves the last letter
      t = temp.substring(size-1, size);
      lastLetter = t.charAt(0);
      
      //Saves the body, which will be gibberized.
      body = temp.substring(1, size-1);
      
      /**Ensures the scrambled word isn't the same as original.
        * Also considers a few exceptions, to avoid infinite loop.
        */
      String gibber;
      if(body.length() == 1 || isBodyException(body))
        gibber = body;
      else {
        gibber = rearrange(body);
        while(gibber.equals(body))
          gibber = rearrange(body);
      }
      
      result = firstLetter + gibber + lastLetter;
      
      //If any exception tokens were found, redistribution.
      if(!exceptions.isEmpty()) {
        for(ExcPos p: exceptions) {
          char c = p.getChar();
          int pos = p.getPos();
          result = new StringBuilder(result).insert(pos, c).toString();
        }
      }
    }
    
    return result;
  }
  
  /**Function: randomly rearranges letters in a given string
    * and returns the finished result to the user.
    *      @require: s != null
    */
  public String rearrange(String s) {
    List<Character> letters = new ArrayList<Character>();
    String result = "";

    //Adds each char in given string to ArrayList "letters."
    for(char c: s.toCharArray()) {
      letters.add(c);
    }  
    
    //Scrambles values in "letters."
    while(!letters.isEmpty()){
      int randPicker = (int)(Math.random()*letters.size());
      result = result + letters.remove(randPicker);
    }
    
    return result;
  }
  
  /**Function: returns true is the given character is
    * either a punctuation mark or a number.
    *      @require: c != null
    */
  public boolean isExceptionToken(char c) {
    return c == '!'
      || c == '?'
      || c == ','
      || c == '.'
      || c == ':'
      || c == ';'
      || c == '('
      || c == ')'
      || c == '\''
      || c == '\"'
      || c == '['
      || c == ']'
      || Character.isDigit(c);
  }
  
  /**Function: exception where two letters are identical,
    * so they do not need to be "different from original."
    * EXAMPLE: need, food, elle
    * "ee" or "oo" or "ll" would infinte loop, otherwise.
    *      @require: s != null
    */
  public boolean isBodyException(String s) {
    return s.length() == 2 && s.charAt(0) == s.charAt(1);
  }
  
  /**Function: gets string value by prompting user
    * with a question.
    *      @require: prompt != null
    */
  public String getString(String prompt){
    String choice = "";
    boolean exit = false;
    do{
      System.out.print(prompt);
      if(input.hasNextLine()) {
        choice = input.nextLine();
        exit = true;
      }
    }
    while(!exit);
    return choice;
  }
  
  /**Private "Exception Position" class, where we will
    * store the value and index of each "punctuation/number,"
    * and then use to re-distribute after the text is gibberated.
    */
  private class ExcPos {
    
    private char c;
    private int pos;
    
    private ExcPos(char c, int pos) {
      this.c = c;
      this.pos = pos;
    }
    
    private char getChar() {
      return c;
    }
    
    private int getPos() {
      return pos;
    }
  }
  
}