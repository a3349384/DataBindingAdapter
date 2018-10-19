package cn.zmy.listfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import cn.zmy.listfragment.adapter.BaseListAdapter;
import cn.zmy.listfragment.rxjava.RxJavaHelper;

import java.util.List;

import io.reactivex.observers.DisposableObserver;

/**
 * Created by zmy on 2017/12/16.
 */

public abstract class BaseListFragment<M> extends RxJavaFragment
{
    protected View mContentView;
    protected View mEmptyView;
    protected FrameLayout mEmptyViewContainer;
    protected RecyclerView mRecyclerView;

    protected IListLayoutProvider mListLayoutProvider;
    protected IRefreshProvider mRefreshProvider;
    protected IEmptyViewProvider mEmptyViewProvider;
    protected BaseListAdapter<M, ?> mAdapter;

    protected int mCurrentPageIndex;
    private DisposableObserver<?> mRefreshObserver;
    private DisposableObserver<?> mLoadMoreObserver;

    private ItemsDataObserver mItemsDataObserver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        onConfig();
        if (mListLayoutProvider == null || mRefreshProvider == null || mAdapter == null)
        {
            return null;
        }
        mContentView = mListLayoutProvider.getRootLayout(getActivity());
        if (mContentView == null)
        {
            return null;
        }
        mRecyclerView = mListLayoutProvider.getRecyclerView(getActivity(), mContentView);
        if (mRecyclerView == null)
        {
            return null;
        }
        FrameLayout root = new FrameLayout(getActivity());
        //添加content view
        root.addView(mContentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        //添加empty view container
        mEmptyViewContainer = new FrameLayout(getActivity());
        mEmptyViewContainer.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        root.addView(mEmptyViewContainer);

        onViewCreate(mContentView, mRecyclerView);
        prepareEmptyView();

        mRecyclerView.setAdapter(mAdapter);
        mRefreshProvider.setOnRefreshListener(this::startRefresh);
        mRefreshProvider.setOnLoadMoreListener(this::startLoadMore);
        onReady();
        return root;
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mRefreshProvider.finishRefresh();
        mRefreshProvider.finishLoadMore();
        RxJavaHelper.disposeObserver(mRefreshObserver);
        RxJavaHelper.disposeObserver(mLoadMoreObserver);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterItemObserver();
    }

    protected void onViewCreate(View root, RecyclerView recyclerView)
    {

    }

    protected void onConfig()
    {
        mItemsDataObserver = new ItemsDataObserver();
        setEmptyViewProvider(new SingleTextEmptyView());
    }

    protected void startRefresh()
    {
        resetStartPageIndex();
        onRefreshStarted();
        RxJavaHelper.disposeObserver(mRefreshObserver);
        RxJavaHelper.disposeObserver(mLoadMoreObserver);
        mRefreshObserver = RxJavaHelper.createIoThenMain(() -> getItems(mCurrentPageIndex))
                                   .subscribeWith(new RefreshObserver());
    }

    protected void startLoadMore()
    {
        onLoadMoreStarted();
        RxJavaHelper.disposeObserver(mRefreshObserver);
        RxJavaHelper.disposeObserver(mLoadMoreObserver);
        mLoadMoreObserver = RxJavaHelper.createIoThenMain(() -> getItems(mCurrentPageIndex + 1))
                                    .subscribeWith(new LoadMoreObserver());
    }

    protected void setListLayoutProvider(IListLayoutProvider provider)
    {
        mListLayoutProvider = provider;
    }

    protected void setRefreshProvider(IRefreshProvider provider)
    {
        mRefreshProvider = provider;
    }

    protected void setEmptyViewProvider(IEmptyViewProvider provider)
    {
        mEmptyViewProvider = provider;
        prepareEmptyView();
    }

