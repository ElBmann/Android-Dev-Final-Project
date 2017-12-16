package imonoko.androiddevfinalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class StatisticsActivity extends AppCompatActivity {
    private LoginActivity LA = new LoginActivity();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseManager DBM = new DatabaseManager(this);
        RelativeLayout RL = new RelativeLayout(this);
        TextView ViewPlayerStat= new TextView(this);
        TextView PlayerNameLabel= new TextView(this);
        int currentID=LA.getloginID();
        Statistics CurrentStat;
        String CurrentAcc;
        CurrentAcc = DBM.getUser(currentID);
        CurrentStat=DBM.searchForStat(currentID);
        ViewPlayerStat.setTextSize(40f);
        PlayerNameLabel.setTextSize(50f);
        PlayerNameLabel.setText(CurrentAcc);
        if(CurrentStat==null)
        {
            ViewPlayerStat.setText("No Stats on file");
        }
        else
        {
            ViewPlayerStat.setText("\nUser ID: "+CurrentStat.getID()
                    +"\nWins: "+CurrentStat.getWins()+"\nLosses: "+CurrentStat.getLosses()+"\nScore "+CurrentStat.getScore());

        }
        RL.addView(ViewPlayerStat);
        setContentView(RL);

    }
}
