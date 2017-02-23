package com.taotao.sso.service;

import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {
    /**
     * 注册用户
     * @param user - 用户信息
     * @return - TaotaoResult
     */
    public TaotaoResult createUser(TbUser user);

    /**
     * 查询该用户是否已注册
     * @param param - 用户数据
     * @param type - 为类型，可选参数1、2、3分别代表username、phone、email
     * @return 返回数据，true：数据可用，false：数据不可用
     */
    public TaotaoResult checkData(String param, Integer type);
}
