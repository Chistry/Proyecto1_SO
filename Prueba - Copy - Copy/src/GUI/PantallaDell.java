/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

/**
 *
 * @author diego
 */
import DellTrabajadores.*;
import EDD.ListaSimple;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.Semaphore;
import EDD.ListaSimple;

public class PantallaDell extends javax.swing.JFrame {

    
    private int ensambladores = 1;
    private int pm = 1;
    private int cpuWorkers = 1;
    private int ramWorkers = 1;
    private int faWorkers = 1;
    private int gpuWorkers = 1;
    private final int MAX_WORKERS = 16;
    private int totalAssignedWorkers = ensambladores + pm + cpuWorkers + ramWorkers + faWorkers + gpuWorkers;

    private ListaSimple<Trabajador> PMlistaTrabajadores = new ListaSimple<>();
    private ListaSimple<Trabajador> CPUlistaTrabajadores = new ListaSimple<>();
    private ListaSimple<Trabajador> RAMlistaTrabajadores = new ListaSimple<>();
    private ListaSimple<Trabajador> FAlistaTrabajadores = new ListaSimple<>();
    private ListaSimple<Trabajador> GPUlistaTrabajadores = new ListaSimple<>();
    private ListaSimple<Ensamblador> listaEnsamblador = new ListaSimple<>();


    


    public PantallaDell() {
        initComponents();
        actualizarLabels();
    }
    

    public static void guardarParametros(double segundos, int deathline, int n) {
        File file = new File("parametros.txt");


        try {
            // Crear el archivo si no existe
            if (!file.exists()) {
                file.createNewFile();
            }

            // Usar FileWriter para escribir en el archivo
            FileWriter writer = new FileWriter(file, true); // 'true' para agregar sin sobrescribir

            writer.write("Segundos: " + segundos + "\n");
            writer.write("Deathline: " + deathline + "\n");
            writer.write("N: " + n + "\n");
            writer.write("-------------------------\n");

            writer.close();

            System.out.println("Parámetros guardados en parametros.txt");

        } catch (IOException e) {
            System.out.println("Ocurrió un error al guardar los parámetros.");
            e.printStackTrace();
        }
    }
    
    private int calcularCostosOperativos(ProjectManager projectmanager, Director director) {
        int totalSalarios = 0;

        // Sumar salarios de cada tipo de trabajador
        for (Trabajador trabajador : PMlistaTrabajadores) {
            totalSalarios += trabajador.getSalariototal();
        }
        for (Trabajador trabajador : CPUlistaTrabajadores) {
            totalSalarios += trabajador.getSalariototal();
        }
        for (Trabajador trabajador : RAMlistaTrabajadores) {
            totalSalarios += trabajador.getSalariototal();
        }
        for (Trabajador trabajador : FAlistaTrabajadores) {
            totalSalarios += trabajador.getSalariototal();
        }
        for (Trabajador trabajador : GPUlistaTrabajadores) {
            totalSalarios += trabajador.getSalariototal();
        }

        // Sumar salarios del Project Manager y el Director
        totalSalarios += projectmanager.getSalariototal(); // Asegúrate de que tengas un método getSalary en ProjectManager
        totalSalarios += director.getSalariototal(); // Asegúrate de que tengas un método getSalary en Director

        return totalSalarios;
    }
    
