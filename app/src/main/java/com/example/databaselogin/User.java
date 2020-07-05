package com.example.databaselogin;

import android.widget.Toast;

import com.example.databaselogin.Register_activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    private String username;
    private String email;
    private String password;
    private String city;
    private String phone_number;
    private String Blood_type;
    private String is_donor;
    private String is_available;

    public User(String username, String email, String password, String city, String phone_number, String blood_type, String is_donor, String is_available) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.city = city;
        this.phone_number = phone_number;
        Blood_type = blood_type;
        this.is_donor = is_donor;
        this.is_available = is_available;
    }
    public User(String username, String email, String password, String city, String phone_number, String is_donor, String is_available) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.city = city;
        this.phone_number = phone_number;
        this.is_donor = is_donor;
        this.is_available = is_available;
    }


    public boolean CheckAll(String name, String password, String email, String phone_number){
        if(CheckEmail(email) && CheckPassword(password) && CheckPhoneNumber(phone_number) && CheckUsername(name)){
            return true;
        }
        return false;
    }

    public boolean CheckPassword(String pass) {
        if(pass.length() > 8) {
            return true;
        }
        return false;
    }

    public boolean CheckEmail(String email_c) {
       // String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
         //       + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern p = Pattern.compile(
                "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$"
        );

       ;
        if( p.matcher(email_c).matches() || email_c.equals(null)) {
            return true;
        }
        return true;
    }

    public boolean CheckUsername(String name) {
        if(name.equals(null) || name.length() > 25) {
            return false;
        }
        return true;
    }

    public boolean CheckPhoneNumber(String number) {
        if(number.length() != 8) {
            //Toast.makeText(Register_activity.this, "Check phone number", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBlood_type() {
        return Blood_type;
    }

    public void setBlood_type(String blood_type) {
        Blood_type = blood_type;
    }

    public String getIs_donor() {
        return is_donor;
    }

    public void setIs_donor(String is_donor) {
        this.is_donor = is_donor;
    }

    public String getIs_available() {
        return is_available;
    }

    public void setIs_available(String is_available) {
        this.is_available = is_available;
    }
}
