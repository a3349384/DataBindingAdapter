package cn.zmy.listfragment.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zmy on 2018/1/19.
 */

public class BaseBindingViewHolder<B> extends RecyclerView.ViewHolder
{
    private B binding;

    public BaseBindingViewHolder(View itemView)
    {
        super(itemView);
    }

    public B getBinding()
    {
        return binding;
    }

    public void setBinding(B binding)
    {
        this.binding = binding;
    }
}
