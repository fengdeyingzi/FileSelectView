package com.xl.filelist;
import android.content.*;
import android.net.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import com.xl.game.tool.*;

import com.xl.game.tool.FileUtils;
import com.xl.game.view.*;
import java.io.*;
//import android.text.*;
import android.widget.AdapterView.*;
import java.util.*;
//import android.support.v7.app.*;
//import com.xl.opmrcc.tool.*;
import com.xl.game.math.*;
//import android.support.design.widget.FloatingActionButton;
import android.app.Dialog;
import android.app.Activity;
import android.app.AlertDialog;

public class FileListActivity extends Activity implements AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener
{

	@Override
	public boolean onItemLongClick(AdapterView<?> p1, View view, final int pos, long id)
	{
		ArrayList<String> strs = new ArrayList<String>();
		//打开方式...
		strs.add(getString( R.string.open_with));
		//复制文件
		strs.add(getString(R.string.copyfile));
		//重命名
		strs.add(getString(R.string.rename));
		//剪切
		strs.add(getString(R.string.cutfile));
		//粘贴
		//if (pastefile != null)
			//menu.add(getString(R.string.pastefile));
		//删除
		strs.add(getString(R.string.delete));
		//创建目录
		strs.add(getString(R.string.new_folder));
		strs.add(getString(R.string.share));
		//压缩文件(夹)
		strs.add(getString(R.string.zip_unzip_files));
		//复制路径
		strs.add(getString(R.string.copy_path));
		new AlertDialog.Builder(this)
			.setTitle(fileListView.getName(pos))
			.setItems(Str.ArrayListToArray(strs), new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int index)
				{
					onContextItemSelected(index,pos);
				}
				
			})
			.show();
		return true;
	}
	
  FileListView fileListView;
	FileListAdapter adapter;
	View fab_add,fab_paste,fab_clear;
	public static String path=null;
	private String pastefile;
	private static final int 
	_COPY=1,
	_CUT=2;
	private static final int
	DLG_ADD =100;
	private int copytype;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	  {
		  
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
			String sdCard = FileUtils.getSDPath();
		setContentView(R.layout.filelist_opmrcc);
	//	if(path==null)path = FileUtils.getSDPath();
		
		Intent intent = this.getIntent();
		getMainLooper().loop();
		Bundle bl = null;
		if(intent!=null)
		{
		bl = intent.getExtras();
		if(bl!=null)
		{
		String title = bl.getString("explorer_title");
		if(title!=null)setTitle(title);
		String filepath = bl.getString("filepath");
		String typeList = bl.getString("returnFile");
		
		if(path==null) path = filepath;
		}
		}
		fileListView = (FileListView)findViewById(R.id.filelist);
		this.adapter = (FileListAdapter)fileListView.getAdapter();
		fab_add = findViewById(R.id.filelist_add);
		fab_paste = findViewById(R.id.filelist_paste);
		fab_clear = findViewById(R.id.filelist_clear);
		setPaste(false);
		
		
		if(Build.VERSION.SDK_INT>=11)
		getActionBar().setSubtitle(path);
		//fileListView.setFilter(FileListView._BROWER_DIR);
		Log.e("fileListActivity","设置fileListView");
		fileListView.setPath(path);
		fileListView.setMinLeve(getLeve(sdCard));
		fileListView.setOnItemClickListener(this);
		fileListView.setOnItemLongClickListener(this);
		fileListView.setIsOpenLeve(true);
		fileListView.setDrawUp(true);
		Log.e("fileListActivity","设置成功");
		//Toast.makeText(this,"",0).show();
		
	  }
  @Override
	public void onItemClick(AdapterView<?> adapter, View view, int pos, long id)
	{
		// TODO: Implement this method
		File file = new File( this.adapter.getFile(pos));
		String filename = this.adapter.getFile(pos);
		if(fileListView.isUpDirButton(pos))
		{
			fileListView.toUp();this.path = fileListView.getPath();
			getActionBar().setSubtitle(fileListView.getPath());
			return;
		}
		if(file.isDirectory())
		{
			fileListView.toDir(file.getName());
			this.path = fileListView.getPath();
			getActionBar().setSubtitle(fileListView.getPath());
		}
		
		else
		{
			Uri uri;
			Intent intent;
			/*
			if(filename.endsWith(".html"))
			{
				uri = Uri.fromFile(file);
				intent = new Intent(this, HttpEditActivity.class);
				intent.setDataAndType(uri,"text/html");
				startActivity(intent);
				return ;
			}
			/*
			else if(filename.endsWith(".rc") && filename.startsWith("res_lang"))
			{
				uri = Uri.parse(filename);
				intent = new Intent(this,com.xl.rcedit.mainActivity.class);
				intent.setDataAndType(uri,"rc");
				startActivity(intent);
				return;
			}
			else if(filename.endsWith("Android.mk"))
			{
				uri = Uri.parse(filename);
				intent = new Intent(this,com.xl.webedit.editMkActivity.class);
				intent.setDataAndType(uri,"mk");
				startActivity(intent);
				return;

			}
			else if(filename.endsWith(".xml"))
			{
				uri = Uri.parse(filename);
				intent = new Intent(this,com.xl.webedit.editUiActivity.class);
				intent.setDataAndType(uri,"mk");
				startActivity(intent);
				return;
			}
			*/
			for (int i=0;i < OPEN_Tab.length;i++)
			{
				if (filename.endsWith(OPEN_Tab[i][0]))
				{
					uri = Uri.fromFile(new File( filename));
					Intent it   = new Intent(Intent.ACTION_VIEW);
					it.setDataAndType(uri, OPEN_Tab[i][1]);

					startActivity(it);
					return;
				}

			}
			/*
			 if(filename.endsWith(".mrp"))
			 {
			 Uri uri = Uri.fromFile(new File(homepath+mrppath+'/'+ filename));
			 Intent it   = new Intent(Intent.ACTION_VIEW);
			 it.setDataAndType(uri, "application/mrp");

			 startActivity(it);
			 }
			 if(filename.endsWith(".png"))
			 {
			 Uri uri = Uri.fromFile(new File(homepath+mrppath+'/'+ filename));
			 Intent it   = new Intent(Intent.ACTION_VIEW);
			 it.setDataAndType(uri, "image/*");

			 }
			 */
			//Toast.makeText(this, "mrppath"+mrppath, 0).show();

			/*
			 if(filename.endsWith(".c"))
			 {
			 //将参数传递到编辑器
			 finishWithResult(mrppath);
			 }
			 if(filename.endsWith(".h"))
			 {
			 finishWithResult(mrppath);
			 }
			 */
			//Logcat.e("传送"+" "+filename);
			//将参数传递到编辑器

			finishWithResult(filename);

		}
	}
  private void finishWithResult(String path)
	{
		Bundle conData = new Bundle();

		conData.putString("results", "Thanks");
		conData.putString("filepath", path);
		Intent intent = new Intent();
		intent.putExtras(conData);
		Uri startDir= Uri.parse(path);
		//	Uri uri_home= Uri.parse(homepath);
		//startDir.fromFile(new File(path));
		intent.setData(startDir);
		//intent.setDataAndType(startDir,	"vnd.android.cursor.dir/lysesoft.andexplorer.file");
		setResult(RESULT_OK, intent);
		this.path = fileListView.getPath();
		finish();
	}
