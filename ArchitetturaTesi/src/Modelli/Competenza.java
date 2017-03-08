package Modelli;

public class Competenza {
	private int compartment;
	private int level;
	
	public Competenza(int compartment, int level) {
		this.compartment = compartment;
		this.level = level;		
	}

	public int getCompartment() {
		return compartment;
	}

	public void setCompartment(int compartment) {
		this.compartment = compartment;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
}
