package com.example.project;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class addCamera extends AppCompatActivity {
    //Global Variables
    private EditText editTextname;
    private EditText editTextipaddress;
    private EditText editTextUsername;
    private EditText editTextpassword;
    private Button button;
    SQLiteDatabase db;
    String InputName;
    String InputIP;
    String InputUsername;
    String InputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera);

        //create our toolbar for this activity
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try
        {
            openDatabase(); // open (create if needed) database
            CreateTable();  // create table
            db.close();     // make sure to release the DB
            Log.i("Database", "- All Done!\n");
        }
        catch (Exception e)
        {
            Log.e("\n#Error:", " onCreate: " + e.getMessage());
            finish();
        }

        editTextname = findViewById(R.id.name);
        editTextipaddress = findViewById(R.id.ipAddress);
        editTextUsername = findViewById(R.id.username);
        editTextpassword = findViewById(R.id.password);
        button = findViewById(R.id.buttonAddCamera);

        editTextname.addTextChangedListener(loginTextWatcher);
        editTextipaddress.addTextChangedListener(loginTextWatcher);
        editTextUsername.addTextChangedListener(loginTextWatcher);
        editTextpassword.addTextChangedListener(loginTextWatcher);

    }

    /**********************************************************************************************/
    private TextWatcher loginTextWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            /*trim() is used to remove any spaces at the beginning or end of the input.
              Retrieve the data entered into the editText fields and cast to a (string)
            */
            InputName = editTextname.getText().toString().trim();
            InputIP = editTextipaddress.getText().toString().trim();
            InputUsername = editTextUsername.getText().toString().trim();
            InputPassword = editTextpassword.getText().toString().trim();

            //Enable the button once all the editText fields have some data entered
            button.setEnabled(!InputName.isEmpty() && !InputIP.isEmpty() && !InputUsername.isEmpty() && !InputPassword.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    /**********************************************************************************************/


    public void UpdateDatabase(View view)
    {
        //Retrieve the most recent data entered into the editText fields and cast to a (string)
        InputName = editTextname.getText().toString().trim();
        InputIP = editTextipaddress.getText().toString().trim();
        InputUsername = editTextUsername.getText().toString().trim();
        InputPassword = editTextpassword.getText().toString().trim();

        try
        {
            openDatabase();      // open (create if needed) database
            insertData();        // insert data
            db.close();         // make sure to release the DB
            Toast.makeText(this, "Camera Added", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {
            Log.e("\n#Error:"," onCreate: " + e.getMessage());
            Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /**********************************************************************************************/
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
    private void insertData() {
        try {
            // ContentValues is an Android dynamic row-like container
            ContentValues initialValues = new ContentValues();
            initialValues.put("Name", InputName);
            initialValues.put("IPAddress", InputIP);
            initialValues.put("Username", InputUsername);
            initialValues.put("Password", InputPassword);

            int rowPosition = (int) db.insert("results", "Name", initialValues);

            Log.i("\nDatabase", "- Data Inserted to Database Successfully: " + rowPosition);

        }

        catch (Exception e)
        {
            Log.e("\n#Error:", " Sending data to Database: " + e.getMessage());
        }
    }
    /*******************************************************************************************/

    public void RunIPScannerActivity(View view)
    {
        // Create an Intent to call Second Activity
        Intent intent = new Intent(this, IpScanner.class);

        //starts the activity
        startActivity(intent);
    }

    /*******************************************************************************************/

    public void ReturnToMainActivity(View view)
    {
        finish();
    }

    /*******************************************************************************************/
}
