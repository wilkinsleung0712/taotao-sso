package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;
import com.taotao.util.ExceptionUtil;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    
    @RequestMapping("/showLogin")
    public String getLoginPage(String redirect, Model model) {
        model.addAttribute("redirect", redirect);
        return "login";
    }

    @RequestMapping("/showRegister")
    public String showRegister() {
        return "register";
    }
    
    
    /**
     * 用户登录
     * 
     * @param username
     *            - 用户名
     * @param password
     *            - 密码
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult userLogin(String username, String password,
            HttpServletRequest request, HttpServletResponse response) {
        TaotaoResult result = null;
        if (StringUtils.isBlank(username)) {
            result = TaotaoResult.build(400, "用户名不能为空");
        }
        if (StringUtils.isBlank(password)) {
            result = TaotaoResult.build(400, "密码不能为空");
        }
        try {
            // 通过检验
            result = userService.userLogin(username, password, request, response);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
        return result;
    }

    /**
     * 用户注册
     * 
     * @param user
     * @return
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult register(TbUser user) {
        TaotaoResult result = null;
        try {
            if (null != user) {
                result = userService.createUser(user);
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
        return result;
    }

    /**
     * 检验用户信息是否可用（存在）
     * 
     * @param param
     * @param type
     * @param callback
     * @return
     */
    @RequestMapping("/check/{param}/{type}")
    @ResponseBody
    public Object checkData(@PathVariable String param, @PathVariable Integer type, String callback) {
        TaotaoResult result = null;
        if (StringUtils.isBlank(param)) {
            result = TaotaoResult.build(400, "校验内容不能为空");
        }

        if (type == null) {
            result = TaotaoResult.build(400, "校验类型不能为空");
        }

        if (type != 1 && type != 2 && type != 3) {
            result = TaotaoResult.build(400, "校验内容类型错误");
        }
        // 校验出错
        if (null != result) {
            if (!StringUtils.isBlank(callback)) {
                // 需要让数据返回调用一个json javascript callback function 来控制cors
                // 所以需要返回Object而不是taotaoresult
                MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
                mappingJacksonValue.setJsonpFunction(callback);
                return mappingJacksonValue;
            } else {
                return result;
            }
        }

        try {
            // 校验通过
            result = userService.checkData(param, type);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
        // 需要判断是否有callback function在前端调用
        if (!StringUtils.isBlank(callback)) {
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }

        return result;
    }

    /**
     * 通过token查询用户信息
     * 
     * @param token
     *            - 用户登录凭证
     * @param callback
     *            -jsonp回调方法
     * @return
     */
    @RequestMapping("/token/{token}")
    @ResponseBody
    public Object getUserByToken(@PathVariable String token, String callback) {
        TaotaoResult result = null;
        if (StringUtils.isBlank(token)) {
            // 没有提交token
            result = TaotaoResult.build(400, "密匙无效");
            if (StringUtils.isBlank(callback)) {
                // 不需要callback
                return result;
            }
            // 需要callback
            MappingJacksonValue mappingJsonValue = new MappingJacksonValue(result);
            mappingJsonValue.setJsonpFunction(callback);
            return mappingJsonValue;
        }

        try {
            // 需要在缓存里查找用户资料
            result = userService.getUserByToken(token);
            if (!StringUtils.isBlank(callback)) {
                // 需要callback
                MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
                mappingJacksonValue.setJsonpFunction(callback);
                return mappingJacksonValue;
            }
            // 不需要callback

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }

        return result;
    }

    @RequestMapping("/logout/{token}")
    @ResponseBody
    public Object userLogout(@PathVariable String token, String callback) {
        TaotaoResult result = null;
        if (StringUtils.isBlank(token)) {
            result = TaotaoResult.build(400, "密匙无效");
            if (StringUtils.isBlank(callback)) {
                // 不需要callback
                return result;
            }
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        // 密匙有效
        try {
            result = userService.userLogoutByToken(token);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            result = TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
        
        if (!StringUtils.isBlank(callback)) {
            // 需要callback
            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return mappingJacksonValue;
        }
        return result;
    }
}
