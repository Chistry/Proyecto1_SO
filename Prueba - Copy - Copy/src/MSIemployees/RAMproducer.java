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
public class RAMproducer extends Worker {
    public RAMproducer(Semaphore mutex, int ArtificialTime) {
        super("RAMWorker: ", mutex, ArtificialTime, 24, 40, 55, 2, ArtificialTime); // Valores específicos para ProducerA
    }
}
