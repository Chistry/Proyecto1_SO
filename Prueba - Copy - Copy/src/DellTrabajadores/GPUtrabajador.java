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
public class GPUtrabajador extends Trabajador{
    public GPUtrabajador(Semaphore mutex, int ArtificialTime, int iteraciones){
    super("Trabajador GPU: ", mutex, 3*ArtificialTime, 72, 34, 10, 1, ArtificialTime, iteraciones);
    }
    
}
