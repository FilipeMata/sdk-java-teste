package br.com.gerencianet.gnsdk;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.json.JSONTokener;


public class Endpoints {
	private APIRequest requester;
	private JSONObject endpoints;
	
	public Endpoints(JSONObject options) throws Exception 
	{
		this.requester = new APIRequest(options);

		JSONTokener tokener = new JSONTokener(getClass().getResourceAsStream("/config.json"));
		JSONObject config = new JSONObject(tokener);
		this.endpoints = (JSONObject)config.get("ENDPOINTS");
	}
	
	public Endpoints(HashMap<String, String> options, APIRequest requester)
	{
		this.requester = requester;
		
		JSONTokener tokener = new JSONTokener(getClass().getResourceAsStream("/config.json"));
		JSONObject config = new JSONObject(tokener);
		this.endpoints = (JSONObject)config.get("ENDPOINTS");
	}
	
	public JSONObject call(String method, HashMap<String, String> params, JSONObject body) throws Exception{
		if(!this.endpoints.has(method))
			throw new Exception("nonexistent endpoint");
		
		JSONObject endpoint = (JSONObject)this.endpoints.get(method);
		String route = getRoute(endpoint, params);
		route += getQueryString(params);
		return this.requester.send(endpoint.get("method").toString(), route, body);
		
	}

	private String getQueryString(HashMap<String, String> params) throws UnsupportedEncodingException {
		Set<Entry<String, String>> set = params.entrySet();
    	String query="";
    	for(Entry<String, String> entry : set){
    		if(!query.isEmpty())query +="&";
    		else query +="?";
    		query += entry.getKey() + "=" + URLEncoder.encode(entry.getValue(),"UTF-8");
    	}
    	return query;
	}
	
	private String getRoute(JSONObject endpoint, HashMap<String, String> params) 
	{
		Pattern pattern = Pattern.compile("/:(\\w+)/");
    	String route = endpoint.get("route").toString();
    	route += "/";
    	Matcher matcher = pattern.matcher(route);
    	while(matcher.find()){
    		String value = route.substring(matcher.start()+2, matcher.end()-1); 
    		if(params.containsKey(value)){
    			route = route.replace(":"+value, params.get(value));
    			params.remove(value);
    			matcher = pattern.matcher(route);
    		}    		
    	}
    	route = route.substring(0, route.length()-1);
    	return route;
	}
}
