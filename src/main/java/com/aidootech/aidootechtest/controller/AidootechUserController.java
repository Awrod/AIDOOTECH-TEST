package com.aidootech.aidootechtest.controller;


import com.aidootech.aidootechtest.dto.userDto;
import com.aidootech.aidootechtest.entity.AidootechUser;
import com.aidootech.aidootechtest.entity.FriendT;
import com.aidootech.aidootechtest.entity.UserLog;
import com.aidootech.aidootechtest.service.AidootechUserService;
import com.aidootech.aidootechtest.service.FriendTService;
import com.aidootech.aidootechtest.service.UserLogService;
import com.aidootech.aidootechtest.service.impl.AidootechUserServiceImpl;
import com.aidootech.aidootechtest.util.ResponseUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zaxxer.hikari.util.FastList;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import org.yaml.snakeyaml.events.Event;
import springfox.documentation.annotations.ApiIgnore;
import sun.security.provider.MD5;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *   用户表操作
 * </p>
 *
 * @author cxl
 * @since 2022-05-09
 */
@RestController
@RequestMapping("/user")
public class AidootechUserController {
    @Autowired
    AidootechUserService aidootechUserService;
    @Autowired
    UserLogService userLogService;
    @Autowired
    RedisTemplate<String,Object> redisTemplate;
    @Autowired
    FriendTService friendTService;

    @ApiOperation(value = "注册session，测试用")
    @GetMapping("/register")
    public Map<String,Object> register(HttpServletRequest request){
        request.getSession().setAttribute("id","1");
        ResponseUtil<String> result = new ResponseUtil<>();
        return result.ResponseSuccess("成功");
    }
    /**
    * @Author cxl
    * @Date 2022/5/9 21:08
    * @return Map<String,Object>
    **/
    @ApiOperation(value = "关注列表")
    @GetMapping("/friendlist")
    public Map<String,Object> friendList(HttpServletRequest request){
        ResponseUtil<userDto> result = new ResponseUtil<>();
        //获取当前用户ID
        String userid = request.getSession().getAttribute("id").toString();
        //查询用户已有关注
        List<FriendT> FriendTIds = friendTService.list(new LambdaQueryWrapper<FriendT>()
                .eq(FriendT::getUserid, userid));
        //获取关注信息
        List<AidootechUser> FriendTNames = aidootechUserService.listByIds(FriendTIds.stream().map(val -> new Integer(val.getFriendid())).collect(Collectors.toList()));
        //筛选在线和未在线关注
        List<userDto> userDtos=new ArrayList<>();
        for (AidootechUser friendTName : FriendTNames) {
            if (redisTemplate.opsForValue().get(friendTName.getId().toString()) != null){
                userDtos.add(new userDto(friendTName.getName(),"yes"));
            }else{
                userDtos.add(new userDto(friendTName.getName(),"no"));
            }
        }
        return result.ResponseSuccess(userDtos,"关注列表");
    }
    /**
    * @Author cxl
    * @Date 2022/5/9 20:35
    * @return Map<String,Object>
    **/
    @ApiOperation(value = "添加关注")
    @GetMapping("/addfriend")
    public Map<String,Object> addFriend(HttpServletRequest request, @RequestParam(value = "friendIds",required=true) List<Integer> friendIds){
        ResponseUtil<FriendT> result = new ResponseUtil<>();
        String userid = request.getSession().getAttribute("id").toString();
        //查询用户已有关注
        List<FriendT> oldList = friendTService.list(new LambdaQueryWrapper<FriendT>()
                .eq(FriendT::getUserid, userid));
        //获取当前用户ID
        //用户ID和朋友ID组合成一个集合(过滤已经存在的关注)，new FriendT处ID为自增，所以设置为NULL
        List<FriendT> friendTS= friendIds.stream().map(f-> new FriendT(null,Integer.parseInt(userid),f)).collect(Collectors.toList());
        friendTS.addAll(oldList);
        friendTS = friendTS.stream().distinct().collect(Collectors.toList());
        if (friendTService.saveBatch(friendTS)) {
            AidootechUserController.insLog(request,"用户："+userid+"添加关注");
            return  result.ResponseSuccess(friendTS,"添加成功！");
        }
        return  result.ResponseError("添加失败");
    }
    /**
    * @Author cxl
    * @Date 2022/5/9 20:21
    * @return Map<String,Object>
    **/
    @ApiOperation(value = "登入")
    @GetMapping("/login")
    public Map<String,Object> login(HttpServletRequest request,String userName,String userPassWord){
        ResponseUtil<userDto> result = new ResponseUtil<>();
        //查询是否有匹配的用户和密码
        AidootechUser aidootechUsers  = aidootechUserService.getOne(new LambdaQueryWrapper<AidootechUser>().eq(AidootechUser::getName, userName)
        .eq(AidootechUser::getPassword,DigestUtils.md5DigestAsHex(userPassWord.getBytes())));
        if (aidootechUsers != null){
            request.getSession().setAttribute("id",aidootechUsers.getId());
            //加入reids在线列表
            request.getSession().setAttribute("reidsVal",aidootechUsers.getId().toString()+":"+aidootechUsers.getName());
            redisTemplate.opsForValue().set(aidootechUsers.getId().toString(),aidootechUsers.getName());
            userDto userDto = new userDto(userName,"yes");
            AidootechUserController.insLog(request,"用户："+userName+"登入");
            return  result.ResponseSuccess(userDto,"登入成功！");
        }
        return  result.ResponseError("登入失败！用户名或密码错误");
    }

