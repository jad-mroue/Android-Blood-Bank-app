package com.example.databaselogin;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main2Activ extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activ_main2);
        String json;
        String city, bloodgroup;
        Intent intent = getIntent();

        json = intent.getStringExtra("json");
        city = intent.getStringExtra("city");
        bloodgroup = intent.getStringExtra(" bloodgroup");
        TextView textView = findViewById(R.id.t1);
        String ss = "Donor in " + city + " with blood group " + bloodgroup;
        textView.setText(ss);

        List<Donors> donorList = new ArrayList<>();
        Gson gson = new Gson();
        Type type = new TypeToken<List<Donors>>(){}.getType();
        List<Donors> d = gson.fromJson(json, type);


        if(d != null && d.isEmpty()){
           textView.setText("No results");
        }
        else{
           donorList.addAll(d);
        }

        RecyclerView recyclerView = findViewById(R.id.id2);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        SearchAdapter adapter = new SearchAdapter(donorList,Main2Activ.this);
        recyclerView.setAdapter(adapter);
    }
}
