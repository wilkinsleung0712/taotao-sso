package com.taotao.sso.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {
    /**
     * 注册用户
     * 
     * @param user
     *            - 用户信息
     * @return - TaotaoResult
     */
    public TaotaoResult createUser(TbUser user);

    /**
     * 查询该用户是否已注册
     * 
     * @param param
     *            - 用户数据
     * @param type
     *            - 为类型，可选参数1、2、3分别代表username、phone、email
     * @return 返回数据，true：数据可用，false：数据不可用
     */
    public TaotaoResult checkData(String param, Integer type);

    /**
     * 用密匙查询缓存用户资料
     * 
     * @param token
     * @return
     */
    public TaotaoResult getUserByToken(String token);

    /**
     * 用户登陆
     * 
     * @param username
     * @param password
     * @param request
     *            - 用于填写cookies
     * @param response
     *            - 用于填写cookies
     * @return - 返回token封装在
     */
    public TaotaoResult userLogin(String username, String password, HttpServletRequest request,
            HttpServletResponse response);

    /**
     * @param token
     * @return
     */
    public TaotaoResult userLogoutByToken(String token);

}
