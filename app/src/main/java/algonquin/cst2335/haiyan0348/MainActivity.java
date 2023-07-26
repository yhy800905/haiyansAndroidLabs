package algonquin.cst2335.haiyan0348;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import algonquin.cst2335.haiyan0348.databinding.ActivityMainBinding;

/**
 *  The MainActivity class represents the main activity of the application.
 *  It serves as the entry point and user interface for the password complexity checker.
 *  The activity contains a text view, an edit text, and a button to check the complexity of a password.
 *  The user enters a password in the edit text and clicks the button to initiate the complexity check.
 *  Toast messages are displayed to indicate whether the password meets the required complexity criteria.
 *
 * @author Haiyan Yang
 * @version 1.0
 * @see AppCompatActivity
 */
public class MainActivity extends AppCompatActivity {

    /**
     * This holds the "Type your Password" text view
     */
    protected TextView theText;

    /**
     * This holds the "Login" button
     */
    protected Button myButton;

    /**
     * This holds the edit text for typing into
     */
    protected EditText theEditText;

    protected String cityName;
    RequestQueue queue = null;
    String iconName = null;
    ImageRequest imgReq;
    Bitmap image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //calling onCreate from parent class
        super.onCreate(savedInstanceState);

        //This part goes at the top of the onCreate function:
        queue = Volley.newRequestQueue(this); //like a constructor

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        //loads an XML file on the page
        setContentView(binding.getRoot());

        binding.button.setOnClickListener(click -> {
            cityName = binding.theEditText.getText().toString();

            //server name and parameters:                           name=value&name2=value2&name3=value3
            String url = null;
            try {
                url = "https://api.openweathermap.org/data/2.5/weather?q=" +
                        URLEncoder.encode(cityName,"UTF-8") //replace spaces, &. = with other characters
                        + "&appid=7e943c97096a9784391a981c4d878b22&units=metric";
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            //this goes in the button click handler:
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    (successfulResponse) -> {
                        Log.d("API Response", successfulResponse.toString()); // Add this line to log the API response

                        try {
                            JSONObject coord = successfulResponse.getJSONObject("coord");

                            int vis = successfulResponse.getInt("visibility");
                            JSONArray weatherArray = successfulResponse.getJSONArray("weather");
                            JSONObject position0 = weatherArray.getJSONObject(0);
                            String description = position0.getString("description");
                            String iconName = position0.getString("icon");

                            JSONObject mainObject = successfulResponse.getJSONObject("main");
                            double temp = mainObject.getDouble("temp");
                            double minTemp = mainObject.getDouble("temp_min");
                            double maxTemp = mainObject.getDouble("temp_max");
                            int humidity = mainObject.getInt("humidity");

                            //iconName = successfulResponse.getJSONArray("weather").getJSONObject(0).getString("icon");
                            String pathname = getFilesDir() + "/" + iconName + ".png";
                            File file = new File(pathname);
                            if (file.exists()) {
                                image = BitmapFactory.decodeFile(pathname);
                                runOnUiThread(() -> binding.icon.setImageBitmap(image));
                            } else {
                                imgReq = new ImageRequest("https://openweathermap.org/img/w/" + iconName + ".png",
                                        bitmap -> {
                                            try {
                                                // Do something with loaded bitmap...
                                                image = bitmap;
                                                image.compress(Bitmap.CompressFormat.PNG, 100, MainActivity.this.openFileOutput(iconName + ".png", Activity.MODE_PRIVATE));
                                                runOnUiThread(() -> binding.icon.setImageBitmap(image));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                Log.e("Icon Error", "Error saving or displaying image: " + e.getMessage());
                                            }
                                        }, 1024, 1024, ImageView.ScaleType.CENTER, Bitmap.Config.RGB_565,
                                        (error) -> {
                                            error.printStackTrace();
                                            Log.e("Icon Error", "Error loading image: " + error.getMessage());
                                        });
                                queue.add(imgReq);
                            }

                            /*String temp, minTemp, maxTemp, humidity, description;
                            temp = minTemp = maxTemp = humidity = description = "";*/


                            binding.temp.setText("The current temperature is " + temp + " °C");
                            binding.temp.setVisibility(View.VISIBLE);

                            binding.minTemp.setText("The min temperature is " + minTemp + " °C");
                            binding.minTemp.setVisibility(View.VISIBLE);

                            binding.maxTemp.setText("The max temperature is " + maxTemp + " °C");
                            binding.maxTemp.setVisibility(View.VISIBLE);

                            binding.humidity.setText("The humidity is " + humidity + "%");
                            binding.humidity.setVisibility(View.VISIBLE);

                            binding.icon.setImageBitmap(image);
                            binding.icon.setVisibility(View.VISIBLE);

                            binding.description.setText("Weather description: " + description);
                            binding.description.setVisibility(View.VISIBLE);

                            //queue.add(imgReq);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    },
                    (error) -> {
                        Log.e("API Error", error.toString()); // Add this line to log API errors
                        Log.e("Error Message", error.getMessage()); // Add this line to log the error message, if any
                        // Handle API error here, show an error message to the user
                        Toast.makeText(MainActivity.this, "Failed to fetch weather data.", Toast.LENGTH_SHORT).show();
                    });
            queue.add(request);
        });
    }
}
