package at.ac.univie.hci.hartmannyawa;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TableRow;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ForecastActivity extends Activity {
    Context context = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id, city = null;
        setContentView(R.layout.activity_forecast);
        context = getApplication();
        /*
        getting the city_id and the city_name from the main activity
         */

        id = getIntent().getExtras().getString("id");
        city = getIntent().getExtras().getString("city");

        /*
        getting a new WeatherClient Builder (factory) and
        a new weatherconfig used by the builder to configure the client
         */
        WeatherClient.ClientBuilder clientBuilder = new WeatherClient.ClientBuilder();
        WeatherConfig weatherConfig = new WeatherConfig();  //
        /*
        Configure the settings like apikey, city id, units used (metric) etc..
         */
        weatherConfig.ApiKey = "2140445c086ba33a6ec6319cc1208765";
        weatherConfig.unitSystem = WeatherConfig.UNIT_SYSTEM.M;
    //    weatherConfig.numDays = 5;

        //create the weatherclient
        try {
            WeatherClient weatherClient =
                    clientBuilder.attach(this).httpClient(StandardHttpClient.class).provider(new OpenweathermapProviderType())
                            .config(weatherConfig).build();
            weatherClient.getHourForecastWeather(new WeatherRequest(id), new WeatherClient.HourForecastWeatherEventListener() {

               /*
               method names are self-descriptive. this is done when the wheater has been retrieved
               by the client
                */
                @Override
                public void onWeatherRetrieved(WeatherHourForecast forecast) {
                    List<HourForecast> hourlist = forecast.getHourForecast(); // List of Forecasts, 1 for every 3 hours
                    System.out.println(hourlist.size()); //could be 37 in special cases??
                    /*
                    To seperate the results from each day, the position of the last entry of one day
                    must be known
                     */
                    int[] j = new int[5];  //int Array to save last position of day
                    int k=0;
                    for(int i=0; i<39; i++){
                        long timestampnow = hourlist.get(i).timestamp;
                        Date datenow = new Date(timestampnow*1000);         //date entry actual pos
                        long timestampthen = hourlist.get(i+1).timestamp;
                        Date datethen = new Date(timestampthen*1000); // date entry next pos
                        if(datenow.getDay() != datethen.getDay()){         // if days are different
                            j[k] = i;                                    //save pos in array
                            ++k;                                         //iterate
                        }
                    }
                    /*
                    Now we can split the original List into 5 "daily" Lists using sublist(from,to)
                    and the known last positions of the days. +1 to begin the next day, actual
                    day must not be part ot it, day five ends at position 40.
                     */
                    List<HourForecast> one = hourlist.subList(j[0]+1,j[1]+1);
                    List<HourForecast> two = hourlist.subList(j[1]+1,j[2]+1);
                    List<HourForecast> three = hourlist.subList(j[2]+1,j[3]+1);
                    List<HourForecast> four = hourlist.subList(j[3]+1,j[4]+1);
                    List<HourForecast> five = hourlist.subList(j[4]+1,40);

                    /*
                    using the method calc_temperatures to get the final avg,min,max temperatures
                    for each day as an array of double values in the order avg,min,max.
                     */
                    double[] result_one = calc_temperatures(one);
                    double[] result_two = calc_temperatures(two);
                    double[] result_three = calc_temperatures(three);
                    double[] result_four = calc_temperatures(four);
                    double[] result_five = calc_temperatures(five);
/*/
                    List<Double[]> list = new ArrayList<>();
                    list.add(calc_temperatures(one));
                    list.add(calc_temperatures(two));
                    list.add(calc_temperatures(three));
                    list.add(calc_temperatures(four));
                    list.add(calc_temperatures(five));
/*/
                    Intent intent = new Intent(context, TabbedForecast.class);
                    intent.putExtra("one",result_one);
                    intent.putExtra("two",result_two);
                    intent.putExtra("three", result_three);
                    intent.putExtra("four", result_four);
                    intent.putExtra("five",result_five);
                    startActivity(intent);

                   // DecimalFormat formatter = new DecimalFormat("0.##");

                    /*
                    Decimal Format used to format the result, should only show 2 numbers after comma
                     */

                   // String avgstring = formatter.format(result_one[0]);

                }
                /*
                if there is a WeahterLibException thrown e.g. the apikey is invalid
                 */
                @Override
                public void onWeatherError(WeatherLibException wle) {
                    wle.printStackTrace();
                }

                //if there are problems with the connection to the server
                @Override
                public void onConnectionError(Throwable t) {
                    t.printStackTrace();
                }
            });

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /*
    method that calculates min,max and avg temperatures out of a HoureForecast List
    it returns an array of 3 double values (avg, min, max)
     */
    public double[] calc_temperatures(List<HourForecast> hourForecastList){
        int i=0;
        double avgtemp = 0.0;
        double mintemp = 0.0;
        double maxtemp = 0.0;
        /*
        to find min and max temperatures, the actual min/max temp is compared to the previous found
        min/max. to get a start value for min/max, they are instantiated with unreachable min/max values
         */
        double prev_min =+100.0;
        double prev_max =-100.0;
        double [] array_result = new double[3];
        for(HourForecast hourForecast : hourForecastList){
            mintemp = hourForecast.weather.temperature.getMinTemp();
            maxtemp = hourForecast.weather.temperature.getMaxTemp();
            if(mintemp < prev_min) {
                prev_min = mintemp;
            }
            if(maxtemp > prev_max){
                prev_max = maxtemp;
            }
            avgtemp = avgtemp + hourForecast.weather.temperature.getTemp();
            ++i;
        }
        avgtemp = avgtemp/i;
        array_result[0] = avgtemp;
        array_result[1] = prev_min;
        array_result[2] = prev_max;
        return array_result;
    }

}
