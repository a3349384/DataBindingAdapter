package cn.zmy.listfragment.rxjava;


import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zmy on 2017/12/26.
 */

public class RxJavaHelper
{
    /**
     * 创建一个Observable
     * <p>主要解决RxJava2不再支持发射null值的问题。</p>
     * <p>RxJava2的逻辑：如果发射null，如果没有取消订阅，会走onError。如果已取消订阅，会抛出异常</p>
     * <p>处理后的逻辑：如果已取消订阅，所有发射的值都会被丢弃，如果没有取消订阅，null值会走onError</p>
     * */
    public static <T> Observable<T> create(Callable<T> callable)
    {
        return Observable.create(e ->
        {
            try
            {
                T result = callable.call();
                if (result == null)
                {
                    if (!e.isDisposed())
                    {
                        e.onError(new NullPointerException("The result is null."));
                    }
                    return;
                }
                if (!e.isDisposed())
                {
                    e.onNext(result);
                    e.onComplete();
                }
            }
            catch (Exception ex)
            {
                if (!e.isDisposed())
                {
                    e.onError(ex);
                }
            }
        });
    }

    /**
     * <p>被观察者在IO线程中工作，观察者在main线程中工作</p>
     * @see #create(Callable)
     * */
    public static <T> Observable<T> createIoThenMain(Callable<T> callable)
    {
        Observable<T> observable = create(callable);
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public static void disposeObserver(Disposable disposable)
    {
        if (disposable != null && !disposable.isDisposed())
        {
            disposable.dispose();
        }
    }
}
