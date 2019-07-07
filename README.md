# apitest
Check the robustness of the interface
该项目基于idea插件 RestfulToolkit的源码开发(http://plugins.jetbrains.com/plugin/10292-restfultoolkit)
# 主要功能
 用于检测接口的健壮性 （比如age传了-1）

 所有Integer,double,float,long类型参数都会用-1,0,10,100等4个默认的值请求一边接口
 如果参数名称中带
   1. age 则 请求参数为-1,0,20,120
   2. status 则 请求参数为-1,0,1,2
   3. type 则 请求参数为-1,0,1,2
   4. page 则 请求参数为-1,0,1,10
  # 如何使用
  1. 下载插件https://github.com/luojoe/apitest/blob/master/src/main/resources/apitest-1.0.jar
  2. 打开idea——》File——》Settings——》Plugins——》点击右上方类似齿轮的按钮——》Install Plugin from Disk
  3. 选中下载的.jar文件,安装后重启
  4. 使用参考下面的动图
  
   ![操作示例](https://github.com/luojoe/apitest/blob/master/demo.gif)
  
  
  
 
