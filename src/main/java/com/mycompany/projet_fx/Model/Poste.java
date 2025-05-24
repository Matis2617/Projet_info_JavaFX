package com.mycompany.projet_fx.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Poste extends Equipement implements Serializable {
    private String nomPoste;
    private List<Machine> machines;

    // --- CONSTRUCTEUR ---
    public Poste(String nomPoste) {
        super(0); // id_equipement inutilisé pour Poste ou à adapter
        this.nomPoste = nomPoste;
        this.machines = new ArrayList<>();
    }

    // --- GETTERS / SETTERS ---
    public String getNomPoste() { return nomPoste; }
    public void setNomPoste(String nomPoste) { this.nomPoste = nomPoste; }

    public List<Machine> getMachines() { return machines; }
    public void setMachines(List<Machine> machines) { this.machines = machines; }

    // Pour l'affichage dans TableView ou ComboBox
    @Override
    public String toString() {
        return nomPoste;
    }

    @Override
    public String affiche() {
        return "Nom du poste : " + nomPoste;
    }
}
