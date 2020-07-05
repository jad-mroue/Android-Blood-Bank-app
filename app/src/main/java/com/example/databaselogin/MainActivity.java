package com.example.databaselogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button login = findViewById(R.id.button);
        final EditText user = findViewById(R.id.editText);
        final EditText pass = findViewById(R.id.editText2);
        final TextView txt = findViewById(R.id.textView);
        final CheckBox RememberMe = findViewById(R.id.checkBox2);
        final SharedPreferences sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        if(!sharedPreferences.getString("username", "").equals("")){
            user.setText(sharedPreferences.getString("username", ""));
            pass.setText(sharedPreferences.getString("password", ""));
            RememberMe.setChecked(true);
            login.callOnClick();
        }

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register_activity.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString();
                String password = pass.getText().toString();

                if(RememberMe.isChecked()){
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", username).apply();
                    editor.putString("password", password).apply();
                }
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute(username, password);
                Intent intent = new Intent(MainActivity.this, UseProfile.class);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                startActivity(intent);
            }
        });
    }
    public class AsyncTaskRunner extends AsyncTask<String, String, String> {

        URL url;
        HttpURLConnection conn;
        @Override
        protected String doInBackground(String... strings) {
            try {
                url = new URL("http://192.168.56.1/login.inc.php");
            }catch(MalformedURLException e){
                e.printStackTrace();
                return "exception";
            }
            try{
                conn = (HttpURLConnection)url.openConnection();
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT );
                conn.setRequestMethod("POST");

                conn.setDoInput(true);
                conn.setDoOutput(true);

                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter("username", strings[0])
                        .appendQueryParameter("password", strings[1]);
                String query = builder.build().getEncodedQuery();

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
            }catch(IOException e){
                e.printStackTrace();
                return "exception";
            }
            try{
                int response_code = conn.getResponseCode();
                if(response_code == HttpURLConnection.HTTP_OK) {
                    InputStream input = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }
                    return "true";
                }
                else{
                    return("unsuccessful");
                }
            }catch(Exception r){
                r.printStackTrace();
                return "exception";
            }finally{
                conn.disconnect();
            }
        }
        @Override
        protected void onPostExecute(String result){
            if(result.equalsIgnoreCase("true")){
                Toast.makeText(MainActivity.this, "done",Toast.LENGTH_LONG).show();
            }else if(result.equalsIgnoreCase("false")){
                Toast.makeText(MainActivity.this, "Invalid username or password",Toast.LENGTH_LONG).show();
            }else if(result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")){
                Toast.makeText(MainActivity.this, result,Toast.LENGTH_LONG).show();
            }
        }
    }
}
