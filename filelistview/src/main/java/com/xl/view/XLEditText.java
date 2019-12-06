package com.xl.view;
import android.text.*;
import android.text.style.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import android.view.inputmethod.*;
import android.content.*;
//import com.xl.Clip.*;
import android.widget.TextView.*;
import java.util.*;


public class XLEditText extends EditText implements Runnable
{

	@Override
	public void run()
	{
		if(isHighLight)
		highlight();
	}
	
	//是否显示行号
	boolean isDrawLine;
	//是否自动换行
	boolean isAutoLine=true;
	//自动缩进
	boolean isAutoText;
	//是否自动提示
	boolean isAuto;
	//是否高亮
	boolean isHighLight;
	
	//保存高亮信息
	int high_start;
	
  //是否为debug模式
	boolean debug;
	
	public XLEditText(android.content.Context context) 
	{
		
		super(context);
		initEdit();
		;
	}

	

	public XLEditText(android.content.Context context, android.util.AttributeSet attrs)
	{
		
		super(context,attrs);
		
		initEdit();
	}

	public XLEditText(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr)
	{
		super(context,attrs,defStyleAttr);
		
		initEdit();
	}

	@Override
	protected void onTextChanged(CharSequence text, int start, int lengthBefore, int count)
	{
		super.onTextChanged(text, start, lengthBefore, count);
		
		if(count>0)
		highlight();
		
		//换行
		Editable edit = getEditableText();
		if (isAutoText && count == 1 && text.charAt(start) == '\n')
		{

			//setLine(getLineCount());
			InputMethodManager imm=(InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			CharSequence space;
			String enter="\n";

			if (imm.isActive())
			{
				//char text[]=edit_c.getText().toString().toCharArray();
				int gb=getSelectionStart();
				int index=gb;
				int spacesize=0;
				//跳转到上一个回车后
				if (gb > 0)gb--;
				while (gb > 0)
				{
					gb--;
					if (edit.charAt(gb) == '\n')
						break;

				}

				//向后查找空格数
				if (gb < edit.length())gb++;
				int i=gb;

				while (i < edit.length())
				{

					if (edit.charAt(i) == ' ')
					{
						i++;
						spacesize++;
					}
					else
						break;

				}

				//当上一行有括号，那么多插一个空格
				if (index > 3)
					if (edit.charAt(index - 2) == '{')
						edit.insert(index, " ");
				//Log.e("XL", ""+ edit.charAt(index-3));


				if (spacesize > 0)
				{

					space = edit.subSequence(gb, i);
					//edit	= edit_c.getEditableText();//获取EditText的文字
					//插入空格

					edit.insert(index, space);

					//edit_c.setText(space,edit_c.getSelectionStart(),spacesize);
				}
				//edit.insert(index,enter);
				//	imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);

			}
		}
			
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		
		super.onLayout(changed, left, top, right, bottom);
	  
	}
  
	int h_scroll_x,h_scroll_y;
	@Override
	protected void onDraw(Canvas canvas)
	{
		int scroll_x=getScrollX();
		int scroll_y=getScrollY();
		if(h_scroll_x!=scroll_x || h_scroll_y!=scroll_y)
		{
		if(isHighLight)
		highlight();
		h_scroll_x = scroll_x;
		h_scroll_y = scroll_y;
		}
		
		
		super.onDraw(canvas);
		//显示start
		if(debug)
		{
		Paint paint =new Paint();
		paint.setColor(0xfff0f0f0);
		canvas.drawText(""+high_start,0,getScrollY()+ getHeight(),paint);
		}
	}

	@Override
	public void setText(CharSequence text, TextView.BufferType type)
	{
		// TODO: Implement this method
		super.setText(text, type);
		if(text.length()>30000)
			isHighLight=false;
		high_start=0;
		post(this);
	}
	
	
	
	
	
	int 
	color_background=0xff404040,
	color_text=0xff40a0ff, //文字 蓝
	color_string=0xff90e090, //字符串 绿
	color_operator=0xff60a0f0,//英文符号
	color_char=0xfff0707a,    //字符 红
	color_include=0xfff0c05a,   //预编译
	color_com=0xff50e050, //注释 绿
	color_error=0xfff0a030, //错误
  color_keyworlds=0xfff09090;//关键字
	
	public void setHighLight(boolean isHighLight)
	{
		this.isHighLight = isHighLight;
	}
	
	//代码高亮
	void highlight()
	{
		int i;
		int len=length();
		char c=0;
		Layout layout = getLayout();
		if(layout==null)return;
		int baselines=getLineHeight();
		int line = (getHeight())/baselines;
		//起始高亮位置
		int h_start=0;
		try
		{
		h_start=layout.getLineStart( getScrollY()/baselines);
		}
		catch(IndexOutOfBoundsException e)
		{
			return;
		}
		int h_end=0;
		//起始循环位置
		int i_start=0;
		if(high_start<= h_start)
		i_start=high_start;
		if(getLineCount()>h_start + line)
		{
			h_end= layout.getLineEnd(h_start+line);
		}
		else
		{
			h_end= length();
		}
		
		int type=0;
		int start=0;
		int end;
		Editable editable = getText();
		SpannableStringBuilder builder = (SpannableStringBuilder)editable;
		for (Object span:builder.getSpans(0, len, ForegroundColorSpan.class))
		{
			builder.removeSpan(span);
		}
		
		
		for(i=i_start;i<h_end;i++)
		{
			c=editable.charAt(i);
			switch(type)
			{
				case 0: //查找左括号
				if(i<h_start)
				high_start = i;
					if(c=='<')
					{
						start=i;
						type=1;
						
					}
					else if(c=='(')
					{
						type=7;
					}
					break;
				case 1: //查找空格 高亮az
					if(c==' ')
					{
						if(i>=h_start && start<h_end)
						{
						ForegroundColorSpan span = new ForegroundColorSpan(color_operator);
						builder.setSpan(span, start, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						start=i;
						type=2;
					}
					else if(c=='!') //注释或。。
					{
						start=i-1;
						type=5;
					}
					else if(c=='>')
					{
						if(i>=h_start && start<h_end)
						{
						ForegroundColorSpan span = new ForegroundColorSpan(color_operator);
						builder.setSpan(span, start, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						
						
						type=0;
					}
					
					break;
				case 2: //匹配参数
				if(c=='=')
				{
					if(i>h_start)
					{
					ForegroundColorSpan span = new ForegroundColorSpan(color_keyworlds);
					builder.setSpan(span, start, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					 span = new ForegroundColorSpan(color_char);
					builder.setSpan(span, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
					type=3;
				}
				else if(c=='>')
				{
					type=0;
					if(i>h_start)
					{
					ForegroundColorSpan span = new ForegroundColorSpan(color_operator);
					builder.setSpan(span, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
					break;
				case 3: //  "
				if(c=='\"')
				{
					type=4;
					
					start=i;
				}
				if(c=='>') //如果是以反括号结尾 那么就等待下一个</出现 中间内容跳过
				{
					type=0;
					if(i>h_start)
					{
					ForegroundColorSpan span = new ForegroundColorSpan(color_operator);
					builder.setSpan(span, i, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
				break;
				case 4:  //  "
				if(c=='\"')
				{
					if(i>h_start)
					{
					ForegroundColorSpan span = new ForegroundColorSpan(color_string);
					
					builder.setSpan(span, start, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					
					}
					start=i+1;
					type=1;
				}
				else if(c=='>')
					type=0;
				break;
				case 5:  //  高亮注释
				if(c== '-')
				{
					type=9;
				}
				if(c=='>')
				{
					if(i>h_start)
					{
					ForegroundColorSpan span = new ForegroundColorSpan(color_operator);
					builder.setSpan(span, start, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
					type=0;
				}
				break;
				case 6:
					
					break;
				case 7: //)
					if(c==')')
					{
						type=0;
					}
				break; 
				case 8:  //}
					break;
				case 9: //-
				  if(c=='-')
				  type=10;
					else
						type=5;
				break;
				case 10: //注释
					if(c=='>')
					{
						if(i>h_start)
						{
							ForegroundColorSpan span = new ForegroundColorSpan(color_com);
							builder.setSpan(span, start, i+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						type=0;
					}
					break;
			}
		}
		
	}
/*
	public xlEditText(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) 
	{
		super(context,attrs,defStyleAttr,defStyleRes);
		initScrollView();
		initEdit();
	}
	*/
	
	
	
	
int mScrollX;
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
					
				break;
			case MotionEvent.ACTION_MOVE:
				if(isMove)
				{
					//设置隐藏光标
					//setCursorVisible(false);
				}
				break;
			case MotionEvent.ACTION_UP:
				if(!isMove)
					setCursorVisible(true);
				mScrollX=getScrollX();

				super.onTouchEvent(event);
				scrollTo(mScrollX,getScrollY());
				return true;
			//	break;
		}
		 super.onTouchEvent(event);
		
		
		return true;
	}
	
	void initEdit()
	{
		isAutoText=true;
		high_start=0;
		isHighLight=true;
		//setScrollbarFadingEnabled(true);
		//setHorizontallyScrolling(true);
	}
	
	
	boolean isMove;
	private static final int INVALID_POINTER = -1;
	private static final float OVER_MOVE_SCALE = 0.25f;

	private int mOverScrollDistance = 100;
	private int mOverflingDistance = mOverScrollDistance/2;

	private float lastX,lastY;
	private int mActivePointerId;

	private boolean mIsBeingDragged;

	
	private int mTouchSlop;

	boolean isHor;

	 //横向滑动条

	/*
	 public MyScrollerView3(Context context, AttributeSet attrs, int defStyle) {
	 super(context, attrs, defStyle);
	 initScrollView();
	 }

	 public MyScrollerView3(Context context, AttributeSet attrs) {
	 this(context, attrs, -1);
	 }

	 public MyScrollerView3(Context context) {
	 this(context, null);
	 }
	 */




	

	

	

	
	

	

	

	
	
	
}
