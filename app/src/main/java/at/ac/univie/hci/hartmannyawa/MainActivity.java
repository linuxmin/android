package at.ac.univie.hci.hartmannyawa;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.survivingwithandroid.weather.lib.StandardHttpClient;
import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.model.HourForecast;
import com.survivingwithandroid.weather.lib.model.Weather;
import com.survivingwithandroid.weather.lib.model.WeatherForecast;
import com.survivingwithandroid.weather.lib.model.WeatherHourForecast;
import com.survivingwithandroid.weather.lib.provider.forecastio.ForecastIOProviderType;
import com.survivingwithandroid.weather.lib.provider.openweathermap.OpenweathermapProviderType;
import com.survivingwithandroid.weather.lib.request.WeatherRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        get some extra permissions for network communication
         */
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        /*
        Instantiate a Button Object to work with by finding it with the specified id (similiar to javascript)
        Then assigning a OnclickListener to the button, here it is the onClick Method specified after onCreate)
         */
        Button btngetforecast = (Button) findViewById(R.id.btn_get_forecast);
        btngetforecast.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Spinner spinner_cities = (Spinner) findViewById(R.id.spinner_city);
        String id = null;
        String city = null;
        switch(spinner_cities.getSelectedItemPosition()){
            case 0: {
                id = "2761369";
                city = "Vienna";
                break;
            }
            case 1: {
                id = "2766824";
                city = "Salzburg";
                break;
            }
            case 2: {
                id = "2775220";
                city = "Innsbruck";
                break;
            }
        }


        Intent intent = new Intent(this,ForecastActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("city",city);
        startActivity(intent);
    }




}
