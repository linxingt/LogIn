package com.example.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowContact extends AppCompatActivity {
    TextView usernom;
    ListView contacts_show;
    Button add, smap;

    ProgressDialog dialog;
    JSONParser parser = new JSONParser();
    ArrayList<HashMap<String, String>> values = new ArrayList<HashMap<String, String>>();
    int succes;

    String unom, unum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        getSupportActionBar().hide();// title bar
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));//color status bar

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            unom = extras.getString("unom");
            unum = extras.getString("unum");

            new Select().execute();
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(ShowContact.this);
            alert.setMessage("required data missing");
            alert.setNeutralButton("ok", null);
            alert.show();
        }

        usernom = findViewById(R.id.usernom);
        contacts_show = findViewById(R.id.contacts_show);

        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), AddContact.class);
                intent.putExtra("unom",unom);
                intent.putExtra("unum",unum);
                startActivity(intent);
            }
        });
        smap = findViewById(R.id.smap);
        smap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Map.class);
                startActivity(intent);
            }
        });
    }

    class Select extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ShowContact.this);
            dialog.setMessage("Patientez svp");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<String, String>();

            map.put("nom", unom);
            map.put("num", unum);

            JSONObject object = parser.makeHttpRequest("http://10.0.2.2/login/show_contact.php", "GET", map);

            Log.d("doInBackgroundShowContact", "JSONObject value: " + object.toString());

            try {
                succes = object.getInt("succes");
                if (succes == 1) {
                    JSONArray contacts = object.getJSONArray("contacts");
                    for (int i = 0; i < contacts.length(); i++) {

                        JSONObject contact = contacts.getJSONObject(i);
                        HashMap<String, String> m = new HashMap<String, String>();
                        m.put("nom", contact.getString("nom"));
                        m.put("prenom", contact.getString("prenom"));
                        m.put("email", contact.getString("email"));

                        values.add(m);
                    }
                    Log.d("doInBackgroundShowContact", "values size: " + values.size());
                }
                else if (succes == 2) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(ShowContact.this, "You have no contacts", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.cancel();
            usernom.setText(unom);
            SimpleAdapter adapter = new SimpleAdapter(ShowContact.this, values, R.layout.showlist_item,
                    new String[]{"nom", "prenom", "email"}, new int[]{R.id.itnom, R.id.itprenom, R.id.itemail});
            contacts_show.setAdapter(adapter);

        }

    }
}