    /**
    * @Author cxl
    * @Date 2022/5/9 20:21
    * @return Map<String,Object>
    **/
    @ApiOperation(value = "注销")
    @GetMapping("/loginoff")
    public Map<String,Object> loginOff(HttpServletRequest request,String userName){
        ResponseUtil<AidootechUser> result = new ResponseUtil<>();
        request.getSession().setAttribute("id", "");
        String reidsVal = request.getSession().getAttribute("reidsVal").toString();
        //删除此ID在reids列表
        redisTemplate.opsForZSet().remove("isonline", reidsVal);
        userDto userDto = new userDto(userName, "yes");
        return result.ResponseSuccess(userDto, "注销成功！");
    }
    /**
    * @Author cxl
    * @Date 2022/5/9 16:59
    * @return ap<String,Object>
    **/
    @ApiOperation(value = "新建用户")
    @GetMapping("/inster")
    public Map<String,Object> insterUser(HttpServletRequest request,AidootechUser user){
        ResponseUtil<AidootechUser> result = new ResponseUtil<>();
        AidootechUser aidootechUsers  = aidootechUserService.getOne(new LambdaQueryWrapper<AidootechUser>().eq(AidootechUser::getName, user.getName()));
        //判断用户名是否存在
        if (aidootechUsers != null){
           return  result.ResponseError(aidootechUsers,"此用户名已存在");
        }else {
            try {
                user.setCreatedAt(new Date(System.currentTimeMillis()));
                user.setType("普通用户");
                //md5加密密码
                user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
                if (user.insert()) {
                    AidootechUserController.insLog(request,"新建用户:"+user.getName());
                    return  result.ResponseSuccess(user,"用户新建成功！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  result.ResponseError("插入失败！参数错误");
    }
    /**
    * @Author cxl
    * @Date 2022/5/9 18:22
    * @return Map<String,Object>
    **/
    @ApiOperation(value = "更新用户")
    @GetMapping("/update")
    public Map<String,Object> updateUser(HttpServletRequest request,AidootechUser user,Integer userId){
        ResponseUtil<AidootechUser> result = new ResponseUtil<>();
        AidootechUser aidootechUsers = aidootechUserService.getOne(new LambdaQueryWrapper<AidootechUser>()
        .eq(AidootechUser::getName,user.getName()));
        //判断新的用户名是否存在
        if (aidootechUsers!= null){
            return  result.ResponseError(aidootechUsers,"此用户名已存在");
        }else {
            try {
                user.setId(userId);
                //md5加密密码
                user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
                if (user.updateById()) {
                    AidootechUserController.insLog(request,"修改用户:"+userId);
                    return  result.ResponseSuccess(user,"用户修改成功！");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return  result.ResponseError("修改失败！参数错误");
    }
    /**
    * @Author cxl
    * @Date 2022/5/9 18:25
    * @return  Map<String,Object>
    **/
    @ApiOperation(value = "查询用户")
    @GetMapping("/select")
    public Map<String,Object> selectUserByName(HttpServletRequest request,String userName){
        ResponseUtil<AidootechUser> result = new ResponseUtil<>();
        AidootechUser aidootechUsers  = aidootechUserService.getOne(new LambdaQueryWrapper<AidootechUser>().eq(AidootechUser::getName, userName));
        if (aidootechUsers != null){
            AidootechUserController.insLog(request,"查询用户:"+userName);
            return  result.ResponseSuccess(aidootechUsers,"查询成功！");
        }
        return  result.ResponseError("查询失败！用户不存在!");
    }
    /**
    * @Author cxl
    * @Date 2022/5/9 18:26
    * @return Map<String,Object>
    **/
    @ApiOperation(value = "获取用户列表")
    @GetMapping("/selectall")
    public Map<String,Object> selectUserAll(HttpServletRequest request){
        Integer user = Integer.parseInt(request.getSession().getAttribute("id").toString());
        ResponseUtil<AidootechUser> result = new ResponseUtil<>();
        if (user!=1){
            return result.ResponseError("用户权限不足");
        }
        List<AidootechUser> aidootechUsers  = aidootechUserService.list();
        if (aidootechUsers != null){
            AidootechUserController.insLog(request,"查询所有用户");
            return  result.ResponseSuccess(aidootechUsers,"查询成功！");
        }
        return  result.ResponseError("无用户！");
    }

    /**
    * @Author cxl
    * @Date 2022/5/9 18:29
    * @return Map<String,Object>
    **/
    @ApiOperation(value = "删除用户")
    @GetMapping("/delete")
    public Map<String,Object> deleteUser(HttpServletRequest request,Integer userId){
        Integer user = Integer.parseInt(request.getSession().getAttribute("id").toString());
        ResponseUtil<AidootechUser> result = new ResponseUtil<>();
        if (user!=1){
            return result.ResponseError("用户权限不足");
        }
        AidootechUser aidootechUser = new AidootechUser();
        if (aidootechUser.deleteById(userId)){
            AidootechUserController.insLog(request,"删除用户："+userId);
            return  result.ResponseSuccess("删除成功！");
        }
        return  result.ResponseError("删除失败！ID不存在");
    }

    /**
    * 描述: 操作记录
    * @Author cxl
    * @Date 2022/5/9 18:34
    * @return void
    **/
    @ApiIgnore
    public static  void  insLog(HttpServletRequest request,String opertion){
        //测试用，防止未登入
        request.getSession().setAttribute("id","1");
        Integer user = Integer.parseInt(request.getSession().getAttribute("id").toString());
        UserLog userLog = new UserLog();
        userLog.setUserid(user);
        userLog.setOperation(opertion);
        userLog.setOperationtime(new Date());
        userLog.insert();
    }
}

