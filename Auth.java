package br.com.gerencianet.gnsdk;

import java.io.IOException;
import java.util.Date;

import org.json.JSONObject;


public class Auth {
	private String accessToken;
	private String tokenType;
	private Date expires;
	private JSONObject config;
	private Request request;
	
	public Auth(JSONObject options) throws Exception {
		this.config = Config.options(options);
		
		if(!this.config.has("clientId") || !this.config.has("clientSecret")){
			throw new Exception("Client_Id or Client_Secret not found");
		}
		
		this.request = new Request(options);
	}
	
	public void authorize() throws IOException, AuthorizationException, GerencianetException{
		JSONObject endpoints = Config.get("ENDPOINTS");
		JSONObject requestOptions = new JSONObject();
		String[] auth = {this.config.getString("clientId"), this.config.getString("clientSecret")};
		JSONObject authBody = new JSONObject();
		authBody.append("grant_type", "client_credentials");
		
		requestOptions.append("auth", auth);
		requestOptions.append("json", authBody);
		JSONObject authorizeConfig= (JSONObject)endpoints.get("athorize");
		JSONObject response = this.request.send(authorizeConfig.getString("method"), authorizeConfig.getString("route"), requestOptions);
		this.accessToken = response.getString("access_token");
		this.expires = new Date(new Date().getTime() + response.getLong("expires_in"));
		this.tokenType = response.getString("token_type");
		
	}

	public boolean getExpires() {
		if(this.expires == null || this.expires.compareTo(new Date()) <= 0)
			return true;
		return false;
	}
	
	public String getAccessToken() {
		return accessToken;
	}
	
	public String getTokenType() {
		return tokenType;
	}
}
