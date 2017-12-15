package imonoko.androiddevfinalproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/*
* There is alot of code here and I want all opinions on whether or not we should migrate some
* of it to a separate class
* */
public class GameActivity extends Activity implements GestureDetector.OnGestureListener{
    private DatabaseManager db;
    LoginActivity LA;
    private CeeLoModel clm;
    private GestureDetectorCompat gDetect;
    private TextView diceResults, p1Status,p2Status,gameStatus,roundCounter;
    public static ImageView dicePos1,dicePos2,dicePos3;
    private Account p1Acc;
    private Account p2Acc;
    private String p1; // status box for player 1
    private String p2; // status box for player 2
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
        diceResults = (TextView) findViewById(R.id.results);
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
        rerolls=0;
        p1Status.setText("player 1\n\n Score: " + Integer.toString(scores[0]) + "\n");
        p2Status.setText("player 2\n\n Score: " + Integer.toString(scores[1]) + "\n");
        updateRoundDisplay();

    }

    // TODO: displays results in the text view - SHOULD REPLACE WITH ANIMATION
    //TODO: ADD Player Objects from the database so we can decipher who one
    public void updateView()
    {
        clm.roll();
        diceResults.setText( clm.displayResult() );
    }

    public void showNextTurnDialog( ) {
        AlertDialog.Builder alert = new AlertDialog.Builder( this );
        alert.setTitle(" Player " + clm.getCurrentPlayer() +" got " + clm.displayRolls() + ".\nNow, Player " + clm.getOtherPlayer() + " will roll.\n");
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
        alert.setTitle( clm.displayResult() + "\n Player " + clm.getCurrentPlayer() + " won the round.\n" );
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
        alert.setTitle( "The Game has ended" );
        if(clm.winMatchChecker()==1)
        {
            wins++;
            winsound.start();
            alert.setMessage( "\n Player " + clm.getCurrentPlayer() + " won the round.\n" + "Congratulations YOU won. \nDo you want to Play again against player2 ?" );
        }
        else
        {
            losses++;
            losesound.start();
            alert.setMessage( "\n Player " + clm.getCurrentPlayer() + " won the round.\n" + "You lost. \nDo you want to Play again against player 2?" );
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
                p1Status.setText("player 1\n\n Score: " + Integer.toString(scores[0]) + "\n");
                p2Status.setText("player 2\n\n Score: " + Integer.toString(scores[1]) + "\n");
                gameStatus.setText("It's Player " + clm.getCurrentPlayer() + " turn!");
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
        diceResults.setText( clm.displayResult() );

        // reroll if necessary, here
       // if ( clm.needToReroll() == true)
            //rollAgain(); // curently doesn't work as it should

        //else
       // {
            gameCheck(); // check for round win or match win
       // }

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
            p1Status.setText("Player 1 \n\n Score: " + Integer.toString(scores[0]) + "\n" + clm.displayResult());

            if (clm.twoOfAKind() == true)
                p1Status.setText("Player 1 \n\n Score: " + Integer.toString(scores[0]) + "\n" + clm.displayResult() + "\n Your Point is: " + clm.showPoint( ));
        }

        else if (player == 2)
        {
            p2Status.setText("Player 2 \n\n Score: " + Integer.toString(scores[1]) + "\n" + clm.displayResult());

            if (clm.twoOfAKind() == true)
                p2Status.setText("Player 2 \n\n Score: " + Integer.toString(scores[1]) + "\n" + clm.displayResult() + "\n Your Point is:: " + clm.showPoint( ));
        }
    }

    public void gameCheck()
    {
        if (clm.getRecentWinner() > 0) // someone won the round
        {
            p1Status.setText("Player 1 \n\n Score: " + Integer.toString(scores[0]));
            p2Status.setText("Player 2 \n\n Score: " + Integer.toString(scores[1]));
            diceResults.setText(clm.displayResult() + "\nPlayer " + clm.getCurrentPlayer() + " won the round.\n" + clm.getOtherPlayer() + " will go first next round.");
            showNextRoundDialog( );
        }

        else //  no round win on first turn, go to other player
        {
            showNextTurnDialog( );
        }

        if (clm.winMatchChecker() > 0) // game is over
        {
            gameStatus.setText("The Game is over");
            // add stats to database
            showGameEndDialog();
        }
    }

    public void changePlayer()
    {
        clm.play(); // switch player
        gameStatus.setText("It's Player " + clm.getCurrentPlayer() + " turn!");
    }

    public void rollAgain2()
    {
        while (clm.needToReroll())
        {
            Toast.makeText(this, "Player " + clm.getCurrentPlayer() + ", needed to reroll", Toast.LENGTH_SHORT).show();

            new Timer().schedule(
                    new TimerTask() {
                        @Override
                        public void run() {
                            clm.roll();
                            scoreChanges();
                            diceResults.setText( clm.displayResult() );

                            if (clm.needToReroll() == false) // if got a valid roll
                                cancel();
                            //removeDialog();
                        }
                    },
                    2000 // 2000 ms or 2 seconds
            );
        }
    }




    public void rollAgain() {

        Thread reroll = new Thread() {
            @Override
            public void run() {
                boolean endReroll = false;
                while (!isInterrupted() && endReroll == false) {

                    if (clm.needToReroll() == false)
                        endReroll = true;

                    else
                    {
                        try {
                            Thread.sleep(3500); // 3500 milliseconds or 3.5 seconds between auto rereolls
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    progressGame();


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
        //progressGame();
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

    // TODO: can possibly change the speed of the animation (if velocityX and velocityY are higher, may wish to reduce duration of animation)
    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,  float velocityX, float velocityY)
    {
        while(progressGame()==false){}
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