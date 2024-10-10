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
public class FAtrabajador extends Trabajador{
    public FAtrabajador(Semaphore mutex, int ArtificialTime, int iteraciones){
    super("Trabajador Fuente de Alimentación: ", mutex, ArtificialTime, 24, 16, 35, 3, ArtificialTime, iteraciones);
    }
    
}
