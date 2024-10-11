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
import static java.lang.Thread.sleep;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DirectorMSI extends Thread {
    private String name = "Director: ";
    private int salary = 60; // Salario por hora
    private int ArtiproductionTime;
    private int productionTime;
    private int maxproduction;
    private int production;
    private Semaphore mutex;
    private int totalsalary = 0;
    private boolean anime;
    private int deathlineDirector;
    private ProjectManagerMSI ProjectManager;
    private ListaSimple<Assembler> Assemblers; // Lista de ensambladores
    private int iteraciones;
    private int ventas = 0;

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

    public DirectorMSI(Semaphore mutex, int ArtificialProductionTime, ProjectManagerMSI ProjectManager,
                        ListaSimple<Assembler> listaassembler, int iteraciones) {
        this.mutex = mutex;
        this.ArtiproductionTime = ArtificialProductionTime;
        this.ProjectManager = ProjectManager;
        this.iteraciones = iteraciones;
        this.Assemblers = listaassembler;
    }

    @Override
    public void run() {
        int originaldeathline = ProjectManager.getDeathline();
        deathlineDirector = ProjectManager.getDeathline();
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
                            System.out.println("Director trabajando en envío");
                            sleep(this.ArtiproductionTime);

                            // Realiza la venta de computadoras si hay producción
                            for (Assembler assembler : Assemblers) {
                                if (assembler.getProductionGC() > 0) {
                                    assembler.setProductionGC(assembler.getProductionGC() - 1);
                                    ventas += 250000;
                                } else if (assembler.getProduction() > 0) {
                                    assembler.setProduction(assembler.getProduction() - 1);
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
                            System.out.println("El Project Manager está viendo anime durante el descanso.");
                            ProjectManager.setTotalsalary(ProjectManager.getTotalsalary() - 100);
                        } else {
                            System.out.println("El Project Manager no está viendo anime.");
                        }

                        // Asignar un nuevo momento aleatorio para el próximo descanso
                        sleep((long) this.ArtiproductionTime - (7 / 288));
                        randomMoment = random.nextInt(this.ArtiproductionTime);
                        deathlineDirector -= 1;
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(DirectorMSI.class.getName()).log(Level.SEVERE, null, ex);
            }
            counter++;
        }
        //calcularSalarioEnsambladores();
    }

        /*private void calcularSalarioEnsambladores() {
            for (Assembler assembler : Assemblers) {
                totalsalary += assembler.getTotalsalary(); // Asumiendo que tienes un método getSalary en Assembler
            }
            System.out.println("Total de salario de ensambladores: " + totalsalary);
        }*/
    }


