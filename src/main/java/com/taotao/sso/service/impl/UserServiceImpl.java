package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.dao.JedisClient;
import com.taotao.sso.service.UserService;
import com.taotao.util.JsonUtils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper tbUserMapper;

    @Autowired
    private JedisClient jedisClient;

    @Value("${SSO_SESSION_EXPIRE}")
    private Integer SSO_SESSION_EXPIRE;

    @Value("${REDIS_USER_SESSION_KEY}")
    private String REDIS_USER_SESSION_KEY;

    @Override
    public TaotaoResult checkData(String param, Integer type) {
        TbUserExample example = new TbUserExample();
        Criteria criteria = example.createCriteria();

        // 查询逻辑
        if (1 == type) {
            // 用户名校验
            criteria.andUsernameEqualTo(param);
        } else if (2 == type) {
            // 电话校验
            criteria.andPhoneEqualTo(param);
        } else {
            // 邮件校验
            criteria.andEmailEqualTo(param);
        }
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if (null == list || list.isEmpty()) {
            return TaotaoResult.ok(true);
        }
        return TaotaoResult.ok(false);
    }

    @Override
    public TaotaoResult createUser(TbUser user) {
        // 补全用户信息
        user.setCreated(new Date());
        user.setUpdated(new Date());
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));

        tbUserMapper.insert(user);

        return TaotaoResult.ok();
    }

    @Override
    public TaotaoResult getUserByToken(String token) {
        TaotaoResult result = null;
        // 需要在缓存里查找用户资料
        String userInfo = jedisClient.get(token);
        if (StringUtils.isBlank(userInfo)) {
            // 用户没有登入或者登入缓存信息已过期
            result = TaotaoResult.build(400, "会话过期，请重新登录");
            return result;

        }
        // 更新过期时间
        jedisClient.expire(REDIS_USER_SESSION_KEY + ":" + token, SSO_SESSION_EXPIRE);
        // 返回用户信息
        TbUser user = JsonUtils.jsonToPojo(userInfo, TbUser.class);
        return TaotaoResult.ok(user);
    }

    @Override
    public TaotaoResult userLogin(String username, String password) {
        TaotaoResult result = new TaotaoResult();
        TbUserExample example = new TbUserExample();
        Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        tbUserMapper.selectByExample(example);
        return null;
    }

}
