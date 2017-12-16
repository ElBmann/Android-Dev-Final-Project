package imonoko.androiddevfinalproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*
* There is alot of code here and I want all opinions on whether or not we should migrate some
* of it to a separate class
* */
public class GameActivity extends Activity implements GestureDetector.OnGestureListener{
    private DatabaseManager db;
    LoginActivity LA;
    private int begin = 0;
    private CeeLoModel clm;
    private GestureDetectorCompat gDetect;
    private TextView diceResults, p1Status,p2Status,gameStatus,roundCounter;
    public static ImageView dicePos1,dicePos2,dicePos3;
    private int p1First;
    private int p2First;
    private String p1; // initials for player 1
    private String p2; // initials for player 2
    private Statistics stat;
    private int [] scores;
    private MediaPlayer winsound,losesound, dicesound;

    private int wins;
    private int losses;
    private int draws;
    private int totalScore;
    private int rerolls;
  //  private waitRoom wr;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ceelo_main);
        LA = new LoginActivity();
      //  wr = new waitRoom();
        winsound= MediaPlayer.create(this,R.raw.yay);
        losesound= MediaPlayer.create(this,R.raw.boo);
        dicesound= MediaPlayer.create(this, R.raw.rolldice);
        clm = new CeeLoModel();
        gDetect = new GestureDetectorCompat(this,this);
        //diceResults = (TextView) findViewById(R.id.results);
        dicePos1 = (ImageView) findViewById(R.id.dice1);
        dicePos2 = (ImageView) findViewById(R.id.dice2);
        dicePos3 = (ImageView) findViewById(R.id.dice3);
        p1Status = (TextView) findViewById(R.id.Player_1);
        p2Status = (TextView) findViewById(R.id.Player_2);
        gameStatus = (TextView) findViewById(R.id.Status);
        roundCounter = (TextView) findViewById(R.id.Round_count);
       // p1Acc=wr.getPlayer1Account();
     //   p2Acc= wr.getPlayer2Account();
        scores = clm.getScores();
        wins=0;
        losses=0;
        draws=0;
        totalScore=0;
        rerolls = 0;
        Intent intent = getIntent();
        p1 = intent.getStringExtra("P1Name");
        p2 = intent.getStringExtra("P2Name");
        p1First = 0;
        p2First = 0;
        p1Status.setText(p1 + "\n\n Score: " + Integer.toString(scores[0]) + "\n");
        p2Status.setText(p2 + "\n\n Score: " + Integer.toString(scores[1]) + "\n");
        updateRoundDisplay();
        checkIfBegin(); // will never begin at the start, both players need to roll to see who gets the honor of starting the game
        gameStatus.setText("Determining who goes first...");
    }
    public void rollFirst() //both players need to roll to see who gets the honor of starting the game
    {
        int first = clm.rollForFirst();

        if (clm.getCurrentPlayer() == 1)
            p1First = first;

        else if (clm.getCurrentPlayer() == 2)
            p2First = first;


        checkIfBegin();

        if (begin == 1) // player 1 gets to start
        {
            clm.setActivePlayer(1);
        }

        else if (begin == 2) // player 2 gets to start
        {
            clm.setActivePlayer(2);
        }

        else
            clm.setActivePlayer(clm.getOtherPlayer()); // switch players to roll the die
    }
    public void checkIfBegin()
    {
        String displayString = "";

        if (p1First == 0 & p2First == 0) // neither player went
            displayString = "Roll the die to see who begins the game.\n" + p1 + ", Swipe to roll the die";

        else if (p2First == 0 && p1First != 0) // player 1 went, but player did not
            displayString = p1 +  " got " + p1First + ".\n" + p2 + ", swipe to roll the die";

        else if (p1First > 0 && p2First > 0 && p1First == p2First) // If both players went, but neither won the first roll
        {
            displayString = "Both of you rolled " + p1First + ". No winner.\n" + p1 + " , Roll Again!";
            p1First = 0; // reset player 1's roll
            p2First = 0; // reset player 1's roll
        }

        else if ( p1First > p2First && p2First != 0) // both players went and player 1 got a higher roll than player 2
        {
            displayString = p1 + " won the initial roll with " + p1First + ".\n" + p1 + " goes first.\n" + p1 + ", swipe to roll the dice and begin the match!";
            begin = 1;
            gameStatus.setText("The Game has begun! It's " + p1 + "'s turn!");
        }

        else if ( p2First > p1First && p1First != 0) // both players went and player 2 got a higher roll than player 1
        {
            displayString = p2 + " won the initial roll with " + p2First + ".\n" + p2 + " goes first.\n" + p2 + ", swipe to roll the dice and begin the match!";
            begin = 2;;
            gameStatus.setText("The Game has begun! It's " + p2 + "'s turn!");
        }

        else // error
        {
            displayString = "Swipe to roll the dice";
        }

        showPreGameDialog(displayString);
    }

    public String identifyCurrentPlayer()
    {
        int player = clm.getCurrentPlayer();

        if (player == 1)
            return p1;

        else
            return p2;
    }

    public String identifyOtherPlayer()
    {
        int player = clm.getCurrentPlayer();

        if (player == 1)
            return p2;

        else
            return p1;
    }

    public void showPreGameDialog(String displayString )
    {
        AlertDialog.Builder alert = new AlertDialog.Builder( this );
        alert.setMessage( displayString);

        BeginningGame game = new BeginningGame( );
        alert.setPositiveButton( "Continue", game );
        AlertDialog dialog = alert.create();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        alert.show( );
    }


    private class BeginningGame implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            if (id == -1) // YES
            {
                // does nothing for now
            }
        }
    }

    public void showInformRerollDialog( )
    {
        AlertDialog.Builder alert = new AlertDialog.Builder( this );
        alert.setMessage( identifyCurrentPlayer() + " got "+ clm.displayRolls() +
        "\nPlease roll again, " + identifyCurrentPlayer());

        PleaseReroll again = new PleaseReroll( );
        alert.setPositiveButton( "I Will Roll Again", again );
        AlertDialog dialog = alert.create();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        alert.show( );
    }


    private class PleaseReroll implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            if (id == -1) // YES
            {
                // does nothing
            }
        }
    }

    public void showNextTurnDialog( ) {
        AlertDialog.Builder alert = new AlertDialog.Builder( this );

        if (clm.twoOfAKind() && clm.foundUniqueSix() == false)
            alert.setMessage(identifyCurrentPlayer() +" got " + clm.displayRolls() + ",\nThe roll to beat is "+ clm.showPoint( ) + ".\nNow, " + identifyOtherPlayer() + " will roll.\n");

        else
            alert.setMessage(identifyCurrentPlayer() +" got " + clm.displayRolls() + ",\nNow, " + identifyOtherPlayer() + " will roll.\n");

        ContinueRound cR = new ContinueRound( );
        alert.setPositiveButton( "Continue", cR );
        alert.show( );
    }

    private class ContinueRound implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            if (id == -1) // YES
            {
                changePlayer(); // switch player
            }
        }
    }

    public void showNextRoundDialog( ) {
        AlertDialog.Builder alert = new AlertDialog.Builder( this );

        int winner = clm.getRecentWinner();
        String outcome = "Round " + (clm.getRound()-1) + " is over. \n";

        if (winner == clm.getCurrentPlayer()) // if the current player won
        {
            if (clm.winMethod() == 1) {

                outcome += identifyCurrentPlayer() + " got 4-5-6. How lucky!";
                winsound.start();

            }

            else if (clm.winMethod() == 3) {

                outcome += identifyCurrentPlayer() + " got three-of-a-kind. Instant Win!";
                winsound.start();

            }

            else if (clm.winMethod() == 2 && clm.foundUniqueSix()) {

                outcome += identifyCurrentPlayer() + " got two-of-a-kind with a six.";
                winsound.start();

            }

            else if (clm.winMethod() == 2 && clm.foundUniqueSix() == false) {
                outcome += identifyCurrentPlayer() + " had a higher unique roll.";
                winsound.start();
            }

            outcome += "\n" + identifyCurrentPlayer() + " won the round.\n";
            winsound.start();
        }

        else // if the current player got an instant loss
        {
            if (clm.winMethod() == 1)
                outcome += identifyCurrentPlayer() + " got 1-2-3. How unlucky!" + "\n" + identifyOtherPlayer() + " won the round.\n";;
        }

        int[] currentStandings = clm.getScores();

        outcome += "\nThe score is " + currentStandings[0] + " to " + currentStandings[1];

        alert.setMessage(outcome);

        ContinueGame nextRound = new ContinueGame( );
        alert.setPositiveButton( "Begin Next Round", nextRound );
        alert.show( );

    }

    private class ContinueGame implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            if (id == -1) // YES
            {
                clm.resetForNextRound();
                changePlayer();
                updateRoundDisplay();
            }
        }
    }

    public void showGameEndDialog( ) {
        AlertDialog.Builder alert = new AlertDialog.Builder( this );
        alert.setTitle( "The match has ended" );
        if(clm.winMatchChecker()==1)
        {

            wins++;
            alert.setMessage( "\n" + identifyCurrentPlayer() + " won the Match.\n" + "Congratulations! \nDo you want to play again?" );
            winsound.start();
        }
        else
        {
            losses++;
            losesound.start();
            if(identifyCurrentPlayer().contains(p1)) {//............................................Fix so it wont say p2 won and lost the game.
                alert.setMessage("\n" + identifyCurrentPlayer() + " won the Match.\n" + "Sorry. You lost, " + p2 + ".\nDo you want to play again?");
            }else if(identifyCurrentPlayer().contains(p2)){//............................................Fix so it wont say p1 won and lost the game.
                alert.setMessage("\n" + identifyCurrentPlayer() + " won the Match.\n" + "Sorry. You lost, " + p1 + ".\nDo you want to play again?");

            }
            winsound.start();
        }
        PlayDialog playAgain = new PlayDialog( );
        alert.setPositiveButton( "YES", playAgain );
        alert.setNegativeButton( "NO", playAgain );
        alert.show( );
    }
    private class PlayDialog implements DialogInterface.OnClickListener {
        public void onClick( DialogInterface dialog, int id ) {
            if( id == -1 ) // YES
            {
                clm.newGame();
                updateRoundDisplay();
                gameStatus.setText("A new match has begun");
                p1Status.setText(p1 + "\n\n Score: " + Integer.toString(scores[0]) + "\n");
                p2Status.setText(p2 + "\n\n Score: " + Integer.toString(scores[1]) + "\n");
                p1First = 0;
                p2First = 0;
                begin = 0; // either player can start the game in a new round
                checkIfBegin(); // display message
                //progressGame();
            }
            else if( id == -2 ) // NO
            {
                UpdateScore();
                GameActivity.this.finish(); // return to main menu
            }
        }
    }

    public boolean progressGame()
    {
        //this.updateRoundDisplay(); // current round
        clm.roll();
        dicesound.start();
        scoreChanges();
        clm.displayResult();

        // reroll if necessary, here
        if( clm.needToReroll() ) {
            rollAgain(); // tell the player 6to roll again
            dicesound.start();
        }

        else // will progress the game
            gameCheck(); // check for round win or match win

        return true;
    }

    public void updateRoundDisplay() // replaces update view
    {
        roundCounter.setText("It's Round " + clm.getRound() + ". ");
    }

    public void scoreChanges()
    {
        clm.updateScores(); // checks for winner/ update round if needed
        scores = clm.getScores(); // update scores

        int player = clm.getCurrentPlayer( );

        if( player == 1)
        {
            p1Status.setText(p1 + "\n\n Score: " + Integer.toString(scores[0]) + "\n" + clm.displayResult());

            if (clm.twoOfAKind() == true)
                p1Status.setText(p1 + "\n\n Score: " + Integer.toString(scores[0]) + "\n" + clm.displayResult() + "\n Your Roll is: " + clm.showPoint( ));
        }

        else if (player == 2)
        {
            p2Status.setText(p2 + "\n\n Score: " + Integer.toString(scores[1]) + "\n" + clm.displayResult());

            if (clm.twoOfAKind() == true)
                p2Status.setText(p2 + "\n\n Score: " + Integer.toString(scores[1]) + "\n" + clm.displayResult() + "\n Your Roll is: " + clm.showPoint( ));
        }
    }

    public void gameCheck()
    {
        if (clm.winMatchChecker() > 0) // game is over
        {
            gameStatus.setText("The Game is over");
            // add stats to database
            showGameEndDialog();
        }

        else if (clm.getRecentWinner() > 0) // someone won the round
        {
            p1Status.setText(p1 + "\n\n Score: " + Integer.toString(scores[0]));
            p2Status.setText(p2 + "\n\n Score: " + Integer.toString(scores[1]));
            showNextRoundDialog( ); // next round
        }

        else //  no round win on first turn, go to other player
        {
            showNextTurnDialog( ); // the other player goes
        }


    }

    public void changePlayer()
    {
        clm.play(); // switch player
        gameStatus.setText("It's " + identifyCurrentPlayer() + "'s turn!");
    }

    public void rollAgain() {
        Toast.makeText(this, identifyCurrentPlayer() + " needs to reroll", Toast.LENGTH_SHORT).show();
        showInformRerollDialog();
        //diceResults.setText( clm.displayResult() );
    }

    public void UpdateScore()//score updating is strictly client-side
    //meaning that data is only recorded for the playing using the current device
    //(this is if we add multiplayer functionality )
    {
        DatabaseManager db = new DatabaseManager(this);
        Statistics oldStat = db.searchForStat(LA.getloginID());
        db.updateScores(new Statistics(
                oldStat.getID(),
                oldStat.getWins()+wins,
                oldStat.getLosses()+losses,
                oldStat.getReRolls()+rerolls,
                oldStat.getScore()+ totalScore,
                oldStat.getDraws()+draws));
    }
    // Gestures - should roll dice ONLY through onFling - other methods unintentionally change the results too quickly/easily
    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gDetect.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return true;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,  float velocityX, float velocityY)
    {
        if (begin > 0) // game has officically started
        {
            progressGame();
            //updateRoundDisplay();
        }

        else if (begin == 0)
        {
            rollFirst(); // might set begin to 1 or 2
        }

        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {;
    }

    @Override
    public boolean onScroll(MotionEvent event1, MotionEvent event2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return true;
    }
}