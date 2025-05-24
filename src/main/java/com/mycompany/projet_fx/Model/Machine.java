package com.mycompany.projet_fx.Model;

import java.io.Serializable;

public class Machine extends Equipement implements Serializable { 
    public enum ETAT {occupe, disponible}

    private int refmachine; // identifiant unique par machine
    private String dmachine;
    private int abscisse;
    private int ordonnee;
    private float c;
    private ETAT etat;

    public Machine(int id_equipement, int refmachine, String dmachine, int abscisse, int ordonnee, float c, ETAT etat) {
        super(id_equipement);
        this.refmachine = refmachine;
        this.dmachine = dmachine;
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
        this.c = c;
        this.etat = etat;
    }

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

    public ETAT getEtat() { return etat; }
    public void setEtat(ETAT etat) { this.etat = etat; }

    @Override
    public String affiche() {
        return "Identifiant équipement : " + getId_equipement()
            + ", identifiant machine : " + refmachine
            + ", description = " + dmachine
            + ", abscisse = " + abscisse
            + ", ordonnée = " + ordonnee
            + ", coût = " + c
            + ", état = " + etat;
    }
}
