package br.com.gerencianet.gnsdk;

public class GerencianetException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int error;
	private String errorDescription;
	
	public GerencianetException(int responseCode, String responseMessage) {
		this.error = responseCode;
		this.errorDescription = responseMessage;
	}
	
	@Override
	public String toString() {
		return "Error " + this.error + ":" + this.errorDescription;
	}
}
