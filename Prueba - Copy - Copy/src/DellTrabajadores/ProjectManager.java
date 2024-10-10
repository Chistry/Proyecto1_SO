/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DellTrabajadores;
import static java.lang.Thread.sleep;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author diego
 */
public class ProjectManager extends Thread {
    private String nombre="Project Manager: ";
    private int salario=40;
    private int tiempoProduArti;
    private int tiempoProdu;
    private int maxprodu;
    private int produccion;
    private Semaphore mutex;
    private int salariototal = 0;
    private boolean anime;
    private int limite;
    private int iteraciones;

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
     * @return the anime
     */
    public boolean isAnime() {
        return anime;
    }

    /**
     * @param anime the anime to set
     */
    public void setAnime(boolean anime) {
        this.anime = anime;
    }

    /**
     * @return the limite
     */
    public int getLimite() {
        return limite;
    }

    /**
     * @param limite the limite to set
     */
    public void setLimite(int limite) {
        this.limite = limite;
    }
    
    public ProjectManager(Semaphore mutex, int tiempoProduArti, int limite, int iteraciones){
        this.nombre = nombre;
        this.mutex = mutex;
        this.tiempoProduArti = tiempoProduArti;
        this.salario= salario;
        this.maxprodu =  maxprodu;
        this.tiempoProdu = tiempoProdu;
        this.anime=anime;
        this.limite=limite;
        this.iteraciones=iteraciones;
    }
    @Override
    public void run(){
        
        
        int contador = 0;
        while(contador!=this.iteraciones){
            try{
                anime = true;  // Valor inicial del booleano
                for (int i = 0; i < 32; i++) {
                    

                    sleep(tiempoProduArti/48);

                    anime = !anime;  // Alterna el valor entre true y false
                }
                // Después de 32 iteraciones, el booleano deja de alternar
                System.out.println(this.nombre+ " esta en modo focus");
                anime = false;
                

                sleep(tiempoProduArti/3);

                
                salariototal=(24*salario)+salariototal;
                
                
                if (limite==0){
                    this.mutex.acquire(); //wait
                } else {
                    this.mutex.release(); //signal
                    this.limite -= 1;
                    
                }
                
            } catch(InterruptedException ex) {
                Logger.getLogger(Trabajador.class.getName()).log(Level.SEVERE, null, ex);
            }
            contador+=1;
        }
    }
}
