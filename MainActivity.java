/**
 * Author:  Thomas Tully
 * Published: 17/03/19
 * This App was designed for my final year of college as part of my Final year project.
 * Any code that that i did not write and have found useful for my project i have referenced
 * in the "README" file attached to this project.
 */

package com.example.project;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
{
    //Global Variables
    String name;
    String ip;
    int rowCount;
    Button camera1;
    Button camera2;
    SQLiteDatabase db;
    TextView txtMsg;
    List<Camera> IPcameras = new ArrayList<>();

    /**********************************************************************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create our toolbar for this activity
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Hide the buttons
        camera1 = (Button) findViewById(R.id.buttonCamera1);
        camera1.setVisibility(View.INVISIBLE);
        camera2 = (Button) findViewById(R.id.buttonCamera2);
        camera2.setVisibility(View.INVISIBLE);

        try
        {
            openDatabase();             // open (create (if needed) database
            CreateTable();              // create table (if needed)
            CheckDatabaseForEntries();  //check for entries
            db.close();                 // make sure to release the DB
            Log.i("Database", "Opened and checked for Entries\n");
        }
        catch (Exception e)
        {
            Log.e("\n#Error:", " onCreate: Opening database or checking for entries " + e.getMessage());
            finish();
        }
    }

    /**********************************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch(item.getItemId())
        {
            case R.id.settings:
                Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.info:
                Toast.makeText(this, "Info Selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**********************************************************************************************/

    public void RunAddCameraActivity(View view)
    {
        // Create an Intent to call Second Activity
        Intent intent = new Intent(this, addCamera.class);

        //starts the activity
        startActivity(intent);
    }

    /***********************************************************************************************/

    private void CheckDatabaseForEntries()
    {
        String count = "select count(*) from results";
        Cursor mcursor = db.rawQuery(count, null);
        mcursor.moveToFirst();
        rowCount = mcursor.getInt(0);


        //If the table has data, grab the data
        if(rowCount>0)
        {
            QueryDatabase();
            if (rowCount ==1)
            {
                String name1;
                name1 = IPcameras.get(0).getName();
                camera1.setVisibility(View.VISIBLE);
                camera1.setText(name1);
            }
            else
            {
                String name1;
                String name2;
                name1 = IPcameras.get(0).getName();
                name2 = IPcameras.get(1).getName();
                camera1.setVisibility(View.VISIBLE);
                camera1.setText(name1);
                camera2.setVisibility(View.VISIBLE);
                camera2.setText(name2);
            }
        }
        else
        {
            Log.i("\nDatabase", "- Query: Database Empty");
        }

    }

    /***********************************************************************************************/

    private void openDatabase()
    {
        try
        {
            // path to private memory:
            String MemoryPath = "data/data/com.example.project";

            String myDbPath = MemoryPath + "/" + "IPCameras.db";
            Log.i("\nDatabase", "-DB Path: " + myDbPath);

            db = SQLiteDatabase.openDatabase(myDbPath, null,
                    SQLiteDatabase.CREATE_IF_NECESSARY);

            Log.i("\nDatabase", "- Database was opened");
        }
        catch (SQLiteException e)
        {
            Log.e("\n#Error:", "Opening Database: " + e.getMessage());
            finish();
        }
    }

    /**********************************************************************************************/

    private void CreateTable()
    {
        db.beginTransaction();
        try
        {
            // create table
            db.execSQL("create table if not exists results ("
                    + " recID integer PRIMARY KEY autoincrement, "
                    + " Name  TEXT, " + " IPAddress TEXT, " + " Username TEXT, " + " Password TEXT);  ");
            // commit your changes
            db.setTransactionSuccessful();

            Log.i("\nDatabase", "- Table was created");
        }

        catch (SQLException e1)
        {
            Log.e("\n#Error:", "Creating Table: " + e1.getMessage());
            finish();
        }

        finally
        {
            db.endTransaction();
        }
    }

    /*******************************************************************************************/

    private void QueryDatabase()
    {
        try
        {
            String mySQL = "select * from results";
            Cursor c1 = db.rawQuery(mySQL,null);
            IPcameras = getCameras(c1);
        }
        catch (Exception e)
        {
            Log.e("\n#Error: ", "QueryDatabase(): " + e.getMessage());
        }
    }

    /**********************************************************************************************/

    private List<Camera> getCameras(Cursor cursor)
    {
        cursor.moveToPosition(-1); //reset cursor's top

        List<Camera> cameras = new ArrayList<>();
        try
        {
            // now get the rows
            cursor.moveToPosition(-1); //reset cursor's top
            while (cursor.moveToNext())
            {
                for (int i = 0; i < cursor.getColumnCount(); i++)
                {
                    name = cursor.getString(1);
                    ip = cursor.getString(2);
                }
                cameras.add(new Camera(name, ip));
            }
        }
        catch (Exception e)
        {
            Log.e("\n#Error:", "Retrieving info from database: " + e.getMessage());
        }
        return cameras;
    }

    /**********************************************************************************************/

    public void StreamIpCam1(View view)
    {
        // Create Intent to call Second Activity
        Intent intent = new Intent(this, ViewIPCameras.class);

        //Create a Bundle (MAP) container to ship data
        Bundle DataToSend = new Bundle();

        DataToSend.putString("name", IPcameras.get(0).getName());
        DataToSend.putString("ip", IPcameras.get(0).getIp());

        //attach the container to the intent
        intent.putExtras(DataToSend);

        //starts the activity
        startActivity(intent);
    }

    public void StreamIpCam2(View view)
    {
        // Create Intent to call Second Activity
        Intent intent = new Intent(this, ViewIPCameras.class);

        //Create a Bundle (MAP) container to ship data
        Bundle DataToSend = new Bundle();

        DataToSend.putString("name", IPcameras.get(1).getName());
        DataToSend.putString("ip", IPcameras.get(1).getIp());

        //attach the container to the intent
        intent.putExtras(DataToSend);

        //starts the activity
        startActivity(intent);
    }
}
