/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EDD;

/**
 *
 * @author diego
 * @param <E>
 */
public class Nodo<E> {
    private E info;
    private Nodo<E> next;
    
    public Nodo(E info, Nodo<E> next ){
    this.info = info;
    this.next = next;
    }

    /**
     * @return the info
     */
    public E getInfo() {
        return info;
    }

    /**
     * @param info the info to set
     */
    public void setInfo(E info) {
        this.info = info;
    }

    /**
     * @return the next
     */
    public Nodo<E> getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(Nodo<E> next) {
        this.next = next;
    }
    
    public Nodo(){
        this(null,null);
    }
    
    public Nodo(E info){
        this(info, null);
    }
    
    

}
