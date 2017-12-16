package imonoko.androiddevfinalproject;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

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
        ArrayList<Statistics> stats = DBM.selectAllStats();
        ArrayList<Account> Acclist = DBM.selectAllAcc();
        LinearLayout LR = new LinearLayout(this);

        if( stats.size( ) > 0 ) {
            // create ScrollView and GridLayout
            ScrollView scrollView = new ScrollView(this);
            GridLayout grid = new GridLayout(this);
            grid.setRowCount(stats.size()+1);
            grid.setColumnCount(4);
            // create an array of stats
            TextView[][] accountStats = new TextView[stats.size()][4];

            // retrieve width of screen
            Point size = new Point();
            getWindowManager().getDefaultDisplay().getSize(size);
            int width = size.x;
            int i = 0;

            for ( Statistics stat : stats
                    ) {

                //initialize textview for each element
                for(int j=0;j<4;j++)
                {
                    accountStats[i][j] = new TextView( this );
                    accountStats[i][j].setTextSize(20.0f);
                }
                    //create label for each stat attribute for every column
                    accountStats[i][0].setText(" " + Acclist.get(i).getUserName());
                    //stat.getID() is a placeholder until we get actual ids
                    accountStats[i][1].setText(" " + stat.getScore());
                    accountStats[i][2].setText(" " + stat.getWins());
                    accountStats[i][3].setText(" " + stat.getLosses());

                    // add the elements to grid
                for(int j=0;j<4;j++) {
                    grid.addView(accountStats[i][j], (int) (width * .15),
                            ViewGroup.LayoutParams.MATCH_PARENT);
                }
                i++;
            }
            accountStats[0][0].setText("name");
            accountStats[0][1].setText("score");
            accountStats[0][2].setText("wins");
            accountStats[0][3].setText("losses");
            scrollView.addView( grid );
            LR.addView(scrollView);
        }
        else
        {
            TextView noStats = new TextView(this);
            noStats.setText("No Stats available\nMake sure you're connected to the internet");
            noStats.setGravity(Gravity.CENTER_HORIZONTAL);
            noStats.setTextSize(COMPLEX_UNIT_PX,70f);
            LR.addView(noStats);
        }
        setContentView(LR);
    }
}
