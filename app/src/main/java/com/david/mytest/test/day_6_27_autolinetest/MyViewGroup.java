package com.david.mytest.test.day_6_27_autolinetest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MyViewGroup extends ViewGroup implements View.OnClickListener {

	interface OnViewClickListener{//条目点击事件
		void OnViewClick(int position);
	}

	static final String TAG = MyViewGroup.class.getName();
	/** 
     * 存储所有的View，按行记录 
     * 每个item就是记录的每行的view
     */  
    private List<List<View>> mAllViews = new ArrayList<List<View>>();

	private OnViewClickListener mOnViewClickListener;

    /** 
     * 记录每一行的最大高度 
     */  
    private List<Integer> mLineHeight = new ArrayList<Integer>();  

	public MyViewGroup(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public MyViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		mAllViews.clear();
		mLineHeight.clear();
		//获取了父view的行宽
		int width = getWidth();
		//行宽
		int lineWidth = 0;
		//行高
		int lineHeight = 0;
		//存储每一行的view
		List<View> lineViews = new ArrayList();
		int childSize = getChildCount();
		//遍历所有view
		for(int i=0; i<childSize; i++) {
			View child = getChildAt(i);
			//获取每个子view的宽高
			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();
			MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
			if(lineWidth +lp.leftMargin+lp.rightMargin+childWidth > width) {
			    //这个时候需要换行了，那么就需要把行高增加一行，然后保存行的viewlist也需要
			    //增加一个元素
				mLineHeight.add(lineHeight);
				mAllViews.add(lineViews);
				//重新初始化行宽，后面会把这一行的第一个元素的宽度加上
				lineWidth = 0;
				//重新初始化保存一行中所有view的list
				lineViews = new ArrayList();
			}
			//第一行的第一个元素进行保存
			lineWidth += lp.leftMargin+lp.rightMargin+childWidth;
			lineHeight = Math.max(lineHeight, childHeight+lp.topMargin+lp.bottomMargin);
			lineViews.add(child);
		}
		//因为上面的代码不能把最后一行包括进去。所以把最后一行包涵进去
		mAllViews.add(lineViews);
		mLineHeight.add(lineHeight);

		//以下开始画此viewgroup第一行的时候左上都为0.
		int left = 0;
		int top = 0;
		int linesNumber = mAllViews.size();
		for(int j=0; j<linesNumber; j++) {
		    //每一行为单位
			lineViews = mAllViews.get(j);
			lineHeight = mLineHeight.get(j);
			int lineNumbers = lineViews.size();
			for(int n=0; n<lineNumbers; n++) {
				View view = lineViews.get(n);
				if(view.getVisibility() == View.GONE) {
				    //如果是不可见的那么不需要为它分配位置
					continue;
				}
				MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
				int vl = left + lp.leftMargin;
				int vr = vl+view.getMeasuredWidth();
				int vt = top +lp.topMargin;
				int vb = vt+view.getMeasuredHeight();
				//为每个view分配具体位置
				view.layout(vl, vt, vr, vb);
				left += view.getMeasuredWidth()+lp.rightMargin+lp.leftMargin;
			}
			//下一行的初始化
			left = 0;
			top += lineHeight;
		}

		initClickListener();
	}

	private void initClickListener() {
		int index = 0;
		for (List<View> views : mAllViews){
			for (View view:views){
				view.setTag(index);
				view.setOnClickListener(this);
				++index;
			}
		}
	}

	@Override
	 protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		//获得父类的大小和设置方式
		int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);
		int parentHeightMode = MeasureSpec.getMode(heightMeasureSpec);
		//获取宽高
		int parentWidthSize = MeasureSpec.getSize(widthMeasureSpec);
		int parentHeifhtSize = MeasureSpec.getSize(heightMeasureSpec);

		//wrap_content 的情况记录最大宽和高
		int width = 0;
		int height = 0;

		//行宽和行高
		int lineWidth = 0;
		int lineHeight = 0;

		int childSize = getChildCount();
		for(int i=0; i<childSize; i++) {
			View view = getChildAt(i);
			if(view == null) {
				return;
			}
			//measure child获取child的大小，所有的view必须先measure一下然后通过
			//getMeasureWidth 和getMeasureHeight获取准确值
			measureChild(view, widthMeasureSpec, heightMeasureSpec);
			MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();
			int childWidth = lp.leftMargin+view.getMeasuredWidth()+lp.rightMargin;
			int childHeight = lp.topMargin+view.getMeasuredHeight()+lp.bottomMargin;

			//判断此行是否超过了parent的宽
			if(lineWidth+childWidth > parentWidthSize) {
				//因为此时已经占用的行宽加上当前的view的宽已经超过一行，所以这个时候
				//需要换行，当前view就在新的一行，而换行之后新的一行的当前行宽就是当前view的宽
				width = Math.max(lineWidth, childWidth);//取最大值
				lineWidth = childWidth; //新一行的宽就是child的宽。
				height += lineHeight;//行高加一行的高度。
				lineHeight = childHeight;//新一行的高就是新的view的高度
			}else {
				//如果当前一行没有满，那么增加行款，然后行高取当前行高和最新view的
				//高的最大值
				lineWidth += childWidth;
				lineHeight = Math.max(lineHeight,childHeight);
			}

			// 如果是最后一个，则将当前记录的最大宽度和当前lineWidth做比较
			if(i == childSize -1) {
				width = Math.max(lineWidth, width);
				height += lineHeight;
			}
		}

		//真正对view的大小起作用的是这里。
		setMeasuredDimension((parentWidthMode == MeasureSpec.EXACTLY ? parentWidthSize : width)
				, (parentHeightMode == MeasureSpec.EXACTLY ? parentHeifhtSize : height));
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs)
	{
		return new MarginLayoutParams(getContext(), attrs);
	}

	public void setOnViewClickListener(OnViewClickListener mOnViewClickListener) {
		this.mOnViewClickListener = mOnViewClickListener;
	}

	@Override
	public void onClick(View v) {
		if (mOnViewClickListener != null) {
			mOnViewClickListener.OnViewClick((Integer) v.getTag());
		}
	}
}
