package com.example.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    EditText nom,num;
    Button l,s;
    ProgressDialog dialog;
    JSONParser parser= new JSONParser();
    int succes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nom=findViewById(R.id.nom);
        num=findViewById(R.id.num);
        l=findViewById(R.id.login);
        l.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                new Log().execute();
            }
        });
        s=findViewById(R.id.signup);
        s.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent();
                intent.setClass(getApplicationContext(),Inscription.class);
                startActivity(intent);
            }
        });
    }

    class Log extends AsyncTask<String,String,String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog=new ProgressDialog(MainActivity.this);
            dialog.setMessage("Patientez svp");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            HashMap<String,String>map= new HashMap<>();

            map.put("nom",nom.getText().toString());
            map.put("num",num.getText().toString());

            JSONObject object=parser.makeHttpRequest("http://10.0.2.2/login/login.php","POST",map);

            try {
                succes=object.getInt("succes");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();

            if(succes==1){
                AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("login done successfully");
                alert.setNeutralButton("ok",null);
                alert.show();
            }
            else{
                AlertDialog.Builder alert=new AlertDialog.Builder(MainActivity.this);
                alert.setMessage("nom or password wrong");
                alert.setNeutralButton("ok",null);
                alert.show();
            }
        }
    }
}