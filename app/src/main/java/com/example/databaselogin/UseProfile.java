package com.example.databaselogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UseProfile extends AppCompatActivity {
    String isCheck;
    String[] Cities = {"Beirut", "Baalbeck", "Saida", "Jbeil", "Aley", "Tripoli", "Tyre", "Marjeyoun"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_profile);
        final Switch ava_sw = findViewById(R.id.switch1);
        TextView txt = findViewById(R.id.textView4);
        final EditText email = findViewById(R.id.editText8);
        final EditText phone_num = findViewById(R.id.editText9);
        Button SaveChanges = findViewById(R.id.button3);
        Button Search = findViewById(R.id.button4);
        final Spinner city = findViewById(R.id.spinner3);
        City_spinner();


        Intent intent = getIntent();
        final String username = intent.getStringExtra("username");
        final String password = intent.getStringExtra("password");
        getInfo(username, password);
        txt.setText("Username: "+ username);

        SaveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedEmail = email.getText().toString().trim();
                String updatedPhoneNum = phone_num.getText().toString().trim();
                String updatedCity = city.getSelectedItem().toString().trim();
                if(ava_sw.isChecked())
                    isCheck = "true";
                else
                    isCheck = "false";
                User user = new User(username, updatedEmail, password, updatedCity, updatedPhoneNum, isCheck, isCheck);
                boolean ready = user.CheckAll(username, password, updatedEmail, updatedPhoneNum);
                if (ready) {
                    Update_data(user);
                } else {
                    if(!user.CheckUsername(username))
                        Toast.makeText(UseProfile.this, "Enter a valid username", Toast.LENGTH_SHORT).show();
                    if(!user.CheckPhoneNumber(updatedPhoneNum))
                        Toast.makeText(UseProfile.this, "Phone number invalid", Toast.LENGTH_SHORT).show();
                    if(!user.CheckEmail(updatedEmail))
                        Toast.makeText(UseProfile.this, "Email invalid", Toast.LENGTH_SHORT).show();
                }
            }

        });
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UseProfile.this, MainActiv.class);
                startActivity(intent);
            }
        });
    }
    public void City_spinner(){
        Spinner spin = findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UseProfile.this,android.R.layout.simple_spinner_item, Cities);
        spin.setAdapter(adapter);
    }
    public String getInfo(final String username, final String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.56.1/jsontest.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    TextView txt2 = findViewById(R.id.textView5);
                    EditText email = findViewById(R.id.editText8);
                    EditText phone_num = findViewById(R.id.editText9);
                    Switch ava_sw = findViewById(R.id.switch1);

                    JSONObject jsonObject = new JSONObject(response);
                    String[] GLOBAL_ATTR = new String[5];
                    GLOBAL_ATTR[0] = jsonObject.getString("phone_number");
                    GLOBAL_ATTR[1] = jsonObject.getString("blood_type");
                    GLOBAL_ATTR[2] = jsonObject.getString("email");
                    GLOBAL_ATTR[3] = jsonObject.getString("available");

                    txt2.setText("Blood Type: "+GLOBAL_ATTR[1]);
                    email.setText(GLOBAL_ATTR[2]);
                    phone_num.setText(GLOBAL_ATTR[0]);

                    if(GLOBAL_ATTR[3].equals("true")){
                        ava_sw.setChecked(true);
                    }
                    else{
                        ava_sw.setChecked(false);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(UseProfile.this, "error"+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UseProfile.this, "error"+error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> parms = new HashMap<String, String>();
                parms.put("username", username);
                parms.put("password", password);
                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(UseProfile.this);
        requestQueue.add(stringRequest);
        return "true";
    }
    public String Update_data(final User user){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.56.1/Update.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success.equals("1")){
                        Toast.makeText(UseProfile.this, "Register Successfully", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(UseProfile.this, success, Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                    Toast.makeText(UseProfile.this, "error "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UseProfile.this, "error"+error.toString(), Toast.LENGTH_LONG).show();

            }
        }){
            @Override
            protected Map<String, String> getParams(){
                Map<String,String> parms = new HashMap<String, String>();
                parms.put("username", user.getUsername());
                parms.put("password", user.getPassword());
                parms.put("email", user.getEmail());
                parms.put("donor", user.getIs_donor());
                parms.put("available", user.getIs_available());
                parms.put("phone_number", user.getPhone_number());
                parms.put("city", user.getCity());

                return parms;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(UseProfile.this);
        requestQueue.add(stringRequest);
        return "true";
    }
}
