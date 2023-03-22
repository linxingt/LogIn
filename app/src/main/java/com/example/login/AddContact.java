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

public class AddContact extends AppCompatActivity {
    EditText cnom, cprenom, cemail;
    Button a;

    ProgressDialog dialog;
    JSONParser parser = new JSONParser();

    int succes;

    String unom, unum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        getSupportActionBar().hide();// title bar
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));//color status bar

        cnom = findViewById(R.id.cnom);
        cprenom = findViewById(R.id.cprenom);
        cemail = findViewById(R.id.cemail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            unom = extras.getString("unom");
            unum = extras.getString("unum");
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(AddContact.this);
            alert.setMessage("required data missing");
            alert.setNeutralButton("ok", null);
            alert.show();
        }

        a = findViewById(R.id.addcontact);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cnom.getText().length() == 0 || cprenom.getText().length() == 0 ||
                        cemail.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "tous les champs sont obligatoire", Toast.LENGTH_LONG).show();
                } else {

                        new Add().execute();
                }
            }
        });

    }

    class Add extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(AddContact.this);
            dialog.setMessage("Patientez svp");
            dialog.show();
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
            dialog.cancel();

            if (succes == 1) {
                Toast.makeText(AddContact.this, "c fait, allez voir", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ShowContact.class);
                intent.putExtra("unom",unom);
                intent.putExtra("unum",unum);
                startActivity(intent);
            }else if (succes == 2) {
                Toast.makeText(AddContact.this, "contact already existe", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(AddContact.this, "echec!!!!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
