package imonoko.androiddevfinalproject;


import java.util.Random;


public class CeeLoModel
{
    int oneKind,twoKind,threeKind = 0;
    private Random diceRoll = new Random();
    private int [] point = new int[2];//............................................................the non-duplicate dice value when getting two of a kind
    private int [] gameScore = new int[2];//........................................................the score for each match
    private int [][] roundValues = new int[2][3];//.................................................2 players and their 3 dice values
    private int turn, round;

    Statistics CurrentStat;//.......................................................................Stats Object
    //Dice Roll

    public CeeLoModel(){
        for (int p = 0; p < point.length; p++)
            point[p] = 0;//.........................................................................instantiates point array

        for (int gs = 0; gs < gameScore.length; gs++)
            gameScore[gs] = 0;//....................................................................instantiates gameScore array

        turn = 1;//.................................................................................Counter for turn
        round = 1;//................................................................................Counter for round
    }
    public int play()
    {
        int currentTurn = turn;

        if(WinRoundChecker()== 0) {//...............................................................0 means no one won or lost
            if(turn == 1)//.........................................................................player 1 just went
            {
                turn = 2;
            }

            else//..................................................................................player 2 just went
            {
                turn = 1;
            }
        }
        return 0;
    }//End Play

    // This gets called within updateScore()
    // TODO: implement winMatcherChecker

    public void Round()
    {
        if (round >= 2)
        {
            //winMatchChecker();
        }

        round++;
    }

    //TODO: call this within GameActivity to check the current round and announce the start of the next round
    public int getRound()//.........................................................................Returns current round
    {
        return round;
    }


    private int singleRoll()
    {
        int rollValue = diceRoll.nextInt(6) + 1;//...........................................random number from 1 to 6
        return rollValue;
    }

    private void tripleRoll()
    {
        int player = getCurrentPlayer();//..........................................................identify the current player Player 1 is either value 1 or 2

        for (int rolls = 0; rolls < 2; rolls++)//...................................................Array to have player roll three times
        {
            int roll = singleRoll();

            roundValues[player-1][rolls] = roll;//......................................................player -1 represents array row of current player. player 1 = row 0/ player 2 = row 1
        }
    }

    public boolean oneOfAKind(int[] rolls){//.......................................................If the dice are all unique Returns True

        if(rolls[0]!=rolls[1] && rolls[0]!=rolls[2] && rolls[1]!=rolls[2]){
            oneKind++;//............................................................................Counter 1 of a kind
            return true;
        }
        return false;
    }

    public boolean twoOfAKind(int[] rolls){//.......................................................If any of the dice are two of a kind Returns True

        if(rolls[0]==rolls[1] && rolls[0]!= rolls[2]){
            point[getCurrentPlayer( )-1] = rolls[0]; //.............................................current player's "point" is the non-duplicated die's value
            twoKind++;//............................................................................Counter 2 of a kind
            return true;
        }
        else if(rolls[0]==rolls[2] && rolls[0]!=rolls[1]){
            point[getCurrentPlayer( )-1] = rolls[0]; // current player's "point" is the non-duplicated die's value
            twoKind++;
            return true;
        }
        else if(rolls[1]==rolls[2] && rolls[1]!=rolls[0]){
            point[getCurrentPlayer( )-1] = rolls[1]; // current player's "point" is the non-duplicated die's value
            twoKind++;
            return true;
        }else {
            return false;
        }
    }
    public boolean threeOfAKind(int[] rolls){//................................................................If the dice are all equal Returns True

        if(rolls[0]==rolls[1] && rolls[0]==rolls[2] && rolls[1] == rolls[2]){
            threeKind++;//..........................................................................Counter 3 Of a Kind
            return true;
        }
        return false;
    }

    public int getCurrentPlayer( )//................................................................returns the current player- the player who is rolling the dice this turn
    {
        if(turn == 1)//.............................................................................It's player 1's turn
        {
            return 1;
        }

        else//......................................................................................It's player 2's turn
        {
            return 2;
        }
    }

