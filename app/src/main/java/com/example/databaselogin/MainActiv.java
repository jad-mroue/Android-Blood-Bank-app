package com.example.databaselogin;


import androidx.appcompat.app.AppCompatActivity;
import android.app.DownloadManager;
import android.app.VoiceInteractor;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.UserHandle;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.net.sip.SipSession;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class MainActiv extends AppCompatActivity {
    EditText city;
    EditText bdg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activ_main);
        Button find = findViewById(R.id.button);
        city = findViewById(R.id.editText);
        bdg = findViewById(R.id.editText2);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bldgroup = bdg.getText().toString();
                String cty = city.getText().toString();
                if (isValid(bldgroup, cty)) {
                    getsearchresult(bldgroup,cty);
                }

            }
        });

    }
    private boolean isValid(String bldgroup, String cty) {
        List<String> bloodgroups = new ArrayList<>();
        bloodgroups.add("O+");
        bloodgroups.add("O-");
        bloodgroups.add("AB+");
        bloodgroups.add("AB-");
        bloodgroups.add("A+");
        bloodgroups.add("A-");
        bloodgroups.add("B+");
        bloodgroups.add("B-");
        if (!bloodgroups.contains(bldgroup)) {

           getMessage("Invalid Blood Group" + bloodgroups);
            return false;
        } else if (cty.isEmpty()) {
            getMessage("Enter City");
            return false;

        }
        return true;
    }

 private void getsearchresult(final String bldgroup,final String cty){
        StringRequest sr = new StringRequest(Request.Method.POST, "http://192.168.56.1/search.php", new Response.Listener<String>() {
            public void onResponse(String response) {
                /*try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success.equals("1")){
                        getMessage("Successful search");*/
                        Intent intent= new Intent(MainActiv.this, Main2Activ.class);
                        intent.putExtra("city", cty);
                        intent.putExtra("bloodgroup", bldgroup);
                        intent.putExtra("json", response);
                        startActivity(intent);
                    /*}
                    else{
                        getMessage(success);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    getMessage(e.getMessage());
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActiv.this, "error " + error.toString(), Toast.LENGTH_SHORT).show();
            }
        })

        {
            protected Map<String, String> getParams() {
                Map<String, String> m = new HashMap<String, String>();
                m.put("city", cty);
                m.put("bloodgroup", bldgroup);
                return m;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(MainActiv.this);
        requestQueue.add(sr);
    }

    public void getMessage(String m){
        Toast.makeText(MainActiv.this, m, Toast.LENGTH_SHORT).show();
    }
}
