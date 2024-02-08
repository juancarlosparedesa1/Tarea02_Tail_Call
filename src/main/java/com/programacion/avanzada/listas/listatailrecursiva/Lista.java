package com.programacion.avanzada.listas.listatailrecursiva;
import com.programacion.avanzada.listas.tailcall.TailCall;

import java.util.function.Function;
import java.util.function.Supplier;

public interface Lista<T> {
    Lista Empty=new Empty();
    T head();
    Lista<T> tail();
    boolean isEmpty();

    private static <T> Lista<T> of(T head, Lista<T> tail){
        return new Cons<>(head,tail);
    }

    static <T> Lista<T> of(T... elements){
        var tmp= Lista.Empty;

        for(int i=elements.length-1;i>=0;i--){
            tmp= new Cons(elements[i],tmp);
        }

        return tmp;
    }

    //COUNT
    static <T> int countTailRecursivo(int contador, Lista<T> lista){
        if(lista.isEmpty()){
            return contador;
        }else{
            return countTailRecursivo(contador+1,lista.tail());
        }
    }
    static <T> int countTailRecursive(Lista<T> lista){
        return countTailRecursivo(0,lista);
    }

    static <T> TailCall<Integer> countTailCallAux(Lista<T> lista, Integer intit) {
        if (lista.isEmpty()) {
            return TailCall.tailReturn(intit);
        } else {
            Supplier<TailCall<Integer>> ret = () ->
                    countTailCallAux(lista.tail(), intit + 1);
            return TailCall.tailSuspend(ret);
        }
    }

    static <T> Integer countTailCall(Lista<T> lista) {

        return countTailCallAux(lista, 0).eval();
    }

    //PREPEND
    static <T> Lista<T> preppendTailRecursivoAux(T val, Lista<T> original, Lista<T> acum) {
        if (acum.isEmpty()) {
            return Lista.of(val, original);
        } else {
            return preppendTailRecursivoAux(val,original.tail(), Lista.of(original.head(),acum));
        }
    }

    static <T> Lista<T> preppendTailRecursive(T val, Lista<T> original) {
        return preppendTailRecursivoAux(val, original, Lista.Empty);

    }

    static <T> TailCall<Lista<T>> preppendTailCallAux(T val, Lista<T> original) {
        if (original.isEmpty()) {
            return TailCall.tailReturn(Lista.of(val));
        } else {
            return TailCall.tailReturn(Lista.of(val, original));
        }
    }

    static <T> Lista<T> preppendTailCall(T val, Lista<T> original) {
        return preppendTailCallAux(val, original).eval();
    }

    //APPEND

    static <T> Lista<T> appendTailRecursivo(Lista<T> orginal, Lista<T> acum, T elem) {
        if (acum.isEmpty()) {
            return invertirTailRecursive(preppendTailRecursive(elem, invertirTailRecursive(orginal)));
        } else {
            return appendTailRecursivo(orginal.tail(), preppendTailRecursive(orginal.head(),acum), elem);
        }
    }
    static <T> Lista<T> appendTailRecursive(Lista<T> list, T elem) {
        return appendTailRecursivo(list, Lista.Empty, elem);
    }

    static <T> TailCall<Lista<T>> appendTailCallAux(Lista<T> original, Lista<T> acum, T elem) {
        if (acum.isEmpty()) {
            return TailCall.tailReturn(invertTailCall(Lista.of(elem, invertTailCall(original)))); //Ojo el invertir
        } else {
            return TailCall.tailSuspend(() -> appendTailCallAux2(original.tail(), preppendTailCall(original.head(),acum), elem));
        }
    }
    static <T> Lista<T> appendTailCall(T elem, Lista<T> list) {
        return appendTailCallAux(list, Lista.of(), elem).eval();
    }
    static <T> TailCall<Lista<T>> appendTailCallAux2(Lista<T> orginal, Lista<T> acum, T elem) {
        if (acum.isEmpty()) {
            return TailCall.tailReturn(invertTailCall(Lista.of(elem, orginal))); //Ojo el invertir
        } else {
            return TailCall.tailSuspend(() -> appendTailCallAux(Lista.of(orginal.head(), acum), acum.tail(), elem));
        }
    }

