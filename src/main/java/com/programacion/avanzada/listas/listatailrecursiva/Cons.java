package com.programacion.avanzada.listas.listatailrecursiva;

public record Cons<T> (T head, Lista<T> tail) implements Lista<T>{

    /*private final T head;
    private final Lista<T> tail;

    Const(T head, Lista<T> tail){
        this.head=head;
        this.tail=tail;
    }

    public T getHead() {
        return head;
    }
    public Lista<T> getTail(){
        return tail;
    }*/

    public String toString(){
        return  String.format("[%s,%S",head,tail.toString());
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
