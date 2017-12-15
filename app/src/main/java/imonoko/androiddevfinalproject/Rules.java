package imonoko.androiddevfinalproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import static android.util.TypedValue.COMPLEX_UNIT_PX;

public class Rules extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView rules = new TextView(this);
        ScrollView SV = new ScrollView(this);
        LinearLayout LR = new LinearLayout(this);
        rules.setText("1. On startup all user will get chance/turn to roll one dice. Highest dice roll goes first." +
                " If it is a draw this must continue until it is established who goes first." +
                "\n\n2. A winner will be determined by the first person who wins 2 rounds. " +
                "\n\n3. The first player who goes will roll first until he/she reaches point." +
                "\nA point is when two of the three die are the same, and the odd dice is point. Ex{2|2|5} 5 is your point." +
                "\n\n4.Once bank has point, the other player now has to roll higher to beat the banker's point. " +
                "If the player who goes second rolls the same point as the first player, then the first player wins the round." +
                "\n\nAutomatic Wins:\nTrips: All three die land on the same number.\nCeelo: Rolling a 4, 5, and 6." +
                "\n\nAutomatic Loss:\nRolling 1,2,3 is also an automatic loss."
        );
        rules.setGravity(Gravity.CENTER_HORIZONTAL);
        rules.setTextSize(COMPLEX_UNIT_PX,50f);
        LR.addView(rules);
        SV.addView(LR);
        setContentView(SV);
    }
}
