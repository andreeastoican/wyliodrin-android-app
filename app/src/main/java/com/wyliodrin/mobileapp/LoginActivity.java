package com.wyliodrin.mobileapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.wyliodrin.mobileapp.api.ServerConnection;

public class LoginActivity extends ActionBarActivity {

    private ProgressDialog progresDialog;
    private SharedPreferences shPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button qrcodeButton = (Button) findViewById(R.id.qrcode_button);

        qrcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator.initiateScan(LoginActivity.this);
            }
        });

        shPref = getSharedPreferences("login", MODE_PRIVATE);
        String user = shPref.getString("user", "");
        String password = shPref.getString("password", "");

        if (!user.isEmpty() && !password.isEmpty()) {
            new LoginThread(user, password).start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String token = scanResult.getContents();
            String user = "stoican.and@wyliodrin.org";
            String password = "cocadiwolo";

            new LoginThread(user, password).start();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoginThread extends Thread {

        private String user;
        private String password;

        LoginThread(String user, String password) {
            this.user = user;
            this.password = password;

            progresDialog = ProgressDialog.show(LoginActivity.this, "Please wait", "Logging in", true, false);
        }

        @Override
        public void run() {
            super.run();

            final ServerConnection.LoginResult result = ServerConnection.getInstance().connect(user, password);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(LoginActivity.this, result.toString(), Toast.LENGTH_SHORT).show();
                    if (progresDialog != null) {
                        progresDialog.dismiss();
                        progresDialog = null;
                    }

                    if (result.equals(ServerConnection.LoginResult.Success)) {
                        SharedPreferences.Editor editor = shPref.edit();
                        editor.putString("user", user);
                        editor.putString("password", password);
                        editor.commit();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }
}
