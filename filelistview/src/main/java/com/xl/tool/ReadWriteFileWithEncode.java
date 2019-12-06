package com.xl.tool;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.content.Context;
import java.io.*;

import android.widget.*;
//import com.xl.runC.ofToApk1.EmuPath;
import android.util.*;

public class ReadWriteFileWithEncode
{
/*
	public void saveFile(Context context,String path, String text, String coding)
	{
	  Log.e("saveFile",path+coding);
		try
		{
			//将文件转移到temp目录下
			File tempfile=new File(EmuPath.getTempPath()+new File(path).getName());
			if(tempfile.exists())
			{
				tempfile.delete();
			}
			//重命名为temp
			File pathfile=new File(path);
			
			boolean renametype=pathfile.renameTo(tempfile);
			//如果重命名失败，创建path
			
			if(!renametype)
			{
			  Log.e("saveFile","重命名失败");
				//pathfile.delete();
				pathfile.createNewFile();
			}
			
	    FileOutputStream outStream =new FileOutputStream(pathfile);
				outStream.write(text.getBytes(coding));
				
	      outStream.close();
				
		//	issave=false;
		}
		catch(FileNotFoundException e)
		{
			Toast.makeText(context, "找不到路径", Toast.LENGTH_SHORT).show();
		}
		catch(IOException e)
		{
			Toast.makeText(context, "IO错误", Toast.LENGTH_SHORT).show();
		}
	}
	
	*/
	
	public static void write(String path,String text,String encoding)
	throws IOException
	{
		File file = new File(path);
		file.delete();
		file.createNewFile();
		BufferedWriter writer = new BufferedWriter(
		new OutputStreamWriter(	new FileOutputStream(file), encoding));
		writer.write(text);
		writer.close();
	}

	public static void write(File file,String text,String encoding)
	throws IOException
	{
	//	File file = new File(path);
		file.delete();
		file.createNewFile();
		BufferedWriter writer = new BufferedWriter(
		new OutputStreamWriter(	new FileOutputStream(file), encoding));
		writer.write(text);
		writer.close();
	}
	
	public static String read(String path,String encoding) throws IOException
	{
		String content = "";
		File file = new File(path);
		/*
		BufferedReader reader = new BufferedReader(new InputStreamReader(
		new FileInputStream(file), encoding));
		String line = null;
		while((line=reader.readLine())!=null)
		{
			content+=line+"\n";
		}
		reader.close();
		*/
		if(file.isFile())
		{
		FileInputStream input= new FileInputStream(file);
		
		byte [] buf=new byte[input.available()];
		input.read(buf);
		content = new String(buf,encoding);
		}
		return content;
	}

	public static String read(File file,String encoding) throws IOException
	{
		String content = "";
	//	File file = new File(path);
		
		if(file.isFile())
		{
			FileInputStream input= new FileInputStream(file);

			byte [] buf=new byte[input.available()];
			content = new String(buf,encoding);
		}
		return content;
	}
	
	
	public static void main(String[] args) throws IOException
	{
		String content = "中文内容";
		String path = "c:/test.txt";
		String encoding = "utf-8";
		ReadWriteFileWithEncode.write(path, content, encoding);
		System.out.println(ReadWriteFileWithEncode.read(path, encoding));
	}
}
