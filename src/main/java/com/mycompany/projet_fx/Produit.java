/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx;

/**
 *
 * @author Matis
 */
public class Produit {
    private int codeproduit;
    private String idproduit;

    public int getCodeproduit() {
        return codeproduit;
    }

    public void setCodeproduit(int codeproduit) {
        this.codeproduit = codeproduit;
    }

    public String getIdproduit() {
        return idproduit;
    }

    public void setIdproduit(String idproduit) {
        this.idproduit = idproduit;
    }

    public Produit(int codeproduit, String idproduit) {
        this.codeproduit = codeproduit;
        this.idproduit = idproduit;
    }
    public void affiche(){
    System.out.print("codeproduit ="+codeproduit);
    System.out.print("idproduit ="+idproduit);
}
    
}
