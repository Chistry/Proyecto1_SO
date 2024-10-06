/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MSIemployees;

/**
 *
 * @author chris
 */

import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;



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
    
    
    
    

    
    
    public Assembler(Semaphore mutex, Worker MBproducer, Worker CPUproducer, Worker RAMproducer, Worker PSproducer, Worker GCproducer, int ArtificialTime){
        this.mutex = mutex;
        this.MBproducer = MBproducer;
        this.CPUproducer = CPUproducer;
        this.RAMproducer = RAMproducer;
        this.PSproducer = PSproducer;
        this.GCproducer = GCproducer;
        this.ArtiproductionTime= ArtificialTime;
    }
    
    
        
    @Override
    public void run(){
        while(true){
            try{
                if (counter < 6){
                    if (MBproducer.getProduction() >= 2 && CPUproducer.getProduction() >= 3 && RAMproducer.getProduction() >= 4 && PSproducer.getProduction() >= 6){
                        this.mutex.release(); //signal
                        
                        
                        production = ++production;
                        MBproducer.reduceProduction(2);
                        CPUproducer.reduceProduction(3);
                        RAMproducer.reduceProduction(4);
                        PSproducer.reduceProduction(6);
                        
                        sleep(ArtiproductionTime*2);
                        totalsalary= (salary*productionTime) + totalsalary;
                        System.out.println(this.name);
                        System.out.println("Salario: "+this.totalsalary);
                        System.out.println("Computadores con Graficas: "+ this.productionGC);
                        System.out.println("ElementosFabricados: "+ this.production + "\n");
                        
                        counter=++counter;
                    } else {
                        

                        this.mutex.acquire(); //wait
                        sleep(this.ArtiproductionTime);
                        totalsalary= (salary*24) + totalsalary;
                        
                        System.out.println(this.name);
                        System.out.println("Salario: "+this.totalsalary);
                        System.out.println("Computadores con Graficas: "+ this.productionGC);
                        System.out.println("ElementosFabricados: "+ this.production + "\n");
     
                    }
                 
                } else {
                        
                        
                        
                        
                        if (MBproducer.getProduction() >= 2 && CPUproducer.getProduction() >= 3 && RAMproducer.getProduction() >= 4 && PSproducer.getProduction() >= 6 && GCproducer.getProduction()>=5){
                            counter=0;
                            productionGC = ++productionGC;
                            this.mutex.release(); //signal
                        
                        
                            MBproducer.reduceProduction(2);
                            CPUproducer.reduceProduction(3);
                            RAMproducer.reduceProduction(4);
                            PSproducer.reduceProduction(6);
                            GCproducer.reduceProduction(5);

                            sleep(ArtiproductionTime);
                            totalsalary= (salary*productionTime) + totalsalary;
                            System.out.println(this.name);
                            System.out.println("Salario: "+this.totalsalary);
                            System.out.println("Computadores con Graficas: "+ this.productionGC);
                            System.out.println("ElementosFabricados: "+ this.production + "\n");
                            
                            
                        } else {


                            this.mutex.acquire(); //wait
                            sleep(this.ArtiproductionTime);
                            totalsalary= (salary*24) + totalsalary;

                            System.out.println(this.name);
                            System.out.println("Salario: "+this.totalsalary);
                            System.out.println("Computadores con Graficas: "+ this.productionGC);
                            System.out.println("ElementosFabricados: "+ this.production + "\n");
                            System.out.println("no hay graficaSSSSSSSSSSSSSSSSSSSSSSS");

                    }

                }
                
                
                
                
                
                
            } catch(InterruptedException ex) {
                Logger.getLogger(Assembler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
