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
import javax.swing.JLabel;
import javax.swing.SwingUtilities;


public class ProjectManagerMSI extends Thread{
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
    private JLabel label;
    
    

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

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }
    
    
    

    
    
    public ProjectManagerMSI(Semaphore mutex, int ArtificialProductionTime, int deathline, int iteraciones, JLabel label){
        this.name = name;
        this.mutex = mutex;
        this.ArtiproductionTime = ArtificialProductionTime;
        this.salary= salary;
        this.maxproduction =  maxproduction;
        this.productionTime = productionTime;
        this.anime=anime;
        this.deathline=deathline;
        this.iteraciones=iteraciones;
        this.label=label;
       
        
        
        
    }
    
        
    @Override
    public void run() {
        int counter = 0;
        while (counter != this.iteraciones) {
            try {
                anime = true;  // Valor inicial del booleano
                for (int i = 0; i < 32; i++) {
                    sleep(ArtiproductionTime / 48);
                    anime = !anime;  // Alterna el valor entre true y false

                    // Actualizar el JLabel cada vez que cambia el estado de anime
                    SwingUtilities.invokeAndWait(() -> {
                        label.setText("Anime: " + (anime ? "True" : "False"));
                        label.repaint(); // Forzar repintado para asegurar que se actualiza
                    });
                }

                //System.out.println(this.name + " está en modo focus");
                anime = false;

                // Actualizar el JLabel al final del ciclo
                SwingUtilities.invokeAndWait(() -> {
                    label.setText("Anime: False");
                    label.repaint(); // Forzar repintado
                });

                sleep(ArtiproductionTime / 3);
                totalsalary = (24 * salary) + totalsalary;

                if (deathline == 0) {
                    this.mutex.acquire(); // wait
                } else {
                    this.mutex.release(); // signal
                    this.deathline -= 1;
                }

            } catch (InterruptedException | java.lang.reflect.InvocationTargetException ex) {
                Logger.getLogger(ProjectManagerMSI.class.getName()).log(Level.SEVERE, null, ex);
            }
            counter += 1;
        }
    }
}
