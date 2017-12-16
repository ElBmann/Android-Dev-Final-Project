package imonoko.androiddevfinalproject;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Nazim Mia on 12/2/2017.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    //user account table
    private static final String DATABASE_NAME = "users";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ACCOUNTS ="User_Accounts";
    private static final String USER_ID = "User_ID";
    private static final String USER_NAME = "User_Name";
    private static final String USER_EMAIL = "User_Email";
    private static final String USER_PASSWORD = "User_Password";
    //Stats table
    private static final String TABLE_STATISTICS ="stats";
    private static final String WINS = "wins";
    private static final String LOSSES= "losses";
    private static final String REROLLS="rerolls";
    private static final String SCORES = "scores";
    private static final String DRAWS = "draws";
    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION );
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sqlCreateTableAcc= "create table " + TABLE_ACCOUNTS + " ( " + USER_ID + " integer primary key autoincrement, ";
        sqlCreateTableAcc+= USER_NAME + " text, " + USER_EMAIL + " text, " + USER_PASSWORD + " text )";
        sqLiteDatabase.execSQL(sqlCreateTableAcc);

        String sqlCreateTableStats= "create table " + TABLE_STATISTICS + " ( " + USER_ID + " integer primary key autoincrement, ";
        sqlCreateTableStats+= WINS + " integer, " + LOSSES + " integer, " + REROLLS + " integer, ";
        sqlCreateTableStats+= SCORES + " integer, " + DRAWS + " integers )";
        sqLiteDatabase.execSQL(sqlCreateTableStats);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int old, int current) {
        sqLiteDatabase.execSQL("drop table if exists "+ TABLE_ACCOUNTS);
        sqLiteDatabase.execSQL("drop table if exists "+ TABLE_STATISTICS);
        onCreate(sqLiteDatabase);
    }

    public void addAccount( Account account ) {
        addStatistics();
        SQLiteDatabase db = this.getWritableDatabase( );
        String sqlInsert = "insert into " + TABLE_ACCOUNTS;
        sqlInsert += " values( null, '" + account.getUserName( ) + "', '";
        sqlInsert += account.getEmail( ) + "', '" +  account.getPassword( ) + "' )";
        db.execSQL( sqlInsert ); // inserts username, email, password
        db.close( );
    }

    public void addStatistics(){
        Statistics stats = new Statistics(0,0,0,0,0,0);
        SQLiteDatabase db = this.getWritableDatabase( );
        String sqlInsert = "insert into " + TABLE_STATISTICS;
        sqlInsert += " values( null, '" + stats.getWins( ) + "', '";
        sqlInsert += stats.getLosses( ) + "', '" + stats.getReRolls( ) + "', '";
        sqlInsert += stats.getScore( ) + "', '" + stats.getDraws( ) + "' )";
        db.execSQL( sqlInsert ); // inserts wins, losses, re-rolls, scores,and draws
        db.close( );
    }

    public void modifyAccount(Account acc){
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlUpdate = "update " + TABLE_ACCOUNTS;
        sqlUpdate += " set " + USER_NAME + " = '" + acc.getUserName()+ "', ";
        sqlUpdate += USER_PASSWORD + " = '" + acc.getPassword() + "', ";
        sqlUpdate += USER_EMAIL + " = '" + acc.getEmail() + "'";
        sqlUpdate += " where " + USER_ID + " = " + acc.getID();

        db.execSQL(sqlUpdate); // updates username, email, password
        db.close();
    }

    public void updateScores(Statistics stats) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlUpdate = "update " + TABLE_STATISTICS;
        sqlUpdate += " set " + WINS + " = '" + stats.getWins() + "'";
        sqlUpdate += ", " + LOSSES + " = '" + stats.getLosses() + "'";
        sqlUpdate += ", " + REROLLS + " = '" + stats.getReRolls() + "'";
        sqlUpdate += ", " + SCORES + " = '" + stats.getScore() + "'";
        sqlUpdate += ", " + DRAWS + " = '" + stats.getDraws() + "'";
        sqlUpdate += " where " + USER_ID + " = " + stats.getID();
        db.execSQL(sqlUpdate); // updates Stats attributes
        db.close();
    }

    public int getIDfromEmail(String mail) {
        SQLiteDatabase db = this.getWritableDatabase( );

        try
        {
            String sqlSearch = "SELECT * FROM " + TABLE_ACCOUNTS;
            sqlSearch += " WHERE User_Email = '" + mail + "'";
            Cursor cursor = db.rawQuery( sqlSearch, null );
            ArrayList<Account> accountList = new ArrayList<Account>( );
            while( cursor.moveToNext( ) )
            {
                Account currentAccount = new Account(cursor.getInt(0),cursor.getString( 1 ),
                        cursor.getString( 2), cursor.getString( 3 ));
                accountList.add( currentAccount );
            }

            if(accountList.size()> 0){
                return accountList.get(0).getID();
            }else{
                return -1;
            }
        }

        catch(SQLException sqlx) // search was unsuccessful
        {
            sqlx.printStackTrace();
            return -1;
        }

        finally
        {
            db.close( );
        }
    }

    public boolean searchForEmail(String mail ) {
        SQLiteDatabase db = this.getWritableDatabase( );

        try // should return true if the email address is already used
        {

            String sqlSearch = "SELECT * FROM " + TABLE_ACCOUNTS;
            sqlSearch += " WHERE User_Email = '" + mail + "'";
            // db.execSQL(sqlSearch); // gets data for entry that has email equal to the parameter, mail

            Cursor cursor = db.rawQuery( sqlSearch, null );

            ArrayList<Account> accountList = new ArrayList<Account>( );
            while( cursor.moveToNext( ) )
            {
                Account currentAccount = new Account(cursor.getInt(0),cursor.getString( 1 ),
                        cursor.getString( 2), cursor.getString( 3 ));
                accountList.add( currentAccount );
            }

            if(accountList.size()> 0){
                return true;
            }else{
                return false;
            }
        }

        catch(SQLException sqlx) // search was unsuccessful
        {
            sqlx.printStackTrace();
            return false;
        }

        finally
        {
            db.close( );
        }
    }

    public boolean searchForPassWord(String pw ) {
        SQLiteDatabase db = this.getWritableDatabase( );

        try // should return true if the email address is already used
        {

            String sqlSearch = "SELECT * FROM " + TABLE_ACCOUNTS;
            sqlSearch += " WHERE USER_PASSWORD = '" + pw + "'";
            // db.execSQL(sqlSearch); // gets data for entry that has email equal to the parameter, mail

            Cursor cursor = db.rawQuery( sqlSearch, null );

            ArrayList<Account> accountList = new ArrayList<Account>( );
            while( cursor.moveToNext( ) )
            {
                Account currentAccount = new Account(cursor.getInt( 0 ), cursor.getString( 1 ), cursor.getString( 2 ),cursor.getString(3));
                accountList.add( currentAccount );
            }

            if(accountList.size()> 0){
                return true;
            }else{
                return false;
            }
        }

        catch(SQLException sqlx) // search was unsuccessful
        {
            sqlx.printStackTrace();
            return false;
        }

        finally
        {
            db.close( );
        }
    }

    public Account searchForAccountbyID(int ID) {
        SQLiteDatabase db = this.getWritableDatabase( );

        try // should return true if the email address is already used
        {

            String sqlSearch = "SELECT * FROM " + TABLE_ACCOUNTS;
            sqlSearch += " WHERE USER_ID = '" + ID + "'";

            Cursor cursor = db.rawQuery( sqlSearch, null );

            ArrayList<Account> accountList = new ArrayList<Account>( );
            while( cursor.moveToNext( ) )
            {
                Account currentAccount = new Account(cursor.getInt( 0 ), cursor.getString( 1 ),
                        cursor.getString( 2 ),cursor.getString(3));
                accountList.add( currentAccount );
            }

            if(accountList.size()> 0){
                return accountList.get(0);
            }else{
                return null;
            }
        }

        catch(SQLException sqlx) // search was unsuccessful
        {
            sqlx.printStackTrace();
            return null;
        }

        finally
        {
            db.close( );
        }
    }

    public Statistics searchForStat(int id) {
        ArrayList<Statistics>stat = selectAllStats();

        Boolean exist=false;
        Statistics s=null;
        try{
            for(int i=0;i<stat.size();i++){
                if(stat.get(i).getID() == id){
                    exist=true;
                    s = stat.get(i);
                    break;
                }
            }
            if(exist) {
                return s;
            } else {
                return null;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Statistics> selectAllStats(){
        // returns all accounts in database
    //Uses an integer value to select either table using '1' or '0'
        String sqlQuery = "select * from " + TABLE_STATISTICS;
        SQLiteDatabase db = this.getWritableDatabase( );
        Cursor cursor = db.rawQuery( sqlQuery, null );

        ArrayList<Statistics> accountList = new ArrayList<Statistics>( );
        while( cursor.moveToNext( ) )
        {
            Statistics currentStats = new Statistics(cursor.getInt( 0 ),cursor.getInt( 1 ),
                    cursor.getInt( 2 ),cursor.getInt( 3 ),cursor.getInt( 4 ),cursor.getInt( 5 ));
            accountList.add( currentStats );
        }
        db.close( );
        return accountList;
    }

    public ArrayList<Account> selectAllAcc( ){ // returns all accounts in database
        String sqlQuery = "select * from " + TABLE_ACCOUNTS;
        SQLiteDatabase db = this.getWritableDatabase( );
        Cursor cursor = db.rawQuery( sqlQuery, null );

        ArrayList<Account> accountList = new ArrayList<Account>( );
        while( cursor.moveToNext( ) )
        {
            Account currentAccount = new Account(cursor.getInt(0),cursor.getString( 1 ),
                    cursor.getString( 2), cursor.getString( 3 ));
            accountList.add( currentAccount );
        }
        db.close( );
        return accountList;
    }

    public String getUser(int userID ){
        String sqlQuery = "select User_Name from " + TABLE_ACCOUNTS;
        sqlQuery +=" where " + USER_ID + " = " + userID;
        SQLiteDatabase db = this.getReadableDatabase( );
        Cursor cursor = db.rawQuery( sqlQuery, null );
        String data = "";
        if (cursor.moveToFirst()){
            do{
                 data = cursor.getString(cursor.getColumnIndex("User_Name"));
                // do what ever you want here
            }while(cursor.moveToNext());
        }



        cursor.close();
        return data;
    }
}