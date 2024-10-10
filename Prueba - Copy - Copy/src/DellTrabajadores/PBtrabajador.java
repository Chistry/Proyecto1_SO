/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DellTrabajadores;

import java.util.concurrent.Semaphore;

/**
 *
 * @author diego
 */
public class PBtrabajador extends Trabajador{
    public PBtrabajador(Semaphore mutex, int ArtificialTime, int iteraciones){
        super("Trabajador Placa Base: ", mutex, 3*ArtificialTime, 72, 20, 25, 1, ArtificialTime, iteraciones);
    
    }
}
