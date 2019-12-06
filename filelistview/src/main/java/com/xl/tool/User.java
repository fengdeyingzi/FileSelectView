package com.xl.tool;
import com.xl.game.tool.*;
import android.content.*;
import java.net.*;
import java.io.*;
import java.util.prefs.*;
import android.preference.*;
import android.net.wifi.*;
import android.content.pm.*;
import com.xl.tool.*;
import android.os.Environment;

public class User
{
	boolean isRoot;
	String mode = "app";
	String imei;
	String imsi;
	String mac;
	int device_width;
	int device_height;
	
	public static String getChannel(Context context)
	{
		ApplicationInfo appInfo=null;
		try
		{
			appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
			Object msg=appInfo.metaData.get("UMENG_CHANNEL");
			//Object msg=appInfo.metaData.get("YOUMI_CHANNEL");
			if(msg!=null)
			return msg.toString();
			else
				return "0";
			//new GetInfo(BaseUrl.getPointsUrl+"?user="+imei,this).start();
			//Toast.makeText(this, ""+msg,0).show();
   
		}
		catch (PackageManager.NameNotFoundException e)
		{
         return "0";
		}
	}
	
	public static String getMacAdress(Context context)
	{
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE); 
		WifiInfo info = wifi.getConnectionInfo(); 
		return info.getMacAddress();
	}
	public static String getUser(Context context)
	{
		String user=null;
		SharedPreferences preference= PreferenceManager.getDefaultSharedPreferences(context);
	    user = preference.getString("user",null);
		if(user!=null)return user;
		user = Tool.getImei(context);
		String imsi = Tool.getImsi(context);
		
		if (user != null)
		{
			if (user.length() == 0)
			{
				user = "USER";
				if(imsi!=null && imsi.length()>1)
				{
					user = "IMSI"+imsi;
				}
			}
		}
		else
		{
			user = "NULL";
			if(imsi!=null && imsi.length()>1)
			{
				user = "IMSI"+imsi;
			}
		}
		user = user.replaceAll(" ", "");
		//user = URLEncoder.encode(user);
		setUser(context,user);
		return user;
	}
public static void setUser(Context context,String user)
{
	SharedPreferences preference= PreferenceManager.getDefaultSharedPreferences(context);
	SharedPreferences.Editor editor=preference.edit();
	editor.putString("user",user);
	editor.apply();
}

	//判断机器 Android是否已经root，即是否获取root权限    
	/*
	 protected static boolean haveRoot() 
	 {
	 int i = execRootCmdSilent("echo test"); //通过执行测试命令来检测 
	 if (i != -1)  return true; 
	 return false;
	 }
	 */

	/**      * 判断手机是否ROOT      */
	/*
	 public boolean isRoot()
	 {
	 boolean root = false;
	 try {
	 if ((!new File("/system/bin/su").exists()) 
	 && (!new File("/system/xbin/su").exists())) 
	 {
	 root = false;
	 }
	 else
	 {
	 root = true;
	 }
	 } catch (Exception e) 
	 {          }
	 return root;
	 }
	 */
	/** 判断手机是否root，不弹出root请求框<br/> */
	public static boolean isRoot() 
	{
	    String binPath = "/system/bin/su"; 
        String xBinPath = "/system/xbin/su";  
        if (new File(binPath).exists() && isExecutable(binPath))  
            return true;  
	    if (new File(xBinPath).exists() && isExecutable(xBinPath))  
			return true;  
		return false; 
	} 

	private static boolean isExecutable(String filePath) 
	{
		Process p = null; 
		try
		{ 
			p = Runtime.getRuntime().exec("ls -l " + filePath); 
			// 获取返回内容

			BufferedReader in = new BufferedReader(new InputStreamReader( 
													   p.getInputStream())); 
			String str = in.readLine(); 
			//Log.i(TAG, str); 
			if (str != null && str.length() >= 4)
			{ 
				char flag = str.charAt(3); 
				if (flag == 's' || flag == 'x') 
					return true; 
			} 
		}
		catch (IOException e)
		{ 
			e.printStackTrace(); 
        }
		finally
		{ 
			if (p != null)
			{ 
				p.destroy(); 
			} 
		} 
		return false; 
	}

 //检测读写权限
 public static boolean isWriteFile()
 {
	 File sdDir=null;
	 boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
	 if(sdCardExist)
	 {
		 sdDir=Environment.getExternalStorageDirectory();//获取sd卡目录
		 File file = new File(sdDir,"writecheck");
		 try
		 {
			 file.createNewFile();
		 }
		 catch (IOException e)
		 {
			 return false;
		 }
		 if(file.exists())
		 {
			 return true;
		 }
		 else
		 {
			 return false;
		 }

	 }
	 else 
	 {
		return false;
	 }
	 
 }

}
