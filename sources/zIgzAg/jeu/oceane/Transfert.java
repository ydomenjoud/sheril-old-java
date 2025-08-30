package zIgzAg.jeu.oceane;

public class Transfert {

	private Position depart;
	private Position arrivee;
	private int planeteDepart;
	private int planeteArrivee;
	private String code;
	private int nombre;
	
	public Transfert() {
		super();
	}
	
	public Transfert(Position depart, Position arrivee, int planeteDepart,
			int planeteArrivee, String code, int nombre) {
		super();
		this.depart = depart;
		this.arrivee = arrivee;
		this.planeteDepart = planeteDepart;
		this.planeteArrivee = planeteArrivee;
		this.code = code;
		this.nombre = nombre;
	}

	public Position getDepart() {
		return depart;
	}
	public void setDepart(Position depart) {
		this.depart = depart;
	}
	public Position getArrivee() {
		return arrivee;
	}
	public void setArrivee(Position arrivee) {
		this.arrivee = arrivee;
	}
	public int getPlaneteDepart() {
		return planeteDepart;
	}
	public void setPlaneteDepart(int planeteDepart) {
		this.planeteDepart = planeteDepart;
	}
	public int getPlaneteArrivee() {
		return planeteArrivee;
	}
	public void setPlaneteArrivee(int planeteArrivee) {
		this.planeteArrivee = planeteArrivee;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public int getNombre() {
		return nombre;
	}
	public void setNombre(int nombre) {
		this.nombre = nombre;
	}
	
	
}
