/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;

import DellTrabajadores.*;
import GUI.PantallaDell;

import static java.lang.Thread.sleep;
import java.util.Scanner;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author chris
 */
public class ProductoresComputadoras {       
    private int dias=0;
    private int contador;
    
    
    /**
     */
    
    
    
    public static void main(String[] args) {
        
           
        PantallaDell pantadell = new PantallaDell();
        pantadell.setVisible(true);
        pantadell.setLocationRelativeTo(null);
       /** Scanner input = new Scanner(System.in);


        System.out.println("Por favor, ingrese un número de milisegundos:");
        System.out.println("Recuerda que 1000 milisegundos son 1 segundo.");
        

        int milisegundos = input.nextInt();
        double segundos = milisegundos / 1000.0;
        System.out.println(milisegundos + " milisegundos son " + segundos + " segundos.");

        System.out.println("Por favor, ingrese el número de días:");
        int diastotales = input.nextInt();
        System.out.println("Numero de dias ingresados: " + diastotales);
        
        System.out.println("Por favor, inserte la DEATHLINE: ");
        int limite = input.nextInt();
        System.out.println("DEATHLINE en "+ limite +" dias.");

        input.close();
        
        
        
        Semaphore mutex = new Semaphore(1);
        
        Trabajador trab1 = new PBtrabajador(mutex, milisegundos, diastotales);
        Trabajador trab2 = new CPUtrabajador(mutex, milisegundos,diastotales);
        Trabajador trab3 = new RAMtrabajador(mutex, milisegundos,diastotales);
        Trabajador trab4 = new FAtrabajador(mutex, milisegundos,diastotales);
        Trabajador trab5 = new GPUtrabajador(mutex, milisegundos, diastotales);
        Ensamblador trab6 = new Ensamblador(mutex, trab1, trab2,trab3,trab4,trab5, milisegundos, diastotales);
        
        
        ProjectManager trab7 = new ProjectManager(mutex, milisegundos, limite, diastotales);
        Director trab8 = new Director(mutex, milisegundos, trab7, trab6 ,diastotales);
        
        
        
        
        trab1.start();
        
        trab2.start();
        trab3.start();
        trab4.start();
        trab5.start();
        trab6.start();
        trab7.start();
        trab8.start();
        
        trab1.contadorTiempo(diastotales);
        
        try {
            // Esperar que todos los hilos terminen
            trab1.join();
            trab2.join();
            trab3.join();
            trab4.join();
            trab5.join();
            trab6.join();
            trab7.join();
            trab8.join();
        } catch (InterruptedException e) {
            Logger.getLogger(ProductoresComputadoras.class.getName()).log(Level.SEVERE, null, e);
        }

        int gananciaBruta= trab8.getVentas();
        int costosOperativos=trab1.getSalariototal()+trab2.getSalariototal()+trab3.getSalariototal()+trab4.getSalariototal()+trab5.getSalariototal()+trab6.getSalariototal()+trab7.getSalariototal()+trab8.getSalariototal();
        int UtilidadEstudio= gananciaBruta-costosOperativos;

        System.out.println("\n\nGanancia Bruta: "+gananciaBruta+"\nCostos Operativos: " + costosOperativos + "\nUtilidad del estudio: "+UtilidadEstudio);
        
        */
        
        
        
    }
    
}
