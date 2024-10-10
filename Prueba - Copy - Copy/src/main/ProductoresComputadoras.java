/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;


import MSIemployees.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
    public static void guardarParametros(double segundos, int deathline, int n) {
        File file = new File("parametros.txt");

        try {
            // Crear el archivo si no existe
            if (!file.exists()) {
                file.createNewFile();
            }

            // Usar FileWriter para escribir en el archivo
            FileWriter writer = new FileWriter(file, true); // 'true' para agregar sin sobrescribir

            writer.write("Segundos: " + segundos + "\n");
            writer.write("Deathline: " + deathline + "\n");
            writer.write("N: " + n + "\n");
            writer.write("-------------------------\n");

            writer.close();

            System.out.println("Parámetros guardados en parametros.txt");

        } catch (IOException e) {
            System.out.println("Ocurrió un error al guardar los parámetros.");
            e.printStackTrace();
        }
    }
    
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);


        System.out.println("Por favor, ingrese un número de milisegundos:");
        System.out.println("Recuerda que 1000 milisegundos son 1 segundo.");
        

        int milisegundos = input.nextInt();
        double segundos = milisegundos / 1000.0;
        System.out.println(milisegundos + " milisegundos son " + segundos + " segundos.");

        System.out.println("Por favor, ingrese el número de días:");
        int totalDays = input.nextInt();
        System.out.println("Numero de dias ingresados: " + totalDays);
        
        System.out.println("Por favor, inserte la DEATHLINE: ");
        int deathline = input.nextInt();
        System.out.println("DEATHLINE en "+ deathline +" dias.");

        input.close();
        
        int n = 0;
        
        
        
        Semaphore mutex = new Semaphore(1);
        
        Worker trab1 = new MBproducer(mutex, milisegundos, totalDays);
        Worker trab2 = new CPUproducer(mutex, milisegundos,totalDays);
        Worker trab3 = new RAMproducer(mutex, milisegundos,totalDays);
        Worker trab4 = new PSproducer(mutex, milisegundos,totalDays);
        Worker trab5 = new GCproducer(mutex, milisegundos, totalDays);
        Assembler trab6 = new Assembler(mutex, trab1, trab2,trab3,trab4,trab5, milisegundos, totalDays);
        
        
        ProjectManager trab7 = new ProjectManager(mutex, milisegundos, deathline, totalDays);
        Director trab8 = new Director(mutex, milisegundos, trab7, trab6 ,totalDays);
        
        
        
        
        trab1.start();
        
        trab2.start();
        trab3.start();
        trab4.start();
        trab5.start();
        trab6.start();
        trab7.start();
        trab8.start();
        
        trab1.timeCounter(totalDays);
        
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
        int costosOperativos=trab1.getTotalsalary()+trab2.getTotalsalary()+trab3.getTotalsalary()+trab4.getTotalsalary()+trab5.getTotalsalary()+trab6.getTotalsalary()+trab7.getTotalsalary()+trab8.getTotalsalary();
        int UtilidadEstudio= gananciaBruta-costosOperativos;

        System.out.println("\n\nGanancia Bruta: "+gananciaBruta+"\nCostos Operativos: " + costosOperativos + "\nUtilidad del estudio: "+UtilidadEstudio);
        
        
        guardarParametros(segundos, deathline, n);
        
        
    }
    
}
