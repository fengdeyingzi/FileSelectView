package com.xl;
import java.lang.Thread.UncaughtExceptionHandler;

import java.io.*;
import android.util.*;
import android.app.*;
import android.content.*;
//import android.support.multidex.*;
public class XLApplication extends Application
{
	public static  String DIR = "mnt/sdcard/";
	//Environment.getExternalStorageDirectory()
	//.getAbsolutePath() + "/survey/log/";
		public static String NAME ="log.txt";

		@Override
		public void onCreate()
		{
				// TODO: Implement this method
				super.onCreate();
				Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
				
		} //getCurrentDateString() + ".txt";
	
	
	
	
	
	/**
	 * 捕获错误信息的handler
	 */
	 
	private UncaughtExceptionHandler uncaughtExceptionHandler = new UncaughtExceptionHandler() {

		
		
		
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {

			
			Log.e("App","我崩溃了"+DIR);

			String info = null;
			ByteArrayOutputStream baos = null;
			PrintStream printStream = null;
			try {
				baos = new ByteArrayOutputStream();
				printStream = new PrintStream(baos);
				ex.printStackTrace(printStream);
				byte[] data = baos.toByteArray();
				info = new String(data);
				data = null;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					if (printStream != null) {
						printStream.close();
					}
					if (baos != null) {
						baos.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			writeErrorLog(info);
			//info=info.substring(info.lastIndexOf(  "com.xl"));
			Log.e("_ERROR",info);
		
			android.os.Process.killProcess(android.os.Process.myPid());
			//System.exit(0);
		}
	};
	
	
	
	/**
	 * 向文件中写入错误信息
	 * 
	 * @param info
	 */
	protected void writeErrorLog(String info) {
		File dir = new File(DIR);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, NAME);

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file, false);
			fileOutputStream.write(info.getBytes());
			fileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	//如果你的Application已经继承了其他类并且不想做改动，那么还有另外一种使用方式,覆写attachBaseContext()方法:
	/*
	@Override   
	protected void attachBaseContext(Context base)
	{
		super.attachBaseContext(base);
		//MultiDex.install(this);
	} 
	*/
	
}
