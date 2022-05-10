# 工程简介
莱豆科技测试


# 延伸阅读
已装配swagger2，可在项目启动后进入 http://localhost:8080/swagger-ui.html#/ 进行测试，所需sql在resource/sql下

#接口简介
需要一定步骤的接口：

##### url:/user/friendlist  param:HttpServletRequest request 用于保存/获取session

关注列表,附近在线关注位置接口  
步骤：需要先登入，再使用关注接口关注好友，关注好友后使用好友账号密码登入将在线及定位信息插入redis，再使用/user/register快速注册session否者获取的关注列表好友都是不在线且获取不到距离。



#####url:/user/friendlist   param:HttpServletRequest request, @RequestParam(value = "friendIds",required=true) List<Integer> friendIds 要关注的好友/好友列表

批量添加关注  
需要使用登入接口注册session

可直接使用的接口：

#####url:/user/login   param:HttpServletRequest request, String userName 用户名, String userPassWord 密码, Double x,Double y  x,y经纬度

登入，需要输入用户名密码验证，及ip经纬度如 13.361389 38.115556


#####url:/user/loginoff   param:HttpServletRequest request,String userName 

注销用户session，及保存在redis中的在线string和定位geo

#####url:/user/inster   param:HttpServletRequest request,AidootechUser user 新用户信息

插入新用户，不可重名

#####url:/user/update   param:HttpServletRequest request,AidootechUser user,Integer userId 用户ID

修改用户信息，不可重名

#####url:/user/select   param:HttpServletRequest request,String userName

模糊查询用户

#####url:/user/selectall   param:HttpServletRequest request

获取所有用户列表

#####url:/user/delete   param:HttpServletRequest request,Integer userId

删除某个用户

#####functionName:insLog   param:HttpServletRequest request,String opertio 操作信息

静态方法，用于记录操作日志
