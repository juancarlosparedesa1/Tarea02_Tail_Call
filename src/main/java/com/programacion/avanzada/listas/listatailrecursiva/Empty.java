package com.programacion.avanzada.listas.listatailrecursiva;

class Empty<T> implements Lista<T> {

    @Override
    public T head() {
        return null;
    }

    @Override
    public Lista<T> tail() {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }
   /* public int count(){
        return tail().count();
    }*/

     @Override
     public String toString() {
         return "Empty";
     }
 }
