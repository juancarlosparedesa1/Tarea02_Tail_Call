package com.programacion.avanzada.listas.tailcall;

import com.programacion.avanzada.listas.listatailrecursiva.Lista;
import com.programacion.avanzada.listas.tailcall.TailCall;

public class TestTailCall {
    public static void main(String[] args) {
        System.out.println("************************************************************");
        //creamos Lista base
        Lista<Integer> ls = Lista.of(1, 2, 3, 4, 5);
        System.out.println("Append-TailCall: " + Lista.appendTailCall(99, ls));
        System.out.println("Prepend-TailCall: " + Lista.preppendTailCall(99, ls));
        System.out.println("Get-TailCall: " + ls.getTailCall(3));
        System.out.println("Insert-TailCall: " + Lista.insertTailCall(ls, 99, 2));
        System.out.println("Invert-TailCall: " + Lista.invertTailCall(ls));
        System.out.println("Take-TailCall: " + Lista.takeTailCall(ls, 3));
        System.out.println("Drop-TailCall: " + Lista.dropTailCall(ls, 3));

        //creamos otra Lista
        Lista<Integer> ls1 = Lista.of(6, 7, 8, 9);
        System.out.println("Concat-TailCall: " + Lista.concatTailCall(ls, ls1));
        System.out.println("Map-TailCall: " + Lista.mapTailCall(ls, (x) -> x * x));

        //Range-Tail-Call
        TailCall<Lista<Integer>> ListaRangeTailCall = Lista.rangeTailCall(1, 500_000, Lista.Empty);
        Lista<Integer> ListaRangeTailCallTest = ListaRangeTailCall.eval();
        System.out.println("Get- TailCall: " + ListaRangeTailCallTest.getTailCall(1000));
        System.out.println("Count-TailCall: " + Lista.countTailCall(ListaRangeTailCallTest));


    }


}