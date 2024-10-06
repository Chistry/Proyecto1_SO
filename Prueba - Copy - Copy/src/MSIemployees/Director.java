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
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Director extends Thread{
    private String name="Project Manager: ";
    private int salary=60;
    private int ArtiproductionTime;
    private int productionTime;
    private int maxproduction;
    private int production;
    private Semaphore mutex;
    private int totalsalary = 0;
    private boolean anime;
    private int deathline;
    private ProjectManager ProjectManager;
    private int iteraciones;

    public int getTotalsalary() {
        return totalsalary;
    }

    public void setTotalsalary(int totalsalary) {
        this.totalsalary = totalsalary;
    }

    
    
    public Director(Semaphore mutex, int ArtificialProductionTime, ProjectManager ProjectManager, int iteraciones){
        this.name = name;
        this.mutex = mutex;
        this.ArtiproductionTime = ArtificialProductionTime;
        this.salary= salary;
        this.maxproduction =  maxproduction;
        this.productionTime = productionTime;
        this.anime=anime;
        this.deathline=deathline;
        this.ProjectManager=ProjectManager;
        this.iteraciones=iteraciones;
        
    }
    
        
    @Override
    public void run(){
        int originaldeathline = ProjectManager.getDeathline();
        Random random = new Random();
        double spyDuration = ArtiproductionTime*(7 / 288);
        int randomMoment = random.nextInt(ArtiproductionTime);    // Momento aleatorio en el día
        int counter=0;
        
        while(counter!=this.iteraciones){
            try{
                if (this.ProjectManager.getDeathline()==0){
                    System.out.println("Director trabajando en envio");
                    sleep(this.ArtiproductionTime);
                    ProjectManager.setDeathline(originaldeathline);
                    System.out.println("Deathline reiniciada quedan: "+ ProjectManager.getDeathline());
                    totalsalary = (24*salary)+totalsalary;
                } else {
                    // Momento en el que el Director toma el descanso de 35 minutos
                    if (randomMoment < ArtiproductionTime) {
                        System.out.println("Director tomando un descanso de 35 minutos...");
                        sleep((long) spyDuration);  // Director se toma 35 minutos

                        // Verificar si el Project Manager está viendo anime
                        if (ProjectManager.isAnime()) {
                            System.out.println("El Project Manager esta viendo anime durante el descanso.");
                        } else {
                            System.out.println("El Project Manager no está viendo anime.");
                        }

                        // Asignar un nuevo momento aleatorio para el próximo descanso
                        sleep((long) this.ArtiproductionTime-(7 / 288));
                        randomMoment = random.nextInt(this.ArtiproductionTime);
                    }
                }
                
            } catch(InterruptedException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            }
            counter++;
        }
    }
}

