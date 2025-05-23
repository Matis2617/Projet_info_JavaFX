package com.mycompany.projet_fx.Utils;

import com.mycompany.projet_fx.Model.Atelier;
import java.io.*;

public class AtelierSauvegarde {

    /**
     * Sauvegarde un objet Atelier dans un fichier.
     * @param atelier      l'objet Atelier à sauvegarder
     * @param nomFichier   le nom du fichier cible (ex: "atelier_utilisateur.ser")
     */
    public static void sauvegarderAtelier(Atelier atelier, String nomFichier) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomFichier))) {
            oos.writeObject(atelier);
            System.out.println("[SAUVEGARDE] Atelier sauvegardé dans " + nomFichier
                + " (" + atelier.getEquipements().size() + " machines, " + atelier.getPostes().size() + " postes)");
        } catch (Exception e) {
            System.err.println("[ERREUR] Sauvegarde échouée !");
            e.printStackTrace();
        }
    }

    /**
     * Charge un objet Atelier depuis un fichier.
     * @param nomFichier  le nom du fichier source
     * @return            l'objet Atelier lu, ou null en cas d'échec
     */
    public static Atelier chargerAtelier(String nomFichier) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomFichier))) {
            Atelier a = (Atelier) ois.readObject();
            System.out.println("[CHARGEMENT] Atelier chargé depuis " + nomFichier
                + " (" + a.getEquipements().size() + " machines, " + a.getPostes().size() + " postes)");
            return a;
        } catch (Exception e) {
            System.err.println("[ERREUR] Chargement échoué !");
            e.printStackTrace();
            return null;
        }
    }
}