/*
	@Override
	public void onBackPressed()
	{
		this.path = fileListView.getPath();
		if(fileListView.toUp())
		{
			getSupportActionBar().setSubtitle(fileListView.getPath());
		}
		else super.onBackPressed();
	}
	*/
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
	
	//开启粘贴功能
	public void setPaste(boolean ispaste)
	{
		if(ispaste)
		{
			fab_add.setVisibility(View.GONE);
			fab_paste.setVisibility(View.VISIBLE);
			fab_clear.setVisibility(View.VISIBLE);
		}
		else
		{
			fab_add.setVisibility(View.VISIBLE);
			fab_paste.setVisibility(View.GONE);
			fab_clear.setVisibility(View.GONE);
		}
		fab_add.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View p1)
			{
			showDialog(DLG_ADD);
			}
			
			
		});
		fab_paste.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View p1)
			{
			// TODO: Implement this method
			CopyFile copyfile= new CopyFile();
			File oldfile=new File(pastefile);
			int last=pastefile.lastIndexOf('/');
			String filename=pastefile.substring(last, pastefile.length());
			File newfile=new File(fileListView.getPath() + '/' + filename);

			switch (copytype)
			{
			case _COPY:
				copyFile(pastefile, newfile.getPath());
				refFileList();
				pastefile = null;
				break;
			case _CUT:
			  moveFile(pastefile,newfile.getPath());
				refFileList();
				pastefile = null;
				break;
			}
			setPaste(false);
			}
			
			
		});
		fab_clear.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View p1)
			{
			setPaste(false);
			}
			
			
		});
	}
	
	
	//复制文件
