package com.mycompany.projet_fx;

public class Machine extends Equipement { 
    public enum ETAT {occupe, disponible};
    private int refmachine; // identifiant unique par machine
    private String dmachine;
    private int abscisse;
    private int ordonnee;
    private float c;
    private float t;
    private ETAT etat;

    // GETTERS/SETTERS
    public int getRefmachine() { return refmachine; }
    public void setRefmachine(int refmachine) { this.refmachine = refmachine; }

    public String getDmachine() { return dmachine; }
    public void setDmachine(String dmachine) { this.dmachine = dmachine; }

    public int getAbscisse() { return abscisse; }
    public void setAbscisse(int abscisse) { this.abscisse = abscisse; }

    public int getOrdonnee() { return ordonnee; }
    public void setOrdonnee(int ordonnee) { this.ordonnee = ordonnee; }

    public float getC() { return c; }
    public void setC(float c) { this.c = c; }

    public float getT() { return t; }
    public void setT(float t) { this.t = t; }

    public ETAT getEtat() { return etat; }
    public void setEtat(ETAT etat) { this.etat = etat; }

    // Constructeur sans "type" ni "reference"
    public Machine(int id_equipement, int refmachine, String dmachine, int abscisse, int ordonnee, float c, float t, ETAT etat) {
        super(id_equipement);
        this.refmachine = refmachine;
        this.dmachine = dmachine;
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
        this.c = c;
        this.t = t;
        this.etat = etat;
    }
    
    @Override
    public String affiche() {
        return super.affiche()
            + ", identifiant machine = " + refmachine
            + ", description = " + dmachine
            + ", abscisse = " + abscisse
            + ", ordonnée = " + ordonnee
            + ", coût = " + c
            + ", temps = " + t
            + ", état = " + etat;
    }
}
