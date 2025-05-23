package com.mycompany.projet_fx.controller;

import com.mycompany.projet_fx.model.Operation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class OperationController {
    private ObservableList<Operation> operations = FXCollections.observableArrayList();

    public ObservableList<Operation> getOperations() {
        return operations;
    }

    public void ajouterOperation(Operation operation) {
        operations.add(operation);
    }

    public void supprimerOperation(Operation operation) {
        operations.remove(operation);
    }
}
