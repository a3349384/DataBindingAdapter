package cn.zmy.listfragment;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by zmy on 2018/1/3.
 */

public class SingleTextEmptyView implements IEmptyViewProvider
{
    private String mEmptyText;
    private int mTextColor;

    public SingleTextEmptyView()
    {
        mEmptyText = "无数据";
        mTextColor = Color.WHITE;
    }

    public SingleTextEmptyView(String mEmptyText, int mTextColor)
    {
        this.mEmptyText = mEmptyText;
        this.mTextColor = mTextColor;
    }

    @Override
    public View onCreateEmptyView(FrameLayout parent)
    {
        TextView textView = new TextView(parent.getContext());
        textView.setText(mEmptyText);
        textView.setTextColor(mTextColor);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
        textView.setGravity(Gravity.CENTER);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        textView.setLayoutParams(params);
        return textView;
    }
}
