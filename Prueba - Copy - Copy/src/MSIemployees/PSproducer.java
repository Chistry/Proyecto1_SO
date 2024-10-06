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
public class PSproducer extends Worker {
    public PSproducer(Semaphore mutex, int ArtificialTime) {
        super("PowerSuplyWorker: ", mutex, ArtificialTime, 24, 16, 35, 3, ArtificialTime); // Valores específicos para ProducerA
    }
}
