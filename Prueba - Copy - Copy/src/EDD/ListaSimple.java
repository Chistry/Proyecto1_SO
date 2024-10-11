/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package EDD;

/**
 *
 * @author diego
 */


import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaSimple<E> implements Iterable<E> {
    protected Nodo<E> first;

    public ListaSimple() {
        this.first = null;
    }

    public Nodo<E> getFirst() {
        return first;
    }

    public void setFirst(Nodo<E> first) {
        this.first = first;
    }

    public boolean isEmpty() {
        return (this.first == null);
    }

    public void insertar(E info) {
        Nodo<E> nodito = new Nodo<E>(info);
        nodito.setNext(this.first);
        this.first = nodito; // Actualizar el primer nodo a ser el nuevo nodo
    }

    public int size() {
        int count = 0;
        Nodo<E> current = this.first;

        // Recorre la lista y cuenta los nodos
        while (current != null) {
            count++;
            current = current.getNext();
        }
        return count;
    }

    public E get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Índice fuera de rango: " + index);
        }

        Nodo<E> current = this.first;
        int count = 0;

        // Recorre la lista hasta encontrar el nodo en el índice especificado
        while (current != null) {
            if (count == index) {
                return current.getInfo();
            }
            count++;
            current = current.getNext();
        }

        // Este código no debería ejecutarse nunca, pero es una salvaguardia
        throw new IndexOutOfBoundsException("Índice no encontrado: " + index);
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Nodo<E> current = first; // Comenzamos desde el primer nodo

            @Override
            public boolean hasNext() {
                return current != null; // Hay un siguiente nodo si current no es null
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("No hay más elementos en la lista.");
                }
                E info = current.getInfo(); // Guardamos la información del nodo actual
                current = current.getNext(); // Movemos al siguiente nodo
                return info; // Devolvemos la información
            }
        };
    }
}


