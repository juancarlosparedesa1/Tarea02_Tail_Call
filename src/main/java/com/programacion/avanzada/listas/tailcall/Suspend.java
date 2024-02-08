package com.programacion.avanzada.listas.tailcall;


import java.util.function.Supplier;

final class Suspend <T> implements TailCall<T>{
    private Supplier<TailCall<T>> next;
    public Suspend(Supplier<TailCall<T>> next){
        //this.next=next;//Usa el Stack
        this.next=next;
    }
    @Override
    public T eval() {

        TailCall<T> tmp=this;
        while(tmp.isSuspend()){
            tmp=tmp.resume();
        }

        return tmp.eval();
    }
    @Override
    public TailCall<T> resume() {
        return next.get();
    }
    @Override
    public boolean isSuspend() {
        return true;
    }



}