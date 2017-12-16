package imonoko.androiddevfinalproject;

import android.graphics.Color;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * Created by Robert on 12/4/2017.
 */
public class StatisticsActivity extends AppCompatActivity {
    private LoginActivity LA = new LoginActivity();
    private int currentID;
    private DatabaseManager DBM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stats();
    }
    public void Stats()
    {
        DBM = new DatabaseManager(this);
        LinearLayout RL = new LinearLayout(this);
        RL.setOrientation(LinearLayout.VERTICAL);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        currentID=LA.getloginID();
        int width = size.x;
        int height = size.y;

        Statistics CurrentStat;
        String CurrentAcc;
        CurrentAcc = DBM.getUser(currentID);
        CurrentStat=DBM.searchForStat(currentID);

        TextView ViewPlayerStat= new TextView(this);
        ViewPlayerStat.setTextSize(45f);
        ViewPlayerStat.setTextColor(Color.WHITE);

        TextView PlayerNameLabel= new TextView(this);
        PlayerNameLabel.setTextSize(50f);
        PlayerNameLabel.setTextColor(Color.WHITE);
        PlayerNameLabel.setText(CurrentAcc+"'s stats");

        if(CurrentStat==null)
        {
            ViewPlayerStat.setText("No Stats on file");
        }
        else
        {
            ViewPlayerStat.setText("User ID: "+CurrentStat.getID()
                    +"\n\nWins: "+CurrentStat.getWins()+"\nLosses: "+CurrentStat.getLosses()+"\nScore: "+CurrentStat.getScore());
        }
        ButtonHandler bh = new ButtonHandler();
        Button back = new Button(this);
        back.setOnClickListener(bh);
        back.setText("BACK");
        back.setTextSize(20f);
        back.setLayoutDirection(RL.getBottom());
        RL.addView(back, (int)(width*.35),(int) (height*.1) );
        RL.setBackgroundColor(Color.BLACK);
        RL.addView(PlayerNameLabel);
        RL.addView(ViewPlayerStat);
        setContentView(RL);

    }
    private class ButtonHandler implements View.OnClickListener {
        public void onClick(View v) {finish();}
    }
}
