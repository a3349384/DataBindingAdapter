package cn.zmy.listfragment.rxjava;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by zmy on 2017/12/26.
 */

public class SimpleObserver<T> extends DisposableObserver<T>
{
    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    public void onNext(T t)
    {

    }

    @Override
    public void onError(Throwable e)
    {

    }

    @Override
    public void onComplete()
    {

    }
}
