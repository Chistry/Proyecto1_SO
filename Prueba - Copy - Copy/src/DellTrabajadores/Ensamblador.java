/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DellTrabajadores;

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
    private Trabajador PBtrabajador;
    private Trabajador CPUtrabajador;
    private Trabajador RAMtrabajador;
    private Trabajador FAtrabajador;
    private Trabajador GPUtrabajador;
    private int contador = 0;
    private int iteraciones;

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
    
    public Ensamblador(Semaphore mutex, Trabajador PBtrabajador, Trabajador CPUtrabajador, Trabajador RAMtrabajador, Trabajador FAtrabajador, Trabajador GPUtrabajador, int ArtificialTime, int iteraciones){
        this.mutex = mutex;
        this.PBtrabajador = PBtrabajador;
        this.CPUtrabajador = CPUtrabajador;
        this.RAMtrabajador = RAMtrabajador;
        this.FAtrabajador = FAtrabajador;
        this.GPUtrabajador = GPUtrabajador;
        this.tiempoProduArti = ArtificialTime;
        this.iteraciones = iteraciones;
       
    }
    
    @Override
    public void run(){
        int contadorIte = 0;
        while(contadorIte != this.iteraciones){
            try{
                if (contador<3){
                    if(PBtrabajador.getProduccion() >= 1 && CPUtrabajador.getProduccion() >= 5 && RAMtrabajador.getProduccion()>=6 && FAtrabajador.getProduccion()>=5){
                        this.mutex.release();
                        
                        produccion = ++produccion;
                        PBtrabajador.reducirProdu(1);
                        CPUtrabajador.reducirProdu(5);
                        RAMtrabajador.reducirProdu(6);
                        FAtrabajador.reducirProdu(5);
                        
                        sleep(this.tiempoProduArti*2);
                        salariototal=(salario*tiempoProdu)+salariototal;
                        contador= ++contador;
                    }else{
                        this.mutex.acquire();
                        sleep(this.tiempoProduArti);
                        salariototal=(salario*24)+salariototal;
                        
                    }
                } else{
                    if (PBtrabajador.getProduccion()>=1 && CPUtrabajador.getProduccion()>=5 && RAMtrabajador.getProduccion() >=6 && FAtrabajador.getProduccion() >= 5 && GPUtrabajador.getProduccion()>=1){
                        contador=0;
                        produccionGPU = ++produccionGPU;
                        this.mutex.release();
                        
                        PBtrabajador.reducirProdu(1);
                        CPUtrabajador.reducirProdu(5);
                        RAMtrabajador.reducirProdu(6);
                        FAtrabajador.reducirProdu(5);
                        GPUtrabajador.reducirProdu(1);
                        
                        sleep(this.tiempoProduArti*2);
                        salariototal = (salario*tiempoProdu)+salariototal;
                        
                    }else{
                        this.mutex.acquire();
                        sleep(this.tiempoProduArti);
                        salariototal=(salario*24)+salariototal;
                    } 
                    }
                } catch(InterruptedException ex) {
                Logger.getLogger(Ensamblador.class.getName()).log(Level.SEVERE, null, ex);
            }
            contadorIte +=1;
            }
        
    }
}
