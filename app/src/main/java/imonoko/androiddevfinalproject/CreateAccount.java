package imonoko.androiddevfinalproject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Nazim Mia on 12/2/2017.
 */

public class CreateAccount extends AppCompatActivity {

    private DatabaseManager dbManager; // used for the addAccount sql operation
    private CreateAccountView caView; // layout for create account activity

    private EditText userNameBox;
    private EditText emailBox;
    private EditText pwBox;

    private String name;
    private String email;
    private String pw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ButtonHandler bh = new ButtonHandler();
        caView = new CreateAccountView(this, bh);
        super.onCreate(savedInstanceState);
        dbManager = new DatabaseManager(this);
        setContentView(caView);
        name = "";
        email = "";
        pw = "";
    }

    private class ButtonHandler implements View.OnClickListener {
        public void onClick(View v) {
            makeAccount();
        }
    }

    public void makeAccount() {
        userNameBox = caView.getName();
        emailBox = caView.getMail();
        pwBox = caView.getPW();
        name = userNameBox.getText().toString();
        email = emailBox.getText().toString();
        pw = pwBox.getText().toString();
        Account acc = new Account(0,name, email, pw); // creates an account
        if(checkInput(acc)==true) {
            dbManager.addAccount(acc); // inserts the account into the database
            Toast.makeText(this, "The account has been successfully created", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    public Boolean checkInput(Account acc) {//rename
        try {
            // Retrieve the name and date of the task

            // validate the entered data
            int checkName = checkUserName(acc.getUserName());
            int checkMail = checkEmail(acc.getEmail());
            int checkPass = checkPassword(acc.getPassword());

            if (name.equals("")) // the username was left blank
            {
                Toast.makeText(this, "Please enter a username.", Toast.LENGTH_SHORT).show();
            } else if (email.equals("")) // the email was left blank
            {
                Toast.makeText(this, "Please enter an email address.", Toast.LENGTH_SHORT).show();
            } else if (pw.equals("")) // the password was left blank
            {
                Toast.makeText(this, "Please enter a password.", Toast.LENGTH_SHORT).show();
            } else if (checkName == -1) // the username was too long
            {
                Toast.makeText(this, "Invalid Username. User name cannot contain more than 10 characters ", Toast.LENGTH_SHORT).show();
            } else if (checkName == -2) // the username was too short
            {
                Toast.makeText(this, "Invalid Username. User name cannot contain fewer than 2 characters ", Toast.LENGTH_SHORT).show();
            } else if (checkName == -3) // the username did not contain any letters
            {
                Toast.makeText(this, "Invalid Username. User name must contain at least one letter ", Toast.LENGTH_SHORT).show();
            } else if (checkMail == -1) // the email was not formatted correctly
            {
                Toast.makeText(this, "Invalid Email. Please check the entered email address ", Toast.LENGTH_SHORT).show();
            } else if (checkPass == -1) // the password was too long
            {
                Toast.makeText(this, "Invalid Password. Password cannot contain more than 20 characters ", Toast.LENGTH_SHORT).show();
            } else if (checkPass == -2) // the password was too short
            {
                Toast.makeText(this, "Invalid Password. Password cannot contain fewer than 6 characters", Toast.LENGTH_SHORT).show();
            } else if (checkPass == -3) // the password did not contain any lowercase letters
            {
                Toast.makeText(this, "Invalid Password. Password must contain at least one lowercase letter", Toast.LENGTH_SHORT).show();
            } else if (checkPass == -4) // the password did not contain any uppercase letters
            {
                Toast.makeText(this, "Invalid Password. Password must contain at least one uppercase letter", Toast.LENGTH_SHORT).show();
            } else if (checkPass == -5) // the password did not contain at least one number
            {
                Toast.makeText(this, "Invalid Password. Password must contain at least one number", Toast.LENGTH_SHORT).show();
            } else if (checkPass == -6) // the password did not contain any of the special characters (of which at least one is required)
            {
                Toast.makeText(this, "Invalid Password. Password must contain at least one special character (!, *, %, $, #, &, ?, ^, -, +) ", Toast.LENGTH_LONG).show();
            } else // the data is in an acceptable format
            {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: The account could not be created.", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    // The username must contain at least two characters and at most ten characters. The username must contain at least one letter.
    public int checkUserName(String user)
    {
        if (user.length() > 10) // too long
            return -1;

        else if (user.length() < 2) // too short
            return -2;

        for(int i = 0; i < user.length(); i++)
        {
            char c = user.charAt(i);

            if (Character.isLetter(c))
                return 0;
        }

        return -3; // contains NO letters - not acceptable for user name
    }

    // The email address must have an @ character and a . character.
    public int checkEmail(String user)
    {
        boolean has_an_at = false;
        boolean has_a_dot = false;

        for(int i = 0; i < user.length(); i++)
        {
            char c = user.charAt(i);

            if (c == '@' & has_a_dot == false) // contains an @ before the . for the top level domain
                has_an_at = true;

            if (c == '.' & has_an_at == true) // contain a . before the top level domain after the @
                has_a_dot = true;
        }

        if (has_an_at == true & has_a_dot == true) // the format is correct
            return 0;

        else // there was something missing
            return -1;
    }

    /*
     The passwored must contain at least six characters and at most twenty characters. The username must contain at least one lowercase letter
     one uppercase letter, one number and one special character(!, *, %, $, #, &, ?, ^, -, +)
    */
    public int checkPassword(String user) {
        boolean has_a_lowercase = false; // a to z
        boolean has_an_uppercase = false; // A to Z
        boolean has_a_number = false; // 0 to 9
        boolean has_special_char = false; // !, *, %, $, #, &, ?, ^, -, +

        if (user.length() > 20) // too long
            return -1;

        else if (user.length() < 6) // too short
            return -2;

        for (int i = 0; i < user.length(); i++) {
            char c = user.charAt(i);

            if (Character.isLowerCase(c))
                has_a_lowercase = true;

            if (Character.isUpperCase(c))
                has_an_uppercase = true;

            if (Character.isDigit(c))
                has_a_number = true;

            if (c == '!' || c == '*' || c == '%' || c == '$' || c == '#' || c == '&' || c == '?' || c == '^' || c == '-' || c == '+')
                has_special_char = true;
        }

        if (has_a_lowercase == false) // missing a lowercase character
            return -3;

        if (has_an_uppercase == false) // missing a uppercase character
            return -4;

        if (has_a_number == false) // missing a numeric character
            return -5;

        if (has_special_char == false) // missing a "special character"
            return -6;

        else {
            return 0;
        }
    }
}
