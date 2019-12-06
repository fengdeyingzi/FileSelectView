## FileSelectView
Android文件选择器控件，调用方便，无需配置Activity，支持选择文件/文件夹，支持设置白色/黑色两种主题。

创建一个FileSelectView
~~~
FileSelectView fileSelectView = new FileSelectView(this);
~~~
//启用黑色主题
~~~
fileSelectView.setThemeBlack(false);
~~~
//设置选择文件
~~~
fileSelectView.selectFile();
~~~
//设置选择文件夹
~~~
fileSelectView.selectDir();
~~~
//获取选择的路径
~~~
fileSelectView.getPath();
~~~

//设置选择的路径
~~~
fileSelectView.setPath("/mnt/sdcard/");
~~~

