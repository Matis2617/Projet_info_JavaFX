/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx.Controller;

/**
 *
 * @author Oscar
 */
import com.mycompany.projet_fx.Model.Operateur;
import com.mycompany.projet_fx.Model.Machine.ETAT;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OperateurController {
    private ObservableList<Operateur> operateurs = FXCollections.observableArrayList();

    public void ajouterOperateur(Operateur operateur) {
        operateurs.add(operateur);
    }

    public ObservableList<Operateur> getOperateurs() {
        return operateurs;
    }

    public void setCompetences(String competences, Operateur operateur) {
        operateur.setCompetences(competences);
    }

    public void setId_op(int id_op, Operateur operateur) {
        operateur.setId_op(id_op);
    }

    public void setEtat(ETAT etat, Operateur operateur) {
        operateur.setEtat(etat);
    }

    public String getCompetences(Operateur operateur) {
        return operateur.getCompetences();
    }

    public int getId_op(Operateur operateur) {
        return operateur.getId_op();
    }

    public ETAT getEtat(Operateur operateur) {
        return operateur.getEtat();
    }
}