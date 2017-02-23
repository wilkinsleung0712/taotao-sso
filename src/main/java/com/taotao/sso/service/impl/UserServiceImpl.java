package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserService;
import com.taotao.util.ExceptionUtil;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private TbUserMapper tbUserMapper;

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

}
