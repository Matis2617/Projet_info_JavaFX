package com.mycompany.projet_fx.Model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FiabiliteUtils {

    public static void ecrireFiabiliteDansFichier(List<Fiabilite> fiabilites, String fichier) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichier))) {
            for (Fiabilite fiabilite : fiabilites) {
                writer.write(fiabilite.toFileString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erreur d'écriture dans le fichier: " + e.getMessage());
        }
    }
}