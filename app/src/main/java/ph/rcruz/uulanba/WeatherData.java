package ph.rcruz.uulanba;

import org.json.JSONObject;

/**
 * Created by rem on 6/9/15.
 */
public class WeatherData {

    public int code;
    public String date;
    public int temp;
    public String condition;

    public String city;
    public String region;

    public int temperatureCelsius() {
        return ((temp - 32)*5)/9;
    }

    public boolean isRainy() {

        return (code == 1
                || code == 2
                || code == 3
                || code == 4
                || code == 5
                || code == 6
                || code == 8
                || code == 9
                || code == 10
                || code == 11
                || code == 12
                || code == 35
                || code == 37
                || code == 38
                || code == 39
                || code == 40
                || code == 45
                || code == 47);

    }

    public static WeatherData parseJson(JSONObject j) {

        try {

            WeatherData wd = new WeatherData();

            JSONObject channel = j.getJSONObject("query").getJSONObject("results").getJSONObject("channel");

            JSONObject location = channel.getJSONObject("location");

            wd.city = location.optString("city");
            wd.region = location.optString("region");

            JSONObject condition = channel.getJSONObject("item").getJSONObject("condition");

            wd.code = condition.optInt("code");
            wd.date = condition.optString("date");
            wd.temp = condition.optInt("temp");
            wd.condition = condition.optString("text");

            return wd;

        }

        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
