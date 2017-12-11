package imonoko.androiddevfinalproject;

/**
 * Created by Nazim on 12/2/2017.
 */

public class Account
{
    private int id;
    private String userName;
    private String email;
    private String password;

    Account(int id, String uN, String eM, String pW)
    {
        this.id=id;
        setUserName(uN) ;
        setEmail(eM);
        setPassword(pW);
    }

    public int getID()
    {return id;}

    public String getUserName()
    {
        return userName;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setUserName(String uN)
    {
        userName = uN;
    }

    public void setEmail(String eM)
    {
        email = eM;
    }

    public void setPassword(String pW)
    {
        password = pW;
    }

    public String toString()
    {
        return "UserName: " + getUserName() + " (Email: " + getEmail() + ") ";
    }
}
