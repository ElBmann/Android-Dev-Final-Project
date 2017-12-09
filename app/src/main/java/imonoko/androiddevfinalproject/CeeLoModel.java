package imonoko.androiddevfinalproject;


import java.util.Random;


public class CeeLoModel
{
    int oneKind,twoKind,threeKind =0;
    Random diceRoll = new Random();
    int[] rolls = new int[3];
    private int turn;
    Statistics CurrentStat;
    //Dice Roll

    public CeeLoModel(){
        turn = 1;//..................................................................................Counter for turn

    }
    public int play(){
        int currentTurn = turn;

        if(WinRoundChecker()== 0){//................................................................0 means no one won or loss
            if(turn == 1){
                turn=2;
            }else{
                turn =1;
            }

        }
        return 0;
    }// End Play
    private int singleRoll()
    {
        int rollValue = diceRoll.nextInt(6) + 1;// random number from 1 to 6
        return rollValue;
    }

    private int[] tripleRoll()
    {


        for (int r = 0; r < rolls.length; r++)
        {
            int roll = singleRoll();

            rolls[r] = roll;
        }

        return rolls;
    }

    public boolean oneOfAKind(){//..................................................................If the dice are all unique Returns True

        if(rolls[0]!=rolls[1] && rolls[0]!=rolls[2] && rolls[1]!=rolls[2]){
            oneKind++;//............................................................................Counter 1 of a kind
            return true;
        }
        return false;
    }

    public boolean twoOfAKind(){//..................................................................If any of the dice are two of a kind Returns True

        if(rolls[0]==rolls[1] && rolls[0]!= rolls[2]){
            twoKind++;//.............................................................................Counter 2 of a kind
            return true;
        }
        else if(rolls[0]==rolls[2] && rolls[0]!=rolls[1]){
            twoKind++;
            return true;
        }
        else if(rolls[1]==rolls[2] && rolls[1]!=rolls[0]){
            twoKind++;
            return true;
        }else {
            return false;
        }
    }
    public boolean threeOfAKind(){//................................................................If the dice are all equal Returns True

        if(rolls[0]==rolls[1] && rolls[0]==rolls[2] && rolls[1] == rolls[2]){
            threeKind++;//..........................................................................Counter 3 Of a Kind
            return true;
        }
        return false;
    }



                /*0 == Nothing, 1 == Win, And 2 == Loss*/
//TODO: Needs a comparsion to see who rolled first in case of a draw
    public int WinRoundChecker(){//.................................................................Checks if player won the Round
        if(oneOfAKind()== true){
            if(rolls[0]==1 && rolls[1]==2 && rolls[3]==3){//........................................Checks for combo 123 is automatic loss

                return 2;
            }
            else if(rolls[0]==4 && rolls[1]==5 && rolls[3]==6){

                return 1;

            }
        }//end if OneOfAKind
        /*Checks if one side of the die is 6 which is an automatic win*/
        if(twoOfAKind()==true){

            if(rolls[0]==6 && rolls[1]!=6 && rolls[2]!=6 ) {//......................................Checks if first dice = 6
                return 1;

            }else if(rolls[1]==6 && rolls[0]!=6 && rolls[2]!=6){//..................................Checks if second dice = 6
                return 1;

            }else if(rolls[2]== 6 && rolls[0]!=6 && rolls[1]!=6){//.................................Checks if thrid dice = 6
                return 1;
            }

        }//end if twoOfAKind

        if(threeOfAKind()==true){//.................................................................Three of a kind auto win
            return 1;
        }
        return 0;
    }



    // TODO: returns a string for the results to display in the in the textView in the game activity- SHOULD REPLACE WITH ANIMATION
    public String displayResult()
    {
        int[] rolls = tripleRoll();
        String values = "";

        for (int r = 0; r < rolls.length; r++)
        {
            values += rolls[r] + "     ";
        }

        return values;
    }
}