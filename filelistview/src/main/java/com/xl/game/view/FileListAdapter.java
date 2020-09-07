package com.xl.game.view;

import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
//import com.xl.Clip.*;
//import com.xl.list.*;

import java.util.*;
import android.graphics.drawable.*;
//import com.xl.opmrcc.tool.ColorUtil;
import com.xl.game.tool.ImgTool;
import com.xl.game.tool.DisplayUtil;
import android.text.TextUtils;
import com.xl.list.ViewHolder;
import com.xl.tool.ColorUtil;
import com.xl.game.math.Str;
import android.os.Handler;
import android.os.Message;

public class FileListAdapter extends BaseAdapter
  {
	private List<Map<String, Object>> mData;
	
	private Context context;
	private LayoutInflater mInflater;
	Handler handler;
	ThreadPool thread;
  //private int textColor;
	
	public FileListAdapter(Context context)
	  {
		this.mInflater = LayoutInflater.from(context);
		this.context = context;
		this.thread = new ThreadPool();
		this.mData=new ArrayList<Map<String, Object>>();
		this.handler = new Handler(){
			  public void handleMessage(android.os.Message msg) {
				  notifyDataSetChanged();
				  
			  }
			  
		};
		//this.textColor= 0xff505050;
	  }

//获取文件名
		private String getName(String filename)
		{
			int start=0;
			for(int i=0;i<filename.length();i++)
			{
				char c = filename.charAt(i);
				if(c=='/' || c=='\\')
				{
					start=i+1;
				}
			}
			return filename.substring(start);
		}

 public void add(Drawable drawable,String filename)
 {
	 Map<String,Object> map=new HashMap<String,Object>();
	 map.put("filename",filename);
	 map.put("img",drawable);
	 map.put("title",getName(filename));
	 map.put("info",filename);
	 mData.add(map);
 }

	public void add(Drawable drawable,String title,String info)
	  {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("img",drawable);
		map.put("title", title);
		map.put("info",info);
		mData.add(map);
	  }
	  
	  
	  
	public void add(int pos,Drawable drawable,String title,String info)
	  {
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("img",drawable);
		map.put("title", title);
		map.put("info",info);
		mData.add(pos,map);
	  }
	public void remove(int pos)
	  {
		mData.remove(pos);
	  }

	public void clear()
	  {
		mData.clear();
		ThreadPool.unInit();
	  }

public void setTextColor(int color)
{
	//this.textColor = color;
	notifyDataSetChanged();
}

	public int getCount()
	  {
		return mData.size();
	  }

	public Object getItem(int arg0)
	  {
		return mData.get(arg0);
	  }
		
	public String getFile(int pos)
	{
		return (String)mData.get(pos).get("info");
	}

	public String getTitle(int pos)
	  {
		return (String)mData.get(pos).get("title");
	  }

	public long getItemId(int arg0)
	  {
		return arg0;
	  }
	//显示按钮
	public void isVisibility()
	  {

	  }

	public List<Map<String, Object>>getData()
	  {
		return mData;
	  }

/*
	public View getView(int position, View convertView, ViewGroup parent)
	  {
		ViewHolder holder = null;
		if (convertView == null)
		  {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.filelistview2, null);
			holder.img = (ImageView) convertView.findViewById(R.id.img);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.info = (TextView) convertView.findViewById(R.id.info);
			LinearLayout layout=(LinearLayout) convertView.findViewById(R.id.listview_LinearLayout);
			
			convertView.setTag(holder);
		  }
		else
		  {
			holder = (ViewHolder) convertView.getTag();
		  }
		//holder.img.setBackgroundColor(0x20ffffff);

		//holder.img.setBackgroundDrawable(new BitmapDrawable((Bitmap) mData.get(position).get("img")));
		//String filename = (String)mData.get(position).get("filename");
		
		holder.img.setImageDrawable((Drawable) mData.get(position).get("img"));
	  
		holder.title.setText((String) mData.get(position).get("title"));
		holder.info.setText((String) mData.get(position).get("info"));
		holder.title.setTextColor(textColor);
		holder.info.setTextColor(ColorUtil.getAlphaColor(textColor,128));
		return convertView;
	  }
		*/
		
		//用代码实现布局
	public View getView(final int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		String endName="";
		//Log.e("fileListAdapter","adapter获取View");
		if (convertView == null)
		{
			//Log.e("fileListAdapter","convertView==null");
			holder = new ViewHolder();
			int padding = DisplayUtil.dip2px(context,8);
			LinearLayout layout_main=new LinearLayout(context);
			layout_main.setOrientation(LinearLayout.HORIZONTAL);
			//layout_main.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
layout_main.setPadding(0,8,0,8);
layout_main.setGravity(Gravity.CENTER);
			ImageView imageView = new ImageView(context);
			imageView.setPadding(padding,padding,padding,padding);
			LinearLayout.LayoutParams par_img = new LinearLayout.LayoutParams(DisplayUtil.dip2px(context,54),DisplayUtil.dip2px(context,54));
			imageView.setLayoutParams(par_img);

			LinearLayout layout_title = new LinearLayout(context);
			layout_title.setOrientation(LinearLayout.VERTICAL);

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
			layout_title.setLayoutParams(params);
			TextView text_title = new TextView(context);
			text_title.setTextSize(18);
			//text_title.setTextColor(textColor);
			TextView text_info = new TextView(context);
			text_info.setTextSize(14);
			//text_info.setTextColor(textColor);
			text_info.setSingleLine(true);
			text_info.setEllipsize(TextUtils.TruncateAt.START);
			layout_title.addView(text_title);
			layout_title.addView(text_info);
			layout_main.addView(imageView);
			layout_main.addView(layout_title);
			
			
			
			convertView = layout_main;
			//convertView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
			holder.img = imageView;
			holder.title = text_title;
			holder.info = text_info;
			//LinearLayout layout=(LinearLayout) convertView.findViewById(R.id.listview_LinearLayout);

			convertView.setTag(holder);
		}
		else
		{
			//Log.e("filelistAdapter","convertView!=null");
			holder = (ViewHolder) convertView.getTag();
		}
		//holder.img.setBackgroundColor(0x20ffffff);

		//holder.img.setBackgroundDrawable(new BitmapDrawable((Bitmap) mData.get(position).get("img")));
		//String filename = (String)mData.get(position).get("filename");

		holder.img.setImageDrawable((Drawable) mData.get(position).get("img"));

		holder.title.setText((String) mData.get(position).get("title"));
		holder.info.setText((String) mData.get(position).get("info"));
		final String filename = (String) mData.get(position).get("info");
		int endindex = Str.strrchr(filename,'.');
		if(endindex>0){
		endName = filename.substring(endindex);
		endName = endName.toLowerCase();
		if(endName.equals(".png") || endName.equals(".jpg") || endName.equals(".gif") || endName.equals(".bmp")){
			if(mData.get(position).get("image_small")==null){
			    final HashMap<String,Object> map=  (HashMap<String, Object>) mData.get(position);
			map.put("image_small",endName);
				ThreadPool.execute( new Runnable(){
				public void run(){
					Bitmap bitmap = ImgTool.getImageThumbnail( filename, 120,120);
					map.put("image_small",bitmap);
					BitmapDrawable drawable = new BitmapDrawable(bitmap);
					map.put("img",drawable);
					Message m = new Message();
					handler.sendMessage(m);
				}
			});
		}
		}
		}
		//holder.title.setTextColor(textColor);
		//holder.info.setTextColor(ColorUtil.getAlphaColor(textColor,128));
		return convertView;
	}
  
		
		
		
  }
	