    protected void setAdapter(BaseListAdapter<M, ?> adapter)
    {
        unregisterItemObserver();
        if (adapter != null)
        {
            adapter.registerAdapterDataObserver(mItemsDataObserver);
        }
        mAdapter = adapter;
        mItemsDataObserver.refreshEmptyViewState();
    }

    protected abstract List<M> getItems(int pageIndex);

    protected void resetStartPageIndex()
    {
        mCurrentPageIndex = 1;
    }

    protected void onRefreshStarted()
    {

    }

    protected void onRefreshCompleted()
    {

    }

    protected void onLoadMoreStarted()
    {

    }

    protected void onLoadMoreCompleted()
    {

    }

    protected void onReady()
    {
        mItemsDataObserver.refreshEmptyViewState();
    }

    private void unregisterItemObserver()
    {
        //当前RecyclerView Adapter不存在Api检测是否注册Observer，故这里用trycache避免异常
        if (mAdapter != null && mItemsDataObserver != null)
        {
            try
            {
                mAdapter.unregisterAdapterDataObserver(mItemsDataObserver);
            }
            catch (Exception ex){}
        }
    }

    private void prepareEmptyView()
    {
        if (mEmptyViewContainer == null)
        {
            return;
        }
        mEmptyViewContainer.removeAllViews();
        if (mEmptyViewProvider != null)
        {
            mEmptyView = mEmptyViewProvider.onCreateEmptyView(mEmptyViewContainer);
            if (mEmptyView != null)
            {
                mEmptyViewContainer.addView(mEmptyView);
            }
            mItemsDataObserver.onChanged();
        }
    }

    private class RefreshObserver extends DisposableObserver<List<M>>
    {
        @Override
        protected void onStart()
        {
            mAdapter.getItems().clear();
        }

        @Override
        public void onNext(List<M> items)
        {
            if (items != null)
            {
                mAdapter.getItems().addAll(items);
            }
            mAdapter.onRefreshCompleted();
            onRefreshCompleted();
        }

        @Override
        public void onError(Throwable e)
        {
            e.printStackTrace();
            mRefreshProvider.finishRefresh();
        }

        @Override
        public void onComplete()
        {
            mRefreshProvider.finishRefresh();
        }
    }

    private class LoadMoreObserver extends DisposableObserver<List<M>>
    {
        @Override
        public void onNext(List<M> items)
        {
            if (items.size() == 0)
            {
                mRefreshProvider.setEnableLoadMore(false);
            }
            mAdapter.getItems().addAll(items);
            mAdapter.onLoadMoreCompleted();
            onLoadMoreCompleted();
        }

        @Override
        public void onError(Throwable e)
        {
            e.printStackTrace();
            mRefreshProvider.finishLoadMore();
        }

        @Override
        public void onComplete()
        {
            mCurrentPageIndex++;
            mRefreshProvider.finishLoadMore();
        }
    }

    private class ItemsDataObserver extends RecyclerView.AdapterDataObserver
    {
        @Override
        public void onChanged()
        {
            super.onChanged();
            refreshEmptyViewState();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount)
        {
            super.onItemRangeChanged(positionStart, itemCount);
            refreshEmptyViewState();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload)
        {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            refreshEmptyViewState();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount)
        {
            super.onItemRangeInserted(positionStart, itemCount);
            refreshEmptyViewState();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount)
        {
            super.onItemRangeRemoved(positionStart, itemCount);
            refreshEmptyViewState();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount)
        {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            refreshEmptyViewState();
        }

        private void refreshEmptyViewState()
        {
            if (mAdapter == null)
            {
                setEmptyViewVisible(true);
                return;
            }
            setEmptyViewVisible(mAdapter.getItemCount() == 0);
        }

        private void setEmptyViewVisible(boolean visible)
        {
            if (visible)
            {
                if (mEmptyView != null)
                {
                    mEmptyView.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                if (mEmptyView != null)
                {
                    mEmptyView.setVisibility(View.GONE);
                }
            }
        }
    }
}
