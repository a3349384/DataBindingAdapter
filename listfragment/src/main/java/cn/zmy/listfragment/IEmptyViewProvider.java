package cn.zmy.listfragment;

import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by zmy on 2018/1/3.
 */

public interface IEmptyViewProvider
{
    View onCreateEmptyView(FrameLayout parent);
}
