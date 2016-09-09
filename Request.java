package br.com.gerencianet.gnsdk;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Request {
	
	private HttpURLConnection client;
	private JSONObject config;
	private String certified_path = "";
	private String partnerToken = "";
	
	public Request(JSONObject options) throws IOException {
		this.config = Config.options(options);
		if(options.has("partner_token"))
			this.partnerToken  = options.getString("partner_token");
		if(options.has("certified_path"))
			this.certified_path = options.getString("certified_path");
		
	}

	public JSONObject send(String method, String route, JSONObject requestOptions) throws AuthorizationException, GerencianetException
	{
		String url = config.getString("baseUri") + route;
		
		try {
	    	byte[] postDataBytes = requestOptions.getString("body").toString().getBytes("UTF-8");
			URL link = new URL(url);
			client = (HttpURLConnection) link.openConnection();
	
			client.setRequestMethod(method.toUpperCase());
			if(requestOptions.has("Authorization"))
				client.addRequestProperty("Authorization", requestOptions.getString("Authorization"));
			
			client.addRequestProperty("Content-Type", "application/json");
			client.addRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
			client.addRequestProperty("api-sdk", "java-"+ Config.getVersion());	 
			client.addRequestProperty("partner-token", this.partnerToken);
			
			if(this.certified_path!=null)
				client.setRequestProperty("verify", this.certified_path);		

			if(!method.toLowerCase().equals("get")){
				client.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(client.getOutputStream());
				wr.write(postDataBytes);
				wr.flush();
				wr.close();
			}			
	
			int responseCode = client.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				InputStream responseStream = client.getInputStream();
				JSONTokener responseTokener = new JSONTokener(responseStream);
				return new JSONObject(responseTokener);
			}else if(responseCode == HttpURLConnection.HTTP_UNAUTHORIZED){
				/*InputStream responseStream = client.getErrorStream();
				JSONTokener responseTokener = new JSONTokener(responseStream);
				JSONObject response = new JSONObject(responseTokener);*/
				throw new AuthorizationException(client.getResponseCode(), client.getResponseMessage());
			}else{
				//throw new HttpRetryException(client.getResponseMessage(), client.getResponseCode());
				//devo enviar o body da exception
				throw new GerencianetException(client.getResponseCode(), client.getResponseMessage());
			}	
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
