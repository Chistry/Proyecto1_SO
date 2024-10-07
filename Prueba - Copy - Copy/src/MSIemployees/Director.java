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
    private int deathlineDirector;
    private ProjectManager ProjectManager;
    private Assembler Assembler;
    private int iteraciones;
    private int ventas=0;

    public int getTotalsalary() {
        return totalsalary;
    }

    public void setTotalsalary(int totalsalary) {
        this.totalsalary = totalsalary;
    }

    public int getVentas() {
        return ventas;
    }

    public void setVentas(int ventas) {
        this.ventas = ventas;
    }

    public int getDeathlineDirector() {
        return deathlineDirector;
    }

    public void setDeathlineDirector(int deathlineDirector) {
        this.deathlineDirector = deathlineDirector;
    }
    
    
    
    
    public Director(Semaphore mutex, int ArtificialProductionTime, ProjectManager ProjectManager, Assembler assembler, int iteraciones){
        this.name = name;
        this.mutex = mutex;
        this.ArtiproductionTime = ArtificialProductionTime;
        this.salary= salary;
        this.maxproduction =  maxproduction;
        this.productionTime = productionTime;
        this.anime=anime;
        this.deathlineDirector=deathlineDirector;
        this.ProjectManager=ProjectManager;
        this.iteraciones=iteraciones;
        this.ventas = ventas;
        this.Assembler=assembler;
        
    }
    
        
    @Override
    // Eliminar el uso de originaldeathline dentro del ciclo del director

public void run() {
    // Guardamos el valor inicial de deathline en Director
    int originaldeathline = ProjectManager.getDeathline();
    deathlineDirector= ProjectManager.getDeathline();
    Random random = new Random();
    double spyDuration = ArtiproductionTime * (7 / 288);
    int randomMoment = random.nextInt(ArtiproductionTime);
    int counter = 0;

    while (counter != this.iteraciones) {
        try {
            if (deathlineDirector == 0) {
                try {
                    mutex.acquire();
                    if (deathlineDirector == 0) {
                        // El director procesa la venta cuando deathline llega a 0
                        System.out.println("Director trabajando en envio");
                        sleep(this.ArtiproductionTime);

                        // Realiza la venta de computadoras si hay producción
                        if (this.Assembler.getProductionGC() > 0 || this.Assembler.getProduction() > 0) {
                            if (this.Assembler.getProductionGC() > 0) {
                                this.Assembler.setProductionGC(this.Assembler.getProductionGC() - 1);
                                ventas += 250000;
                            } else {
                                this.Assembler.setProduction(this.Assembler.getProduction() - 1);
                                ventas += 180000;
                            }
                        }

                        System.out.println("Ventas realizadas: " + ventas);

                        // Reiniciar la deathline después de la venta
                        ProjectManager.setDeathline(originaldeathline);
                        System.out.println("Reiniciando la DEATHLINE a: " + originaldeathline);
                    }
                } finally {
                    mutex.release();
                }
            } else {
                // Descanso aleatorio del director
                if (randomMoment < ArtiproductionTime) {
                    System.out.println("Director tomando un descanso de 35 minutos...");
                    sleep((long) spyDuration);

                    // Verificar si el Project Manager está viendo anime
                    if (ProjectManager.isAnime()) {
                        System.out.println("El Project Manager esta viendo anime durante el descanso.");
                    } else {
                        System.out.println("El Project Manager no esta viendo anime.");
                    }

                    // Asignar un nuevo momento aleatorio para el próximo descanso
                    sleep((long) this.ArtiproductionTime - (7 / 288));
                    randomMoment = random.nextInt(this.ArtiproductionTime);
                    deathlineDirector-=1;
                }
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
        counter++;
    }
}


}

