package imonoko.androiddevfinalproject;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;


public class GameActivity extends Activity implements GestureDetector.OnGestureListener{

    private CeeLoModel clm;
    private GestureDetectorCompat gDetect;
    private TextView diceResults;
    private TextView p1Status;
    private TextView p2Status;
    private TextView gameStatus;
    private TextView roundCounter;
    private String p1; // status box for player 1
    private String p2; // status box for player 2
    private int [] scores;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ceelo_main);

        clm = new CeeLoModel();
        gDetect = new GestureDetectorCompat(this,this);
        diceResults = (TextView) findViewById(R.id.results);
        p1Status = (TextView) findViewById(R.id.Player_1);
        p2Status = (TextView) findViewById(R.id.Player_2);
        gameStatus = (TextView) findViewById(R.id.Status);
        roundCounter = (TextView) findViewById(R.id.Round_count);

        scores = clm.getScores();
        p1Status.setText("Player 1 \n\n Score: " + Integer.toString(scores[0]) + "\n");
        p2Status.setText("Player 2 \n\n Score: " + Integer.toString(scores[1]) + "\n");
    }

    // TODO: displays results in the text view - SHOULD REPLACE WITH ANIMATION
    //TODO: ADD Player Objects from the database so we can decipher who one
    public void updateView()
    {
        clm.roll();
        diceResults.setText( clm.displayResult() );
    }

    public void showGameEndDialog( ) {
        AlertDialog.Builder alert = new AlertDialog.Builder( this );
        alert.setTitle( "The Game has ended" );
        alert.setMessage( "Player " + clm.winMatchChecker() + " has won. \nDo you want to Play again against this player?" );
        PlayDialog playAgain = new PlayDialog( );
        alert.setPositiveButton( "YES", playAgain );
        alert.setNegativeButton( "NO", playAgain );
        alert.show( );
    }
    private class PlayDialog implements DialogInterface.OnClickListener {
        public void onClick( DialogInterface dialog, int id ) {
            if( id == -1 ) // YES
            {
                // need to implement
            }
            else if( id == -2 ) // NO
                GameActivity.this.finish( ); // return to main menu
        }
    }

    public boolean progressGame()
    {
        scores = clm.getScores(); // update scores
        roundCounter.setText("It's Round " + clm.getRound() + ". ");

        this.updateView(); // swiped so dice is rolled



        clm.updateScores(); // checks for winner/ update round if needed

        int player = clm.getCurrentPlayer( );

        if( player == 1)
        {
            p1Status.setText("Player 1 \n\n Score: " + Integer.toString(scores[0]) + "\n" + clm.displayResult());

            if (clm.twoOfAKind() == true)
                p1Status.setText("Player 1 \n\n Score: " + Integer.toString(scores[0]) + "\n" + clm.displayResult() + "\n Point: " + clm.showPoint( ));
        }

        else if (player == 2)
        {
            p2Status.setText("Player 2 \n\n Score: " + Integer.toString(scores[1]) + "\n" + clm.displayResult());

            if (clm.twoOfAKind() == true)
                p2Status.setText("Player 2 \n\n Score: " + Integer.toString(scores[1]) + "\n" + clm.displayResult() + "\n Point: " + clm.showPoint( ));
        }

        // reroll if necessary, here
        if ( clm.needToReroll() == true)
            return false;

        clm.play(); // switch player
        gameStatus.setText("It's Player " + clm.getCurrentPlayer() + " turn!");


        if (clm.getRecentWinner() > 0) // someone won
        {
            clm.resetForNextRound();
        }

        if (clm.winMatchChecker() > 0) // game is over
        {
            gameStatus.setText("The Game is over");
            // add stats to database
            showGameEndDialog();
        }
        return  true;
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