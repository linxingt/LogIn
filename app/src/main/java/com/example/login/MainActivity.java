package com.example.login;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private EditText nom, num;
    private Button l, s;
    private JSONParser parser = new JSONParser();
    private int succes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();// title bar
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));//color status bar

        this.nom = findViewById(R.id.nom);
        this.num = findViewById(R.id.num);

        this.l = findViewById(R.id.login);
        this.s = findViewById(R.id.signup);

        this.l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nom.getText().length() == 0 || num.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            "tous les champs sont obligatoire", Toast.LENGTH_LONG).show();
                } else {
                    new Log(new ProgressDialog(MainActivity.this)).execute();
                }
            }
        });

        this.s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Inscription.class);
                startActivity(intent);
            }
        });


    }

    private class Log extends Task {

        public Log(ProgressDialog p) {
            super(p);
        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String, String> map = new HashMap<>();

            map.put("nom", nom.getText().toString());
            map.put("num", num.getText().toString());

            JSONObject object = parser.makeHttpRequest("http://10.0.2.2/login/login.php", "POST", map);

            try {
                succes = object.getInt("succes");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            super.getDialogue().cancel();

            if (succes == 1) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ShowContact.class);
                intent.putExtra("unom", nom.getText().toString());
                intent.putExtra("unum", num.getText().toString());
                startActivity(intent);

            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("nom or password wrong");
                alert.setNeutralButton("ok", null);
                alert.show();
            }
        }
    }
}