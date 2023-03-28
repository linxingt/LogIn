package com.example.login;

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

public class Inscription extends AppCompatActivity {
    private EditText ins_nom, ins_prenom, ins_email, ins_num, ins_numc;
    private Button v, b;
    private JSONParser parser;

    private int succes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        getSupportActionBar().hide();// title bar
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));//color status bar

        this.ins_nom = findViewById(R.id.ins_nom);
        this.ins_prenom = findViewById(R.id.ins_prenom);
        this.ins_email = findViewById(R.id.ins_email);
        this.ins_num = findViewById(R.id.ins_num);
        this.ins_numc = findViewById(R.id.ins_numc);
        this.parser = new JSONParser();

        this.v = findViewById(R.id.validate);
        this.b = findViewById(R.id.back);

        this.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOneFiledEmpty()) {
                    Toast.makeText(
                            getApplicationContext(),
                            "tous les champs sont obligatoire",
                            Toast.LENGTH_LONG).show();
                } else {

                    if (ins_num.getText().toString().equals(ins_numc.getText().toString()))
                        new Add(new ProgressDialog(Inscription.this)).execute();
                    else
                        Toast.makeText(getApplicationContext(),
                                "verifiez votre mdp", Toast.LENGTH_LONG).show();
                }
            }
        });

        this.b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }

    private boolean isOneFiledEmpty(){
        return this.ins_nom.getText().length() == 0 || this.ins_prenom.getText().length() == 0 ||
                this.ins_email.getText().length() == 0 || this.ins_num.getText().length() == 0 ||
                this.ins_numc.getText().length() == 0;
    }

    private class Add extends Task{

        public Add(ProgressDialog p) {
            super(p);
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<>();

            map.put("nom", ins_nom.getText().toString());
            map.put("prenom", ins_prenom.getText().toString());
            map.put("email", ins_email.getText().toString());
            map.put("num", ins_num.getText().toString());

            JSONObject object = parser.makeHttpRequest("http://10.0.2.2/login/add_user.php", "GET", map);

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
                Toast.makeText(Inscription.this, "c fait, allez login", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(Inscription.this, "echec!!!!", Toast.LENGTH_LONG).show();
            }
        }
    }
}