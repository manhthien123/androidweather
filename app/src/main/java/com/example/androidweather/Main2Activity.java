package com.example.androidweather;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.lang.ref.ReferenceQueue;
import java.net.ResponseCache;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.lang.String;

import android.content.Intent;

public class Main2Activity extends AppCompatActivity {

    String tenthanhpho = "";
    ImageView imback;
    TextView txtName;
    ListView lv;
    CustomAdapter customAdapter;
    ArrayList<Thoitiet> mangthoitiet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Anhxa();
        Intent intent = getIntent();
        String city = intent.getStringExtra("name");
        Log.d("ketqua","Dữ liệu chuyền qua:" + city);
        if (city.equals("")){
            tenthanhpho="Saigon";
            Get7DaysData(tenthanhpho);
        }
        else {
            tenthanhpho = city;
            Get7DaysData(tenthanhpho);
        }
        imback.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                onBackPressed();
            }
        });
    }

    private void Anhxa() {
        imback = (ImageView) findViewById(R.id.imageviewBack);
        txtName = (TextView) findViewById(R.id.textviewTenthanhpho);
        lv = (ListView) findViewById(R.id.listView);
        mangthoitiet = new ArrayList<Thoitiet>();
        customAdapter = new CustomAdapter(Main2Activity.this,mangthoitiet);
        lv.setAdapter(customAdapter);
    }

    private <SimpleDateformat> void Get7DaysData(String data){
        RequestQueue requestQueue = Volley.newRequestQueue(Main2Activity.this);
        String url = "http://api.openweathermap.org/data/2.5/forecast?q="+data+"&units=metric&cnt=7&appid=2ea10942fe7e98fea8e88d0900af65db";
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET,url,
                new Response.Listener<String>(){
                @Override
                public void onResponse(String response){
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONObject jsonObjectCity = jsonObject.getJSONObject("city");
                        String name = jsonObjectCity.getString("name");
                        txtName.setText(name);

                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectList = jsonArray.getJSONObject(i);

                            String ngay = jsonObjectList.getString("dt");

                            long l = Long.valueOf(ngay);
                            Date date = new Date(l * 1000L);
                            SimpleDateFormat simpleDateFormat = (SimpleDateFormat) new SimpleDateFormat("EEEE yyyy-MM-dd");
                            String Day = simpleDateFormat.format(date);

                            JSONObject jsonObjectMain = jsonObjectList.getJSONObject("main");
                            String tmax = jsonObjectMain.getString("temp_max");
                            String tmin = jsonObjectMain.getString("temp_min");

                            double a = Double.valueOf(tmax);
                            double b = Double.valueOf(tmin);
                            String ndmax = String.valueOf(a);
                            String ndmin = String.valueOf(b);

                            JSONArray jsonArrayWeather = jsonObjectList.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("description");
                            String icon = jsonObjectWeather.getString("icon");

                            mangthoitiet.add(new Thoitiet(Day, status, icon, ndmax, ndmin));
                        }
                        customAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error){
                    }
                });
            requestQueue.add(stringRequest);
    }
}
