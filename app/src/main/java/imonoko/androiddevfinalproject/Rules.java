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
        rules.setText("1. On startup all user will get chance/turn to roll 1 dice. Highest dice roll wins bank." +
                "\nIf more than 1 player have highest number, players " +
                "that have the highest number must roll again to get the " +
                "highest number. This must continue until it is established who has bank.");
        rules.setGravity(Gravity.CENTER_HORIZONTAL);
        rules.setTextSize(COMPLEX_UNIT_PX,50f);
        LR.addView(rules);
        SV.addView(LR);
        setContentView(SV);
    }
}