private void copyFile(String filename,String newfilename)
{
CopyFile copyfile= new CopyFile();
File file = new File(filename);
int start=0;
String endtext = null;
if(filename.equals(newfilename)) //同一个文件
{
	//取后缀
	start = Str.strrchr(newfilename.toCharArray(),'.');
	if(start>0)
	{
		endtext = newfilename.substring(start);
	newfilename = newfilename.substring(0,start)+"_new"+endtext;
	}
	else
	{
		newfilename = newfilename+"_new";
	}
	if(new File(newfilename).exists())
	{
		toast("操作出错，文件名冲突");
		return ;
	}
}
if(file.isFile())
{
copyfile.copyFile(filename, newfilename);
}
else
{
copyfile.copyFolder(filename, newfilename);
}
}

//移动文件
private void moveFile(String oldfilename,String newfilename)
{
	File oldfile = new File(oldfilename);
	File newfile = new File(newfilename);
  if(!oldfile.renameTo(newfile))
	{
		toast("操作失败！");
	}
}

	public void clipSet(CharSequence text)
	{
		ClipboardManager clipboarManager=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
		clipboarManager.setText(text);
		//Toast.makeText(this,"文本已复制到剪切板",0).show();
	}
	


	//获得listView列表项
	public boolean onContextItemSelected(int pos, int list_index)
	{
		AdapterContextMenuInfo info;
		//info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		int position= pos;
		
		switch (position)
		{
			case 0:
				File f=new File(fileListView.getFileName(list_index));
				Uri uri = Uri.fromFile(f);
				try
				{
					if (f.isDirectory())
					{
						/*
						 Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
						 intent.setDataAndType(uri, "file/*");
						 //startActivityForResult(intent, OPEN_WITH_CODE);
						 startActivity(intent);
						 */
					} 
					else 
					{
						Intent it   = new Intent(Intent.ACTION_VIEW);
						it.setDataAndType(uri, "*/*");

						startActivity(it);
					}
				}
				catch (Exception e)
				{

				}

				break;
			case 1://复制
				pastefile = fileListView.getFileName(list_index);
				copytype = _COPY;
				setPaste(true);
				break;
			case 2:
				edit_rename("文件重命名", fileListView.getFileName(list_index),list_index);
				break;

			case 3: //剪切
				pastefile = fileListView.getFileName(list_index);
				copytype = _CUT;
				setPaste(true);
				break;
			case 4: //删除
				edit_delete(fileListView.getName(list_index),fileListView.getFileName(list_index));
				
				break;
			case 5: //新建文件夹
				edit_mkdir("请输入文件夹名", "");
				
				break;
			case 6: //分享
				Intent share=new Intent(Intent.ACTION_SEND);
				File files=new File(fileListView.getFileName(list_index));
				File outfiles=new File(fileListView.getFileName(list_index)+ ".zip");
				if(files.isDirectory())
				{
					List<File> zipfiles=new ArrayList<File>();
					zipfiles.add(files);

					try
					{
						ZipUtils.zipFiles(zipfiles, outfiles, "手机C语言");
						toast("压缩成功");
						files=outfiles;
					}
					catch (IOException e)
					{
						toast("压缩失败，IO错误");
						break;
					}


				}
				share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(files));//此处一定要用Uri.fromFile(file),其中file为File类型，否则附件无法发送成功。
				share.setType("*/*");
				startActivity(Intent.createChooser(share,"分享"));
				
				break;
			case 8: //复制路径
			String path = fileListView.getFileName(list_index);
			clipSet(path);
			toast(getString(R.string.copy_clipboar_ok));
			break;
			
			/*
				CopyFile copyfile= new CopyFile();
				File oldfile=new File(pastefile);
				int last=pastefile.lastIndexOf('/');
				String filename=pastefile.substring(last, pastefile.length());
				File newfile=new File(fileListView.getPath() + '/' + filename);

				switch (copytype)
				{
					case _COPY:
						if(new File(pastefile).isFile())
							copyfile.copyFile(pastefile, newfile.getPath());
						else
						{
							copyfile.copyFolder(pastefile, newfile.getPath());
						}
						refFileList();
						this.pastefile = null;
						break;
					case _CUT:

						oldfile.renameTo(newfile);
						refFileList();
						this.pastefile = null;
						break;
				}
				break;
				*/
			case 7: //压缩/解压
				String zipfilename=(fileListView.getFileName(list_index))  ;
				File file=new File(zipfilename);
				File outfile=new File(fileListView.getFileName(list_index) + ".zip");
				if(file.isFile() && file.getPath().endsWith(".zip"))
				{
					try
					{
						ZipUtils.unZipFile(file, file.getParent());
						toast("解压成功");
						refFileList();
					}
					catch (IOException e)
					{
						toast("解压失败，io错误");
					}

				}
				else
				{
					List<File> zipfiles=new ArrayList<File>();
					zipfiles.add(file);
					try
					{
						ZipUtils.zipFiles(zipfiles, outfile, "手机C语言");
						toast("压缩成功");

						adapter.add(ViewTool. getDrawable(this,R.drawable.ex_doc), outfile.getPath());
						adapter.notifyDataSetChanged();
					}
					catch (IOException e)
					{
						toast("压缩失败，io错误");
					}
				}
				break;
		}




		return true;
	}



	//菜单
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// TODO: Implement this method
	SubMenu subSkin;

	//subSkin = menu.addSubMenu(0, 3, 1, "刷新");
	//subSkin.setIcon(getWhiteDrawable(R.drawable.run));
	//subSkin.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
	
		
		//menu.add(0, 0, 1, getString(R.string.new_project));
		menu.add(0, 1, 1, getString(R.string.new_file));
		menu.add(0, 2, 1, getString(R.string.new_folder));
		//menu.add(0,i++,1,"保存");


		return super.onCreateOptionsMenu(menu);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			case 0://新建工程
				//edit_newProject(getString(R.string.new_project), "");
				break;
			case 1://新建文件
				edit_createfile(getString(R.string.please_filename), "");
				break;
			case 2://新建文件夹
				edit_mkdir(getString(R.string.please_dirname), "");
				break;
			case 3://刷新
			refFileList();
				break;
			case 5://

				break;
			case 6: //

				break;
			case 7:  //

				break;
		}

		return super.onOptionsItemSelected(item);
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
	// TODO: Implement this method
	switch(id)
	{
		case DLG_ADD:
		AlertDialog.Builder builder= new AlertDialog.Builder(this);	
		builder.setItems(new String[]{getString(R.string.new_file),getString(R.string.new_folder)},new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
				switch(p2)
				{
					
					case 0:
						edit_createfile(getString(R.string.please_filename),"");
						break;
					case 1:
						edit_mkdir(getString(R.string.please_dirname),"");
						break;
				}
				}
				
			
		});
		builder.setTitle("添加");
		builder.setCancelable(true);
		builder.setPositiveButton(getString(R.string.refused), new DialogInterface.OnClickListener() 
			{

				@Override
				public void onClick(DialogInterface p1, int p2)
				{
				// TODO: Implement this method
				}
				
				});
		return builder.show();
	}
	
	return super.onCreateDialog(id);
	}
	
	
	
	
	

	void refFileList()
	{
		this.fileListView.refList();
		this.adapter.notifyDataSetChanged();
	}
	
	//编辑框
	EditText edit;
	void editCreate(String title, String text, int pos)
	{
		final int position=pos;
		AlertDialog.Builder builder= new AlertDialog.Builder(this);	

		final EditText edit = new EditText(this);
		edit.setText(text);
//	builder.setIcon(R.drawable.icon);
		builder.setTitle(title);
		builder.setView(edit);
		builder.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{

					String text=edit.getText().toString();

					refFileList();

				}
			});
		builder.setNegativeButton(getString(R.string.refused), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton)
				{

				}
			}).show();
	}

	void edit_createfile(String title, String text)
	{

		AlertDialog.Builder builder= new AlertDialog.Builder(this);	
		final EditText edit = new EditText(this);
	 	edit.setText(text);
//	builder.setIcon(R.drawable.icon);
		builder.setTitle(title);
		builder.setView(edit);
		builder.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{

					String text=edit.getText().toString();
					File file=new File(fileListView.getPath()+ '/' + text);
					if (!file.isFile())
					{
						try
						{
							file.createNewFile();
						}
						catch (IOException e)
						{
							toast("创建文件失败");
						}
					}
					else
					{
						toast("文件已存在");
					}
					refFileList();

				}
			});
		builder.setNegativeButton(getString(R.string.refused), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton)
				{

				}
			}).show();
	}


	void edit_mkdir(String title, String text)
	{

		AlertDialog.Builder builder= new AlertDialog.Builder(this);	
		final EditText edit = new EditText(this);
		edit.setText(text);
//	builder.setIcon(R.drawable.icon);
		builder.setTitle(title);
		builder.setView(edit);
		builder.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{

					String text=edit.getText().toString();
					File file=new File(fileListView.getPath() + File.separatorChar + text);
					if (!file.isDirectory())
					{
						file.mkdir();
					}
					else
					{
						toast("文件夹已存在");
					}
					refFileList();

				}
			});
		builder.setNegativeButton(getString(R.string.refused), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton)
				{

				}
			}).show();
	}

