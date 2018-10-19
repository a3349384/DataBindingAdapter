package cn.zmy.listfragment;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

/**
 * Created by zmy on 2017/12/26.
 */

public class RxJavaFragment extends Fragment
{
    protected static final int LIFECYCLE_CREATE = 1;
    protected static final int LIFECYCLE_PAUSE = 2;
    protected static final int LIFECYCLE_STOP = 3;
    protected static final int LIFECYCLE_DESTROY = 4;
    protected static final int LIFECYCLE_VISIBLE = 5;
    protected static final int LIFECYCLE_INVISIBLE = 6;

    @IntDef({LIFECYCLE_CREATE, LIFECYCLE_PAUSE, LIFECYCLE_STOP, LIFECYCLE_DESTROY, LIFECYCLE_VISIBLE, LIFECYCLE_INVISIBLE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LifeCycleEvent {}

    protected @LifeCycleEvent int mUnsubscribeWhenEvent;

    protected final PublishSubject<Integer> mLifecycleSubject;

    public RxJavaFragment()
    {
        mLifecycleSubject = PublishSubject.create();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (mUnsubscribeWhenEvent == LIFECYCLE_CREATE)
        {
            mLifecycleSubject.onNext(LIFECYCLE_CREATE);
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mUnsubscribeWhenEvent == LIFECYCLE_PAUSE)
        {
            mLifecycleSubject.onNext(LIFECYCLE_PAUSE);
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        if (mUnsubscribeWhenEvent == LIFECYCLE_STOP)
        {
            mLifecycleSubject.onNext(LIFECYCLE_STOP);
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mUnsubscribeWhenEvent == LIFECYCLE_DESTROY)
        {
            mLifecycleSubject.onNext(LIFECYCLE_DESTROY);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser)
        {
            if (mUnsubscribeWhenEvent == LIFECYCLE_VISIBLE)
            {
                mLifecycleSubject.onNext(LIFECYCLE_VISIBLE);
            }
        }
        else
        {
            if (mUnsubscribeWhenEvent == LIFECYCLE_INVISIBLE)
            {
                mLifecycleSubject.onNext(LIFECYCLE_INVISIBLE);
            }
        }
    }

    /**
     * 在指定生命周期取消订阅
     * Observable.compose(unsubscribeWhen())
     * */
    protected <T> ObservableTransformer<T, T> unsubscribeWhen(final @LifeCycleEvent int event)
    {
        mUnsubscribeWhenEvent = event;
        return observable -> observable.takeUntil(mLifecycleSubject);
    }

    protected <T> ObservableTransformer<T, T> ioThenMain()
    {
        return observable -> observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
