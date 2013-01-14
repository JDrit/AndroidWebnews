package edu.rit.csh.androidwebnews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

public class WebnewsExpandableListView extends ExpandableListView {

	public int height;
	
	public WebnewsExpandableListView(Context context) {
		super(context);
		height = getMeasuredHeight();
	}
	
	public WebnewsExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		height = getMeasuredHeight();
		
	}
	
	public WebnewsExpandableListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		height = getMeasuredHeight();
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		//setMeasuredDimension(getMeasuredWidth(), height);
	}
	
	public void setDimensions(int widthMeasureSpec, int heightMeasureSpec)
	{
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}

}