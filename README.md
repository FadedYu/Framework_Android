
# Framework_Android
Android架构搭建，根据腾讯QMUI_Android框架搭建，适用于快速运用到项目中去。
由于本项目与自己的后台服务器搭配使用，在使用时请改成自己的服务器IP地址。

感谢QMUI_Android团队为广大开发者提供的 UI 库 ，官网：[http://qmuiteam.com/android](http://qmuiteam.com/android)


### （持续更新）

### 开发日志 —— 2
 - 添加并重写OkGo中的JsonCallback，添加JsonConvert类，对后台baseJson进行统一管理。
   每个项目请求后台返回的json格式都有所不同，**请根据实际项目需求修改JsonConvert类**。 本项目同时提供了两种json格式进行解析操作：
   
    1. json格式为`{code:0,msg:"成功",data:{…}}`。根据后台返回的`code`数值来判断返回的数据是否成功。`code:0`表示成功返回，`code:101` 等其他标识表示返回失败，并返回失败的`msg`。
    
    2. json格式为`{success:true,msg:"成功",data:{…}}`。根据后台返回的`success`数值来判断返回的数据是否成功。`success:true`表示成功返回，`success:false` 表示返回失败，并返回失败的`msg`。
	    
- 实现启动和登录功能，在app启动时通过后台服务器判断token是否失效，如果没有token或者失效，则跳转到登录页面，反之直接跳转到主界面。
- 添加测试用户信息开关，并且在gradle配置只能在debug版本使用，当编译成release正式版时，关闭测试用户信息开关。
- 添加SPConstants，统一管理SharedPreferences的key。

### 开发日志 —— 1

- 根据QMUI搭建viewPager+fragment+Navigation主页，添加Android7.0 provider
- 添加登录界面，界面根据软键盘弹出自适应上下移动，添加文件相关工具类
- 添加OkGo并配置框架，添加Logger框架，添加内存泄漏检测框架，base类添加EasyPermission权限管理框架。

### 项目截图（持续更新）
![登录界面](https://raw.githubusercontent.com/FadedYu/Framework_Android/master/img_folder/im_login.png)
![主页](https://raw.githubusercontent.com/FadedYu/Framework_Android/master/img_folder/im_pager1.png)
