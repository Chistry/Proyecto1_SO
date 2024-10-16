/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import MSIemployees.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Semaphore;
import EDD.ListaSimple;


/**
 *
 * @author diego
 */


public class PantallaMSI extends javax.swing.JFrame {

    private int ensambladores = 1;
    private int pm = 1;
    private int cpuWorkers = 1;
    private int ramWorkers = 1;
    private int faWorkers = 1;
    private int gpuWorkers = 1;
    private final int MAX_WORKERS = 16;
    private int totalAssignedWorkers = ensambladores + pm + cpuWorkers + ramWorkers + faWorkers + gpuWorkers;
    
    private ListaSimple<Worker> workersListMB = new ListaSimple<>();
    private ListaSimple<Worker> workersListCPU = new ListaSimple<>();
    private ListaSimple<Worker> workersListRAM = new ListaSimple<>();
    private ListaSimple<Worker> workersListPS = new ListaSimple<>();
    private ListaSimple<Worker> workersListGC = new ListaSimple<>();

    
    private ListaSimple<Assembler> assemblersList = new ListaSimple<>();
    
    public PantallaMSI() {
        initComponents();
        actualizarLabels();
    }
    
    public static void guardarParametros(double segundos, int deathline, int ensamblador, int mb, int cpu, int ram, int ps, int gc) {
        File file = new File("parametros.txt");

        try {
            // Crear el archivo si no existe
            if (!file.exists()) {
                file.createNewFile();
            }

            // Usar FileWriter para escribir en el archivo
            FileWriter writer = new FileWriter(file, true); // 'true' para agregar sin sobrescribir
            writer.write("MSI:"+"\n");
            writer.write("Segundos: " + segundos + "\n");
            writer.write("Deathline: " + deathline + "\n");
            writer.write("Trabajadores MB: " + mb + "\n");
            writer.write("Trabajadores CPU: " + cpu + "\n");
            writer.write("Trabajadores RAM: " + ram + "\n");
            writer.write("Trabajadores fuente de alimentacion: " + ps + "\n");
            writer.write("Trabajadores tarjeta graficas: " + gc + "\n");
            writer.write("Ensamblador: " + ensamblador + "\n");
            
            
            writer.write("-------------------------\n");

            writer.close();

            System.out.println("Parámetros guardados en parametros.txt");

        } catch (IOException e) {
            System.out.println("Ocurrió un error al guardar los parámetros.");
            e.printStackTrace();
        }
    }
    
    private int calcularCostosOperativos(ProjectManagerMSI projectmanager, DirectorMSI director) {
        int totalSalarios = 0;

        // Sumar salarios de cada tipo de trabajador
        for (Worker worker : workersListMB) {
            totalSalarios += worker.getTotalsalary();
        }
        for (Worker worker : workersListCPU) {
            totalSalarios += worker.getTotalsalary();
        }
        for (Worker worker : workersListRAM) {
            totalSalarios += worker.getTotalsalary();
        }
        for (Worker worker : workersListPS) {
            totalSalarios += worker.getTotalsalary();
        }
        for (Worker worker : workersListGC) {
            totalSalarios += worker.getTotalsalary();
        }

        // Sumar salarios del Project Manager y el Director
        totalSalarios += projectmanager.getTotalsalary(); // Asegúrate de que tengas un método getSalary en ProjectManager
        totalSalarios += director.getTotalsalary(); // Asegúrate de que tengas un método getSalary en Director

        return totalSalarios;
    }


    
    
