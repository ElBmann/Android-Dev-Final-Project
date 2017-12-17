package imonoko.androiddevfinalproject;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity {
    private String p1String;
    private String p2String;
    private TextView player1Initials;
    private EditText player2Initials;
    DatabaseManager db;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        player= MediaPlayer.create(this,R.raw.menumusic);
        db = new DatabaseManager(this);
        String displayUser = db.getUser(LoginActivity.getloginID());

        Toast.makeText(this, "Welcome "+displayUser+"!", Toast.LENGTH_LONG).show();

        setContentView(R.layout.activity_main_menu);//..............................................Make sure to add the setText stuff after setContentView or it will CRASH!

        Configuration config = getResources().getConfiguration();//.................................Make sure to add the setText stuff after config or it will not display!
        modifyLayout(config);
        player1Initials = (TextView) findViewById(R.id.Player1Initials);
        player1Initials.setText(displayUser);
        player2Initials = (EditText) findViewById(R.id.Player2Initials);
        player2Initials.setText("");
        p1String = player1Initials.getText().toString();
        p2String = player2Initials.getText().toString();
        Toast.makeText(this, "Player 2 must enter initials.\nInitials may contain 2 or 3 letters.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    /**
     * onOptionsItemSelected:
     * Is for the menu dropped down.
     * once an item is click it will
     * perform the action in the switch-
     * case.
     * **/
    public void gotoLeaderBoard(View v)
    {
        Intent leadBoard_intent = new Intent(this, LeaderBoardActivity.class);
        this.startActivity(leadBoard_intent);
    }
    public void startGame(View v)
    {
        if (checkPlayer2Initials() == -2)
            Toast.makeText(this, "Player 2 must enter initials.\nInitials may contain 2 or 3 letters.", Toast.LENGTH_SHORT).show();

        else if (checkPlayer2Initials() == -1)
            Toast.makeText(this, "Initials may contain 2 to 3 letters.\nPlease change your initials, Player 2", Toast.LENGTH_SHORT).show();

        else if (checkSameInitials())
            Toast.makeText(this, "Both players may not have exactly the same initials.\nInitials may contain 2 or 3 letters.", Toast.LENGTH_SHORT).show();

        else
        {
            Intent startGame_intent = new Intent(this, GameActivity.class);
            startGame_intent.putExtra("P1Name", player1Initials.getText().toString() );
            startGame_intent.putExtra("P2Name", player2Initials.getText().toString() );
            this.startActivity(startGame_intent);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_howToPlay:
                Intent rules_intent = new Intent(this, Rules.class);
                this.startActivity(rules_intent);
                break;
            case R.id.action_stats:
                Intent stats_intent = new Intent(this, StatisticsActivity.class);
                this.startActivity(stats_intent);
                break;
            case R.id.action_modifyAccount:
                Intent modify_intent = new Intent(this, ModifyAccount.class);
                this.startActivity(modify_intent);
                // another startActivity, this is for item with id "menu_item2"
                break;
            case R.id.action_logout:
                // another startActivity, this is for item with id "menu_item2"
                this.finish();
                Intent logout = new Intent(this, LoginActivity.class);
                this.startActivity(logout);

                break;
            default:
                return super.onOptionsItemSelected(item);
        }


        return true;
    }//End onOptionItemSelected


    @Override
    protected void onStart() {
        super.onStart();
        player.setLooping(true);
        player.start();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        modifyLayout(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            modifyLayout(newConfig);
            player1Initials .setText(p1String);
            player2Initials.setText(p2String);
        }else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            modifyLayout(newConfig);
            player1Initials .setText(p1String);
            player2Initials.setText(p2String);
        }else{
            Log.w("MA","Undetermined Position");
        }
    }

    private void modifyLayout(Configuration newConfig) {

        if(newConfig.orientation== Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_main_menu_landscape);

        } else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            setContentView(R.layout.activity_main_menu);

        }

    }

    public int checkPlayer2Initials ()
    {
        String p2Initials = player2Initials.getText().toString();

        if (p2Initials.equals(""))
            return -2;

        else if (p2Initials.length() < 2 ||  p2Initials.length() > 3)
            return -1;

        else
            return 0;
    }

    public boolean checkSameInitials()
    {
        String p1Initials = player1Initials.getText().toString();
        String p2Initials = player2Initials.getText().toString();

        if (p1Initials.equals(p2Initials))
            return true;

        else
            return false;
    }


}
