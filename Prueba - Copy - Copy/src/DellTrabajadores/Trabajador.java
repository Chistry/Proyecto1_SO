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
public class Trabajador extends Thread {
    private String nombre;
    private int salario;
    private int tiempoProduArti ;
    private int tiempoProdu;
    private int maxprodu;
    private int produccion;
    private Semaphore mutex;
    private int salariototal = 0;
    private int numunidades;
    private int dias=0;
    private int inmutableTime;
    private int iteraciones;

    /**
     * @return the nombre
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * @param nombre the nombre to set
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * @return the salario
     */
    public int getSalario() {
        return salario;
    }

    /**
     * @param salario the salario to set
     */
    public void setSalario(int salario) {
        this.salario = salario;
    }

    /**
     * @return the tiempoproduarti
     */
    public int getTiempoProduArti() {
        return tiempoProduArti;
    }

    /**
     * @param tiempoProduArti the tiempoproduarti to set
     */
    public void setTiempoProduArti(int tiempoProduArti) {
        this.tiempoProduArti = tiempoProduArti;
    }

    /**
     * @return the tiempoprodu
     */
    public int getTiempoProdu() {
        return tiempoProdu;
    }

    /**
     * @param tiempoProdu the tiempoprodu to set
     */
    public void setTiempoProdu(int tiempoProdu) {
        this.tiempoProdu = tiempoProdu;
    }

    /**
     * @return the maxprodu
     */
    public int getMaxprodu() {
        return maxprodu;
    }

    /**
     * @param maxprodu the maxprodu to set
     */
    public void setMaxprodu(int maxprodu) {
        this.maxprodu = maxprodu;
    }

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

    /**
     * @return the numunidades
     */
    public int getNumunidades() {
        return numunidades;
    }

    /**
     * @param numunidades the numunidades to set
     */
    public void setNumunidades(int numunidades) {
        this.numunidades = numunidades;
    }

    /**
     * @return the dias
     */
    public int getDias() {
        return dias;
    }

    /**
     * @param dias the dias to set
     */
    public void setDias(int dias) {
        this.dias = dias;
    }

    /**
     * @return the inmutableTime
     */
    public int getInmutableTime() {
        return inmutableTime;
    }

    /**
     * @param inmutableTime the inmutableTime to set
     */
    public void setInmutableTime(int inmutableTime) {
        this.inmutableTime = inmutableTime;
    }

    /**
     * @return the iteraciones
     */
    public int getIteraciones() {
        return iteraciones;
    }

    /**
     * @param iteraciones the iteraciones to set
     */
    public void setIteraciones(int iteraciones) {
        this.iteraciones = iteraciones;
    }

    
    
    public void reducirProdu(int cantidad){
        if (this.produccion >= cantidad) {
            this.produccion -= cantidad;
        } else {
            System.out.println("No hay suficientes unidades para descontar");
        }
    }
    
    public Trabajador (String nombre, Semaphore mutex, int tiempoProduArti, int tiempoProdu, int salario, int maxprodu, int numunidades, int inmutableTime, int iteraciones){
        this.nombre = nombre;
        this.mutex = mutex;
        this.tiempoProduArti = tiempoProduArti;
        this.tiempoProdu = tiempoProdu;
        this.salario = salario;
        this.maxprodu = maxprodu;
        this.numunidades = numunidades;
        this.inmutableTime = inmutableTime;
        this.iteraciones = iteraciones;
    }
    
    public void contadorTiempo(int diasTotal){
        while(dias != diasTotal){
            try{
            sleep(this.inmutableTime);
            dias = ++dias;
            
                System.out.println("Dia: "+ this.dias);
            } catch (InterruptedException ex){
                Logger.getLogger(Trabajador.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    }
    
    @Override
    public void run(){
        int contador = 0;
        while(contador != this.iteraciones){
            try{
                if (produccion < maxprodu){
                    this.mutex.release();
                    
                    sleep(tiempoProduArti);
                    produccion = numunidades + produccion;
                    salariototal = (salario*tiempoProdu)+salariototal;
                    
                }
                if (produccion >= maxprodu){
                    
                    this.mutex.acquire();
                    produccion = maxprodu;
                    sleep(inmutableTime);
                    
                    salariototal = (salario*24) + salariototal;
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Trabajador.class.getName()).log(Level.SEVERE, null, ex);
            }
            contador+=1;
        }
    }
    
}
