# apitest
Check the robustness of the interface
该项目基于idea插件 RestfulToolkit的源码开发(http://plugins.jetbrains.com/plugin/10292-restfultoolkit)
# 主要功能
 用于开发完接口后检测接口的健壮性 

 所有Integer,double,float,long类型参数都会用-1,0,10,100等4个默认的值请求一边接口
 如果参数名称中带
   1. age 则 请求参数为-1,0,20,120
   2. status 则 请求参数为-1,0,1,2
   3. type 则 请求参数为-1,0,1,2
   4. page 则 请求参数为-1,0,1,10
 
