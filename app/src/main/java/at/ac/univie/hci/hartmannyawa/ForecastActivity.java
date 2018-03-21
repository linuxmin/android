package at.ac.univie.hci.hartmannyawa;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.survivingwithandroid.weather.lib.StandardHttpClient;
import com.survivingwithandroid.weather.lib.WeatherClient;
import com.survivingwithandroid.weather.lib.WeatherConfig;
import com.survivingwithandroid.weather.lib.exception.WeatherLibException;
import com.survivingwithandroid.weather.lib.model.HourForecast;
import com.survivingwithandroid.weather.lib.model.Weather;
import com.survivingwithandroid.weather.lib.model.WeatherHourForecast;
import com.survivingwithandroid.weather.lib.provider.openweathermap.OpenweathermapProviderType;
import com.survivingwithandroid.weather.lib.request.WeatherRequest;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ForecastActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id, city = null;
        setContentView(R.layout.activity_forecast);
        id = getIntent().getExtras().getString("id");
        city = getIntent().getExtras().getString("city");

        WeatherClient.ClientBuilder clientBuilder = new WeatherClient.ClientBuilder();  //get a new WeatherClient Builder (factory)
        WeatherConfig weatherConfig = new WeatherConfig();  // get a new weatherconfig used by the builder to configurate the client
        /*
        Configure the settings like apikey, city id, units used (metric) etc..
         */
        weatherConfig.ApiKey = "2140445c086ba33a6ec6319cc1208765";
        weatherConfig.unitSystem = WeatherConfig.UNIT_SYSTEM.M;
        weatherConfig.numDays = 5;

        //create the weatherclient
        try {
            WeatherClient weatherClient =
                    clientBuilder.attach(this).httpClient(StandardHttpClient.class).provider(new OpenweathermapProviderType())
                            .config(weatherConfig).build();
            weatherClient.getHourForecastWeather(new WeatherRequest(id), new WeatherClient.HourForecastWeatherEventListener() {
                @Override
                public void onWeatherRetrieved(WeatherHourForecast forecast) {
                    List<HourForecast> hourlist = forecast.getHourForecast();
                    List<List<HourForecast>> daylist = new ArrayList<>();
                    int[] j = new int[5];
                    int k=0;
                    for(int i=0; i<39; i++){
                        System.out.println(hourlist.get(i).weather.temperature.getTemp() + " I" + i);
                        long timestampnow = hourlist.get(i).timestamp;
                        Date datenow = new Date(timestampnow*1000);
                        long timestampthen = hourlist.get(i+1).timestamp;
                        Date datethen = new Date(timestampthen*1000);
                        if(datenow.getDay() != datethen.getDay()){
                            j[k] = i;
                            System.out.println("Position: " + j[k]);
                            ++k;
                        }
                    }
                    List<HourForecast> one = hourlist.subList(j[0]+1,j[1]+1);
                    List<HourForecast> two = hourlist.subList(j[1]+1,j[2]+1);
                    List<HourForecast> three = hourlist.subList(j[2]+1,j[3]+1);
                    List<HourForecast> four = hourlist.subList(j[3]+1,j[4]+1);
                    List<HourForecast> five = hourlist.subList(j[4]+1,40);

                    for(HourForecast hourForecast : five){
                        Weather weather = hourForecast.weather;
                        //    System.out.println(weather.temperature.getTemp());
                    }
                    TextView textCity = (TextView) findViewById(R.id.text_city);
                    double avgtemp = calc_avgtemp(one);
                    DecimalFormat formatter = new DecimalFormat("0.##");
                    String avgstring = formatter.format(avgtemp);
                    textCity.setText(avgstring);



                }

                @Override
                public void onWeatherError(WeatherLibException wle) {
                    wle.printStackTrace();
                }

                @Override
                public void onConnectionError(Throwable t) {
                    t.printStackTrace();
                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public double calc_avgtemp(List<HourForecast> hourForecastList){
        int i=0;
        double avgtemp = 0.0;
        for(HourForecast hourForecast : hourForecastList){
            avgtemp = avgtemp + hourForecast.weather.temperature.getTemp();
            ++i;
        }
        avgtemp = avgtemp/i;
        return avgtemp;
    }

}
