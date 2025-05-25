package com.mycompany.projet_fx.controller;

import com.mycompany.projet_fx.Model.Machine;
import com.mycompany.projet_fx.Model.Operateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class MachineController {
    private ObservableList<Machine> machines = FXCollections.observableArrayList();

    public ObservableList<Machine> getMachines() {
        return machines;
    }
    
    public MachineController(ObservableList<Machine> machines) {
        this.machines = machines;
    }

    public void ajouterMachine(Machine machine) {
        machines.add(machine);
    }

    public void supprimerMachine(Machine machine) {
        machines.remove(machine);
    }
    
    public void lierOperateur(Machine machine, Operateur operateur) {
    // Logique pour lier l'opérateur à la machine
    machine.setOperateur(operateur);
}
}
