package br.com.gerencianet.gnsdk;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Config {
	private final static String version = "0.0.1";
	
	public static JSONObject get(String parameter) throws IOException{
		InputStream configFile;
		try {
			configFile = new FileInputStream("/config.json");
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("O arquivo config.json não foi encontrado.");
		}
		JSONTokener tokener = new JSONTokener(configFile);
		JSONObject config = new JSONObject(tokener);
		try {
			configFile.close();
		} catch (IOException e) {
			throw new IOException("Falha ao fechar o arquivo config.json.");
		}
		if(config.has(parameter))
			return(JSONObject)config.get(parameter);
		return null;
	}
	
	public static JSONObject options(JSONObject options) throws IOException{
		JSONObject conf = new JSONObject();
		boolean sandbox = false;
		boolean debug = false;
		System.out.println((Boolean)options.get("sandbox"));
		if(options.has("sandbox"))
			sandbox = options.getBoolean("sandbox");
		if(options.has("debug"))
			debug = options.getBoolean("debug");
		
		conf.append("sandbox", sandbox);
		conf.append("debug", debug);
		
		if(options.has("client_id"))
			conf.append("clientId", options.getString("client_id"));
		if(options.has("client_secret"))
			conf.append("clientSecret", options.getString("client_secret"));
		if(options.has("url")){
			conf.append("baseUri", options.getString("url"));
		}	
		else{
			JSONObject urls = Config.get("URL");
			String baseUri = urls.getString("production");
			if(conf.getBoolean("sandbox"))
				baseUri = urls.getString("sandbox");
			
			conf.append("baseUri", baseUri);
		}
		
		return conf;
	}

	public static String getVersion() {
		return Config.version;
	}
}
