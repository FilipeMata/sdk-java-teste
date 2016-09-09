package br.com.gerencianet.gnsdk;

public class AuthorizationException extends Exception 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int status;
	private String reason;

	public AuthorizationException(int responseCode, String responseMessage) {
		super();
		this.status = responseCode;
		this.reason = responseMessage;
	}
	
	@Override
	public String toString() {
		return "Authorization Error " + this.status + ":" + this.reason;
	}
}
