package br.com.gerencianet.gnsdk;

import java.io.FileNotFoundException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONString;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Teste {
	public static void main(String[] args) throws FileNotFoundException {
		JSONObject options = new JSONObject();
		JsonObject options2 = new JsonObject();
		JSONArray a3 = new JSONArray();
		options.append("client_id", "Client_Id");
		options.append("client_secret", "Client_Secret");
		options.append("sandbox", Boolean.TRUE);
		JsonObject a = new JsonObject();
		a.addProperty("b", "value1");
		a.addProperty("c", "value2");
		options2.addProperty("client_id","Client_Id");
		options2.add("a", a);
		options2.addProperty("sandbox", true);
		
		System.out.println(options2);
		//JSONObject params = new JSONObject();
		//params.append("id", 99573);
		HashMap<String, String> params = new HashMap<>();
		params.put("id", "99573");
		try {
			
			Gerencianet gn = new Gerencianet(options);
			gn.call("detailCharge", params, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
