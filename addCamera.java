package com.example.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

public class addCamera extends AppCompatActivity {
    private EditText editTextname;
    private EditText editTextipaddress;
    private EditText editTextUsername;
    private EditText editTextpassword;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_camera);

        editTextname = findViewById(R.id.name);
        editTextipaddress = findViewById(R.id.ipAddress);
        editTextUsername = findViewById(R.id.username);
        editTextpassword = findViewById(R.id.password);
        button = findViewById(R.id.buttonAddCamera);

        editTextname.addTextChangedListener(loginTextWatcher);
        editTextipaddress.addTextChangedListener(loginTextWatcher);
        editTextUsername.addTextChangedListener(loginTextWatcher);
        editTextpassword.addTextChangedListener(loginTextWatcher);

        // Get the Intent continuing the Bundle sent from MainActivity
        Intent myLocalIntent= getIntent();

        // Look at the Bundle sent from the MainActivity and update (textview)
        Bundle BundleReceived = myLocalIntent.getExtras();


    }

    /**********************************************************************************************/
    private TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //trim() is used to remove any spaces at the beginning or end of the input
            String InputName = editTextname.getText().toString().trim();
            String InputIP = editTextipaddress.getText().toString().trim();
            String InputUsername = editTextUsername.getText().toString().trim();
            String InputPassword = editTextpassword.getText().toString().trim();

            button.setEnabled(!InputName.isEmpty() && !InputIP.isEmpty() && !InputUsername.isEmpty() && !InputPassword.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    /**********************************************************************************************/
}
