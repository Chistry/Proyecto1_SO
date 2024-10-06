/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package main;


import MSIemployees.*;
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
        
        
        
        Semaphore mutex = new Semaphore(1);
        Worker trab1 = new MBproducer(mutex, milisegundos);
        Worker trab2 = new CPUproducer(mutex, milisegundos);
        Worker trab3 = new RAMproducer(mutex, milisegundos);
        Worker trab4 = new PSproducer(mutex, milisegundos);
        Worker trab5 = new GCproducer(mutex, milisegundos);
        
        Assembler trab6 = new Assembler(mutex, trab1, trab2,trab3,trab4,trab5, milisegundos);
        ProjectManager trab7 = new ProjectManager(mutex, milisegundos, deathline);
        Director trab8 = new Director(mutex, milisegundos, trab7);
        
        
        
        
        trab1.start();
        
        trab2.start();
        trab3.start();
        trab4.start();
        trab5.start();
        trab6.start();
        trab7.start();
        trab8.start();
        
        trab1.timeCounter(totalDays);
        
        
        
        
        
    }
    
}
