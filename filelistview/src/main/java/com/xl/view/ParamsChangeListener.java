package com.xl.view;
import android.os.*;
import android.graphics.*;
import android.content.*;
import java.util.*;


//内部接口用于动态修改地图的一些参数
public interface ParamsChangeListener {
	//地图尺寸参数
	public void paramsChange(int mapWidth, int mapHeight, int blockWidth, int blockHeight);
	//移动拖动条，修改画布的X Y坐标
	public void moveSeekBar(int moveX, int moveY);
	//给View传递图片的Bitmap对象
	public void sendImage(Bitmap bitmap, int id);
	//上一步 下一步操作
	public void stepNext(Handler handler, int requestCode);
	//保存地图
	//参数：文件名 图片列表 图片名称列表
	public void saveMap(String filename )
	;
}
