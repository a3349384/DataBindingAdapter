package cn.zmy.listfragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zmy on 2018/1/5.
 */

public class SingleRecyclerViewLayoutProvider implements IListLayoutProvider
{
    @Override
    public View getRootLayout(Context context)
    {
        RecyclerView recyclerView = new RecyclerView(context);
        return recyclerView;
    }

    @Override
    public RecyclerView getRecyclerView(Context context, View root)
    {
        return (RecyclerView) root;
    }
}
