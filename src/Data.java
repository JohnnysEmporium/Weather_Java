import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


// Class for parsing JSON data 

public class Data {

	//Setting data types to be used as holders for weather information
	static String cTime, city, country, lat = "30", lon="20";
	static String[] hTime = new String[49], dTime = new String[8];
	static String cTemp, cHumid, cPreassure, cPrecipProb;
	static String cWindBearing, cWindSpeed;
	static String cPrecipType, cSummary;
	static String[] hTemp = new String[49], dTemp = new String[8], dTempMin = new String[8], hPreassure = new String[49], dPreassure = new String[8], hPrecipProb = new String[49], dPrecipProb = new String[49];
	static String[] hWindBearing = new String[49], dWindBearing = new String[8], hWindSpeed = new String[49], dWindSpeed = new String[8], hHumid = new String[49], dHumid = new String[8];
	static String[] hPrecipType = new String[49], dPrecipType = new String[8], hSummary = new String[49], dSummary = new String[8];
	static DateFormat dfCurrently = new SimpleDateFormat("HH:mm EEE, dd-MM-YYYY");
	static DateFormat dfHourly = new SimpleDateFormat("HH:mm");
	static DateFormat dfDaily = new SimpleDateFormat("EEEE");
	

	public static void getWeatherForLocation(String[] location) {
		String URLGeocode = "https://open.mapquestapi.com/geocoding/v1/address?key=OzIV2XUPlfo3gTJOZjGVKs6eMINwpmt4&location=";
		for(String x : location){
			if(x != " ") {
				URLGeocode = URLGeocode + x + "+";
			}
		}
		System.out.println(URLGeocode);
		JsonObject data = getJsonObjectFromURL(URLGeocode);
		data = data.getAsJsonArray("results").get(0).getAsJsonObject().get("locations").getAsJsonArray().get(0).getAsJsonObject();
		lat = data.get("latLng").getAsJsonObject().get("lat").getAsString();
		lon = data.get("latLng").getAsJsonObject().get("lng").getAsString();
		city = data.get("adminArea5").getAsString();
		country = data.get("adminArea1").getAsString();
		System.out.println(data);
		updateValues();
		
	}
	
	//Update all data values
	private static void updateValues(){
		int i, j;
		String URLWeather = "https://api.darksky.net/forecast/051de7d98fd7cc6a18796c4e96f2058e/" + lat + "," + lon + "?units=si";
		JsonObject data = getJsonObjectFromURL(URLWeather);
		JsonObject cDat =  data.getAsJsonObject("currently");
		JsonArray hDat = data.getAsJsonObject("hourly").getAsJsonArray("data");
		JsonArray dDat = data.getAsJsonObject("daily").getAsJsonArray("data");
		
		//Current values
		cTime = dfCurrently.format(new Date(cDat.get("time").getAsLong()*1000));
		cTemp = cDat.get("temperature").getAsString();
		cHumid = cDat.get("humidity").getAsString();
		cPreassure = cDat.get("pressure").getAsString();
		cPrecipProb = cDat.get("precipProbability").getAsString();
		cWindBearing = cDat.get("windBearing").getAsString();
		cWindSpeed = cDat.get("windSpeed").getAsString();
		cPrecipType = ((Double.parseDouble(cPrecipProb) == 0) ? "N/A" : cDat.get("precipType").getAsString());
		cSummary = cDat.get("summary").getAsString();
	
		//Hourly Values
		for(i = 0; i < hDat.size(); i++) {
			hTime[i] = dfHourly.format(new Date(hDat.get(i).getAsJsonObject().get("time").getAsLong() * 1000));
			hTemp[i] = hDat.get(i).getAsJsonObject().get("temperature").getAsString();
			hHumid[i] = hDat.get(i).getAsJsonObject().get("humidity").getAsString();
			hPreassure[i] = hDat.get(i).getAsJsonObject().get("pressure").getAsString();
			hPrecipProb[i] = hDat.get(i).getAsJsonObject().get("precipProbability").getAsString();
			hWindBearing[i] = hDat.get(i).getAsJsonObject().get("windBearing").getAsString();
			hWindSpeed[i] = hDat.get(i).getAsJsonObject().get("windSpeed").getAsString();
			hSummary[i] = hDat.get(i).getAsJsonObject().get("summary").getAsString();
			//"precipType" field is not included in JSON when "precipProbability" is 0.
			//When trying to assign non-existant field it will produce nullPointerException error on runtime.
			//This statement prevents such cases (similar statement can be found in loop below)
			hPrecipType[i] = ((Double.parseDouble(hPrecipProb[i]) == 0) ? "N/A" : hDat.get(i).getAsJsonObject().get("precipType").getAsString());
		}
	
		//Daily Values
		for(j = 0; j < dDat.size(); j++) {
			dTime[j] = dfDaily.format(new Date(dDat.get(j).getAsJsonObject().get("time").getAsLong() * 1000));
			dTemp[j] = dDat.get(j).getAsJsonObject().get("temperatureHigh").getAsString();
			dTempMin[j] = dDat.get(j).getAsJsonObject().get("temperatureLow").getAsString();
			dHumid[j] = dDat.get(j).getAsJsonObject().get("humidity").getAsString();
			dPreassure[j] = dDat.get(j).getAsJsonObject().get("pressure").getAsString();
			dPrecipProb[j] = dDat.get(j).getAsJsonObject().get("precipProbability").getAsString();
			dWindBearing[j] = dDat.get(j).getAsJsonObject().get("windBearing").getAsString();
			dWindSpeed[j] = dDat.get(j).getAsJsonObject().get("windSpeed").getAsString();
			dSummary[j] = dDat.get(j).getAsJsonObject().get("summary").getAsString();
			dPrecipType[j] = ((Double.parseDouble(dPrecipProb[j]) == 0) ? "N/A" : dDat.get(j).getAsJsonObject().get("precipType").getAsString());
		}
	}

	//----------- This section is responsible for handling 
	//----------- GET request for JSON
	
	// Pass Java I/O InputStream to receive String
	private static String streamToString(InputStream inputStream) {		
		Scanner scan = new Scanner(inputStream, "UTF-8");
		String text = scan.useDelimiter("\\Z").next();
		scan.close();
		return text;
	}

	// Pass URL to JSON string in order to get JSON as String
	private static String jsonGetRequest(String urlQueryString) {
		System.setProperty("java.net.useSystemProxies", "true");
		String json = null;
		
		try {
			
			System.setProperty("java.net.useSystemProxies", "true");
		
			URL url = new URL(urlQueryString);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			
			//Setting request parameters
			connection.setDoOutput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("charset", "utf-8");
			connection.connect();
			
			InputStream inStream = connection.getInputStream();
			json = streamToString(inStream);
			
		} catch (IOException ex) {
			
			ex.printStackTrace();
		}
		return json;
	}
	
	private static JsonObject getJsonObjectFromURL(String url) {
		Gson gson = new Gson();
		JsonObject body = gson.fromJson(jsonGetRequest(url), JsonObject.class);
		return body;
	}

}