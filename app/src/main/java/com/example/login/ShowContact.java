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
    private TextView usernom;
    private ListView contacts_show;
    private Button add, showback, locamap;

    private JSONParser parser = new JSONParser();
    private JSONParser parserd = new JSONParser();
    private ArrayList<HashMap<String, String>> values = new ArrayList<HashMap<String, String>>();
    private int succes;

    private String unom, unum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        getSupportActionBar().hide();// title bar
        getWindow().setStatusBarColor(getResources().getColor(R.color.black));//color status bar


        this.usernom = findViewById(R.id.usernom);
        this.contacts_show = findViewById(R.id.contacts_show);
        this.showback = findViewById(R.id.showback);
        this.add = findViewById(R.id.add);
        this.locamap = findViewById(R.id.locamap);

        putInExtras();

        this.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), AddContact.class);
                intent.putExtra("unom", unom);
                intent.putExtra("unum", unum);
                startActivity(intent);
            }
        });

        this.showback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        this.locamap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), Map.class);
                intent.putExtra("unom", unom);
                intent.putExtra("unum", unum);
                startActivity(intent);
            }
        });
    }

    public void putInExtras(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.unom = extras.getString("unom");
            this.unum = extras.getString("unum");

            new Select(new ProgressDialog(ShowContact.this)).execute();
        } else {
            AlertDialog.Builder alert = new AlertDialog.Builder(ShowContact.this);
            alert.setMessage("required data missing");
            alert.setNeutralButton("ok", null);
            alert.show();
        }
    }

    private class Delete extends Task{
        AdapterView<?> parent;
        int position;

        Delete(AdapterView<?> parent, int position, ProgressDialog p) {
            super(p);
            this.parent = parent;
            this.position = position;
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
            super.getDialogue().cancel();

            if (succes == 1) {
                Toast.makeText(ShowContact.this, "c fait", Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), ShowContact.class);
                intent.putExtra("unom", unom);
                intent.putExtra("unum", unum);
                startActivity(intent);
            } else {
                Toast.makeText(ShowContact.this, "echec!!!!", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class Select extends Task{

        public Select(ProgressDialog p) {
            super(p);
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
            super.getDialogue().cancel();
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
                                    new Delete(parent, position, new ProgressDialog(ShowContact.this)).execute();
                                }
                            })
                            .setNeutralButton("NO", null).show();
                    return true;
                }
            });
        }

    }
}