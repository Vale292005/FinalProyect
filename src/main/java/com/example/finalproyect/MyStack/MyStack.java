package com.example.finalproyect.MyStack;

public class MyStack<T> {
    public static class Node<T> {
        private T data;
        private Node<T> next;

        public Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> top; // Elemento superior de la pila
    private int size;    // Tamaño de la pila

    public MyStack() {
        top = null;
        size = 0;
    }

    // Verifica si la pila está vacía
    public boolean isEmpty() {
        return top == null;
    }

    // Devuelve el tamaño de la pila
    public int getSize() {
        return size;
    }

    // Agrega un elemento a la parte superior de la pila
    public void push(T data) {
        Node<T> newNode = new Node<>(data);
        newNode.next = top;
        top = newNode;
        size++;
    }

    // Elimina y devuelve el elemento en la parte superior de la pila
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("La pila está vacía");
        }
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }

    // Devuelve el elemento en la parte superior sin eliminarlo
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("La pila está vacía");
        }
        return top.data;
    }
}

