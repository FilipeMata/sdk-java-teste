package br.com.gerencianet.gnsdk;

import java.io.IOException;

import org.json.JSONObject;

public class APIRequest {
	private Request requester;
	private Auth authenticator;
	
	public APIRequest(JSONObject options) throws Exception {
		this.authenticator = new Auth(options);
		this.requester = new Request(options);
	}
	
	public JSONObject send(String method, String route, JSONObject body) throws AuthorizationException, GerencianetException, IOException{
		if (!this.authenticator.getExpires()) {
            this.authenticator.authorize();
        }
		JSONObject requestOptions = new JSONObject();
		requestOptions.append("Authorization", "Bearer " + this.authenticator.getAccessToken());
		requestOptions.append("json", body);
		
        try {
			return this.requester.send(method, route, requestOptions);
		} catch (AuthorizationException e) {
			this.authenticator.authorize();
			return this.requester.send(method, route, requestOptions);
		}
	}
	
	
}
