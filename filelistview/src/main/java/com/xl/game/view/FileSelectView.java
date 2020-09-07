package com.xl.game.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.xl.filelist.R;
import com.xl.game.tool.DisplayUtil;
import com.xl.game.tool.FileUtils;
import java.io.File;
import android.widget.TextView;
import android.view.Gravity;
import android.os.Build;
import android.view.MotionEvent;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import com.xl.game.tool.ViewTool;
import com.xl.game.tool.Log;

import android.view.ViewGroup;
import android.graphics.drawable.ColorDrawable;
import android.view.WindowManager;

/*
文件选择器
风的影子
2018.12.10
*/
public class FileSelectView extends LinearLayout implements View.OnClickListener ,AdapterView.OnItemClickListener
{
String TAG = "FileSelectView";
	private FileSelectView.OnSelectListener listener;

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int pos, long id)
	{
		// TODO: Implement this method
		String filename=fileListView.getFileName(pos);
		File file = new File(filename);
		if(fileListView.isUpDirButton(pos))
		{
			fileListView.toUp();
		}
		else if(type == FILE_SELECT_FILE){
			if(file.isFile()){
				setPath(filename);
				if(this.listener!=null){
					this.listener.onSelectFile(getPath());
				}
				dialog.dismiss();
			}
			else if(file.isDirectory()){
				fileListView.toDir(file.getName());
			}
		}
		else if(type == FILE_SELECT_DIR){
			if(file.isDirectory()){
				fileListView.toDir(file.getName());
				//setPath(filename);
				//if(this.listener!=null)
				//this.listener.onSelectFile(getPath());
				//dialog.dismiss();
			}
		}
		
	}
	
	
	public void click(){
		onClick(null);
	}
	
