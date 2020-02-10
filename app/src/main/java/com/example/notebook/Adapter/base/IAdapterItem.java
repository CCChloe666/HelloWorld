package com.example.notebook.Adapter.base;

public interface IAdapterItem<T> {
    //将数据data绑定到position位置的View上
    void bindDataToView(T data, int position);
}
