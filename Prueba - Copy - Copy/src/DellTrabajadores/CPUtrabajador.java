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
public class CPUtrabajador extends Trabajador{
    public  CPUtrabajador (Semaphore mutex, int ArtificialTime, int iteraciones){
        super("Trabajador CPU: ", mutex, 3*ArtificialTime, 72, 26, 20, 1, ArtificialTime, iteraciones);
    }
}
