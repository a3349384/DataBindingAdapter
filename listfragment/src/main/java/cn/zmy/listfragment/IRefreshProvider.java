package cn.zmy.listfragment;

/**
 * Created by zmy on 2017/12/16.
 */

public interface IRefreshProvider
{
    void startRefresh();

    void startLoadMore();

    void finishRefresh();

    void finishLoadMore();

    void setEnableRefresh(boolean enable);

    void setEnableLoadMore(boolean enable);

    boolean isRefreshing();

    boolean isLoadingMore();

    void setOnRefreshListener(Runnable listener);

    void setOnLoadMoreListener(Runnable listener);
}
