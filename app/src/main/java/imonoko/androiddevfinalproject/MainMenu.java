package imonoko.androiddevfinalproject;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainMenu extends AppCompatActivity {
    private LoginActivity LA = new LoginActivity();
    private TextView p1Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //  MediaPlayer player= MediaPlayer.create(this,R.raw.menumusic);
     //   player.start();
        TextView top = new TextView(this);
       // top = (TextView) findViewById(R.id.editText);
        setContentView(R.layout.activity_main_menu);
        Configuration config = getResources().getConfiguration();
        modifyLayout(config);
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
        Intent startGame_intent = new Intent(this, waitRoom.class);
        this.startActivity(startGame_intent);
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
            case R.id.action_ClearDataBase:
                DatabaseManager db = new DatabaseManager(this);
                db.clearDataBase();
                // another startActivity, this is for item with id "menu_item2"
                break;
            case R.id.action_logout:
                // another startActivity, this is for item with id "menu_item2"
                Intent logout = new Intent(this, LoginActivity.class);
                this.startActivity(logout);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }


        return true;
    }//End onOptionItemSelected


    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        modifyLayout(newConfig);
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
            modifyLayout(newConfig);
        }else if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){
            modifyLayout(newConfig);
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

}
