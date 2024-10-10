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
public class RAMtrabajador extends Trabajador{
    public RAMtrabajador (Semaphore mutex, int ArtificialTime, int iteraciones){
    super("trabajador RAM: ", mutex, ArtificialTime, 24, 40, 55, 2, ArtificialTime, iteraciones);
    }
    
}
