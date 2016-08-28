package com.example.csi_pro.csi_open;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.util.Properties;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        final String contraseña = "cebiche";
        final Intent goToFirst= new Intent();

        Button submit = (Button) findViewById(R.id.button2_entrar);
        goToFirst.setClass(this, FirstActivity.class);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText pass = (EditText) findViewById(R.id.editText);
                if (pass.getText().toString().equals(contraseña)) {
                    Toast.makeText(getApplicationContext(),"Contraseña exitosa",Toast.LENGTH_SHORT).show();
                    new AsyncTask<Integer, Void, Void>(){
                        @Override
                        protected Void doInBackground(Integer... params) {

                            try {
                                executeRemoteCommand("root", "Csi-ProAcces","148.225.50.149", 22);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                    }.execute(1);
                    Toast.makeText(getApplicationContext(),"Esperar hasta que suene la puerta",Toast.LENGTH_LONG).show();
                    pass.setText("");


                } else {
                    Toast.makeText(getApplicationContext(), "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                    pass.setText("");
                }

            }
        });



        //goToFirst.setClass(this, FirstActivity.class);
       // startActivity(goToFirst);


    }
    public static String executeRemoteCommand(String username,String password,String hostname,int port)
            throws Exception {
        JSch jsch = new JSch();
        Session session = jsch.getSession(username, hostname, port);
        session.setPassword(password);

        // Avoid asking for key confirmation
        Properties prop = new Properties();
        prop.put("StrictHostKeyChecking", "no");
        session.setConfig(prop);

        session.connect();

        // SSH Channel
        ChannelExec channelssh = (ChannelExec)
                session.openChannel("exec");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        channelssh.setOutputStream(baos);

        // Execute command
        channelssh.setCommand("abrir");
        channelssh.connect();
        channelssh.disconnect();

        return baos.toString();
    }

}
