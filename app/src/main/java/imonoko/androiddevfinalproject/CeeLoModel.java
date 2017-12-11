package imonoko.androiddevfinalproject;


import java.util.Random;


public class CeeLoModel
{
    int oneKind,twoKind,threeKind = 0;
    private Random diceRoll = new Random();
    private int [] roll;
    private int [] point; // the non-duplicate dice value when getting two of a kind
    private int [] gameScore; // the score for each match
    private boolean [] played; // checks if both player went
    private int turn;
    private int round;
    private int recentWinner; // the player who just won the previous round
    Statistics CurrentStat;
    //Dice Roll

    public CeeLoModel(){
        recentWinner = 0;
        played = new boolean[2];
        roll = new int[3];
        point = new int[2];
        gameScore = new int[2];

        for (int pl = 0; pl < played.length; pl++)
            played[pl] = false;

        for (int p = 0; p < point.length; p++)
            point[p] = 0;

        for (int gs = 0; gs < gameScore.length; gs++)
            gameScore[gs] = 0;

        turn = 1;//..................................................................................Counter for turn
        round = 1;//..................................................................................Counter for round
    }
    public int play()
    {
        int currentTurn = turn;

        if(winRoundChecker()== 0) {//................................................................0 means no one won or lost
            if(turn == 1) // player 1 just went
            {
                turn = 2;
            }

            else // player 2 just went
            {
                turn = 1;
            }
        }
        return 0;
    }// End Play

    // This gets called within updateScore()
    // TODO: implement winMatcherChecker
    public int Round()
    {
        if (round >= 2 && winMatchChecker() > 0)
        {
            return winMatchChecker(); // game is over do not increment
        }

        round++;
        return 0;
    }

    // call this within GameActivity to check the current round and announce the start of the next round
    public int getRound()
    {
        return round;
    }

    private int singleRoll()
    {
        int rollValue = diceRoll.nextInt(6) + 1;// random number from 1 to 6
        return rollValue;
    }

    private int[] tripleRoll()
    {
        for (int r = 0; r < roll.length; r++)
        {
            int oneRoll = singleRoll();

            roll[r] = oneRoll;
        }

        return roll;
    }

    public void roll()
    {
        int[] rolls = tripleRoll(); // rolls 3 die

        for (int r = 0; r < rolls.length; r++)
        {
            roll[r] = rolls[r];
        }

        if(getCurrentPlayer() == 1)
            played[0] = true;

        if(getCurrentPlayer() == 2)
            played[1] = true;
    }

    public boolean needToReroll()
    {
        if (oneOfAKind() == false && twoOfAKind() == false && threeOfAKind() == false)
            return true;

        return false;
    }

    public boolean oneOfAKind(){//..................................................................If the dice are all unique Returns True

        if(roll[0]!=roll[1] && roll[0]!=roll[2] && roll[1]!=roll[2]){
            oneKind++;//............................................................................Counter 1 of a kind
            return true;
        }
        return false;
    }

    public boolean twoOfAKind(){//..................................................................If any of the dice are two of a kind Returns True
        if(roll[0]==roll[1] && roll[0]!= roll[2]){
            point[getCurrentPlayer( )-1] = roll[2]; // current player's "point" is the non-duplicated die's value
            twoKind++;//.............................................................................Counter 2 of a kind
            return true;
        }
        else if(roll[0]==roll[2] && roll[0]!=roll[1]){
            point[getCurrentPlayer( )-1] = roll[1]; // current player's "point" is the non-duplicated die's value
            twoKind++;
            return true;
        }
        else if(roll[1]==roll[2] && roll[1]!=roll[0]){
            point[getCurrentPlayer( )-1] = roll[0]; // current player's "point" is the non-duplicated die's value
            twoKind++;
            return true;
        }else {
            return false;
        }
    }
    public boolean threeOfAKind(){//................................................................If the dice are all equal Returns True

        if(roll[0]==roll[1] && roll[0]==roll[2] && roll[1] == roll[2]){
            threeKind++;//..........................................................................Counter 3 Of a Kind
            return true;
        }
        return false;
    }

    public int showPoint( ) // if got a twoOfAKind display point
    {
        if (twoOfAKind() == true)
            return point[getCurrentPlayer()-1];

        else
            return 0;
    }

