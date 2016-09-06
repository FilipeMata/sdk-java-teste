package br.com.gerencianet.gnsdk;

import java.io.FileNotFoundException;
import java.util.HashMap;

public class Gerencianet extends Endpoints{
	public Gerencianet(HashMap<String, String> options) throws FileNotFoundException {
		super(options);
	}
}
