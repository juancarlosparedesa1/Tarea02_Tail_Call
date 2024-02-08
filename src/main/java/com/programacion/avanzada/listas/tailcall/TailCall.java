package com.programacion.avanzada.listas.tailcall;

import java.util.function.Supplier;

public interface TailCall<T>{
      T eval();
      TailCall<T> resume();
      boolean isSuspend();
      static <T> TailCall<T> tailReturn(T value){
            return new Return<>(value);
      }
      static <T> TailCall<T> tailSuspend(Supplier<TailCall<T>> next){
            return new Suspend <>(next);
      }

}