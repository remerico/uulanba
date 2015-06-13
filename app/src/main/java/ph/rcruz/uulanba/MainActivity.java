package ph.rcruz.uulanba;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

/**
 * Created by rem on 6/9/15.
 */
public class MainActivity extends Activity {

    static final char DEGREE_SYMBOL = 0x00B0;

    OkHttpClient client = new OkHttpClient();

    View loadingView;
    View contentView;

    TextView messageText;
    TextView regionText;
    TextView temperatureText;
    TextView forecastText;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        loadingView = findViewById(R.id.loading_view);
        contentView = findViewById(R.id.content_view);

        messageText = (TextView) findViewById(R.id.message_text);
        regionText = (TextView) findViewById(R.id.region_text);
        temperatureText = (TextView) findViewById(R.id.temperature_text);
        forecastText = (TextView) findViewById(R.id.forecast_text);

        new WeatherLoader().execute();

    }


    private void setLoading(boolean loading) {
        loadingView.setVisibility(loading ? View.VISIBLE : View.GONE);
        contentView.setVisibility(loading ? View.GONE : View.VISIBLE);
    }


    private void setData(WeatherData wd) {
        if (wd != null) {
            messageText.setText(wd.isRainy() ? R.string.oo_tanga : R.string.hindi_gago);
            forecastText.setText(wd.condition);
            regionText.setText(wd.city + ", " + wd.region);
            temperatureText.setText(wd.temperatureCelsius() + Character.toString(DEGREE_SYMBOL) + "C");
        }
        else {
            messageText.setText("Error");
        }

    }


    class WeatherLoader extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected void onPreExecute() {
            setLoading(true);
        }

        @Override
        protected JSONObject doInBackground(Void... voids) {

            String address = "https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20%3D%20%271199477%27&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";

            Request request = new Request.Builder()
                    .url(address)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return new JSONObject(response.body().string());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            setLoading(false);
            setData(WeatherData.parseJson(result));
        }

    }

}
