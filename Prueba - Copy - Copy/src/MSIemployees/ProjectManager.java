/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MSIemployees;

/**
 *
 * @author chris
 */
import static java.lang.Thread.sleep;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ProjectManager extends Thread{
    private String name="Project Manager: ";
    private int salary=40;
    private int ArtiproductionTime;
    private int productionTime;
    private int maxproduction;
    private int production;
    private Semaphore mutex;
    private int totalsalary = 0;
    private boolean anime;
    private int deathline;
    private int iteraciones;
    

    public int getDeathline() {
        return deathline;
    }

    public void setDeathline(int deathline) {
        this.deathline = deathline;
    }

    public boolean isAnime() {
        return anime;
    }

    public void setAnime(boolean anime) {
        this.anime = anime;
    }

    public int getTotalsalary() {
        return totalsalary;
    }

    public void setTotalsalary(int totalsalary) {
        this.totalsalary = totalsalary;
    }
    
    
    
    

    
    
    public ProjectManager(Semaphore mutex, int ArtificialProductionTime, int deathline, int iteraciones){
        this.name = name;
        this.mutex = mutex;
        this.ArtiproductionTime = ArtificialProductionTime;
        this.salary= salary;
        this.maxproduction =  maxproduction;
        this.productionTime = productionTime;
        this.anime=anime;
        this.deathline=deathline;
        this.iteraciones=iteraciones;
       
        
        
        
    }
    
        
    @Override
    public void run(){
        
        
        int counter = 0;
        while(counter!=this.iteraciones){
            try{
                anime = true;  // Valor inicial del booleano
                for (int i = 0; i < 32; i++) {
                    

                    sleep(ArtiproductionTime/48);

                    anime = !anime;  // Alterna el valor entre true y false
                }
                // Después de 32 iteraciones, el booleano deja de alternar
                System.out.println(this.name+ " esta en modo focus");
                anime = false;
                //FALTA DINAMICA DE LOS DIAS DE DEATHLINE

                sleep(ArtiproductionTime/3);

                
                totalsalary=(24*salary)+totalsalary;
                
                
                if (deathline==0){
                    this.mutex.acquire(); //wait
                } else {
                    this.mutex.release(); //signal
                    this.deathline = deathline-1;
                }
                
            } catch(InterruptedException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
            counter+=1;
        }
    }
}
