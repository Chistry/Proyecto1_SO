/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MSIemployees;

/**
 *
 * @author chris
 */

import EDD.ListaSimple;
import GUI.PantallaMSI;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;



/**
 *
 * @author chris
 */
public class Assembler extends Thread{
    private String name = "Asssembler: ";
    private int salary = 50;
    private int ArtiproductionTime;
    private int productionTime  = 48;
    private int production=0;
    private int productionGC=0;
    private Semaphore mutex;
    private int totalsalary = 0;
    private int numproducts = 1;
    private Worker MBproducer;
    private Worker CPUproducer;
    private Worker RAMproducer;
    private Worker PSproducer;
    private Worker GCproducer;
    private int counter = 0;
    private int iteraciones;
    private ListaSimple<Worker> workersMB;
    private ListaSimple<Worker> workersCPU;
    private ListaSimple<Worker> workersRAM;
    private ListaSimple<Worker> workersPS;
    private ListaSimple<Worker> workersGC;
    private JLabel cpuLabel, gcLabel, psLabel, ramLabel, mbLabel, pcLabel, pcConCpuLabel;

    
    


    public Semaphore getMutex() {
        return mutex;
    }

    public void setMutex(Semaphore mutex) {
        this.mutex = mutex;
    }

    public int getProduction() {
        return production;
    }

    public void setProduction(int production) {
        this.production = production;
    }

    public int getProductionGC() {
        return productionGC;
    }

    public void setProductionGC(int productionGC) {
        this.productionGC = productionGC;
    }

    public int getTotalsalary() {
        return totalsalary;
    }

    public void setTotalsalary(int totalsalary) {
        this.totalsalary = totalsalary;
    }
    
    
    
    public void reduceProduction(int amount) {
        if (this.production >= amount) {
            this.production -= amount;
        } else {
            System.out.println("No hay suficientes productos para descontar.");
        }
    }
    
    private boolean checkWorkersProduction(ListaSimple<Worker> workersList, int requiredProduction) {
        for (int i = 0; i < workersList.size(); i++) {
            Worker worker = workersList.get(i);
            if (worker.getProduction() < requiredProduction) {
                return false;
            }
        }
        return true;
    }
    
    private void reduceWorkersProduction(ListaSimple<Worker> workersList, int amount) {
        for (int i = 0; i < workersList.size(); i++) {
            Worker worker = workersList.get(i);
            worker.reduceProduction(amount);
        }
    }


