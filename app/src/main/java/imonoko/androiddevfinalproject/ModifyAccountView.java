package imonoko.androiddevfinalproject;

import android.content.Context;
import android.graphics.Color;
import android.text.InputType;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by ImonokoI Stolen from Nazim on 12/11/2017.
 */

public class ModifyAccountView  extends LinearLayout{
    // TextViews to serve as labels
    private TextView userNamePrompt;
    private TextView emailPrompt;
    private TextView passwordPrompt;

    // EditTexts to collect the Info
    private EditText userNameEntry;
    private EditText emailEntry;
    private EditText passwordEntry;

    // Button to add account to database
    private Button modifyAccount;

    // TableLayout and TableRows to format the create account form
    private TableLayout table;
    private TableRow userNameRow;
    private TableRow emailRow;
    private TableRow passwordRow;

    public ModifyAccountView(Context context, OnClickListener listener)
    {
        super(context);
        this.setOrientation(LinearLayout.VERTICAL);
        this.setBackgroundColor(Color.parseColor("#EEEEEE")); // very light gray

        // TableLayout Layout parameters
        LayoutParams tableParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        tableParams.weight = 1.0f;

        // TableRow Layout parameters
        TableLayout.LayoutParams tableRowParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tableRowParams.setMargins(0,40,0,10);

        //EditText and TextView Layout parameters
        TableRow.LayoutParams editParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        editParams.weight = 0.20f;
        editParams.setMargins(5,0,0,0);

        TableRow.LayoutParams textParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.MATCH_PARENT);
        textParams.weight = 0.40f;
        textParams.setMargins(0,0,5,0);

        // Button Layout parameters
        LayoutParams buttonParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        buttonParams.weight = 0.25f;
        buttonParams.setMargins(140,0,140,75);

        // add TableLayout to LinearLayout
        table = new TableLayout(context);
        table.setLayoutParams(tableParams);
        addView( table );

        // add TableRow
        userNameRow = new TableRow(context);
        userNameRow.setLayoutParams(tableRowParams);
        table.addView( userNameRow );

        // TextView for username
        userNamePrompt = new TextView(context);
        userNamePrompt.setText("Username: ");
        userNamePrompt.setTextSize(16);
        userNamePrompt.setBackgroundColor(Color.parseColor("#CCCCCC"));
        userNamePrompt.setTextColor(Color.parseColor("#444444"));
        userNamePrompt.setLayoutParams(textParams);
        userNameRow.addView( userNamePrompt );

        // editText for username
        userNameEntry = new EditText(context);
        userNameEntry.setMaxLines(1);
        userNameEntry.setText("");
        userNameEntry.setHint("enter new username");
        userNameEntry.setGravity(Gravity.CENTER);
        userNameEntry.setBackgroundColor(Color.parseColor("#CCCCCC"));// light gray
        userNameEntry.setHintTextColor(Color.parseColor("#444444")); // dark gray
        userNameEntry.setTextColor(Color.parseColor("#000000")); // black
        userNameEntry.setLayoutParams(editParams);
        userNameRow.addView( userNameEntry );


        emailRow = new TableRow(context);
        emailRow.setLayoutParams(tableRowParams);
        table.addView( emailRow );

        // TextView for email
        emailPrompt = new TextView(context);
        emailPrompt.setText("Email Address: ");
        emailPrompt.setTextSize(16);
        emailPrompt.setBackgroundColor(Color.parseColor("#CCCCCC"));
        emailPrompt.setTextColor(Color.parseColor("#444444"));
        emailPrompt.setLayoutParams(textParams);
        emailRow.addView( emailPrompt );

        emailEntry = new EditText(context);
        emailEntry.setMaxLines(1);
        emailEntry.setText("");
        emailEntry.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS); // email input
        emailEntry.setHint("enter new email address");
        emailEntry.setGravity(Gravity.CENTER);
        emailEntry.setBackgroundColor(Color.parseColor("#CCCCCC"));
        emailEntry.setHintTextColor(Color.parseColor("#444444"));
        emailEntry.setTextColor(Color.parseColor("#000000"));
        emailEntry.setLayoutParams(editParams);
        emailRow.addView( emailEntry );


        passwordRow = new TableRow(context);
        passwordRow.setLayoutParams(tableRowParams);
        table.addView( passwordRow );

        // TextView for password
        passwordPrompt = new TextView(context);
        passwordPrompt.setText("Password: ");
        passwordPrompt.setTextSize(16);
        passwordPrompt.setBackgroundColor(Color.parseColor("#CCCCCC"));
        passwordPrompt.setTextColor(Color.parseColor("#444444"));
        passwordPrompt.setLayoutParams(textParams);
        passwordRow.addView( passwordPrompt );

        passwordEntry = new EditText(context);
        passwordEntry.setMaxLines(1);
        passwordEntry.setText("");
        passwordEntry.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD); // password input
        passwordEntry.setHint("enter new password");
        passwordEntry.setGravity(Gravity.CENTER);
        passwordEntry.setBackgroundColor(Color.parseColor("#CCCCCC"));
        passwordEntry.setHintTextColor(Color.parseColor("#444444"));
        passwordEntry.setTextColor(Color.parseColor("#000000"));
        passwordEntry.setLayoutParams(editParams);
        passwordRow.addView( passwordEntry );


        modifyAccount = new Button (context);
        modifyAccount.setText("Save Changes");
        modifyAccount.setGravity(Gravity.CENTER);
        modifyAccount.setBackgroundColor(Color.parseColor("#CCCCCC"));
        modifyAccount.setTextColor(Color.parseColor("#444444"));
        modifyAccount.setOnClickListener( listener );
        modifyAccount.setEnabled( true );
        modifyAccount.setMaxLines(1);
        modifyAccount.setLayoutParams(buttonParams);
        addView(modifyAccount);
    }

    public EditText getName()
    {
        return userNameEntry;
    }

    public EditText getMail()
    {
        return emailEntry;
    }

    public EditText getPW()
    {
        return passwordEntry;
    }
}
