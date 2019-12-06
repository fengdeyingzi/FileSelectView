package com.xl.view;
import java.util.ArrayList;
import java.io.File;
import java.util.Comparator;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.ToDoubleFunction;
import java.io.UnsupportedEncodingException;
import java.util.TreeSet;
import java.util.Set;
import java.util.Iterator;
import com.xl.game.tool.*;
/*
文件路径管理


*/
public class PathManager
{
  //文件路径层级
  private ArrayList<String> dirs;
  //最低路径层级
  private int levemin;
  //是否启用最低路径层级限制
  private boolean isOpenLeveMin;
  
  
  public PathManager(String path)
  {
	this.dirs = new ArrayList<String>();
	doPath(path);
	this.levemin= getLeve();
  }
  
  
  //将路径解析到list
  private void doPath(String path)
  {
	int i=0;
	int start =0;
	int end=0;
	int type=0;
	int len=0;
	String item=null;
	int itemsize=0;
	char c=0;
	dirs.clear();
	if(path==null)return;
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
			  addDir(item);
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
			  addDir(item);
			}
		  }
	  
	  i++;
	}
  }
	
	//是否启用层级限制
	public void setIsOpenLeve(boolean isopen)
	{
		this.isOpenLeveMin = isopen;
	}
  
  //获取当前路径层级
  public int getLeve()
  {
	return dirs.size();
  }
	
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
  
  //设置最低路径层级
  public void setMinLeve(int size)
  {
	this.levemin = size;
  }
