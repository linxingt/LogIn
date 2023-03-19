package com.example.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Inscription extends AppCompatActivity {
    EditText ins_nom,ins_prenom,ins_email,ins_num,ins_numc;
    Button v,b;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription);

        getSupportActionBar().hide();// title bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);// statu bar

        ins_nom=findViewById(R.id.ins_nom);
        ins_prenom=findViewById(R.id.ins_prenom);
        ins_email=findViewById(R.id.ins_email);
        ins_num=findViewById(R.id.ins_num);
        ins_numc=findViewById(R.id.ins_numc);

        v=findViewById(R.id.validate);
        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(ins_nom.getText().length()==0||ins_prenom.getText().length()==0||
                        ins_email.getText().length()==0||ins_num.getText().length()==0||
                        ins_numc.getText().length()==0){
                    Toast.makeText(getApplicationContext(),
                            "tous les champs sont obligatoire",Toast.LENGTH_LONG).show();
                }else {
                    //new Inscription.valide().execute();//pas encore fait
                }
            }
        });
        b=findViewById(R.id.back);
        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent=new Intent();
                intent.setClass(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

    }
}

//SHA 1 pour map
//    Type de fichier de clés : PKCS12
//        Fournisseur de fichier de clés : SUN
//
//        Votre fichier de clés d'accès contient 1 entrée
//
//        Nom d'alias : androiddebugkey
//        Date de création : 16 févr. 2023
//        Type d'entrée : PrivateKeyEntry
//        Longueur de chaîne du certificat : 1
//        Certificat[1]:
//        Propriétaire : C=US, O=Android, CN=Android Debug
//        Emetteur : C=US, O=Android, CN=Android Debug
//        Numéro de série : 1
//        Valide du Thu Feb 16 09:11:28 CET 2023 au Sat Feb 08 09:11:28 CET 2053
//        Empreintes du certificat :
//        SHA 1: 63:E4:9F:C7:DD:37:D5:95:C4:77:CE:0E:4B:84:3F:DF:8F:E1:E4:CA
//        SHA 256: 0D:A1:68:10:82:29:9E:4F:90:82:48:54:A9:07:16:FA:B5:67:05:DA:C4:61:6B:49:E8:A0:21:FE:3F:62:03:70
//        Nom de l'algorithme de signature : SHA256withRSA
//        Algorithme de clé publique du sujet : Clé RSA 2048 bits
//        Version : 1

