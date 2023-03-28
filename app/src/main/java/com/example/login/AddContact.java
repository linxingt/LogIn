package com.example.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Objects;

public class AddContact extends AppCompatActivity {
    private EditText cnom, cprenom, cemail;
    private Button addback, a;

    private JSONParser parser;

    private int succes;

    private String unom, unum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Objects.requireNonNull(getSupportActionBar()).hide();// title bar
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));//color status bar

        this.cnom = findViewById(R.id.cnom);
        this.cprenom = findViewById(R.id.cprenom);
        this.cemail = findViewById(R.id.cemail);
        this.addback = findViewById(R.id.addback);
        this.parser = new JSONParser();
        this.a = findViewById(R.id.addcontact);

        putIntExtras();

        this.addback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ShowContact.class);
                intent.putExtra("unom", unom);
                intent.putExtra("unum", unum);
                startActivity(intent);
            }
        });
        this.a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cnom.getText().length() == 0 || cprenom.getText().length() == 0 ||
                        cemail.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "Tous les champs sont requient", Toast.LENGTH_LONG).show();
                } else {
                    new Add(new ProgressDialog(AddContact.this)).execute();
                }
            }
        });

    }

    private void putIntExtras(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.unom = extras.getString("unom");
            this.unum = extras.getString("unum");
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(AddContact.this);
            alert.setMessage("required data missing");
            alert.setNeutralButton("ok", null);
            alert.show();
        }
    }

    private class Add extends Task{

        public Add(ProgressDialog p) {
            super(p);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<>();

            map.put("nom", unom);
            map.put("num", unum);
            map.put("cnom", cnom.getText().toString());
            map.put("cprenom", cprenom.getText().toString());
            map.put("cemail", cemail.getText().toString());

            JSONObject object = parser.makeHttpRequest("http://10.0.2.2/login/add_contact.php", "GET", map);

            try {
                succes = object.getInt("succes");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            super.getDialogue().cancel();

            if (succes == 1) {
                Toast.makeText(AddContact.this, "Contact added successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ShowContact.class);
                intent.putExtra("unom", unom);
                intent.putExtra("unum", unum);
                startActivity(intent);
            } else if (succes == 2) {
                Toast.makeText(AddContact.this, "Contact already existing", Toast.LENGTH_LONG).show();
            } else if (succes == 3) {
                Toast.makeText(AddContact.this, "This person doesn't exist", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AddContact.this, "FAIL !!!!", Toast.LENGTH_LONG).show();
            }
        }
    }
}