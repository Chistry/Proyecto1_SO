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
public class GCproducer extends Worker {
    public GCproducer(Semaphore mutex, int ArtificialTime, int iteraciones) {
        super("GraphicCardWorker: ", mutex, 3*ArtificialTime, 72, 34, 10, 1, ArtificialTime, iteraciones, 3); // Valores específicos para ProducerA
    }
}
