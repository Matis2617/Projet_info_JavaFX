/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx;

/**
 *
 * @author Matis
 */
public class Machine extends Equipement{ 
    public enum ETAT{occupe,disponible} ;
    private int refmachine;
    private String dmachine;
    private String Type;
    private float abscisse;
    private float ordonnee;
    private float c;
    private float t;
   
    private ETAT etat;

    public String getType() {
        return Type;
    }

    public void setType(String Type) {
        this.Type = Type;
    }

    public int getRefmachine() {
        return refmachine;
    }

    public void setRefmachine(int refmachine) {
        this.refmachine = refmachine;
    }

    public String getDmachine() {
        return dmachine;
    }

    public void setDmachine(String dmachine) {
        this.dmachine = dmachine;
    }

    public float getAbscisse() {
        return abscisse;
    }

    public void setAbscisse(float abscisse) {
        this.abscisse = abscisse;
    }

    public float getOrdonnee() {
        return ordonnee;
    }

    public void setOrdonnee(float ordonnee) {
        this.ordonnee = ordonnee;
    }

    public float getC() {
        return c;
    }

    public void setC(float c) {
        this.c = c;
    }

    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.t = t;
    }

    public ETAT getEtat() {
        return etat;
    }

    public void setEtat(ETAT etat) {
        this.etat = etat;
    }

    public Machine(int refmachine, String dmachine, String Type, float abscisse, float ordonnee, float c, float t, ETAT etat, int id_equipement) {
        super(id_equipement);
        this.refmachine = refmachine;
        this.dmachine = dmachine;
        this.Type = Type;
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
        this.c = c;
        this.t = t;
        this.etat = etat;
    }
    
    @Override public String affiche(){
    return "etat ="+etat+",refmachine ="+refmachine+"dmachine ="+dmachine+"Type ="+Type+"abscisse ="+abscisse+"ordonnee ="+ordonnee+"cout ="+c+"temps ="+t;
}
}