    //INSERT
    static <T> Lista<T> insertTailRecursivo(Lista<T> original, Lista<T> acum, T elem, int index){
        if(index==0){
            return invertirTailRecursive(concatTailRecursive(invertirTailRecursive(original), Lista.of(elem,acum)));
        }else{
            return insertTailRecursivo(original.tail(), preppendTailRecursive(original.head(),acum), elem,index-1);
        }

    }
    static <T> Lista<T> insertTailRecursive(Lista<T> orginal, T elem, int index) {

        return insertTailRecursivo(orginal, Lista.Empty, elem,index);

    }
    static <T> TailCall<Lista<T>> insertTailCallAux(Lista<T> original, Lista<T> acum, T elem, int index){
        if(index==0){
            return TailCall.tailReturn(invertirTailRecursive(concatTailRecursive(invertirTailRecursive(original), Lista.of(elem,acum))));    //Ojo el concat
        }else{
            Supplier<TailCall<Lista<T>>> ret=()-> insertTailCallAux(original.tail(), preppendTailRecursive(original.head(),acum), elem,index-1);
            return TailCall.tailSuspend(ret);
        }
    }
    static <T> Lista<T> insertTailCall(Lista<T> orginal, T elem, int index) {

        return insertTailCallAux(orginal, Lista.of(), elem,index).eval();

    }

    //GET

    default TailCall<T> getTailCallAux(int index){
        if(index==0 ){
            return TailCall.tailReturn(this.head());
        }else{
            Supplier<TailCall<T>> ret= ()-> this.tail().getTailCallAux(index-1);
            return TailCall.tailSuspend(ret);
        }
    }

    default T getTailCall(int index){
        return getTailCallAux(index).eval();
    }

    //TAKE
    static <T> Lista<T> takeTailRecursivoAux(Lista<T> list, Lista<T> acum, Integer elem) {
        if (list.isEmpty()) {
            return Lista.Empty;
        } else {
            if (elem == 0) {
                return acum;
            } else {
                return takeTailRecursivoAux(list.tail(), preppendTailRecursive(list.head(), acum), elem - 1);
            }
        }
    }

    static <T> Lista<T> takeTailRecursive(Lista<T> list, Integer elem) {
        return invertirTailRecursive(takeTailRecursivoAux(list, Lista.Empty, elem));
    }
    static <T> TailCall<Lista<T>> takeTailCallAux(Lista<T> list, Lista<T> acum, Integer elem) {
        if (list.isEmpty()) {
            return TailCall.tailReturn(Lista.Empty);
        } else {
            if (elem == 0) {
                return TailCall.tailReturn(acum);
            } else {
                Supplier<TailCall<Lista<T>>> ret = () ->
                        takeTailCallAux(list.tail(), preppendTailCall(list.head(), acum), elem - 1);
                return TailCall.tailSuspend(ret);
            }
        }

    }
    static <T> Lista<T> takeTailCall(Lista<T> list, Integer elem) {
        return invertTailCall(takeTailCallAux(list, Lista.of(), elem).eval());
    }

    //DROP

    static <T> Lista<T> dropTailRecursivoAux(Lista<T> list, Integer elem) {
        if (!(elem == 0)) {

            return dropTailRecursivoAux(list.tail(), elem - 1);

        } else if (elem == 0) {

            return list;
        } else {
            return list.tail();
        }

    }
    static <T> Lista<T> dropTailRecursive(Lista<T> list, Integer elem) {

        return dropTailRecursivoAux(list, elem);
    }
    static <T> TailCall<Lista<T>> dropTailCallAux(Lista<T> list, Integer elem) {
        if (!(elem == 0)) {
            Supplier<TailCall<Lista<T>>> ret = () ->
                    dropTailCallAux(list.tail(), elem - 1);
            return TailCall.tailSuspend(ret);
        } else if (elem == 0) {
            return TailCall.tailReturn(list);
        } else {
            return TailCall.tailReturn(list.tail());
        }
    }
    static <T> Lista<T> dropTailCall(Lista<T> list, Integer elem) {
        return dropTailCallAux(list, elem).eval();
    }

    //CONCAT

