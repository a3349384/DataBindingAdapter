package cn.zmy.listfragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zmy on 2017/12/16.
 */

public interface IListLayoutProvider
{
    View getRootLayout(Context context);

    RecyclerView getRecyclerView(Context context, View root);
}
