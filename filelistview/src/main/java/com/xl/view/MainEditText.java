package com.xl.view;

/*
主编辑器控件
包含保存 读取文本 重新读取 截图 数据恢复

*/
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;

public class MainEditText extends XLEditText
{
	public String path; //文件路径
	public String coding="UTF-8"; //文件编码
	public int filetype; //文件类型 取值如下
	
	public static final int
	FILE_JS=1,
	FILE_C=2,
	FILE_XML=3,
	FILE_HTML=4,
	FILE_JAVA=5;
	
	
	public void initView()
	{
		
	}
	
	public MainEditText(android.content.Context context) 
	{

		super(context);
		
	
	}

	public MainEditText(android.content.Context context, android.util.AttributeSet attrs)
	{

		super(context,attrs);
		
		
	}

	public MainEditText(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr)
	{
		super(context,attrs,defStyleAttr);
		
		
	}
	
	public String getPath()
	{
		return this.path;
	}
	
	public boolean setPath(String path)
	{
		this.path = path;
		return true;
	}
	
	public String read(String path) throws IOException
	{
		String content = "";
		this.path = path;
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
			content = new String(buf,coding);
		}
		setText(content);
		return content;
	}
	
	public void save()
	throws IOException
	{
		File file = new File(path);
		file.delete();
		file.createNewFile();
		BufferedWriter writer = new BufferedWriter(
			new OutputStreamWriter(	new FileOutputStream(file), coding));
		writer.write(getText().toString());
		writer.close();
	}
}