    static <T> Lista<T> concatTRBase(Lista<T> primero, Lista<T> segundo, Lista<T> acumulador) {
        if(segundo.isEmpty() && primero.isEmpty()){
            return invertirTailRecursive(acumulador);
        }else if(primero.isEmpty()){
            return concatTRBase(primero,segundo.tail(), preppendTailRecursive(segundo.head(),acumulador));
        }else{
            return concatTRBase(primero.tail(),segundo, preppendTailRecursive(primero.head(),acumulador));

        }
    }
    static <T> Lista<T> concatTailRecursive(Lista<T> primero, Lista<T> segundo) {
        return concatTRBase(primero,segundo, Lista.Empty);
    }

    static <T> TailCall<Lista<T>> concatTailCallAux(Lista<T> ListaTC1, Lista<T> ListaTC2) {
        if (ListaTC1.isEmpty()) {
            return TailCall.tailReturn(ListaTC2);
        } else {
            Supplier<TailCall<Lista<T>>> ret1 = () ->
                    concatTailCallAux(ListaTC1.tail(), preppendTailCall(ListaTC1.head(), ListaTC2));
            return TailCall.tailSuspend(ret1);
        }
    }

    static <T> Lista<T> concatTailCall(Lista<T> lista1, Lista<T> lista2) {
        return concatTailCallAux(invertTailCall(lista1), lista2).eval();
    }

    //MAP

    static <T, U> Lista<U> mapTailRecursivoAux(Lista<T> original, Lista<U> acumulador, Function<T, U> fn) {
        if (original.isEmpty()) {
            return invertirTailRecursive(acumulador);
        } else {
            return mapTailRecursivoAux(original.tail(), Lista.of(fn.apply(original.head()), acumulador), fn);
        }
    }

    static <T,U> Lista<U> mapTailRecursive(Lista<T> original, Function<T, U> fn) {
        return mapTailRecursivoAux(original, Lista.Empty, fn);
    }

    static <T, U> TailCall<Lista<U>> mapTailCallAux(Lista<T> original, Lista<U> acumulador, Function<T, U> fn) {
        if (original.isEmpty()) {
            return TailCall.tailReturn(invertirTailRecursive(acumulador));
        } else {
            Supplier<TailCall<Lista<U>>> ret= ()-> mapTailCallAux(original.tail(), Lista.of(fn.apply(original.head()), acumulador), fn);
            return TailCall.tailSuspend(ret);
        }
    }

    static <T,U> Lista<U> mapTailCall(Lista<T> original, Function<T, U> fn) {
        return (Lista<U>) mapTailCallAux(original, Lista.Empty, fn).eval();
    }

    //FOLD LEFT
    default <U> U foldLeft(U identity, Function<U,Function<T,U>> fn){
        U ret = identity;
        var tmp=this;

        while(!tmp.isEmpty()){
            ret =fn.apply(ret).apply(tmp.head());
            tmp=tmp.tail();
        }

        return ret;
    }


    //INVERT

    static <T> Lista<T> invertirTailRecursivoAux(Lista<T> listaTC, Lista<T> acum) {
        if (listaTC.isEmpty()) {
            return acum;
        } else {
            return invertirTailRecursivoAux(listaTC.tail(), preppendTailRecursive(listaTC.head(), acum));
        }
    }

    static <T> Lista<T> invertirTailRecursive(Lista<T> list) {
        return invertirTailRecursivoAux(list, Lista.Empty);
    }

    static <T> TailCall<Lista<T>> invertirTailCallAux(Lista<T> lista, Lista<T> acum) {
        if (lista.isEmpty()) {
            return TailCall.tailReturn(acum);
        } else {
            Supplier<TailCall<Lista<T>>> ret = () ->
                    invertirTailCallAux(lista.tail(), preppendTailCall(lista.head(), acum));
            return TailCall.tailSuspend(ret);
        }
    }

    static <T> Lista<T> invertTailCall(Lista<T> list) {
        return invertirTailCallAux(list, Lista.of()).eval();
    }


    static TailCall<Lista<Integer>> rangeTailCall(Integer start, Integer end, Lista<Integer> acc) {
        if (start < end) {
            Supplier<TailCall<Lista<Integer>>> ret = () ->{
                return  rangeTailCall(start,end - 1, preppendTailCall(end-1, acc));
            };

            return TailCall.tailSuspend(ret);

        } else {
            return TailCall.tailReturn(acc);
        }


    }


}