    public int getCurrentPlayer( ) // returns the current player- the player who is rolling the dice this turn
    {
        if(turn == 1) // It's player 1's turn
        {
            return 1;
        }

        else // It's player 2's turn
        {
            return 2;
        }
    }

    public int getOtherPlayer( )// returns the opponent- the player who is not going this turn
    {
        if(turn == 1) // It's player 1's turn
        {
            return 2;
        }

        else // It's player 2's turn
        {
            return 1;
        }
    }

    /*0 == Nothing, 1 == Win, And 2 == Loss*/
// returns the corresponding int of the winner
//TODO: Needs a comparison to see who rolled first in case of a draw - Done through ComparePoint private method for twoOfAKind scenario
    public int winRoundChecker(){//.................................................................Checks if player won the Round

        // call the win checking methods
        if(oneOfAKind()== true)
        {
            boolean hasOne = false;
            boolean hasTwo = false;
            boolean hasThree = false;
            boolean hasFour = false;
            boolean hasFive = false;
            boolean hasSix = false;

            for (int o = 0; o < roll.length; o++ ) // checks which of the following is present
            {
                if (roll[o] == 1)
                    hasOne = true;

                if (roll[o] == 2)
                    hasTwo = true;

                if (roll[o] == 3)
                    hasThree = true;

                if (roll[o] == 4)
                    hasFour = true;

                if (roll[o] == 5)
                    hasFive = true;

                if (roll[o] == 6)
                    hasSix = true;
            }

            if(hasOne && hasTwo && hasThree){//........................................Checks for combo 123, is automatic loss
                return getOtherPlayer();// the other player wins
            }
            else if(hasFour && hasFive && hasSix){//........................................Checks for combo 456, is automatic win
                return getCurrentPlayer();
            }

        }//end if OneOfAKind
        /*Checks if one side of the die is 6 which is an automatic win*/
        if(twoOfAKind()==true){

            if(roll[0]==6 && roll[1]!=6 && roll[2]!=6 ) {//......................................Checks if first dice = 6
                return getCurrentPlayer();

            }else if(roll[1]==6 && roll[0]!=6 && roll[2]!=6){//..................................Checks if second dice = 6
                return getCurrentPlayer();

            }else if(roll[2]== 6 && roll[0]!=6 && roll[1]!=6){//.................................Checks if third dice = 6
                return getCurrentPlayer();
            }
            else //................................. No instant winner, compare point instead
            {
                if ( played[0] == true && played[1]  == true) //both players went
                {
                    return comparePoint();
                }
            }

        }//end if twoOfAKind

        if(threeOfAKind()==true){//.................................................................Three of a kind auto win
            return getCurrentPlayer();
        }

        return 0;
    }

    private int comparePoint()
    {
        if ( point[getOtherPlayer()-1] >= point [getCurrentPlayer()-1]) //the player who went first has greater or equal value then the person who went second
        {
            return getOtherPlayer(); // the player who went first won
        }

        else
        {
            return getCurrentPlayer(); // the player who went afterwards won
        }
    }

    // increase the score counter of the winner
    // TODO: call this within GameActivity to progress the game
    public void updateScores()
    {
        int winner = winRoundChecker(); // get the identity of the winner, if any

        if(winner > 0) // there was a winner
        {
            gameScore[winner-1] += 1; //increment the score of the winner by one
            recentWinner = winner; // identify the winner
            Round(); // check if there is a winner and increment the round counter
        }
    }

    // TODO: call this within GameActivity to display the scores in textviews for the players
    public int[] getScores()
    {
        return gameScore;
    }

    public int getRecentWinner()
    {
        return recentWinner;
    }

    // clears variables for next round
    public void resetForNextRound()
    {
        turn = getOtherPlayer(); // let the loser of the previous round go first
        roll = new int[3]; // reset the roll
        point = new int[2]; // reset point
        recentWinner = 0;
        for (int pl = 0; pl < played.length; pl++)
            played[pl] = false;
    }

    public String displayResult()
    {
        String values = "";

        for (int r = 0; r < roll.length; r++)
        {
            values += roll[r] + "     ";
        }

        return values;
    }

    public int winMatchChecker(){//.................................................................Checks if player won the match
        if (gameScore[getCurrentPlayer()-1] == 2)
            return getCurrentPlayer();

        else if(gameScore[getOtherPlayer()-1] == 2)
            return getOtherPlayer();

        else
            return 0;
     }
}