package com.example.androidweather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    EditText edtSearch;
    Button btnSearch, btnChangeActivity;
    TextView txtName, txtCountry, txtTemp, txtStatus, txtHumidity, txtClouds, txtWind, txtDay;
    ImageView imgIcon;
    String City = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Anhxa();
        GetCurrentWeatherData("Saigon");
    btnSearch.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            String city = edtSearch.getText().toString();
            if (city.equals("")) {
                City = "Saigon";
                GetCurrentWeatherData(city);
            }
            else {
                City=city;
                GetCurrentWeatherData(city);
            }
        }
    });
    btnChangeActivity.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view){
            String city = edtSearch.getText().toString();
            Intent intent = new Intent (MainActivity.this,Main2Activity.class);
            intent.putExtra("name",city);
            startActivity(intent);
       }
    });
}

    private <SimpleDateformat> void GetCurrentWeatherData(String data) {
    RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://api.openweathermap.org/data/2.5/weather?q="+data+"&units=metric&appid=803e43b50fafef50924b5135634e2a41";
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dt");
                            String name = jsonObject.getString("name");
                            txtName.setText("City:" + name);

                            long l = Long.valueOf(day);
                            Date date = new Date(l * 1000L);
                            SimpleDateFormat simpleDateFormat = (SimpleDateFormat) new SimpleDateFormat("EEEE yyyy-MM-dd HH-mm-ss");
                            String Day = simpleDateFormat.format(date);
                            txtDay.setText(Day);

                            JSONArray jsonArrayWeather = jsonObject.getJSONArray("weather");
                            JSONObject jsonObjectWeather = jsonArrayWeather.getJSONObject(0);
                            String status = jsonObjectWeather.getString("main");
                            String icon = jsonObjectWeather.getString("icon");
                            Picasso.with(MainActivity.this).load("http://openweathermap.org/img/wn/"+icon+".png").into(imgIcon);
                            txtStatus.setText(status);

                            JSONObject jsonObjectMain = jsonObject.getJSONObject("main");
                            String nhietdo = jsonObjectMain.getString("temp");
                            String doam = jsonObjectMain.getString("humidity");

                            double a = Double.parseDouble(nhietdo);
                            String Nhietdo = String.valueOf(a);

                            txtTemp.setText(nhietdo+"C");
                            txtHumidity.setText(doam+"%");

                            JSONObject jsonObject1Wind = jsonObject.getJSONObject("wind");
                            String gio = jsonObject1Wind.getString("speed");

                            txtWind.setText(gio+"m/s");

                            JSONObject jsonObjectClouds = jsonObject.getJSONObject("clouds");
                            String may = jsonObjectClouds.getString("all");

                            txtClouds.setText(may+"%");

                            JSONObject jsonObjectSys = jsonObject.getJSONObject("sys");
                            String country = jsonObjectSys.getString("country");

                            txtCountry.setText("Country:" + country);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse (VolleyError error){
                        }
                });
        requestQueue.add(stringRequest);
    };


    private void Anhxa() {
        edtSearch = (EditText) findViewById(R.id.edittextSearch);
        btnSearch = (Button) findViewById(R.id.buttonSearch);
        btnChangeActivity = (Button) findViewById(R.id.buttonChangeActivity);
        txtName = findViewById(R.id.textviewName);
        txtCountry = (TextView) findViewById(R.id.textviewCountry);
        txtTemp = (TextView) findViewById(R.id.textviewTemp);
        txtStatus = (TextView) findViewById(R.id.textviewStatus);
        txtHumidity = (TextView) findViewById(R.id.textviewHumidity);
        txtClouds = (TextView) findViewById(R.id.textviewClouds);
        txtWind = (TextView) findViewById(R.id.textviewWind);
        txtDay = (TextView) findViewById(R.id.textviewDay);
        imgIcon = (ImageView) findViewById(R.id.imageIcon);

    }
}
