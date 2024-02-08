package com.programacion.avanzada.listas.tailcall;

final class Return<T> implements TailCall<T> {
    private T value;

    Return(T value) {
        this.value = value;
    }

    @Override
    public T eval() {
        return value;
    }

    @Override
    public TailCall<T> resume() {
        return null;
    }

    @Override
    public boolean isSuspend() {
        return false;
    }

}
