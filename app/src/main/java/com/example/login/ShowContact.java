package com.example.login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
    Button add, showback, locamap;

    ProgressDialog dialog;
    JSONParser parser = new JSONParser();
    JSONParser parserd = new JSONParser();
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
                intent.putExtra("unom", unom);
                intent.putExtra("unum", unum);
                startActivity(intent);
            }
        });
        showback = findViewById(R.id.showback);
        showback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        locamap = findViewById(R.id.locamap);
        locamap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Map.class);
                startActivity(intent);
            }
        });
    }

    class Delete extends AsyncTask<String, String, String> {
        AdapterView<?> parent;
        int position;

        Delete(AdapterView<?> parent,int position) {
            this.parent = parent;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ShowContact.this);
            dialog.setMessage("Patientez svp");
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> map = new HashMap<>();
            HashMap<String, String> data = (HashMap<String, String>) parent.getItemAtPosition(position);
            map.put("nom", unom);
            map.put("num", unum);
            map.put("cnom", data.get("nom"));
            map.put("cprenom", data.get("prenom"));
            map.put("cemail", data.get("email"));

            JSONObject object = parserd.makeHttpRequest("http://10.0.2.2/login/delete_contact.php", "GET", map);

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
                Toast.makeText(ShowContact.this, "c fait", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ShowContact.class);
                intent.putExtra("unom",unom);
                intent.putExtra("unum",unum);
                startActivity(intent);
            } else {
                Toast.makeText(ShowContact.this, "echec!!!!", Toast.LENGTH_LONG).show();
            }
        }
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
                } else if (succes == 2) {
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

            contacts_show.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    new AlertDialog.Builder(ShowContact.this).setMessage("Do you want to delete this contact?")
                            .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    new Delete(parent,position).execute();
                                }
                            })
                            .setNeutralButton("NO", null).show();
                    return true;
                }
            });
        }

    }
}
