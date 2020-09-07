package com.xl.game.view;
import android.graphics.drawable.*;
import android.widget.*;
import com.xl.game.tool.*;
import com.xl.filelist.*;
import java.io.*;
import java.util.*;
import com.xl.game.math.Str;

public class FileListView extends ListView 
{

	
	private boolean isDrawUp; //显示上层按钮
  private FileListAdapter adapter;
  private PathManager pathManager;
	private int textColor;
	private Map<String, Drawable> mDrawable;
	//过滤文件类型
	private int filter;
	public static final int
	_BROWER_ALL=0, //全部
	_BROWER_FILE=1,//只浏览文件
	_BROWER_DIR=2,//只浏览文件夹
	_PATH_ONE=3,//只浏览单层目录
	_SELECT_MORE=4,//多重选择
	_DIR=5, //只选择文件夹
	_FILE=6; //只选择文件
	
	public FileListView(android.content.Context context)
	{
	  super(context);
	  initView();
	}

    public FileListView(android.content.Context context, android.util.AttributeSet attrs) 
	{
	  super(context,attrs);
	  initView();
	}

    public FileListView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr)
	{
	  super(context,attrs,defStyleAttr);
	  initView();
	}

    public FileListView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes)
	{
	  super(context,attrs,defStyleAttr,defStyleRes);
	  initView();
	}
	
	private void initView()
	{
	  adapter = new FileListAdapter(getContext());
	  this.mDrawable = new HashMap<String, Drawable>();
	  pathManager = new PathManager("");
	  setAdapter(adapter);
		//setOnItemClickListener(this);
		//adapter.add(getContext().getDrawable(R.drawable.ex_folder),"","");
	}
	
	//设置显示类型
	public void setFilter(int type)
	{
		this.filter = type;
	}
	
	//层级限制
	public void setIsOpenLeve(boolean isopen)
	{
		this.pathManager.setIsOpenLeve(isopen);
	}
	
	//显示上层按钮
	public void setDrawUp(boolean isDraw)
	{
		if(isDraw!=this.isDrawUp)
		if(isDraw) //显示
		{
			this.isDrawUp = isDraw;
			if (!pathManager.isUpPath())
			this.adapter.add(0,ViewTool.getDrawable( getContext(), R.drawable.ex_folder), getContext().getString(R.string.uplayer),pathManager.getPath());
		}
		else //隐藏
		{
			this.isDrawUp = isDraw;
			this.adapter.remove(0);
		}
		this.adapter.notifyDataSetChanged();
		
	}
	
	//设置文字颜色
	public void setTextColor(int color)
	{
		this.textColor = color;
		this.adapter.setTextColor(color);
	}
	
	//设置最低层级
	public void setMinLeve(int leve)
	{
		this.pathManager.setMinLeve(leve);
	}
	
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
	//设置目录
	public void setPath(String path)
	{
		//Logcat.e("设置目录"+path);
	  pathManager = new PathManager(path);
		refList();
		/*
	 // String upPath = pathManager.getPath();
	  ArrayList<File> list = pathManager.getFileList();
	  adapter.clear();
		if(isDrawUp)
		if(!pathManager.isUpPath())
			adapter.add(0,getContext().getDrawable(R.drawable.ex_folder), getContext().getString(R.string.uplayer),pathManager.getPath());
	  for(File item:list)
	  {
		adapter.add(onLoadFileDrawable(item),item.getName(),item.getPath());
		Logcat.e(""+item);
	  }
		//setAdapter(adapter);
	  adapter.notifyDataSetChanged();
		*/
	}
	
	//返回上一层
	public boolean toUp()
	{
	  if(pathManager.upDir())
		{
			refList();
		/*
	  ArrayList<File> list = pathManager.getFileList();
	  adapter.clear();
		if(isDrawUp && !pathManager.isUpPath())adapter.add(0,getContext().getDrawable(R.drawable.ex_folder), getContext().getString(R.string.uplayer),pathManager.getPath());
	  for(File item:list)
		{
		  adapter.add(onLoadFileDrawable(item),item.getName(),item.getPath());
		}
	  adapter.notifyDataSetChanged();
		*/
		return true;
		}
		
		return false;
	}
	
	//刷新
	public void refList()
	{
	ArrayList<File> list = pathManager.getFileList();
	adapter.clear();
	if(isDrawUp && !pathManager.isUpPath())adapter.add(0,getContext().getResources().getDrawable(R.drawable.ex_folder), getContext().getString(R.string.uplayer),pathManager.getPath());
	for(File item:list)
	{
	switch(filter)
	{
		case _BROWER_ALL:
			adapter.add(onLoadFileDrawable(item),item.getName(),item.getPath());
			break;
		case _BROWER_FILE:
			if(item.isFile())
				adapter.add(onLoadFileDrawable(item),item.getName(),item.getPath());
			break;
		case _BROWER_DIR:
			if(item.isDirectory())
				adapter.add(onLoadFileDrawable(item),item.getName(),item.getPath());
	    break;
		default:
			adapter.add(onLoadFileDrawable(item),item.getName(),item.getPath());
	}
	}
	adapter.notifyDataSetChanged();
	}
	
	//进入一个目录
    public boolean toDir(String dirName)
	{
	  pathManager.addDir(dirName);
		refList();
		setSelection(0);
	  //ArrayList<File> list = pathManager.getFileList();
	  /*
	  adapter.clear();
		if(isDrawUp && !pathManager.isUpPath())adapter.add(0,getContext().getDrawable(R.drawable.ex_folder), getContext().getString(R.string.uplayer),pathManager.getPath());
	  
		for(File item:list)
		{
		  adapter.add(onLoadFileDrawable(item),item.getName(),item.getPath());
		}
		
	  adapter.notifyDataSetChanged();
		
	  if(list.size()==0)return false;
		*/
	  return true;
	}
	
	
  //显示list
	private void showFileList(ArrayList<File> list)
	{
		adapter.clear();
	  for(File item:list)
		{
		  adapter.add(onLoadFileDrawable(item),item.getName(),item.getPath());
		}
	  adapter.notifyDataSetChanged();
	}
	
	//获取文件路径
	public String getPath()
	{
		return pathManager.getPath();
	}
	
	//获取文件全路径
	public String getFileName(int pos)
	{
		return this.adapter.getFile(pos);
	}
	
	//获取文件名
	public String getName(int pos)
	{
		return this.adapter.getTitle(pos);
	}
	
	//判断是否为返回上层
	public boolean isUpDirButton(int pos)
	{
		if(pos==0 && this.isDrawUp)return true;
		else return false;
	}
	
	//设置指定后缀的drawable
	public void setFileDrawable(String name, Drawable drawable){
		mDrawable.put(name,drawable);
	}
	
	//获取文件图标
	public Drawable onLoadFileDrawable(File file)
	{
	  String filename = file.getName();
	  String endName ="";
	  Drawable drawable=null;
	  int start = Str.strrchr(filename,'.');
	  if(start>0){
		  endName = filename.substring(start);
	  }
	  String name= Str.toLower(endName);
	  if(file.isDirectory())
	  {
		return ViewTool.getDrawable(getContext(),R.drawable.ex_folder);
	  }/*
	  else if(name.endsWith(".tmx")){
		return getContext().getResources().getDrawable(R.drawable.ex_tiled);
	  }*/
	  else if((drawable = mDrawable.get(name))!=null){
		  return drawable;
	  }
		else
	  return ViewTool.getDrawable(getContext(),R.drawable.ex_doc);
	}
	
	
	
	
	//把一个字符串中的大写转为小写
	/*
	public static String exChange(String str)
	  {  
		StringBuffer sb = new StringBuffer(200);  
		if (str != null)
		  {  
			for (int i=0;i < str.length();i++)
			  {  
				char c = str.charAt(i);  
				if (Character.isUpperCase(c))//判断是否为大写
				  {  
					sb.append(Character.toLowerCase(c));  
				  }
				else
				  {  
					sb.append(c);   
				  }  
			  }  
		  }  

		return sb.toString();  
	  }
  */
	 
		
}
