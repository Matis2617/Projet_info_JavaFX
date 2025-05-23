package com.mycompany.projet_fx;
import java.io.Serializable;
import java.util.ArrayList;

public class Poste extends Equipement implements Serializable {
    private int refposte;
    private String dposte;
    private ArrayList<Machine> machines;

    // ---- CONSTRUCTEUR ----
    public Poste(int refposte, String dposte, ArrayList<Machine> machines, int id_equipement) {
        super(id_equipement);
        this.refposte = refposte;
        this.dposte = dposte;
        this.machines = machines;
    }

    // ---- GETTERS / SETTERS ----
    public int getRefposte() { return refposte; }
    public void setRefposte(int refposte) { this.refposte = refposte; }

    public String getDposte() { return dposte; }
    public void setDposte(String dposte) { this.dposte = dposte; }

    public ArrayList<Machine> getMachines() { return machines; }
    public void setMachines(ArrayList<Machine> machines) { this.machines = machines; }

    // ---- AFFICHAGE POUR MVC / DEBUG ----
    @Override
    public String toString() {
        return "Poste: " + dposte + " (Réf: " + refposte + ")";
    }

    // Méthode pour afficher les machines liées à ce poste
    public String machinesToString() {
        if (machines == null || machines.isEmpty()) return "Aucune machine";
        StringBuilder sb = new StringBuilder();
        for (Machine m : machines) {
            sb.append(m.getDmachine()).append(" | ");
        }
        return sb.toString();
    }

    // (Optionnel, garder la méthode "affiche" si tu veux, mais toString est la norme MVC)
    @Override
    public String affiche() {
        return "Référence = " + refposte + ", dposte = " + dposte;
    }
}
