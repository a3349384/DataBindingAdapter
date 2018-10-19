package cn.zmy.listfragment;

/**
 * Created by zmy on 2018/1/5.
 */

public class NothingRefreshProvider implements IRefreshProvider
{
    private Runnable mRunnableRefresh;
    private Runnable mRunnableLoadMore;

    @Override
    public void startRefresh()
    {
        if (mRunnableRefresh != null)
        {
            mRunnableRefresh.run();
        }
    }

    @Override
    public void startLoadMore()
    {
        if (mRunnableLoadMore != null)
        {
            mRunnableLoadMore.run();
        }
    }

    @Override
    public void finishRefresh()
    {

    }

    @Override
    public void finishLoadMore()
    {

    }

    @Override
    public void setEnableRefresh(boolean enable)
    {

    }

    @Override
    public void setEnableLoadMore(boolean enable)
    {

    }

    @Override
    public boolean isRefreshing()
    {
        return false;
    }

    @Override
    public boolean isLoadingMore()
    {
        return false;
    }

    @Override
    public void setOnRefreshListener(Runnable listener)
    {
        mRunnableRefresh = listener;
    }

    @Override
    public void setOnLoadMoreListener(Runnable listener)
    {
        mRunnableLoadMore = listener;
    }
}
