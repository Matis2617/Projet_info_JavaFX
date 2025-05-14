/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projet_fx;

/**
 *
 * @author Matis
 */
import java.util.Scanner;

public class Lire {
    private static Scanner scanner = new Scanner(System.in);
    
    public static float f(){
        while (true){
            try {
                return Float.parseFloat(scanner.nextLine());
            } catch (NumberFormatException e){
                System.out.println("Veuillez entrez un nombre valide");
            }
            
        }
    }
    public static String s(){
        return scanner.nextLine();
    }
    public static void close(){
        scanner.close();
    }
}
