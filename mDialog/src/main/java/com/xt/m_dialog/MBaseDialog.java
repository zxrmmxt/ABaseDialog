package com.xt.m_dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by xuti on 2018/9/29.
 */
public abstract class MBaseDialog extends Dialog {
    private Context context;    //下面三个定义的跟上面讲得就是一样的啦
    private OnItemCheckListener onItemCheckListener;
    protected View view;    //看到这里我们定义的就清楚，我们也是借用view这个父类来引入布局的

    public MBaseDialog(Context context) {
        super(context, R.style.BaseDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    public void setOnItemCheckListener(OnItemCheckListener onItemCheckListener) {
        if (onItemCheckListener != null)
            this.onItemCheckListener = onItemCheckListener;
    }


    protected void init() {
        //以view来引入布局
//        View view = LayoutInflater.from(context).inflate(getLayoutId(), null);
        this.view = getRootView();
//        initViews(view);
        setContentView(view);
        //设置dialog大小
        final Window dialogWindow = getWindow();
        WindowManager manager = ((Activity) context).getWindowManager();
        final WindowManager.LayoutParams params = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        params.horizontalMargin = getHorizontalMargin();
        dialogWindow.setGravity(getGravity());
        Display d = manager.getDefaultDisplay(); // 获取屏幕宽、高度
        params.width = getWidth();
//        params.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65，根据实际情况调整
        params.height = getHeight();
        dialogWindow.setAttributes(params);
    }

    protected void setMaxHeight(final float percent) {
        final WindowManager.LayoutParams attributes = getWindow().getAttributes();
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
                windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
                float maxHeight = dm.heightPixels * percent;
                if (view.getHeight() > maxHeight) {
                    attributes.height = (int) maxHeight;
                    getWindow().setAttributes(attributes);
                }
                return false;
            }
        });
    }

    protected float getHorizontalMargin() {
        return 0;
    }

    protected int getHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    protected int getWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    protected int getGravity() {
        return Gravity.CENTER;
    }

    //可以看到这里定义了一个抽象方法，这个将交由子类去实现
//    public abstract int getLayoutId();
    public abstract View getRootView();

    //为了逻辑分层管理，接口的管理实现方式
    public interface OnItemCheckListener {
        void onItemCheck(int checkedId);
    }

    /*************点击空白地方，输入法隐藏******************/
    InputMethodManager manager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }
    /*************点击空白地方，输入法隐藏******************/
}
