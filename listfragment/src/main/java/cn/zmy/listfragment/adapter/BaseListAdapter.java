package cn.zmy.listfragment.adapter;

import android.databinding.ObservableArrayList;
import android.support.v7.widget.RecyclerView;

/**
 * Created by zmy on 2017/12/7.
 */

public abstract class BaseListAdapter<M, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>
        implements IListChangeCallback<ObservableArrayList<M>>
{
    protected ObservableArrayList<M> mItems;
    private ListChangedCallbackProxy mItemsChangeCallback;
    protected OnItemClickListener<M> mItemClickListener;

    public BaseListAdapter()
    {
        this.mItems = new ObservableArrayList<>();
        this.mItemsChangeCallback = new ListChangedCallbackProxy(this);
    }

    public ObservableArrayList<M> getItems()
    {
        return mItems;
    }

    @Override
    public int getItemCount()
    {
        return this.mItems.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView)
    {
        super.onAttachedToRecyclerView(recyclerView);
        this.mItems.addOnListChangedCallback(mItemsChangeCallback);
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView)
    {
        super.onDetachedFromRecyclerView(recyclerView);
        this.mItems.removeOnListChangedCallback(mItemsChangeCallback);
    }

    public void onRefreshCompleted()
    {

    }

    public void onLoadMoreCompleted()
    {

    }

    public void setItemClickListener(OnItemClickListener<M> listener)
    {
        this.mItemClickListener = listener;
    }

    //region 处理数据集变化
    @Override
    public void onChanged(ObservableArrayList<M> sender)
    {
        notifyDataSetChanged();
    }

    @Override
    public void onItemRangeChanged(ObservableArrayList<M> sender, int positionStart, int itemCount)
    {
        notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void onItemRangeInserted(ObservableArrayList<M> sender, int positionStart, int itemCount)
    {
        notifyItemRangeInserted(positionStart, itemCount);
        notifyItemRangeChanged(positionStart + itemCount, mItems.size() - positionStart - itemCount);
    }

    @Override
    public void onItemRangeMoved(ObservableArrayList<M> sender, int fromPosition, int toPosition, int itemCount)
    {
        notifyDataSetChanged();
    }

    @Override
    public void onItemRangeRemoved(ObservableArrayList<M> sender, int positionStart, int itemCount)
    {
        notifyItemRangeRemoved(positionStart, itemCount);
        notifyItemRangeChanged(positionStart, mItems.size() - positionStart);
    }
    //endregion

    class ListChangedCallbackProxy extends ObservableArrayList.OnListChangedCallback<ObservableArrayList<M>>
    {
        private IListChangeCallback<ObservableArrayList<M>> mBase;

        ListChangedCallbackProxy(IListChangeCallback<ObservableArrayList<M>> base)
        {
            this.mBase = base;
        }

        @Override
        public void onChanged(ObservableArrayList<M> sender)
        {
            mBase.onChanged(sender);
        }

        @Override
        public void onItemRangeChanged(ObservableArrayList<M> sender, int positionStart, int itemCount)
        {
            mBase.onItemRangeChanged(sender, positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(ObservableArrayList<M> sender, int positionStart, int itemCount)
        {
            mBase.onItemRangeInserted(sender, positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(ObservableArrayList<M> sender, int fromPosition, int toPosition, int itemCount)
        {
            mBase.onItemRangeMoved(sender, fromPosition, toPosition, itemCount);
        }

        @Override
        public void onItemRangeRemoved(ObservableArrayList<M> sender, int positionStart, int itemCount)
        {
            mBase.onItemRangeRemoved(sender, positionStart, itemCount);
        }
    }

    public interface OnItemClickListener<M>
    {
        void onItemClick(M item);
    }
}