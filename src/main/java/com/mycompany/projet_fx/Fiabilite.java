/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx;

public class Fiabilite {
    private String nomMachine;
    private int totalTempsDeMarche; // en minutes
    private int totalTempsDePanne;  // en minutes

    // Constructeur
    public Fiabilite(String nomMachine) {
        this.nomMachine = nomMachine;
        this.totalTempsDeMarche = 0;
        this.totalTempsDePanne = 0;
    }

    // Getters et Setters
    public String getNomMachine() {
        return nomMachine;
    }

    public void setNomMachine(String nomMachine) {
        this.nomMachine = nomMachine;
    }

    public int getTotalTempsDeMarche() {
        return totalTempsDeMarche;
    }

    public void setTotalTempsDeMarche(int totalTempsDeMarche) {
        this.totalTempsDeMarche = totalTempsDeMarche;
    }

    public int getTotalTempsDePanne() {
        return totalTempsDePanne;
    }

    public void setTotalTempsDePanne(int totalTempsDePanne) {
        this.totalTempsDePanne = totalTempsDePanne;
    }

    // Méthodes pour ajouter du temps
    public void ajouterTempsDeMarche(int minutes) {
        this.totalTempsDeMarche += minutes;
    }

    public void ajouterTempsDePanne(int minutes) {
        this.totalTempsDePanne += minutes;
    }

    // Calcul de la fiabilité
    public double calculerFiabilite() {
        int totalTemps = totalTempsDeMarche + totalTempsDePanne;
        if (totalTemps == 0) return 0.0;
        return (double) totalTempsDeMarche / totalTemps;
    }

    @Override
    public String toString() {
        return String.format("Machine : %s | Fiabilité : %.2f%%", nomMachine, calculerFiabilite() * 100);
    }
}
