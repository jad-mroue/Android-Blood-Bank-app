package com.example.databaselogin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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

import java.util.HashMap;
import java.util.Map;


public class Register_activity extends AppCompatActivity {
    String[] BloodTypes = {"O+","O-","AB+","AB-","A+","A-","B+","B-"};
    String[] Cities = {"Beirut", "Baalbeck", "Saida", "Jbeil", "Aley", "Tripoli", "Tyre", "Marjeyoun"};
    EditText username, password, phone, email,confirm_password;
    Button Register;
    CheckBox donor;
    Spinner spin,spin2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);
        spinner();
        spinner2();
        spin = findViewById(R.id.spinner);
        spin2 = findViewById(R.id.spinner2);
        Register = findViewById(R.id.button2);
        password = findViewById(R.id.editText2);
        confirm_password = findViewById(R.id.editText7);
        email = findViewById(R.id.editText5);
        username = findViewById(R.id.editText3);
        phone = findViewById(R.id.editText6);
        donor = findViewById(R.id.checkBox);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String blood_type = spin.getSelectedItem().toString().trim();
                String city = spin2.getSelectedItem().toString().trim();
                String pass = password.getText().toString().trim();
                String con_pass = password.getText().toString().trim();
                String email_c = email.getText().toString().trim();
                String name = username.getText().toString().trim();
                String number = phone.getText().toString().trim();
                String is_donor;
                if(!con_pass.equals(pass)){
                    Toast.makeText(Register_activity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                }else {
                    if (donor.isChecked()) {
                        is_donor = "true";
                    } else {
                        is_donor = "false";
                    }
                    User user = new User(name, email_c, pass, city, number, blood_type, is_donor, is_donor);
                    boolean ready = user.CheckAll(name, pass, email_c, number);
                    if (ready) {
                        BackgroundCheck task = new BackgroundCheck();
                        task.execute(user);
                    } else {
                        if(!user.CheckUsername(name))
                            Toast.makeText(Register_activity.this, "Enter a valid username", Toast.LENGTH_SHORT).show();
                        if(!user.CheckPassword(pass))
                            Toast.makeText(Register_activity.this, "Password too short" + pass.length() + "  " + pass, Toast.LENGTH_SHORT).show();
                        if(!user.CheckPhoneNumber(number))
                            Toast.makeText(Register_activity.this, "Phone number invalid", Toast.LENGTH_SHORT).show();
                        if(!user.CheckEmail(email_c))
                            Toast.makeText(Register_activity.this, "Email invalid", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    public void spinner(){
        Spinner spin = findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Register_activity.this,android.R.layout.simple_spinner_item, BloodTypes);
        spin.setAdapter(adapter);
    }
    public void spinner2(){
        Spinner spin = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Register_activity.this,android.R.layout.simple_spinner_item, Cities);
        spin.setAdapter(adapter);
    }
    public class BackgroundCheck extends AsyncTask<User, String, String> {

        public String Insert_data(final User user){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.56.1/register.inc.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        //Toast.makeText(Register_activity.this, response, Toast.LENGTH_LONG).show();
                        String success = jsonObject.getString("success");
                        //Toast.makeText(Register_activity.this, success, Toast.LENGTH_LONG).show();
                        if(success.equals("1")){
                            Toast.makeText(Register_activity.this, "Register Successfully", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(Register_activity.this, success, Toast.LENGTH_LONG).show();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(Register_activity.this, "error "+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Register_activity.this, "error"+error.toString(), Toast.LENGTH_LONG).show();

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
                    parms.put("blood_type", user.getBlood_type());
                    parms.put("city", user.getCity());

                    return parms;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(Register_activity.this);
            requestQueue.add(stringRequest);
            return "true";
        }
        @Override
        protected String doInBackground(User... user) {
            String result = Insert_data(user[0]);
            /*
            Intent intent = new Intent(Register_activity.this, MainActivity.class);
            startActivity(intent);*/
            return result;
            //input info into database
            //intent to login
        }
        @Override
        protected void onPostExecute(String result){
            if(result.equalsIgnoreCase("true")){
                Toast.makeText(Register_activity.this, "Thank you for registering", Toast.LENGTH_LONG).show();
            }else if(result.equalsIgnoreCase("false")){
                Toast.makeText(Register_activity.this, "Invalid username or password",Toast.LENGTH_LONG).show();
            }else if(result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")){
                Toast.makeText(Register_activity.this, result,Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(Register_activity.this, result,Toast.LENGTH_LONG).show();
            }
        }
    }
}
