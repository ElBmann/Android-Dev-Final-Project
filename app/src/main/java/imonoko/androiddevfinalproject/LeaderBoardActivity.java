package imonoko.androiddevfinalproject;
/**
 * Created by Robert on 12/4/2017.
 */

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.ArrayList;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

public class LeaderBoardActivity extends AppCompatActivity {

    private DatabaseManager DBM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBM = new DatabaseManager(this);
        StatsPage();
    }
    public void StatsPage( ) {
        LinearLayout LR = new LinearLayout(this);
        LR.setOrientation(LinearLayout.VERTICAL);

        ScrollView scrollView = new ScrollView(this);
        GridLayout grid = new GridLayout(this);
        ArrayList<Statistics> stats = DBM.selectAllStats();
        ArrayList<Account> Acclist = DBM.selectAllAcc();
        TextView[][] accountStats = new TextView[stats.size()+1][4];
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        int width = size.x;
        int height = size.y;
        grid.setRowCount(stats.size()+1);
        grid.setColumnCount(4);

        //Title of the activity
        TextView title = new TextView(this);
        title.setText("LEADERBOARD");
        title.setTextColor(Color.WHITE);
        title.setTextSize(50f);
        LR.addView(title,width,ViewGroup.LayoutParams.WRAP_CONTENT);

        //back button to go back to the menu
        //placed just under the title
        ButtonHandler bh = new ButtonHandler();
        Button back = new Button(this);
        back.setOnClickListener(bh);
        back.setText("BACK");
        back.setDrawingCacheBackgroundColor(Color.RED);
        back.setTextSize(20f);
        back.setLayoutDirection(LR.getBottom());
        LR.addView(back, (int)(width*.35),(int) (height*.1) );

        TextView[] label = new TextView[4];
        //create label for each stat attribute for every column
        for(int i=0;i<4;i++)
        {
            label[i]= new TextView( this );
            label[i].setTextColor(Color.WHITE);
            label[i].setTextSize(20.0f);
            grid.addView(label[i], (int) (width * .25),
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        label[0].setText("Name");
        label[1].setText("Score");
        label[2].setText("Wins");
        label[3].setText("Losses");

        if( stats.size( ) > 0 ) {
            int i=0;
            for(Statistics stat: stats) {
                for(int j=0;j<4;j++)
                {
                    accountStats[i][j] = new TextView( this );
                    accountStats[i][j].setTextColor(Color.WHITE);
                    accountStats[i][j].setTextSize(20.0f);
                    grid.addView(accountStats[i][j], (int) (width * .25),
                            ViewGroup.LayoutParams.WRAP_CONTENT);
                }
                accountStats[i][0].setText("" +getInitials(Acclist.get(i).getUserName()));
                accountStats[i][1].setText("" + stat.getScore());
                accountStats[i][2].setText("" + stat.getWins());
                accountStats[i][3].setText("" + stat.getLosses());
                i++;
            }

            LR.setBackgroundColor(Color.BLACK);
            scrollView.addView( grid );
            LR.addView(scrollView);
        }
        else
        {
            TextView noStats = new TextView(this);
            noStats.setTextColor(Color.WHITE);
            noStats.setText("\n\n\nNo Stats available\nMake sure you're connected to the internet");
            noStats.setGravity(Gravity.CENTER_HORIZONTAL);
            noStats.setTextSize(COMPLEX_UNIT_PX,70f);
            LR.addView(noStats);
        }

        setContentView(LR);
    }
    private class ButtonHandler implements View.OnClickListener {
        public void onClick(View v) {finish();}
    }
    public String getInitials(String string)
    {
        char[] charArr= string.toCharArray();
        return charArr[0]+""+charArr[charArr.length-1];
    }
}