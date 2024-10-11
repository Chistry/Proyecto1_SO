/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DellTrabajadores;

import EDD.ListaSimple;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author diego
 */
public class Ensamblador extends Thread{
    private String nombre = "Ensamblador: ";
    private int salario = 50;
    private int tiempoProduArti;
    private int tiempoProdu = 48;
    private int produccion=0;
    private int produccionGPU=0;
    private Semaphore mutex;
    private int salariototal = 0;
    private int numunidades = 1;
    private Trabajador PMtrabajador;
    private Trabajador CPUtrabajador;
    private Trabajador RAMtrabajador;
    private Trabajador FAtrabajador;
    private Trabajador GPUtrabajador;
    private int contador = 0;
    private int iteraciones;
    private ListaSimple<Trabajador> PMtrabajadores;
    private ListaSimple<Trabajador> CPUtrabajadores;
    private ListaSimple<Trabajador> RAMtrabajadores;
    private ListaSimple<Trabajador> FAtrabajadores;
    private ListaSimple<Trabajador> GPUtrabajadores;

    /**
     * @return the produccion
     */
    public int getProduccion() {
        return produccion;
    }

    /**
     * @param produccion the produccion to set
     */
    public void setProduccion(int produccion) {
        this.produccion = produccion;
    }

    /**
     * @return the produccionGPU
     */
    public int getProduccionGPU() {
        return produccionGPU;
    }

    /**
     * @param produccionGPU the produccionGPU to set
     */
    public void setProduccionGPU(int produccionGPU) {
        this.produccionGPU = produccionGPU;
    }

    /**
     * @return the mutex
     */
    public Semaphore getMutex() {
        return mutex;
    }

    /**
     * @param mutex the mutex to set
     */
    public void setMutex(Semaphore mutex) {
        this.mutex = mutex;
    }

    /**
     * @return the salariototal
     */
    public int getSalariototal() {
        return salariototal;
    }

    /**
     * @param salariototal the salariototal to set
     */
    public void setSalariototal(int salariototal) {
        this.salariototal = salariototal;
    }
    
    public void reduceProduction(int cantidad){
        if(this.produccion >= cantidad){
            this.produccion -= cantidad;
        }else{
            System.out.println("No hay suficientes productos para descontar.");
        }
    }
    
    private boolean verificarTrabajadoresProdu(ListaSimple<Trabajador> listaTrabajadores, int Produrequerida) {
        for (int i = 0; i < listaTrabajadores.size(); i++) {
            Trabajador trabajador = listaTrabajadores.get(i);
            if (trabajador.getProduccion() < Produrequerida) {
                return false;
            }
        }
        return true;
    }
    
    private void reduceWorkersProduction(ListaSimple<Trabajador> listaTrabajadores, int qty) {
        for (int i = 0; i < listaTrabajadores.size(); i++) {
            Trabajador trabajador = listaTrabajadores.get(i);
            trabajador.reducirProdu(qty);
        }
         
    }
    
    
    
    public Ensamblador(Semaphore mutex, ListaSimple<Trabajador> listaPMtrabajadores, ListaSimple<Trabajador> listaCPUtrabajadores,ListaSimple<Trabajador> listaRAMtrabajadores, ListaSimple<Trabajador> listaFAtrabajadores, ListaSimple<Trabajador> listaGPUtrabajadores, int ArtificialTime, int iteraciones){
        this.mutex = mutex;
        this.PMtrabajador = PMtrabajador;
        this.CPUtrabajador = CPUtrabajador;
        this.RAMtrabajador = RAMtrabajador;
        this.FAtrabajador = FAtrabajador;
        this.GPUtrabajador = GPUtrabajador;
        this.tiempoProduArti = ArtificialTime;
        this.iteraciones = iteraciones;
        this.PMtrabajadores = listaPMtrabajadores;
        this.CPUtrabajadores = listaCPUtrabajadores;
        this.RAMtrabajadores = listaRAMtrabajadores;
        this.FAtrabajadores = listaFAtrabajadores;
        this.GPUtrabajadores = listaGPUtrabajadores;
       
    }
    

    public void run() {
        int counterIte = 0;
        while(counterIte != this.iteraciones) {
            try {
                // Verificar que cada lista de trabajadores tiene suficiente producción
                boolean canAssemble = true;

                // Verificar si la lista de cada componente tiene trabajadores con producción suficiente
                if (!verificarTrabajadoresProdu(PMtrabajadores, 1) || 
                    !verificarTrabajadoresProdu(CPUtrabajadores, 5) ||
                    !verificarTrabajadoresProdu(RAMtrabajadores, 6) || 
                    !verificarTrabajadoresProdu(FAtrabajadores, 5)) {
                    canAssemble = false;
                }

                if (canAssemble) {
                    // Todos los trabajadores tienen suficiente producción, procedemos a ensamblar
                    this.mutex.release(); // Signal

                    // Ensamblar un producto y reducir la producción de los trabajadores
                    produccion++;
                    reduceWorkersProduction(PMtrabajadores, 1);
                    reduceWorkersProduction(CPUtrabajadores, 5);
                    reduceWorkersProduction(RAMtrabajadores, 6);
                    reduceWorkersProduction(FAtrabajadores, 5);

                    sleep(this.tiempoProduArti * 2);
                    salariototal = (salario * tiempoProdu) + salariototal;

                    

                    contador++;
                } else {
                    // Si no hay suficiente producción, espera
                    this.mutex.acquire(); // Wait
                    sleep(this.tiempoProduArti);
                    salariototal = (salario * 24) + salariototal;

                    
                }

                // Si el contador alcanza 6, intentamos ensamblar con tarjeta gráfica
                if (contador >= 3) {
                    boolean canAssembleWithGraphics = true;

                    // Verificar si todas las listas de trabajadores tienen producción suficiente, incluyendo las tarjetas gráficas
                    if (!verificarTrabajadoresProdu(PMtrabajadores, 1) || 
                        !verificarTrabajadoresProdu(CPUtrabajadores, 5) ||
                        !verificarTrabajadoresProdu(RAMtrabajadores, 6) || 
                        !verificarTrabajadoresProdu(FAtrabajadores, 5) ||
                        !verificarTrabajadoresProdu(GPUtrabajadores, 1)) {
                        canAssembleWithGraphics = false;
                    }

                    if (canAssembleWithGraphics) {
                        // Ensamblar con tarjeta gráfica
                        contador = 0;
                        produccionGPU++;
                        this.mutex.release(); // Signal

                        // Reducir la producción de todos los componentes, incluyendo la tarjeta gráfica
                        reduceWorkersProduction(PMtrabajadores, 1);
                        reduceWorkersProduction(CPUtrabajadores, 5);
                        reduceWorkersProduction(RAMtrabajadores, 6);
                        reduceWorkersProduction(FAtrabajadores, 5);
                        reduceWorkersProduction(GPUtrabajadores, 1);

                        sleep(this.tiempoProduArti * 2);
                        salariototal = (salario * tiempoProdu) + salariototal;

                        
                    } else {
                        this.mutex.acquire(); // Wait
                        sleep(this.tiempoProduArti);
                        salariototal = (salario * 24) + salariototal;

                        
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Ensamblador.class.getName()).log(Level.SEVERE, null, ex);
            }

            counterIte++;
        }
    }
}
