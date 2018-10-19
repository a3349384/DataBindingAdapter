package cn.zmy.listfragment.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import cn.zmy.listfragment.adapter.viewholder.BaseBindingViewHolder;


/**
 * Created by zmy on 2018/1/19.
 */

public abstract class BaseBindingListAdapter<M, B extends ViewDataBinding> extends BaseListAdapter<M, BaseBindingViewHolder<B>>
{
    @Override
    public BaseBindingViewHolder<B> onCreateViewHolder(ViewGroup parent, int viewType)
    {
        B binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), getItemLayout(viewType), parent, false);
        BaseBindingViewHolder<B> viewHolder = new BaseBindingViewHolder<B>(binding.getRoot());
        viewHolder.setBinding(binding);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(BaseBindingViewHolder<B> holder, int position)
    {
        onBindItem(holder.getBinding(), position);
    }

    protected abstract @LayoutRes int getItemLayout(int viewType);

    protected abstract void onBindItem(B binding, int position);
}
