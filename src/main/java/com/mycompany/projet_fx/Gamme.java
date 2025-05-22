/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx;
import java.io.Serializable;

/**
 *
 * @author Matis
 */
import java.io.Serializable;
import java.util.ArrayList;
public class Gamme implements Serializable{
    private String refGamme;
    ArrayList<Operation> operations;
    ArrayList<Equipement> listeEquipements;

    public ArrayList<Operation> getOperations() {
        return operations;
    }

    public void setOperations(ArrayList<Operation> operations) {
        this.operations = operations;
    }

    public String getRefGamme() {
        return refGamme;
    }

    public void setRefGamme(String refGamme) {
        this.refGamme = refGamme;
    }

    public ArrayList<Equipement> getListeEquipements() {
        return listeEquipements;
    }

    public void setListeEquipements(ArrayList<Equipement> listeEquipements) {
        this.listeEquipements = listeEquipements;
    }

    public Gamme(ArrayList<Operation> operations) {
        this.operations = operations;
    }
    public void affiche(){
    System.out.print("operations ="+operations);
    }
    
    public void creerGamme() {
    this.refGamme = "GAMME_" + System.currentTimeMillis();
    this.operations = new ArrayList<>();
    this.listeEquipements = new ArrayList<>();
    }
    
    public void modifierGamme(Operation operation, Equipement equipement) {
        this.operations.add(operation);
        this.listeEquipements.add(equipement);
    }
    
    public void supprimerGamme() {
        this.operations.clear();
        this.listeEquipements.clear();
    }
    
    public void afficheGamme() {
        System.out.println("Gamme: " + this.refGamme);
        System.out.println("Liste des équipements:");
        for (Equipement equipement : this.listeEquipements) {
            System.out.println(equipement);
        }
        System.out.println("Liste des opérations:");
        for (Operation operation : this.operations) {
            System.out.println(operation);
        }
    }
    
    public float coutGamme() {
        float coutTotal = 0;
        for (Operation operation : this.operations) {
            for (Equipement equipement : this.listeEquipements) {
                coutTotal = coutTotal + ((Machine) equipement).getC() * operation.getDureeOperation();  
            }
        }
        return coutTotal;
    }
    
    public float dureeGamme() {
        float dureeTotale = 0;
        for (Operation operation : operations) {
            dureeTotale += operation.getDureeOperation();
        }
        return dureeTotale;
    }
    
}
