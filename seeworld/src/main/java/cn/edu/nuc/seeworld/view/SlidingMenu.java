package cn.edu.nuc.seeworld.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import cn.edu.nuc.seeworld.Config;
import cn.edu.nuc.seeworld.R;

/**
 * Created by lenovo on 2015/9/9.
 */
public class SlidingMenu extends HorizontalScrollView {
    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private int mScreenWidth;
    private boolean once=false;
    private int mMenuWidth;
    //dp
    private int mMenuRightPidding=50;
    private boolean isOpen;
    //未使用自定义属性时调用
    public SlidingMenu(Context context, AttributeSet attrs) {
        this(context, attrs, -1);

    }

    public SlidingMenu(Context context) {
        this(context, null);
    }
    //使用自定义属性的时候设置
    public SlidingMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray a=context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu,defStyleAttr,0);
        int n=a.getIndexCount();
        for(int i=0;i<n;i++){
            int attr=a.getIndex(i);
            switch (attr){
                case R.styleable.SlidingMenu_rightPadding:
                    mMenuRightPidding=a.getDimensionPixelSize(attr,(int) TypedValue
                            .applyDimension(TypedValue.COMPLEX_UNIT_DIP,50,context.getResources().getDisplayMetrics()));
                    break;
                default:
                    break;
            }
        }
        a.recycle();
        WindowManager wm= (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth=outMetrics.widthPixels;
    }

    //设置子view的宽和高，自己的宽和高
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(!once){
            mWapper= (LinearLayout) getChildAt(0);
            mMenu= (ViewGroup) mWapper.getChildAt(0);
            mContent= (ViewGroup) mWapper.getChildAt(1);
            mMenuWidth=mMenu.getLayoutParams().width=mScreenWidth-mMenuRightPidding;
            mContent.getLayoutParams().width=mScreenWidth;
            once=true;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //通过设置偏移量将menu隐藏
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(changed){
            this.scrollTo(mMenuWidth,0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if(Config.isStreeFg){
            return true;
        }
        int action=ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                if (scrollX >= mMenuWidth / 2) {
                    this.smoothScrollTo(mMenuWidth, 0);
                    isOpen = false;
                } else {
                    this.smoothScrollTo(0, 0);
                    isOpen = true;
                }
                return true;
        }
        return super.onTouchEvent(ev);

    }
    public void openMenu(){
        if(isOpen)return;
        this.smoothScrollTo(0,0);
        isOpen=true;
    }
    public void closeMenu(){
        if(!isOpen)return;
        this.smoothScrollTo(mMenuWidth,0);
        isOpen=false;
    }
    //切换菜单
    public void toggle(){
        if(isOpen){
            closeMenu();
        }else {
            openMenu();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        float scale = l * 1.0f / mMenuWidth; // 1 ~ 0

        /**
         * 区别1：内容区域1.0~0.7 缩放的效果 scale : 1.0~0.0 0.7 + 0.3 * scale
         *
         * 区别2：菜单的偏移量需要修改
         *
         * 区别3：菜单的显示时有缩放以及透明度变化 缩放：0.7 ~1.0 1.0 - scale * 0.3 透明度 0.6 ~ 1.0
         * 0.6+ 0.4 * (1- scale) ;
         *
         */
        float rightScale = 0.8f + 0.2f * scale;
        float leftScale = 1.0f - scale * 0.3f;
        float leftAlpha = 0.6f + 0.4f * (1 - scale);

        // 调用属性动画，设置TranslationX
        mMenu.setTranslationX(mMenuWidth * scale * 0.8f);
        mMenu.setScaleX(leftScale);
        mMenu.setScaleY(leftScale);
        mMenu.setAlpha(leftAlpha);
        // 设置content的缩放的中心点
        mContent.setPivotX(0);
        mContent.setPivotY(mContent.getHeight() / 2);
        mContent.setScaleX(rightScale);
        mContent.setScaleY(rightScale);
    }
}
