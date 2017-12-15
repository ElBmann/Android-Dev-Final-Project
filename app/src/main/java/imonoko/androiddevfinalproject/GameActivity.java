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
        alert.setTitle( clm.displayResult() + "\n\n Player " + clm.getCurrentPlayer() + " won the round." );
        ContinueGame nextRound = new ContinueGame( );
        alert.setPositiveButton( "Now", nextRound );
    }

    private class continueRound implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            if (id == -1) // YES
            {
                if (clm.getCurrentPlayer() == 1)
                     p1Status.setText("Player " + clm.getCurrentPlayer() +" got " + clm.displayRolls());

                else if (clm.getCurrentPlayer() == 2)
                    p1Status.setText("Player " + clm.getCurrentPlayer() +" got " + clm.displayRolls());

                clm.resetForNextRound();
            }
        }
    }

    public void showNextRoundDialog( ) {
        AlertDialog.Builder alert = new AlertDialog.Builder( this );
        alert.setTitle( clm.displayResult() + "\n\n Player " + clm.getCurrentPlayer() + " won the round." );
        ContinueGame nextRound = new ContinueGame( );
        alert.setPositiveButton( "Next Round", nextRound );
    }

    private class ContinueGame implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int id) {
            if (id == -1) // YES
            {
                p1Status.setText("Player 1 \n\n Score: " + Integer.toString(scores[0]));
                p2Status.setText("Player 2 \n\n Score: " + Integer.toString(scores[1]));
                diceResults.setText(clm.getOtherPlayer() + " will go first next round.");
                clm.resetForNextRound();
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
            alert.setMessage( "Congratulations YOU won. \nDo you want to Play again against player2 ?" );
        }
        else
        {
            losses++;
            losesound.start();
            alert.setMessage( "You lost. \nDo you want to Play again against player 2?" );
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
                progressGame();
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
        this.updateRoundDisplay(); // current round
        clm.roll();
        scoreChanges();
        diceResults.setText( clm.displayResult() );

        // reroll if necessary, here
        //if ( clm.needToReroll() == true)
         //   rollAgain();

        //else
        //{
            gameCheck(); // check for round win or match win
            changePlayer(); // switch player
      //  }

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
            diceResults.setText(clm.displayResult() + "\n\n Player " + clm.getCurrentPlayer() + " won the round.");
            showNextRoundDialog( );
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