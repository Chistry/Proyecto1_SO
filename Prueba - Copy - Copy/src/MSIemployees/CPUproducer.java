/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MSIemployees;

import java.util.concurrent.Semaphore;

/**
 *
 * @author chris
 */
public class CPUproducer extends Worker {
    public CPUproducer(Semaphore mutex, int ArtificialTime, int iteraciones) {
        super("CPUWorker: ", mutex, 3*ArtificialTime, 72, 26, 20, 1, ArtificialTime, iteraciones, 3); 
    }
}
