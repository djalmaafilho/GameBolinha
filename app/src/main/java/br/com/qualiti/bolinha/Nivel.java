package br.com.qualiti.bolinha;

public enum Nivel {

	FACIL(1), MEDIO(10), DIFICIL(20);
	
	private int velodidade;
	
	private Nivel(int velocidade) {
		this.velodidade = velocidade;
	
	}

	public int getVelodidade() {
		return velodidade;
	}
}
