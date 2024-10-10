/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DellTrabajadores;
import static java.lang.Thread.sleep;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author diego
 */
public class Director extends Thread {
    private String nombre= "Director: ";
    private int salario=60;
    private int tiempoProduArti;
    private int tiempoProdu;
    private int maxprodu;
    private int produccion;
    private Semaphore mutex;
    private int salariototal = 0;
    private boolean anime;
    private int limiteDirector;
    private ProjectManager ProjectManager;
    private Ensamblador Ensamblador;
    private int iteraciones;
    private int ventas=0;

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
     * @return the limiteDirector
     */
    public int getLimiteDirector() {
        return limiteDirector;
    }

    /**
     * @param limiteDirector the limiteDirector to set
     */
    public void setLimiteDirector(int limiteDirector) {
        this.limiteDirector = limiteDirector;
    }

    /**
     * @return the ventas
     */
    public int getVentas() {
        return ventas;
    }

    /**
     * @param ventas the ventas to set
     */
    public void setVentas(int ventas) {
        this.ventas = ventas;
    }
    public Director(Semaphore mutex, int tiempoPorduArti, ProjectManager ProjectManager, Ensamblador ensamblador, int iteraciones){
        this.nombre = nombre;
        this.mutex = mutex;
        this.tiempoProduArti = tiempoProduArti;
        this.salario= salario;
        this.maxprodu =  maxprodu;
        this.tiempoProdu = tiempoProdu;
        this.anime=anime;
        this.limiteDirector=limiteDirector;
        this.ProjectManager=ProjectManager;
        this.iteraciones=iteraciones;
        this.ventas = ventas;
        this.Ensamblador=ensamblador;
        
    }
    
    @Override
public void run() {
    // Guardamos el valor inicial de deathline en Director
    int limiteOriginal = ProjectManager.getLimite();
    limiteDirector= ProjectManager.getLimite();
    Random random = new Random();
    double DuracionEspia = tiempoProduArti * (7 / 288);
    int randomMoment = random.nextInt(tiempoProduArti);
    int contador = 0;

    while (contador != this.iteraciones) {
        try {
            if (limiteDirector == 0) {
                try {
                    mutex.acquire();
                    if (limiteDirector == 0) {
                        // El director procesa la venta cuando deathline llega a 0
                        System.out.println("Director trabajando en envio");
                        sleep(this.tiempoProduArti);

                        // Realiza la venta de computadoras si hay producción
                        if (this.Ensamblador.getProduccionGPU() > 0 || this.Ensamblador.getProduccion() > 0) {
                            if (this.Ensamblador.getProduccionGPU()> 0) {
                                this.Ensamblador.setProduccionGPU(this.Ensamblador.getProduccionGPU()- 1);
                                ventas += 250000;
                            } else {
                                this.Ensamblador.setProduccion(this.Ensamblador.getProduccion()- 1);
                                ventas += 180000;
                            }
                        }

                        System.out.println("Ventas realizadas: " + ventas);

                        // Reiniciar la deathline después de la venta
                        ProjectManager.setLimite(limiteOriginal);
                        System.out.println("Reiniciando la DEATHLINE a: " + limiteOriginal);
                    }
                } finally {
                    mutex.release();
                }
            } else {
                // Descanso aleatorio del director
                if (randomMoment < tiempoProduArti) {
                    System.out.println("Director tomando un descanso de 35 minutos...");
                    sleep((long) DuracionEspia);

                    // Verificar si el Project Manager está viendo anime
                    if (ProjectManager.isAnime()) {
                        System.out.println("El Project Manager esta viendo anime durante el descanso.");
                        ProjectManager.setSalariototal(ProjectManager.getSalariototal() - 100);
                    } else {
                        System.out.println("El Project Manager no esta viendo anime.");
                    }

                    // Asignar un nuevo momento aleatorio para el próximo descanso
                    sleep((long) this.tiempoProduArti - (7 / 288));
                    randomMoment = random.nextInt(this.tiempoProduArti);
                    limiteDirector-=1;
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Trabajador.class.getName()).log(Level.SEVERE, null, ex);
        }
        contador++;
    }
}
    
}
