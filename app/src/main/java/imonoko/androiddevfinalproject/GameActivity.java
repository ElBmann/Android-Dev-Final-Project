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
    private volatile Thread again;
    private CeeLoModel clm;
    private GestureDetectorCompat gDetect;
    private TextView diceResults, p1Status,p2Status,gameStatus,roundCounter;
    public static ImageView dicePos1,dicePos2,dicePos3;
    private Account p1Acc;
    private Account p2Acc;
    private String p1; // initials for player 1
    private String p2; // initials for player 2
    private Statistics stat;
    private int [] scores;
    private MediaPlayer winsound,losesound;

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
        p1Status.setText(p1 + "\n\n Score: " + Integer.toString(scores[0]) + "\n");
        p2Status.setText(p2 + "\n\n Score: " + Integer.toString(scores[1]) + "\n");
        updateRoundDisplay();
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
        String outcome = "Round " + clm.getRound() + " is over. \n";

        if (winner == clm.getCurrentPlayer()) // if the current player won
        {
            if (clm.winMethod() == 1)
                outcome += identifyCurrentPlayer() + " got 4-5-6. How lucky!";

            else if (clm.winMethod() == 3)
                outcome += identifyCurrentPlayer() + " got three-of-a-kind. Instant Win!";

            else if (clm.winMethod() == 2 && clm.foundUniqueSix())
                outcome += identifyCurrentPlayer() + " got two-of-a-kind with a six.";

            else if (clm.winMethod() == 2 && clm.foundUniqueSix() == false)
                outcome += identifyCurrentPlayer() + " had a higher unique roll.";

            outcome += "\n" + identifyCurrentPlayer() + " won the round.\n";
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
            winsound.start();
            alert.setMessage( "\n" + identifyCurrentPlayer() + " won the round.\n" + "Congratulations, you won, " + p1 + "\nDo you want to play against " + p2 + " again?" );
        }
        else
        {
            losses++;
            losesound.start();
            alert.setMessage( "\n" + identifyCurrentPlayer() + " won the round.\n" + "Sorry. You lost, " + p1 + ".\nDo you want to Pplay against " + p2 + " again?" );
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
                gameStatus.setText("The Game is over");
                p1Status.setText(p1 + "\n\n Score: " + Integer.toString(scores[0]) + "\n");
                p2Status.setText(p2 + "\n\n Score: " + Integer.toString(scores[1]) + "\n");
                gameStatus.setText("It's " + identifyCurrentPlayer() + "'s turn!");
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
        scoreChanges();
        clm.displayResult();

        // reroll if necessary, here
        if( clm.needToReroll() )
            rollAgain(); // tell the player to roll again

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
                p2Status.setText(p2 + "\n\n Score: " + Integer.toString(scores[1]) + "\n" + clm.displayResult() + "\n Your Roll is:: " + clm.showPoint( ));
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

    public void stopReroll()
    {
        again = null;
    }

    public void rollAgain() {
        Toast.makeText(this, identifyCurrentPlayer() + " needs to reroll", Toast.LENGTH_SHORT).show();
        showInformRerollDialog();
        //diceResults.setText( clm.displayResult() );
    }




/*
    public void rollAgain() {

        Thread reroll = new Thread() {
            @Override
            public void run() {
                while (!isInterrupted()) {

                    if (clm.needToReroll() == false)
                        stopReroll();

                    else
                    {
                        try {
                            Thread.sleep(3500); // 3500 milliseconds or 3.5 seconds between auto rerolls
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    clm.roll();
                                    scoreChanges();
                                    diceResults.setText( clm.displayResult() );
                                    //Toast.makeText(GameActivity, "Player "+ clm.getCurrentPlayer() + ", ROLLED AGAIN!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (InterruptedException intX) {
                            intX.printStackTrace();
                        }
                    }
                }
            }
        };

            reroll.start();
            Toast.makeText(this, "Player " + clm.getCurrentPlayer() + ", needed to reroll", Toast.LENGTH_SHORT).show();
            gameCheck();
        //progressGame();
    }
*/
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
        progressGame();
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