    private void updateLabels() {
        SwingUtilities.invokeLater(() -> {
            cpuLabel.setText("CPU: " + workersCPU.size());
            gcLabel.setText("GC: " + productionGC);
            psLabel.setText("PS: " + workersPS.size());
            ramLabel.setText("RAM: " + workersRAM.size());
            mbLabel.setText("MB: " + workersMB.size());
            pcLabel.setText("PC: " + production);
            pcConCpuLabel.setText("PC con CPU: " + (production + productionGC)); // Ejemplo de cálculo
        });
    }
    
        

public Assembler(Semaphore mutex, ListaSimple<Worker> workersListMB, ListaSimple<Worker> workersListCPU, ListaSimple<Worker> workersListRAM, ListaSimple<Worker> workersListPS,  ListaSimple<Worker> workersListGC, int ArtificialTime, int iteraciones, JLabel cpuLabel, JLabel gcLabel, JLabel psLabel,  JLabel ramLabel, JLabel mbLabel, JLabel pcLabel, JLabel pcConCpuLabel) {
    this.mutex = mutex;
    this.ArtiproductionTime = ArtificialTime;
    this.iteraciones = iteraciones;
    this.workersMB = workersListMB;
    this.workersCPU = workersListCPU;
    this.workersRAM = workersListRAM;
    this.workersPS = workersListPS;
    this.workersGC = workersListGC;

    // Agregar referencias a los JLabels
    this.cpuLabel = cpuLabel;
    this.gcLabel = gcLabel;
    this.psLabel = psLabel;
    this.ramLabel = ramLabel;
    this.mbLabel = mbLabel;
    this.pcLabel = pcLabel;
    this.pcConCpuLabel = pcConCpuLabel;
}

    
    
        
    @Override
    public void run() {
        int counterIte = 0;
        while(counterIte != this.iteraciones) {
            try {
                // Verificar que cada lista de trabajadores tiene suficiente producción
                boolean canAssemble = true;

                // Verificar si la lista de cada componente tiene trabajadores con producción suficiente
                if (!checkWorkersProduction(workersMB, 2) || 
                    !checkWorkersProduction(workersCPU, 3) ||
                    !checkWorkersProduction(workersRAM, 4) || 
                    !checkWorkersProduction(workersPS, 6)) {
                    updateLabels();
                    canAssemble = false;
                }
                

                if (canAssemble) {
                    // Todos los trabajadores tienen suficiente producción, procedemos a ensamblar
                    this.mutex.release(); // Signal

                    // Ensamblar un producto y reducir la producción de los trabajadores
                    production++;
                    reduceWorkersProduction(workersMB, 2);
                    reduceWorkersProduction(workersCPU, 3);
                    reduceWorkersProduction(workersRAM, 4);
                    reduceWorkersProduction(workersPS, 6);

                    sleep(this.ArtiproductionTime * 2);
                    totalsalary = (salary * productionTime) + totalsalary;

                    System.out.println(this.name);
                    System.out.println("Salario: " + this.totalsalary);
                    System.out.println("Computadores con Graficas: " + this.productionGC);
                    System.out.println("ElementosFabricados: " + this.production + "\n");
                    updateLabels();

                    counter++;
                } else {
                    // Si no hay suficiente producción, espera
                    this.mutex.acquire(); // Wait
                    sleep(this.ArtiproductionTime);
                    totalsalary = (salary * 24) + totalsalary;

                    System.out.println(this.name);
                    System.out.println("Salario: " + this.totalsalary);
                    System.out.println("Computadores con Graficas: " + this.productionGC);
                    System.out.println("ElementosFabricados: " + this.production + "\n");
                    updateLabels();
                }

                // Si el contador alcanza 6, intentamos ensamblar con tarjeta gráfica
                if (counter >= 6) {
                    boolean canAssembleWithGraphics = true;

                    // Verificar si todas las listas de trabajadores tienen producción suficiente, incluyendo las tarjetas gráficas
                    if (!checkWorkersProduction(workersMB, 2) || 
                        !checkWorkersProduction(workersCPU, 3) ||
                        !checkWorkersProduction(workersRAM, 4) || 
                        !checkWorkersProduction(workersPS, 6) ||
                        !checkWorkersProduction(workersGC, 5)) {
                        canAssembleWithGraphics = false;
                        updateLabels();
                    }

                    if (canAssembleWithGraphics) {
                        // Ensamblar con tarjeta gráfica
                        counter = 0;
                        productionGC++;
                        this.mutex.release(); // Signal

                        // Reducir la producción de todos los componentes, incluyendo la tarjeta gráfica
                        reduceWorkersProduction(workersMB, 2);
                        reduceWorkersProduction(workersCPU, 3);
                        reduceWorkersProduction(workersRAM, 4);
                        reduceWorkersProduction(workersPS, 6);
                        reduceWorkersProduction(workersGC, 5);

                        sleep(this.ArtiproductionTime * 2);
                        totalsalary = (salary * productionTime) + totalsalary;

                        System.out.println(this.name);
                        System.out.println("Salario: " + this.totalsalary);
                        System.out.println("Computadores con Graficas: " + this.productionGC);
                        System.out.println("ElementosFabricados: " + this.production + "\n");
                        updateLabels();
                    } else {
                        this.mutex.acquire(); // Wait
                        sleep(this.ArtiproductionTime);
                        totalsalary = (salary * 24) + totalsalary;

                        System.out.println(this.name);
                        System.out.println("Salario: " + this.totalsalary);
                        System.out.println("Computadores con Graficas: " + this.productionGC);
                        System.out.println("ElementosFabricados: " + this.production + "\n");
                        System.out.println("No hay suficientes componentes para ensamblar con tarjeta gráfica");
                        updateLabels();
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Assembler.class.getName()).log(Level.SEVERE, null, ex);
            }

            counterIte++;
        }
    }

}
