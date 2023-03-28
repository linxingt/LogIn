package com.example.login;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public abstract class Task extends AsyncTask<String, String, String> {
    private final ProgressDialog dialog;

    public Task (ProgressDialog p){
        this.dialog = p;
    }

    protected ProgressDialog getDialogue(){
        return this.dialog;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        this.dialog.setMessage("Patientez svp");
        this.dialog.show();
    }
}