    public int getOtherPlayer( )//..................................................................are turns the opponent- the player who is not going this turn
    {
        if(turn == 1)//.............................................................................It's player 1's turn
        {
            return 2;
        }

        else//......................................................................................It's player 2's turn
        {
            return 1;
        }
    }

    /*0 == Nothing, 1 == Win, And 2 == Loss
    * returns the corresponding int of the winner*/

//TODO: Needs a comparison to see who rolled first in case of a draw - Done through ComparePoint private method for twoOfAKind scenario
    public int WinRoundChecker(){//.................................................................Checks if player won the Round

        // get the row of the 2D array for the current player
        int[] rolls = new int[3];
        for (int r = 0; r < rolls.length; r++)
        {
            int player = getCurrentPlayer();//......................................................identify the current player
            rolls[r] = roundValues[player-1][r];//..................................................populate the 1D array to test if the player won
        }

        // call the win checking methods
        if(oneOfAKind(rolls)==true)
        {
            if(rolls[0]==1 && rolls[1]==2 && rolls[2]==3){//........................................Checks for combo 123, is automatic loss
                return getOtherPlayer();//..........................................................the other player wins
            }
            else if(rolls[0]==4 && rolls[1]==5 && rolls[2]==6){//...................................Checks for combo 456, is automatic win
                return getCurrentPlayer();

            }
        }//end if OneOfAKind
        /*Checks if one side of the die is 6 which is an automatic win*/
        if(twoOfAKind(rolls)==true){

            if(rolls[0]==6 && rolls[1]!=6 && rolls[2]!=6 ) {//......................................Checks if first dice = 6
                return getCurrentPlayer();

            }else if(rolls[1]==6 && rolls[0]!=6 && rolls[2]!=6){//..................................Checks if second dice = 6
                return getCurrentPlayer();

            }else if(rolls[2]== 6 && rolls[0]!=6 && rolls[1]!=6){//.................................Checks if third dice = 6
                return getCurrentPlayer();
            }
            else //................................. No instant winner, compare point instead
            {
                if ( gameScore[0] > 0 && gameScore[1] > 0) //both players went
                {
                    return comparePoint();
                }
            }

        }//end if twoOfAKind

        if(threeOfAKind(rolls)==true){//............................................................Three of a kind auto win
            return getCurrentPlayer();
        }
        return 0;
    }

    private int comparePoint()
    {
        if ( point[getOtherPlayer()-1] >= point [getCurrentPlayer()-1])//...........................the player who went first has greater or equal value then the person who went second
        {
            return getOtherPlayer();//..............................................................the player who went first won
        }

        else
        {
            return getCurrentPlayer();//............................................................the player who went afterwards won
        }
    }

    // increase the score counter of the winner
    // TODO: call this within GameActivity to progress the game
    public void updateScores()
    {
        if(WinRoundChecker() > 0)
        {
            int winner = WinRoundChecker();//.......................................................get the identity of the winner
            gameScore[winner-1] += 1;//.............................................................increment the score of the winner by one
            Round();//..............................................................................check if there is a winner and increment the round counter
            resetForNextRound();
        }
    }

    // TODO: call this within GameActivity to display the scores in textviews for the players
    public int[] getScores()
    {
        return gameScore;
    }

    // clears variables for next round
    public void resetForNextRound()
    {
        turn = getOtherPlayer();//..................................................................let the loser of the previous round go first

        for (int p = 0; p < point.length; p++)//....................................................reset the point
            point[p] = 0;
    }

    // TODO: returns a string for the results to display in the in the textView in the game activity- SHOULD REPLACE WITH ANIMATION
    public String displayResult()
    {
        // get the row of the 2D array for the current player
        int[] rolls = new int[3];

        for (int r = 0; r < rolls.length; r++)
        {
            int player = getCurrentPlayer(); //identify the current player
            rolls[r] = roundValues[player-1][r]; // populate the 1D array to test if the player won
        }

        String values = "";

        for (int r = 0; r < rolls.length; r++)
        {
            values += rolls[r] + "     ";
        }

        return values;
    }
}