    private void iniciarSimulacion(int milisegundos, int diastotales, int limite) {
        Semaphore mutex = new Semaphore(1);
        
            

        // Crear los productores dinámicamente según la asignación del usuario
        for (int i = 0; i < pm; i++) {
            Worker mbWorker = new MBproducer(mutex, milisegundos, diastotales);
            workersListMB.insertar(mbWorker);
            mbWorker.start();
        }
        for (int i = 0; i < cpuWorkers; i++) {
            Worker cpuWorker = new CPUproducer(mutex, milisegundos, diastotales);
            workersListCPU.insertar(cpuWorker);
            cpuWorker.start();
        }
        for (int i = 0; i < ramWorkers; i++) {
            Worker ramWorker = new RAMproducer(mutex, milisegundos, diastotales);
            workersListRAM.insertar(ramWorker);
            ramWorker.start();
        }
        for (int i = 0; i < faWorkers; i++) {
            Worker psWorker = new PSproducer(mutex, milisegundos, diastotales);
            workersListPS.insertar(psWorker);
            psWorker.start();
        }
        for (int i = 0; i < gpuWorkers; i++) {
            Worker gcWorker = new GCproducer(mutex, milisegundos, diastotales);
            workersListGC.insertar(gcWorker);
            gcWorker.start();
        }
        

        // Crear ensambladores dinámicamente
        for (int i = 0; i < ensambladores; i++) {
            
            Assembler assembler = new Assembler(mutex, workersListMB,workersListCPU,workersListRAM,workersListPS,workersListGC, milisegundos, diastotales, cpu,gc,ps,ram,mb,PC,PCconGC);
            assemblersList.insertar(assembler);
            assembler.start();
        }

        
        // Iniciar el Project Manager y Director
        ProjectManagerMSI projectmanager = new ProjectManagerMSI(mutex, milisegundos, limite, diastotales, anime);
        DirectorMSI director = new DirectorMSI(mutex, milisegundos, projectmanager, assemblersList, diastotales);

        projectmanager.start();
        director.start();

        try {
            director.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(PantallaMSI.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        // Calcular la ganancia y mostrar los resultados
        int gananciaBruta = director.getVentas();
        int costosOperativos = calcularCostosOperativos(projectmanager, director);
        int UtilidadEstudio = gananciaBruta - costosOperativos;
        bruto.setText(String.valueOf(gananciaBruta)+"$");
        costos.setText(String.valueOf(costosOperativos)+"$");
        ganaciasTotales.setText(String.valueOf(UtilidadEstudio)+"$");
        //javax.swing.JOptionPane.showMessageDialog(this, "Ganancia Bruta: " + gananciaBruta + "\nCostos Operativos: " + costosOperativos + "\nUtilidad del estudio: " + UtilidadEstudio);

            
        } 


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        CPU = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        GC = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        RAM = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        masEnsamblador = new javax.swing.JButton();
        menosEnsamblador = new javax.swing.JButton();
        masPM = new javax.swing.JButton();
        masCPU = new javax.swing.JButton();
        masRAM = new javax.swing.JButton();
        masFA = new javax.swing.JButton();
        masGPU = new javax.swing.JButton();
        menosPM = new javax.swing.JButton();
        menosCPU = new javax.swing.JButton();
        menosRAM = new javax.swing.JButton();
        menosFA = new javax.swing.JButton();
        menosGPU = new javax.swing.JButton();
        workersMB = new javax.swing.JLabel();
        workerscpu = new javax.swing.JLabel();
        workersram = new javax.swing.JLabel();
        workersPS = new javax.swing.JLabel();
        workersgc = new javax.swing.JLabel();
        costos = new javax.swing.JLabel();
        workersAssembler1 = new javax.swing.JLabel();
        cpu = new javax.swing.JLabel();
        bruto = new javax.swing.JLabel();
        ganaciasTotales = new javax.swing.JLabel();
        anime = new javax.swing.JLabel();
        workersAssembler3 = new javax.swing.JLabel();
        ram = new javax.swing.JLabel();
        ps = new javax.swing.JLabel();
        PC = new javax.swing.JLabel();
        mb = new javax.swing.JLabel();
        PCconGC = new javax.swing.JLabel();
        gc = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 81, -1, -1));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("MSI");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jButton1.setText("Iniciar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 40, -1, -1));

        jLabel2.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel2.setText("Trabajadores");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 100, 110, 20));

        jLabel3.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel3.setText("Computadoras producidas");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 220, 220, 20));

        jLabel4.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel4.setText("Director");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, 70, 20));

        jLabel5.setText("Con GPU: ");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 280, 60, -1));

        jLabel6.setText("Status:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, 40, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Placa Madre: ");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, 90, -1));

        CPU.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        CPU.setText("CPU: ");
        jPanel2.add(CPU, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 110, 70, -1));

        jLabel9.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel9.setText("Costos:");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 400, 60, 20));

        GC.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        GC.setText("GPU:");
        jPanel2.add(GC, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 200, 70, -1));

        jLabel11.setText("Estándar: ");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 250, 60, -1));

        jLabel12.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel12.setText("Project Manager");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, 140, 20));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("Ensamblador");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 340, -1, -1));

        jLabel14.setText("Amonestaciones: ");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 150, 120, -1));

        jLabel15.setText("Descontado: ");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 180, 110, -1));

        RAM.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        RAM.setText("RAM: ");
        jPanel2.add(RAM, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 140, 60, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Fuente de Alimentación: ");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, 160, -1));

        jLabel18.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel18.setText("Componentes");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 20, 120, 20));

        jLabel19.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel19.setText("Dias Restantes: ");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 310, 130, 20));

        jLabel20.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel20.setText("Ingresos:");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 370, 70, 20));

        jLabel21.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel21.setText("Ganancias Totales:");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 340, 150, 20));

        jLabel22.setText("Estatus:");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 120, 50, -1));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Placa Madre");
        jPanel2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 80, -1));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setText("CPU");
        jPanel2.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 180, -1, -1));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("RAM ");
        jPanel2.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, -1, -1));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel26.setText("Fuente Poder");
        jPanel2.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 90, 20));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel27.setText("GPU");
        jPanel2.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 300, -1, -1));

        masEnsamblador.setText("+");
        masEnsamblador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masEnsambladorActionPerformed(evt);
            }
        });
        jPanel2.add(masEnsamblador, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 330, 50, 30));

        menosEnsamblador.setText("-");
        menosEnsamblador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menosEnsambladorActionPerformed(evt);
            }
        });
        jPanel2.add(menosEnsamblador, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 330, 50, 30));

        masPM.setText("+");
        masPM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masPMActionPerformed(evt);
            }
        });
        jPanel2.add(masPM, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 130, 50, 30));

        masCPU.setText("+");
        masCPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masCPUActionPerformed(evt);
            }
        });
        jPanel2.add(masCPU, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 170, 50, 30));

        masRAM.setText("+");
        masRAM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masRAMActionPerformed(evt);
            }
        });
        jPanel2.add(masRAM, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 50, 30));

        masFA.setText("+");
        masFA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masFAActionPerformed(evt);
            }
        });
        jPanel2.add(masFA, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 250, 50, 30));

        masGPU.setText("+");
        masGPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masGPUActionPerformed(evt);
            }
        });
        jPanel2.add(masGPU, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 290, 50, 30));

        menosPM.setText("-");
        menosPM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menosPMActionPerformed(evt);
            }
        });
        jPanel2.add(menosPM, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 50, 30));

        menosCPU.setText("-");
        menosCPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menosCPUActionPerformed(evt);
            }
        });
        jPanel2.add(menosCPU, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 170, 50, 30));

        menosRAM.setText("-");
        menosRAM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menosRAMActionPerformed(evt);
            }
        });
        jPanel2.add(menosRAM, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 210, 50, 30));

        menosFA.setText("-");
        menosFA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menosFAActionPerformed(evt);
            }
        });
        jPanel2.add(menosFA, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 50, 30));

        menosGPU.setText("-");
        menosGPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menosGPUActionPerformed(evt);
            }
        });
        jPanel2.add(menosGPU, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 290, 50, 30));

        workersMB.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        workersMB.setText("0");
        jPanel2.add(workersMB, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 130, -1, 30));

        workerscpu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        workerscpu.setText("0");
        jPanel2.add(workerscpu, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 170, -1, 30));

        workersram.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        workersram.setText("0");
        jPanel2.add(workersram, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 210, -1, 30));

        workersPS.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        workersPS.setText("0");
        jPanel2.add(workersPS, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 250, -1, 30));

        workersgc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        workersgc.setText("0");
        jPanel2.add(workersgc, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 290, -1, 30));

        costos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        costos.setText("0");
        jPanel2.add(costos, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 400, 110, 20));

        workersAssembler1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        workersAssembler1.setText("0");
        jPanel2.add(workersAssembler1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 330, -1, 30));

        cpu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cpu.setText("0");
        jPanel2.add(cpu, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 110, 100, 20));

        bruto.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        bruto.setText("0");
        jPanel2.add(bruto, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 370, 110, 20));

        ganaciasTotales.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ganaciasTotales.setText("0");
        jPanel2.add(ganaciasTotales, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 340, 100, 20));

        anime.setText("0");
        jPanel2.add(anime, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 120, 70, -1));

        workersAssembler3.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        workersAssembler3.setText("0");
        jPanel2.add(workersAssembler3, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 310, 100, 20));

        ram.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ram.setText("0");
        jPanel2.add(ram, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 140, 100, 20));

        ps.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        ps.setText("0");
        jPanel2.add(ps, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 170, 100, 20));

        PC.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        PC.setText("0");
        jPanel2.add(PC, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 250, 100, 20));

        mb.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        mb.setText("0");
        jPanel2.add(mb, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 80, 100, 20));

        PCconGC.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        PCconGC.setText("0");
        jPanel2.add(PCconGC, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 280, 100, 20));

        gc.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        gc.setText("0");
        jPanel2.add(gc, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 200, 100, 20));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 710, 440));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menosGPUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menosGPUActionPerformed
        if (gpuWorkers > 1) {
            gpuWorkers--;
            totalAssignedWorkers--;
            actualizarLabels();
        }
    }//GEN-LAST:event_menosGPUActionPerformed

    private void menosFAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menosFAActionPerformed
        if (faWorkers > 1) {
            faWorkers--;
            totalAssignedWorkers--;
            actualizarLabels();
        }
    }//GEN-LAST:event_menosFAActionPerformed

    private void menosRAMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menosRAMActionPerformed
        if (ramWorkers > 1) {
            ramWorkers--;
            totalAssignedWorkers--;
            actualizarLabels();
        }
    }//GEN-LAST:event_menosRAMActionPerformed

    private void menosCPUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menosCPUActionPerformed
        if (cpuWorkers > 1) {
            cpuWorkers--;
            totalAssignedWorkers--;
            actualizarLabels();
        }
    }//GEN-LAST:event_menosCPUActionPerformed

    private void menosPMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menosPMActionPerformed
        if (pm > 1) {
            pm--;
            totalAssignedWorkers--;
            actualizarLabels();
        }
    }//GEN-LAST:event_menosPMActionPerformed

    private void masGPUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masGPUActionPerformed
        if (verificarAsignacion()) {
            gpuWorkers++;
            totalAssignedWorkers++;
            actualizarLabels();
        }
    }//GEN-LAST:event_masGPUActionPerformed

    private void masFAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masFAActionPerformed
        if (verificarAsignacion()) {
            faWorkers++;
            totalAssignedWorkers++;
            actualizarLabels();
        }
    }//GEN-LAST:event_masFAActionPerformed

    private void masRAMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masRAMActionPerformed
        if (verificarAsignacion()) {
            ramWorkers++;
            totalAssignedWorkers++;
            actualizarLabels();
        }
    }//GEN-LAST:event_masRAMActionPerformed

    private void masCPUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masCPUActionPerformed
        if (verificarAsignacion()) {
            cpuWorkers++;
            totalAssignedWorkers++;
            actualizarLabels();
        }
    }//GEN-LAST:event_masCPUActionPerformed

    private void masPMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masPMActionPerformed
        if (verificarAsignacion()) {
            pm++;
            totalAssignedWorkers++;
            actualizarLabels();
        }
    }//GEN-LAST:event_masPMActionPerformed

    private void menosEnsambladorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menosEnsambladorActionPerformed
        if (ensambladores > 1) {
            ensambladores--;
            totalAssignedWorkers--;
            actualizarLabels();
        }
    }//GEN-LAST:event_menosEnsambladorActionPerformed

    private void masEnsambladorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masEnsambladorActionPerformed
        if (verificarAsignacion()) {
            ensambladores++;
            totalAssignedWorkers++;
            actualizarLabels();
        }
    }//GEN-LAST:event_masEnsambladorActionPerformed

    public void updateMBLabel(int value) {
        cpu.setText(String.valueOf(value));
    }

    public void updateCPULabel(int value) {
        CPU.setText(String.valueOf(value));
    }

    public void updateRAMLabel(int value) {
        RAM.setText(String.valueOf(value));
    }

    public void updatePSLabel(int value) {
        ps.setText(String.valueOf(value));
    }

    public void updateGCLabel(int value) {
        GC.setText(String.valueOf(value));
    }

    public void updatePCLabel(int value) {
        PC.setText(String.valueOf(value));
    }

    public void updatePCconGPULabel(int value) {
        PCconGC.setText(String.valueOf(value));
    }

    
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {

            if (totalAssignedWorkers == MAX_WORKERS) {
                // Iniciar la simulación
                // Solicitar los milisegundos
                String milisegundosInput = javax.swing.JOptionPane.showInputDialog(this, "Por favor, ingrese un número de milisegundos (1000 milisegundos = 1 segundo):");
                int milisegundos = Integer.parseInt(milisegundosInput);
                int segundos=milisegundos/1000;

                // Solicitar el número de días
                String diasInput = javax.swing.JOptionPane.showInputDialog(this, "Por favor, ingrese el número de días:");
                int diastotales = Integer.parseInt(diasInput);

                // Solicitar la DEATHLINE
                String limiteInput = javax.swing.JOptionPane.showInputDialog(this, "Por favor, inserte la DEATHLINE en días:");
                int limite = Integer.parseInt(limiteInput);

                // Iniciar la simulación con los valores capturados
                iniciarSimulacion(milisegundos, diastotales, limite);

                System.out.println("Simulación iniciada con la siguiente asignación:");
                System.out.println("Ensambladores: " + ensambladores);
                System.out.println("Trabajadores MotherBoard: " + pm);
                System.out.println("Trabajadores CPU: " + cpuWorkers);
                System.out.println("Trabajadores RAM: " + ramWorkers);
                System.out.println("Trabajadores FA: " + faWorkers);
                System.out.println("Trabajadores GPU: " + gpuWorkers);
                
                guardarParametros(segundos, limite, ensambladores, pm, cpuWorkers, ramWorkers, faWorkers, gpuWorkers);
            } else {
                System.out.println("Asegúrate de asignar un total de " + MAX_WORKERS + " trabajadores.");
            }
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Por favor, ingrese valores válidos.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PantallaMSI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PantallaMSI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PantallaMSI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PantallaMSI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PantallaMSI().setVisible(true);
            }
        });
    }
    
    
    // Función para verificar si se pueden asignar más trabajadores
    private boolean verificarAsignacion() {
        return totalAssignedWorkers < MAX_WORKERS;
    }
    // Acciones para los botones de "+" y "-" de ensambladores

    
    private void actualizarLabels() {
        workersAssembler1.setText(String.valueOf(ensambladores));
        workersMB.setText(String.valueOf(pm));
        workerscpu.setText(String.valueOf(cpuWorkers));
        workersram.setText(String.valueOf(ramWorkers));
        workersPS.setText(String.valueOf(faWorkers));
        workersgc.setText(String.valueOf(gpuWorkers));
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CPU;
    private javax.swing.JLabel GC;
    private javax.swing.JLabel PC;
    private javax.swing.JLabel PCconGC;
    private javax.swing.JLabel RAM;
    private javax.swing.JLabel anime;
    private javax.swing.JLabel bruto;
    private javax.swing.JLabel costos;
    private javax.swing.JLabel cpu;
    private javax.swing.JLabel ganaciasTotales;
    private javax.swing.JLabel gc;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton masCPU;
    private javax.swing.JButton masEnsamblador;
    private javax.swing.JButton masFA;
    private javax.swing.JButton masGPU;
    private javax.swing.JButton masPM;
    private javax.swing.JButton masRAM;
    private javax.swing.JLabel mb;
    private javax.swing.JButton menosCPU;
    private javax.swing.JButton menosEnsamblador;
    private javax.swing.JButton menosFA;
    private javax.swing.JButton menosGPU;
    private javax.swing.JButton menosPM;
    private javax.swing.JButton menosRAM;
    private javax.swing.JLabel ps;
    private javax.swing.JLabel ram;
    private javax.swing.JLabel workersAssembler1;
    private javax.swing.JLabel workersAssembler3;
    private javax.swing.JLabel workersMB;
    private javax.swing.JLabel workersPS;
    private javax.swing.JLabel workerscpu;
    private javax.swing.JLabel workersgc;
    private javax.swing.JLabel workersram;
    // End of variables declaration//GEN-END:variables
}
