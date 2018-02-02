package com.example.kennwu.memorableplaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //static allows variable to be accessible from anywhere in the app
    //static variables must be defined in the class itself
    static ArrayList<String> places = new ArrayList<>();
    static ArrayList<LatLng> locations = new ArrayList<>();
    static ArrayAdapter arrayAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);



        //restoring from storage
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.kennwu.memorableplaces", Context.MODE_PRIVATE);

        ArrayList<String> latitudes = new ArrayList<>();
        ArrayList<String> longitudes = new ArrayList<>();

        //need to empty all arrraylist
        places.clear();
        latitudes.clear();
        longitudes.clear();
        locations.clear();



        try {
            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>())));

            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("latitudes",ObjectSerializer.serialize(new ArrayList<String>())));

            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longitudes",ObjectSerializer.serialize(new ArrayList<String>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //check if possibility have not save anything yet
        if(places.size()>0 && latitudes.size() > 0 && longitudes.size() > 0){

            //another check for error
            if(places.size() == latitudes.size() && latitudes.size() == longitudes.size()){

                for(int i =0; i <latitudes.size();i++){
                    locations.add(new LatLng(Double.parseDouble(latitudes.get(i)),Double.parseDouble(longitudes.get(i))));
                }

            }
        } else {
            places.add("Add a new place...");
            locations.add(new LatLng(0,0));
        }


        //default listView
        places.add("Add a new place...");
        locations.add(new LatLng(0,0));

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,places);

        listView.setAdapter(arrayAdapter);

        //make it clickable
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                //jump to map activity
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);

                //variable passing is an integer
                intent.putExtra("placeNumber",position);

                startActivity(intent);

            }
        });
    }
}
