package com.example.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;

public class ShowContact extends AppCompatActivity {
    TextView usernom,contacts_show;
    Button add,smap,scontacts;

    String response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        getSupportActionBar().hide();// title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// statu bar

        usernom=findViewById(R.id.usernom);
        contacts_show=findViewById(R.id.contacts_show);

        add=findViewById(R.id.add);
        smap=findViewById(R.id.smap);
        scontacts=findViewById(R.id.scontacts);
    }



}