//打开文件浏览器
	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		fileListView.setPath(getPath());
		File file = new File(getPath());
		if(!file.isDirectory())
			fileListView.setPath(file.getParent());	
		if(dialog==null){
			int height = DisplayUtil.dip2px(getContext(),360);
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-1,height);
			fileListView.setLayoutParams(layoutParams);
			
			LinearLayout layout_main = new LinearLayout(getContext());
			layout_main.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
			layout_main.addView(fileListView);
			builder.setView(layout_main);
			builder.setTitle(btn_file.getText());
			builder.setNegativeButton(getContext().getString(R.string.refused), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton)
					{

					}
				}).create();
			if(type == FILE_SELECT_FILE){

			}
			else if(type == FILE_SELECT_DIR){
				builder.setPositiveButton("选择文件夹", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface p1, int p2)
						{
							// TODO: Implement this method
							setPath(fileListView.getPath());
							if(listener!=null)
							listener.onSelectFile(getPath());
							
						}


					});
			}
			builder.setOnDismissListener(new DialogInterface.OnDismissListener(){

					@Override
					public void onDismiss(DialogInterface p1)
					{
						Log.i(TAG,"onDismiss");

					}


				});
			dialog=builder.create();
			//dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			//setContentView(R.layout.custom_dialog1);
			//按空白处不能取消动画
			dialog.setCanceledOnTouchOutside(true);
			//设置window背景，默认的背景会有Padding值，不能全屏。当然不一定要是透明，你可以设置其他背景，替换默认的背景即可。
			dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getWindowBackground()));
			//dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
			//一定要在setContentView之后调用，否则无效
			//dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
			
		}
			
		
		dialog.show();
		dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
		WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
		layoutParams.gravity = Gravity.CENTER;
		layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
		layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

		dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
	    
		dialog.getWindow().setAttributes(layoutParams);
		
		
	}
	
	
	public static final int
	FILE_SELECT_DIR=1,
	FILE_SELECT_FILE=0;
	FileListView fileListView;
	
	EditText edit;
	TextView btn_file;
	Dialog dialog;
	int color;
	int color_line;
	int type;
	
	
	//设置选择文件
	public void selectFile(){
		this.type = FILE_SELECT_FILE;
		btn_file.setText(R.string.select_file);
	}
	//设置选择文件夹
	public void selectDir(){
		this.type = FILE_SELECT_DIR;
		btn_file.setText(R.string.select_dir);
	}
	//设置指定后缀的drawable
	public void setFileDrawable(String name, Drawable drawable){
		this.fileListView.setFileDrawable(name, drawable);
	}
    
    private int getColorAccent(){
        TypedValue typedValue = new  TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.colorAccent, typedValue, true);
        return typedValue.data;
    }
    
    private int getWindowBackground(){
        TypedValue typedValue = new  TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.colorBackground, typedValue, true);
        return typedValue.data;
    }
	
	//设置黑色/白色模式
	public void setThemeBlack(boolean isBlack){
		
		if(isBlack){
			edit.setBackgroundResource(R.drawable.edit_select_black);
			btn_file.setBackgroundResource(R.drawable.btn_select_black);
			
		}
		else{
			edit.setBackgroundResource(R.drawable.edit_select_light);
			btn_file.setBackgroundResource(R.drawable.btn_select_light);
		}
		
	}
	//设置编辑框是否可编辑
	public void setEdit(boolean isEdit){
		edit.setEnabled(isEdit);
		/*
		if(!isEdit)
		edit.setKeyListener(null);
		else
			edit.setKeyListener(KeyListener.getContentType());
			*/
	}
	
	//设置文件选择监听
	public void setOnSelectListener(OnSelectListener listener){
		this.listener = listener;
	}
	//设置路径
	public void setPath(String path){
		edit.setText(path);
	}
	//获取路径
	public String getPath(){
		return edit.getText().toString();
	}
	
	//获取File
	public File getFile(){
		return new File(edit.getText().toString());
	}
	//
	//获取目录层级
	public static int getLeve(String path)
	{
		int i=0;
		int start =0;
		int end=0;
		int type=0;
		int len=0;
		String item=null;
		int itemsize=0;
		char c=0;
		int leve=0;
		if(path==null)return 0;
		len = path.length();
		while(i<len)
		{
			c=path.charAt(i);

			if(c=='/' || c=='\\')
			{
				if(itemsize>0)
				{
					item = path.substring(start,i);
					itemsize=0;
					leve++;
				}
				start=i+1;

			}
			else
			{
				itemsize++;
				if(i==len-1)
				{
					item = path.substring(start);
					itemsize=0;
					leve++;
				}
			}

			i++;
		}
		return leve;
	}
	
	
	public FileSelectView(Context context) {
		super(context);
		initView();
	}

    public FileSelectView(Context context, android.util.AttributeSet attrs) {
		super(context,attrs);
		initView();
	}
	
	private void initView(){
		setOrientation(LinearLayout.HORIZONTAL);
		setGravity(Gravity.TOP|Gravity.LEFT);
		
		/*this.listener = new OnSelectListener(){

			@Override
			public void onSelectFile(String path)
			{
				
			}

			
		};*/
		int padding = DisplayUtil.dip2px(getContext(),8);
		if(Build.VERSION.SDK_INT>=14){
			setPadding(padding,padding,padding,padding);
		}
		Context context = getContext();
		edit = new EditText(context);
		btn_file = new EditText(context);
		//btn_file.setEnabled(false);
		btn_file.setKeyListener(null);
		btn_file.setOnTouchListener(new View.OnTouchListener(){

				@Override
				public boolean onTouch(View p1, MotionEvent p2)
				{
					// TODO: Implement this method
					if(p2.getAction()==p2.ACTION_UP){
						onClick(p1);
					}
					return true;
				}
				
			
		});
		/*
		if(Build.VERSION.SDK_INT>=21)
		btn_file.setZ(0);*/
		//btn_file.setPadding(0,0,0,0);
		
		fileListView = new FileListView(context);
		fileListView.setId(1000);
	    setFileDrawable(".tmx", ViewTool.getDrawable(getContext(),R.drawable.ex_tiled));
		int height = DisplayUtil.dip2px(context,60);
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height, 10);
		
		addView(edit,param);
		addView(btn_file, LinearLayout.LayoutParams.WRAP_CONTENT,height);
		//设置背景 
		color = 0xffe0e0e0;
		color_line = 0xffa0a0a0;
		
		//
		fileListView.setPath(FileUtils.getSDPath());
		fileListView.setMinLeve(fileListView.getLeve(FileUtils.getSDPath()));
		
		edit.setText(FileUtils.getSDPath());
		edit.setSingleLine(true);
		btn_file.setOnClickListener(this);
		selectFile();
		
		
		fileListView.setPath(getPath());
		fileListView.setMinLeve(getLeve(FileUtils.getSDPath()));
		fileListView.setOnItemClickListener(this);

		fileListView.setIsOpenLeve(true);
		fileListView.setDrawUp(true);
        		//edit.setPadding(DisplayUtil.dip2px(context,16),8,DisplayUtil.dip2px(context,16),8);
		//edit.setHint(R.string.edit_save_hint);
//	builder.setIcon(R.drawable.icon);

		fileListView.setPadding(8,8,8,8);
		 //,DisplayUtil.dip2px(p1.getContext(),16),8,DisplayUtil.dip2px(p1.getContext(),16),8);


		
		int bcolor = getWindowBackground();
		//Toast.makeText(getContext(),XL.sprintf( "%x",bcolor&0xff),0).show();
        if( (((bcolor>>8)&0xff)+ ((bcolor>>16)&0xff) + (bcolor&0xff))/3 >128){
            setThemeBlack(false);
        }
        else
            setThemeBlack(true);
	}
	
	//文件选择监听器
	//
	public interface OnSelectListener{
		public void onSelectFile(String path);
	}
	
}