//检测是否到达顶层
	public boolean isUpPath()
	{
		if(this.levemin==0)
			return true;
		if(isOpenLeveMin && this.levemin==getLeve())
			return true;
		return false;
	}
  
  //进入一个文件夹
  public void addDir(String dir)
  {
	dirs.add(dir);
  }
  
  //返回上一层
  public boolean upDir()
  {
	int size = dirs.size();
	if(size==0)return false;
	if(isOpenLeveMin && size==levemin)return false;
	dirs.remove(size-1);
	return true;
  }
  
  //获取当前路径
  public String getPath()
  {
	StringBuffer buffer = new StringBuffer();
	int len=dirs.size();
	if(len==0)return File.separator;
	for(int i=0;i<dirs.size();i++)
	{
	  buffer.append(dirs.get(i));
	  buffer.append(File.separator);
	}
	return buffer.toString();
  }
  
  //获取路径
  
  
  //获取当前文件列表(相对名称)
  public ArrayList<String> getFileNameList()
  {
	String path = getPath();
	ArrayList<String> list=new ArrayList<String>();
	String[] arrfile = new File(path).list();
	FileSorter fileSorter=new FileSorter();
	if (arrfile == null)
	  {
		return list;
	  }
    
	TreeSet treeSet = new TreeSet(fileSorter);
	
	
	for (int i = 0; i < arrfile.length; ++i)
	  {
		treeSet.add(arrfile[i]);
	  }
	Iterator iterator = treeSet.iterator();
	while (iterator.hasNext())
	  {
		String item = (String)iterator.next();
        list.add(item);
	  }
	  return list;
  }
	
	public ArrayList<String> getArray(ArrayList<String> array)
	{
		ArrayList<String> list = new ArrayList<String>();
		FileSorter fileSorter=new FileSorter();
		TreeSet treeSet = new TreeSet(fileSorter);


		for (int i = 0; i < array.size(); ++i)
		{
			treeSet.add(array.get(i));
			//Logcat.e(arrfile[i].getPath());
		}
		Iterator iterator = treeSet.iterator();
		while (iterator.hasNext())
		{
			//String item = (String)iterator.next();
			list.add((String)iterator.next());
		}
		return list;
	}
  
	//获取当前文件列表
	public ArrayList<File> getFileList()
	  {
		String path = getPath();
		ArrayList<File> list=new ArrayList<File>();
		File[] arrfile = new File(path).listFiles();
		//Logcat.e("浏览目录"+path);
		FileSorter fileSorter=new FileSorter();
		if (arrfile == null)
		  {
			return list;
		  }

		TreeSet treeSet = new TreeSet(fileSorter);


		for (int i = 0; i < arrfile.length; ++i)
		  {
			treeSet.add(arrfile[i]);
			//Logcat.e(arrfile[i].getPath());
		  }
		Iterator iterator = treeSet.iterator();
		while (iterator.hasNext())
		  {
			//String item = (String)iterator.next();
			list.add((File)iterator.next());
		  }
		return list;
	  }
  
	static class FileSorter
	implements Comparator<File>
	  {
/*
		@Override
		public Comparator<File> thenComparingInt(ToIntFunction<? super File> keyExtractor)
		  {
			// TODO: Implement this method
			return null;
		  }

		@Override
		public Comparator<File> thenComparingLong(ToLongFunction<? super File> keyExtractor)
		  {
			// TODO: Implement this method
			return null;
		  }

		@Override
		public Comparator<File> thenComparingDouble(ToDoubleFunction<? super File> keyExtractor)
		  {
			// TODO: Implement this method
			return null;
		  }

		
		public static <T extends Object> Comparator<T> comparingInt(ToIntFunction<? super T> keyExtractor)
		  {
			// TODO: Implement this method
			return null;
		  }

		
		public static <T extends Object> Comparator<T> comparingLong(ToLongFunction<? super T> keyExtractor)
		  {
			// TODO: Implement this method
			return null;
		  }

		
		public static <T extends Object> Comparator<T> comparingDouble(ToDoubleFunction<? super T> keyExtractor)
		  {
			// TODO: Implement this method
			return null;
		  }
		

		@Override
		public Comparator<File> reversed()
		  {
			// TODO: Implement this method
			return null;
		  }

		@Override
		public Comparator<File> thenComparing(Comparator<? super File> other)
		  {
			// TODO: Implement this method
			return null;
		  }

		@Override
		public  <U extends Object> Comparator<File> thenComparing(java.util.function.Function<? super File, ? extends U> keyExtractor, Comparator<? super U> keyComparator)
		  {
			// TODO: Implement this method
			return null;
		  }

		@Override
		public <U extends Comparable<? super U>> Comparator<File> thenComparing(java.util.function.Function<? super File, ? extends U> keyExtractor)
		  {
			// TODO: Implement this method
			return null;
		  }

		
		

		@Override
		public static <T extends Comparable<? super T>> Comparator<T> reverseOrder()
		  {
			// TODO: Implement this method
			return null;
		  }

		@Override
		public static <T extends Comparable<? super T>> Comparator<T> naturalOrder()
		  {
			// TODO: Implement this method
			return null;
		  }

		@Override
		public static <T extends Object> Comparator<T> nullsFirst(Comparator<? super T> comparator)
		  {
			// TODO: Implement this method
			return null;
		  }

		@Override
		public static <T extends Object> Comparator<T> nullsLast(Comparator<? super T> comparator)
		  {
			// TODO: Implement this method
			return null;
		  }

		@Override
		public static <T extends Object, U extends Object> Comparator<T> comparing(java.util.function.Function<? super T, ? extends U> keyExtractor, Comparator<? super U> keyComparator)
		  {
			// TODO: Implement this method
			return null;
		  }

		@Override
		public static <T extends Object, U extends Comparable<? super U>> Comparator<T> comparing(java.util.function.Function<? super T, ? extends U> keyExtractor)
		  {
			// TODO: Implement this method
			return null;
		  }

		
		public static <T extends Object> Comparator<T> comparingInt(ToIntFunction<? super T> keyExtractor)
		  {
			// TODO: Implement this method
			return null;
		  }

		
		public static <T extends Object> Comparator<T> comparingLong(ToLongFunction<? super T> keyExtractor)
		  {
			// TODO: Implement this method
			return null;
		  }

		
		public static <T extends Object> Comparator<T> comparingDouble(ToDoubleFunction<? super T> keyExtractor)
		  {
			// TODO: Implement this method
			return null;
		  }
*/
		FileSorter()
		  {
		  }

		public int compare(File file, File file2)
		  {
			if (file.isDirectory() && !file2.isDirectory())
			  {
				return -1;
			  }
			if (!file.isDirectory() && file2.isDirectory())
			  {
				return 1;
			  }/*
			byte[] str1=null;
			byte[] str2 = null;
			try
			  {
				str1 = file.getName().getBytes("GBK");
			  str2 = file2.getName().getBytes("GBK");
			  }
			catch (UnsupportedEncodingException e)
			  {}*/
			return compare(exChange( file.getName()),exChange(file2.getName()));
		  }
		  
		public int compare(String str1,String str2){
			try {
				byte[] b1 = str1.getBytes("GBK");
				byte[] b2 = str2.getBytes("GBK");
				int l1=b1.length;
				int l2=b2.length;
				int l=Math.min(l1, l2);
				int k=0;
				while(k<l){
					int bt1=b1[k]&0xff;
					int bt2=b2[k]&0xff;
					if(bt1!=bt2)
					  return bt1-bt2;
					k++;
				  }
				return l1-l2;
			  } catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			  }
			return 0;
		  }
		//字符串比较
		private int strcmp(byte[] str1, byte[] str2)
		  {
			int len1=str1.length;
			int len2 = str2.length;
			int minlen=Math.min(len1,len2);
			int num=0;
			if(str1==null || str2==null)
			  return 0;
			for(int i=0;i<minlen;i++)
			  {
				if(str1[i]<str2[i])
				  {
					num=-1; break;
				  }
				else if(str1[i]>str2[i])
				  {
					num=1; break;
				  }
				else
				  continue;
			  }
			if(num==0)
			  if(len1<len2)
				{
				  num=-1;
				}
			  else
				{
				  num=1;
				}
			return num;
		  }
	  }
		
	//把一个字符串中的大写转为小写
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
		
}