    private void iniciarSimulacion(int milisegundos, int diastotales, int limite) {
            Semaphore mutex = new Semaphore(1);
            
            // Crear los productores dinámicamente según la asignación del usuario
        for (int i = 0; i < pm; i++) {

            Trabajador mbWorker = new PBtrabajador(mutex, milisegundos, diastotales);

            PMlistaTrabajadores.insertar(mbWorker);
            mbWorker.start();
        }
        for (int i = 0; i < cpuWorkers; i++) {
            Trabajador cpuWorker = new CPUtrabajador(mutex, milisegundos, diastotales);
            CPUlistaTrabajadores.insertar(cpuWorker);
            cpuWorker.start();
        }
        for (int i = 0; i < ramWorkers; i++) {
            Trabajador ramWorker = new RAMtrabajador(mutex, milisegundos, diastotales);
            RAMlistaTrabajadores.insertar(ramWorker);
            ramWorker.start();
        }
        for (int i = 0; i < faWorkers; i++) {
            Trabajador psWorker = new FAtrabajador(mutex, milisegundos, diastotales);
            FAlistaTrabajadores.insertar(psWorker);
            psWorker.start();
        }
        for (int i = 0; i < gpuWorkers; i++) {
            Trabajador gcWorker = new GPUtrabajador(mutex, milisegundos, diastotales);
            GPUlistaTrabajadores.insertar(gcWorker);
            gcWorker.start();
        }
        // Crear ensambladores dinámicamente
        for (int i = 0; i < ensambladores; i++) {
            
            Ensamblador ensamblador = new Ensamblador(mutex, PMlistaTrabajadores,CPUlistaTrabajadores,RAMlistaTrabajadores,FAlistaTrabajadores,GPUlistaTrabajadores, milisegundos, diastotales);
            listaEnsamblador.insertar(ensamblador);
            ensamblador.start();
        }
        // Iniciar el Project Manager y Director
        ProjectManager projectmanager = new ProjectManager(mutex, milisegundos, limite, diastotales);
        Director director = new Director(mutex, milisegundos, projectmanager, listaEnsamblador, diastotales);

        projectmanager.start();
        director.start();

        try {
            director.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(PantallaDell.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        // Calcular la ganancia y mostrar los resultados
        int gananciaBruta = director.getVentas();
        int costosOperativos = calcularCostosOperativos(projectmanager, director);
        int UtilidadEstudio = gananciaBruta - costosOperativos;
        Ingresos.setText(String.valueOf(gananciaBruta)+"$");
        Costos.setText(String.valueOf(costosOperativos)+"$");
        GananciasTotales.setText(String.valueOf(UtilidadEstudio)+"$");
        //javax.swing.JOptionPane.showMessageDialog(this, "Ganancia Bruta: " + gananciaBruta + "\nCostos Operativos: " + costosOperativos + "\nUtilidad del estudio: " + UtilidadEstudio);
        
        }

    
    private int calcularCostosOperativos(ProjectManager projectmanager, Director director) {
        int totalSalarios = 0;

        // Sumar salarios de cada tipo de trabajador
        for (Trabajador trabajador : PMlistaTrabajadores) {
            totalSalarios += trabajador.getSalariototal();
        }
        for (Trabajador trabajador : CPUlistaTrabajadores) {
            totalSalarios += trabajador.getSalariototal();
        }
        for (Trabajador trabajador : RAMlistaTrabajadores) {
            totalSalarios += trabajador.getSalariototal();
        }
        for (Trabajador trabajador : FAlistaTrabajadores) {
            totalSalarios += trabajador.getSalariototal();
        }
        for (Trabajador trabajador : GPUlistaTrabajadores) {
            totalSalarios += trabajador.getSalariototal();
        }

        // Sumar salarios del Project Manager y el Director
        totalSalarios += projectmanager.getSalariototal(); // Asegúrate de que tengas un método getSalary en ProjectManager
        totalSalarios += director.getSalariototal(); // Asegúrate de que tengas un método getSalary en Director

        return totalSalarios;
    }


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PantallaDell().setVisible(true);
            }
        });
    }
    
    // Función para verificar si se pueden asignar más trabajadores
    private boolean verificarAsignacion() {
        return totalAssignedWorkers < MAX_WORKERS;
    }
    // Acciones para los botones de "+" y "-" de ensambladores

    
    private void actualizarLabels() {
        qtyEnsamblador.setText(String.valueOf(ensambladores));
        qtyPM2.setText(String.valueOf(pm));
        qtyCPU1.setText(String.valueOf(cpuWorkers));
        qtyRAM1.setText(String.valueOf(ramWorkers));
        qtyFA1.setText(String.valueOf(faWorkers));
        qtyGPU1.setText(String.valueOf(gpuWorkers));
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
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
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
        EstadoDirector = new javax.swing.JLabel();
        qtyEnsamblador = new javax.swing.JLabel();
        qtyCPU1 = new javax.swing.JLabel();
        qtyRAM1 = new javax.swing.JLabel();
        qtyFA1 = new javax.swing.JLabel();
        qtyGPU1 = new javax.swing.JLabel();
        qtyPM2 = new javax.swing.JLabel();
        qtyConGPU1 = new javax.swing.JLabel();
        qtyestandar2 = new javax.swing.JLabel();
        qtyDescuentoProjectM = new javax.swing.JLabel();
        qtyAmonestacionesPM2 = new javax.swing.JLabel();
        EstadoProjectManager = new javax.swing.JLabel();
        qtyPMs1 = new javax.swing.JLabel();
        qtyCPUs1 = new javax.swing.JLabel();
        qtyRAMs2 = new javax.swing.JLabel();
        qtyFAs1 = new javax.swing.JLabel();
        qtyGPUs1 = new javax.swing.JLabel();
        qtyDiasRestantes2 = new javax.swing.JLabel();
        GananciasTotales = new javax.swing.JLabel();
        Ingresos = new javax.swing.JLabel();
        Costos = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 81, -1, -1));

        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setText("DELL");
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
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 110, 20));

        jLabel3.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel3.setText("Computadoras producidas");
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 220, 220, 20));

        jLabel4.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel4.setText("Director");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 10, 70, 20));

        jLabel5.setText("Con GPU: ");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 280, 110, -1));

        jLabel6.setText("Status:");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, 40, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel7.setText("Placa Madre: ");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 80, 90, -1));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("CPU: ");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 110, 110, -1));

        jLabel9.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel9.setText("Costos:");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 410, 60, 20));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("GPU:");
        jPanel2.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 200, 110, -1));

        jLabel11.setText("Estándar: ");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 250, 110, -1));

        jLabel12.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel12.setText("Project Manager");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 90, 140, 20));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel13.setText("Ensamblador");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 380, -1, -1));

        jLabel14.setText("Amonestaciones: ");
        jPanel2.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 150, 110, -1));

        jLabel15.setText("Descontado: ");
        jPanel2.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 180, 110, -1));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel16.setText("RAM: ");
        jPanel2.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 140, 110, -1));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel17.setText("Fuente de Alimentación: ");
        jPanel2.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 170, 160, -1));

        jLabel18.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel18.setText("Componentes");
        jPanel2.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 20, 120, 20));

        jLabel19.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel19.setText("Dias Restantes: ");
        jPanel2.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, 130, 20));

        jLabel20.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel20.setText("Ingresos:");
        jPanel2.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 370, 80, 20));

        jLabel21.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        jLabel21.setText("Ganancias Totales:");
        jPanel2.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 340, 150, 20));

        jLabel22.setText("Estatus:");
        jPanel2.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 120, 40, -1));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel23.setText("Placa Madre");
        jPanel2.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 180, 80, -1));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel24.setText("CPU");
        jPanel2.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 220, -1, -1));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel25.setText("RAM ");
        jPanel2.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 260, -1, -1));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel26.setText("Fuente Poder");
        jPanel2.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 90, 20));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel27.setText("GPU");
        jPanel2.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 340, -1, -1));

        masEnsamblador.setText("+");
        masEnsamblador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masEnsambladorActionPerformed(evt);
            }
        });
        jPanel2.add(masEnsamblador, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 380, 50, 30));

        menosEnsamblador.setText("-");
        menosEnsamblador.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menosEnsambladorActionPerformed(evt);
            }
        });
        jPanel2.add(menosEnsamblador, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 380, 50, 30));

        masPM.setText("+");
        masPM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masPMActionPerformed(evt);
            }
        });
        jPanel2.add(masPM, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, 50, 30));

        masCPU.setText("+");
        masCPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masCPUActionPerformed(evt);
            }
        });
        jPanel2.add(masCPU, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, 50, 30));

        masRAM.setText("+");
        masRAM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masRAMActionPerformed(evt);
            }
        });
        jPanel2.add(masRAM, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 260, 50, 30));

        masFA.setText("+");
        masFA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masFAActionPerformed(evt);
            }
        });
        jPanel2.add(masFA, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 300, 50, 30));

        masGPU.setText("+");
        masGPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                masGPUActionPerformed(evt);
            }
        });
        jPanel2.add(masGPU, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 340, 50, 30));

        menosPM.setText("-");
        menosPM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menosPMActionPerformed(evt);
            }
        });
        jPanel2.add(menosPM, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 180, 50, 30));

        menosCPU.setText("-");
        menosCPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menosCPUActionPerformed(evt);
            }
        });
        jPanel2.add(menosCPU, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 220, 50, 30));

        menosRAM.setText("-");
        menosRAM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menosRAMActionPerformed(evt);
            }
        });
        jPanel2.add(menosRAM, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 260, 50, 30));

        menosFA.setText("-");
        menosFA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menosFAActionPerformed(evt);
            }
        });
        jPanel2.add(menosFA, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 300, 50, 30));

        menosGPU.setText("-");
        menosGPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menosGPUActionPerformed(evt);
            }
        });
        jPanel2.add(menosGPU, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 340, 50, 30));

        EstadoDirector.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        EstadoDirector.setText("0");
        jPanel2.add(EstadoDirector, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 40, 20, 20));

        qtyEnsamblador.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        qtyEnsamblador.setText("0");
        jPanel2.add(qtyEnsamblador, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 380, -1, 30));

        qtyCPU1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        qtyCPU1.setText("0");
        jPanel2.add(qtyCPU1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 220, -1, 30));

        qtyRAM1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        qtyRAM1.setText("0");
        jPanel2.add(qtyRAM1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 260, -1, 30));

        qtyFA1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        qtyFA1.setText("0");
        jPanel2.add(qtyFA1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 300, -1, 30));

        qtyGPU1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        qtyGPU1.setText("0");
        jPanel2.add(qtyGPU1, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 340, -1, 30));

        qtyPM2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        qtyPM2.setText("0");
        jPanel2.add(qtyPM2, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 180, -1, 30));

        qtyConGPU1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        qtyConGPU1.setText("0");
        jPanel2.add(qtyConGPU1, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 280, 20, 20));

        qtyestandar2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        qtyestandar2.setText("0");
        jPanel2.add(qtyestandar2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 250, -1, -1));

        qtyDescuentoProjectM.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        qtyDescuentoProjectM.setText("0");
        jPanel2.add(qtyDescuentoProjectM, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 180, 20, 20));

        qtyAmonestacionesPM2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        qtyAmonestacionesPM2.setText("0");
        jPanel2.add(qtyAmonestacionesPM2, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 150, 20, 20));

        EstadoProjectManager.setText("Viendo Anime");
        jPanel2.add(EstadoProjectManager, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 120, 100, 20));

        qtyPMs1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        qtyPMs1.setText("0");
        jPanel2.add(qtyPMs1, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 80, 20, 20));

        qtyCPUs1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        qtyCPUs1.setText("0");
        jPanel2.add(qtyCPUs1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 110, 20, 20));

        qtyRAMs2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        qtyRAMs2.setText("0");
        jPanel2.add(qtyRAMs2, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 140, 20, 20));

        qtyFAs1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        qtyFAs1.setText("0");
        jPanel2.add(qtyFAs1, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 170, 20, 20));

        qtyGPUs1.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        qtyGPUs1.setText("0");
        jPanel2.add(qtyGPUs1, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 200, 20, 20));

        qtyDiasRestantes2.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        qtyDiasRestantes2.setText("0");
        jPanel2.add(qtyDiasRestantes2, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 310, 130, 20));

        GananciasTotales.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        GananciasTotales.setText("0");
        jPanel2.add(GananciasTotales, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 340, 120, 20));

        Ingresos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Ingresos.setText("0");
        jPanel2.add(Ingresos, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 370, 110, -1));

        Costos.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Costos.setText("0");
        jPanel2.add(Costos, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 410, 100, -1));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 710, 440));

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    public static void guardarParametros(double segundos, int deathline, int ensamblador, int mb, int cpu, int ram, int ps, int gc) {
        File file = new File("parametros.txt");

        try {
            // Crear el archivo si no existe
            if (!file.exists()) {
                file.createNewFile();
            }

            // Usar FileWriter para escribir en el archivo
            FileWriter writer = new FileWriter(file, true); // 'true' para agregar sin sobrescribir
            writer.write("DELL:"+"\n");
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
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            
            if (totalAssignedWorkers == MAX_WORKERS) {
                // Iniciar la simulación
                // Solicitar los milisegundos
                String milisegundosInput = javax.swing.JOptionPane.showInputDialog(this, "Por favor, ingrese un número de milisegundos (1000 milisegundos = 1 segundo):");
                int milisegundos = Integer.parseInt(milisegundosInput);
                int segundos = milisegundos/1000;

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
                System.out.println("Project Managers: " + pm);
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

    private void masEnsambladorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masEnsambladorActionPerformed
        if (verificarAsignacion()) {
            ensambladores++;
            totalAssignedWorkers++;
            actualizarLabels();
        }
    }//GEN-LAST:event_masEnsambladorActionPerformed

    private void masPMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masPMActionPerformed
        if (verificarAsignacion()) {
            pm++;
            totalAssignedWorkers++;
            actualizarLabels();
        }
    }//GEN-LAST:event_masPMActionPerformed
    
    private void masCPUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masCPUActionPerformed
        if (verificarAsignacion()) {
            cpuWorkers++;
            totalAssignedWorkers++;
            actualizarLabels();
        }
    }//GEN-LAST:event_masCPUActionPerformed
 
    
    private void masRAMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masRAMActionPerformed
        if (verificarAsignacion()) {
            ramWorkers++;
            totalAssignedWorkers++;
            actualizarLabels();
        }
    }//GEN-LAST:event_masRAMActionPerformed

    private void masFAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masFAActionPerformed
        if (verificarAsignacion()) {
            faWorkers++;
            totalAssignedWorkers++;
            actualizarLabels();
        }
    }//GEN-LAST:event_masFAActionPerformed

    private void masGPUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_masGPUActionPerformed
        if (verificarAsignacion()) {
            gpuWorkers++;
            totalAssignedWorkers++;
            actualizarLabels();
        }
    }//GEN-LAST:event_masGPUActionPerformed

    private void menosPMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menosPMActionPerformed
        if (pm > 1) {
            pm--;
            totalAssignedWorkers--;
            actualizarLabels();
        }
    }//GEN-LAST:event_menosPMActionPerformed

    private void menosCPUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menosCPUActionPerformed
        if (cpuWorkers > 1) {
            cpuWorkers--;
            totalAssignedWorkers--;
            actualizarLabels();
        }
    }//GEN-LAST:event_menosCPUActionPerformed

    private void menosEnsambladorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menosEnsambladorActionPerformed
        if (ensambladores > 1) {
            ensambladores--;
            totalAssignedWorkers--;
            actualizarLabels();
        }
    }//GEN-LAST:event_menosEnsambladorActionPerformed

    private void menosRAMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menosRAMActionPerformed
        if (ramWorkers > 1) {
            ramWorkers--;
            totalAssignedWorkers--;
            actualizarLabels();
        }        
    }//GEN-LAST:event_menosRAMActionPerformed

    private void menosFAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menosFAActionPerformed
        if (faWorkers > 1) {
            faWorkers--;
            totalAssignedWorkers--;
            actualizarLabels();
        }
    }//GEN-LAST:event_menosFAActionPerformed

    private void menosGPUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menosGPUActionPerformed
        if (gpuWorkers > 1) {
            gpuWorkers--;
            totalAssignedWorkers--;
            actualizarLabels();
        }
    }//GEN-LAST:event_menosGPUActionPerformed
     

                                    
    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Costos;
    private javax.swing.JLabel EstadoDirector;
    private javax.swing.JLabel EstadoProjectManager;
    private javax.swing.JLabel GananciasTotales;
    private javax.swing.JLabel Ingresos;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
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
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton masCPU;
    private javax.swing.JButton masEnsamblador;
    private javax.swing.JButton masFA;
    private javax.swing.JButton masGPU;
    private javax.swing.JButton masPM;
    private javax.swing.JButton masRAM;
    private javax.swing.JButton menosCPU;
    private javax.swing.JButton menosEnsamblador;
    private javax.swing.JButton menosFA;
    private javax.swing.JButton menosGPU;
    private javax.swing.JButton menosPM;
    private javax.swing.JButton menosRAM;
    private javax.swing.JLabel qtyAmonestacionesPM2;
    private javax.swing.JLabel qtyCPU1;
    private javax.swing.JLabel qtyCPUs1;
    private javax.swing.JLabel qtyConGPU1;
    private javax.swing.JLabel qtyDescuentoProjectM;
    private javax.swing.JLabel qtyDiasRestantes2;
    private javax.swing.JLabel qtyEnsamblador;
    private javax.swing.JLabel qtyFA1;
    private javax.swing.JLabel qtyFAs1;
    private javax.swing.JLabel qtyGPU1;
    private javax.swing.JLabel qtyGPUs1;
    private javax.swing.JLabel qtyPM2;
    private javax.swing.JLabel qtyPMs1;
    private javax.swing.JLabel qtyRAM1;
    private javax.swing.JLabel qtyRAMs2;
    private javax.swing.JLabel qtyestandar2;
    // End of variables declaration//GEN-END:variables
}