/*
	void edit_newProject(String title, String text)
	{

		AlertDialog.Builder builder= new AlertDialog.Builder(this);	
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.input_newproject, null);
		final EditText edit_projectname=(EditText)textEntryView.findViewById(R.id.inputnewProject_projectName);
		final EditText edit_filename=(EditText)textEntryView.findViewById(R.id.inputnewProject_fileName);
		edit_projectname.setText(text);
//	builder.setIcon(R.drawable.icon);
		builder.setTitle(title);
		builder.setView(textEntryView,DisplayUtil.dip2px(this,16),8,DisplayUtil.dip2px(this,16),8);
		builder.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{

					String projecttext=edit_projectname.getText().toString();
					String filetext=edit_filename.getText().toString();
					//工程文件夹
					File filedir=new File(fileListView.getPath() + '/' + projecttext);
					//工程文件名
					File filec=new File(filedir.getPath() + '/' + filetext);
					//assets
					File assfile=new File(filedir.getPath()+"/"+"assets");

					String mkname= "LOCAL_SRC_FILES := " + filetext + ".c\n";
					String mkfile="LOCAL_MODULE    := c\n" + mkname;
					if (filedir.mkdir())
					{
						assfile.mkdir();
						Unpack.unpack(FileListActivity.this, "c/main.c", fileListView.getPath() + '/' + projecttext + '/' + filetext + ".c");
						File file=new File(fileListView.getPath() + '/' + projecttext + '/' + "Android.mk");
						try
						{
							file.createNewFile();
						}
						catch (IOException e)
						{}
						try
						{
							FileOutputStream out=new FileOutputStream(file);
							try
							{
								out.write(mkfile.getBytes("GB2312"));
								out.close();
							}
							catch (IOException e)
							{}
						}
						catch (FileNotFoundException e)
						{}

						//	unpack.unpack(FileListDialog.this,"c/helloworld.h",mrppath+'/'+projecttext+'/'+"helloworld.h");
						toast("" + filec.getPath());
					}
					else
					{
						toast("工程创建失败\n存在同名文件夹");
					}

					refFileList();
				}
			});
		builder.setNegativeButton(getString(R.string.refused), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton)
				{

				}
			}).show();
	}

*/


	//删除

	void edit_delete(String title,final String filename)
	{

		AlertDialog.Builder builder= new AlertDialog.Builder(this);	
//		LayoutInflater factory = LayoutInflater.from(this);
//		final View textEntryView = factory.inflate(R.layout.input, null);
//		edit=(EditText)textEntryView.findViewById(R.id.edit);
//		edit.setText("你确定要删除吗？");
//	builder.setIcon(R.drawable.icon);
		builder.setTitle(title);
//		builder.setView(textEntryView);
		builder.setMessage("你确定要删除吗？");
		builder.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{

					//String text=edit.getText().toString();
					File file=new File(filename);
					if (file.isDirectory())
					{
						CopyFile copyfile=new CopyFile();
						copyfile.delFolder(filename);
					}
					else
					{
						if (file.delete())
						{
							toast("删除成功");
						}
						else
						{
							toast("删除失败");
						}
					}
					refFileList();

				}
			});
		builder.setNegativeButton(getString(R.string.refused), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton)
				{

				}
			}).show();
	}

	//重命名

	void edit_rename(String title, String filaname, int pos)
	{
		final int position=pos;

		AlertDialog.Builder builder= new AlertDialog.Builder(this);	
		final EditText edit = new EditText(this);
    
		edit.setText(fileListView.getName(position));
//	builder.setIcon(R.drawable.icon);
		builder.setTitle(title);
		builder.setView(edit);
		builder.setPositiveButton(getString(R.string.accept), new DialogInterface.OnClickListener() 
			{
				public void onClick(DialogInterface dialog, int whichButton) 
				{

					String text=edit.getText().toString();
					File file=new File(fileListView.getFileName(position));
					File newfile= new File(fileListView.getPath() + '/' + text);
					file.renameTo(newfile);
					refFileList();

				}
			});
		builder.setNegativeButton(getString(R.string.refused), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton)
				{

				}
			}).show();
	}


	void toast(String text)
	{
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	
	
	
	//异常恢复
	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		Log.e("","onSaveInstanceState");
		outState.putString("filepath", this.path);
		
		super.onSaveInstanceState(outState);
	}

	//恢复
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		
	Log.e("","onRestoreInstanceState");
			this.path=savedInstanceState.getString("filepath");
			if(path!=null)
			{
				this.fileListView.setPath(path);
			}
			super.onRestoreInstanceState(savedInstanceState);

	
		
	}
	

	private final String[][] OPEN_Tab=
	{
		{".apk",    "application/vnd.android.package-archive"},
		{".mrp","application/mrp"},
		//{".png","image/png"},
		 //{".gif","image/gif"},
	//	{".jpg","image/jpeg"},
	//	{".bmp","image/bmp"},
		//{".html",   "text/html"},
		{".zip",    "application/x-zip-compressed"},
		{".mp3","audio/x-mpeg"},
		{".wav","audio/x-wav"},
		{".mid","audio"},
		{".m4a","audio/mp4a-latm"},
		{".amr","audio"},
		{".mp4","video/mp4"},
		{".avi","video/x-msvideo"}
	};

	private final String[][] MIME_MapTable=
	{ 
		//{后缀名，MIME类型} 
		{".3gp",    "video/3gpp"}, 
		{".apk",    "application/vnd.android.package-archive"}, 
		{".asf",    "video/x-ms-asf"}, 
		{".avi",    "video/x-msvideo"}, 
		{".bin",    "application/octet-stream"}, 
		{".bmp",    "image/bmp"}, 
		{".c",  "text/plain"}, 
		{".class",  "application/octet-stream"}, 
		{".conf",   "text/plain"}, 
		{".cpp",    "text/plain"}, 
		{".doc",    "application/msword"}, 
		{".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"}, 
		{".xls",    "application/vnd.ms-excel"},  
		{".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}, 
		{".exe",    "application/octet-stream"}, 
		{".gif",    "image/gif"}, 
		{".gtar",   "application/x-gtar"}, 
		{".gz", "application/x-gzip"}, 
		{".h",  "text/plain"}, 
		{".htm",    "text/html"}, 
		{".html",   "text/html"}, 
		{".jar",    "application/java-archive"}, 
		{".java",   "text/plain"}, 
		{".jpeg",   "image/jpeg"}, 
		{".jpg",    "image/jpeg"}, 
		{".js", "application/x-javascript"}, 
		{".log",    "text/plain"}, 
		{".m3u",    "audio/x-mpegurl"}, 
		{".m4a",    "audio/mp4a-latm"}, 
		{".m4b",    "audio/mp4a-latm"}, 
		{".m4p",    "audio/mp4a-latm"}, 
		{".m4u",    "video/vnd.mpegurl"}, 
		{".m4v",    "video/x-m4v"},  
		{".mov",    "video/quicktime"}, 
		{".mp2",    "audio/x-mpeg"}, 
		{".mp3",    "audio/x-mpeg"}, 
		{".mp4",    "video/mp4"}, 
		{".mid",    "audio"},
		//   {".mpc",    "application/vnd.mpohun.certificate"},        
		{".mpe",    "video/mpeg"},   
		{".mpeg",   "video/mpeg"},   
		{".mpg",    "video/mpeg"},   
		{".mpg4",   "video/mp4"},    
		{".mpga",   "audio/mpeg"}, 
		{".mrp",     "application/mrp"},
		{".msg",    "application/vnd.ms-outlook"}, 
		{".ogg",    "audio/ogg"}, 
		{".pdf",    "application/pdf"}, 
		{".png",    "image/png"}, 
		{".pps",    "application/vnd.ms-powerpoint"}, 
		{".ppt",    "application/vnd.ms-powerpoint"}, 
		{".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"}, 
		{".prop",   "text/plain"}, 
		{".rc", "text/plain"}, 
		{".rmvb",   "audio/x-pn-realaudio"}, 
		{".rtf",    "application/rtf"}, 
		{".sh", "text/plain"}, 
		{".tar",    "application/x-tar"},    
		{".tgz",    "application/x-compressed"},  
		{".txt",    "text/plain"}, 
		{".wav",    "audio/x-wav"}, 
		{".wma",    "audio/x-ms-wma"}, 
		{".wmv",    "audio/x-ms-wmv"}, 
		{".wml",   "text/html"}, 
		{".wps",    "application/vnd.ms-works"}, 
		{".xml",    "text/plain"}, 
		{".z",  "application/x-compress"}, 
		{".zip",    "application/x-zip-compressed"}, 
		{"",        "*/*"}   
	};
  
}
