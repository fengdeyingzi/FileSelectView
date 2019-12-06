package com.xl.filelist;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.xl.game.tool.ActivityCompat;
import com.xl.game.view.ConfirmationDialogFragment;
import com.xl.view.FileSelectView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileSelectActivity extends Activity
{

	private static final int REQUEST_PERMISSION = 100;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		// TODO: Implement this method
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Material);
		LinearLayout layout = new LinearLayout(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		FileSelectView fileSelectView = new FileSelectView(this);
		layout.addView(fileSelectView);
		setContentView(layout);
		
		//fileSelectView.setThemeBlack(false);
		fileSelectView.selectDir();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
			requestPermission();
		}
		requestPermission();
	}


	void requestPermission() {
		//检测单个权限是否已经设置
		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//			android.util.Log.i(TAG, "requestPermission: 权限申请成功");
//如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don't ask again 选项，此方法将返回 false。如果设备规范禁止应用具有该权限，此方法也会返回 false。
			//
			//如果应用之前请求过此权限但用户拒绝了请求，此方法将返回 true。如果用户在过去拒绝了权限请求，并在权限请求系统对话框中选择了 Don’t ask again 选项，此方法将返回 false。
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {


			}
			else if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
//				ConfirmationDialogFragment.newInstance(R.string.sdcard_permission_confirmation,
//						new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
//						REQUEST_PERMISSION, R.string.sdcard_permission_not_granted)
//						.show(getFragmentManager(), "dialog");
			}
			//申请
			else {
//				android.util.Log.i(TAG, "requestPermission: 申请权限");
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
						REQUEST_PERMISSION);


			}

		}

	}
}
