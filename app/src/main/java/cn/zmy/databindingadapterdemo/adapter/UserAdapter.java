package cn.zmy.databindingadapterdemo.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.zmy.databindingadapter.BaseBindingAdapter;
import cn.zmy.databindingadapter.BaseBindingViewHolder;
import cn.zmy.databindingadapterdemo.R;
import cn.zmy.databindingadapterdemo.databinding.ItemUserBinding;
import cn.zmy.databindingadapterdemo.model.User;

/**
 * Created by zmy on 2017/7/26.
 */

public class UserAdapter extends BaseBindingAdapter<User, ItemUserBinding>
{
    public UserAdapter(Context context)
    {
        super(context);
    }

    @Override
    protected int getLayoutResId(int viewType)
    {
        return R.layout.item_user;
    }

    @Override
    protected void onBindItem(ItemUserBinding binding, User user)
    {
        binding.setModel(user);
        binding.executePendingBindings();
    }
}
