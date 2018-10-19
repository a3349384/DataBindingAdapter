package cn.zmy.listfragment;

/**
 * Created by zmy on 2018/1/5.
 */

public abstract class SimpleListFragment<M> extends BaseListFragment<M>
{
    @Override
    protected void onConfig()
    {
        super.onConfig();
        setListLayoutProvider(new SingleRecyclerViewLayoutProvider());
        setRefreshProvider(new NothingRefreshProvider());
    }

    @Override
    protected void onReady()
    {
        super.onReady();
        mRefreshProvider.startRefresh();
    }
}
