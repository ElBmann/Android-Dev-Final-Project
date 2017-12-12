package imonoko.androiddevfinalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class waitRoom extends AppCompatActivity {
    private Random rand;
    private LoginActivity LA;
    private DatabaseManager db;
    private TextView p1name;
    private TextView p2name;
    private Account p1Acc;
    private Account p2Acc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        p1name=(TextView) findViewById(R.id.Player1Label);
        p2name=(TextView) findViewById(R.id.Player2Label);

        setContentView(R.layout.activity_wait_room);
        db = new DatabaseManager(this);
        LA = new LoginActivity();
        ArrayList<Account> accList = db.selectAllAcc();
        rand = new Random();
        p1Acc = db.searchForAccountbyID(LA.getloginID());
        int index= rand.nextInt()%accList.size()+1;
        if(index == LA.getloginID())
            index++;
        p2Acc = new Account(27,"Bruce","Brucewayne@waneEnterPrize.batman","ImBatman");//db.searchForAccountbyID(index);
       // p1name.setText(p1Acc.getUserName());
        //p2name.setText(p2Acc.getUserName());
    }
    public void gotoGame(View v)
    {
        Intent gotogame = new Intent(this,GameActivity.class);
        this.startActivity(gotogame);
    }
    public Account getPlayer1Account()
    {return p1Acc;}
    public Account getPlayer2Account()
    {return p2Acc;}
}
