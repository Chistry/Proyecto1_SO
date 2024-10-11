/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EDD;

/**
 *
 * @author diego
 */
public class ListaSimple<E> {
    protected Nodo<E> first;
    
    public ListaSimple(){
        this.first = null;
    }

    /**
     * @return the first
     */
    public Nodo<E> getFirst() {
        return first;
    }

    /**
     * @param first the first to set
     */
    public void setFirst(Nodo<E> first) {
        this.first = first;
    }
    
    public boolean isEmpty(){
        return (this.first == null);   
    }
    
    public void insertar(E info){
        Nodo<E> nodito = new Nodo<E>(info);
        nodito.setNext(this.first);
    }
    
    
}
