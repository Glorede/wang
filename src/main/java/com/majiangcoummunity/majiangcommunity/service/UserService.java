package com.majiangcoummunity.majiangcommunity.service;

import com.majiangcoummunity.majiangcommunity.Provider.GitHubProvider;
import com.majiangcoummunity.majiangcommunity.dto.GitHubUser;
import com.majiangcoummunity.majiangcommunity.mapper.UserMapper;
import com.majiangcoummunity.majiangcommunity.model.User;
import com.majiangcoummunity.majiangcommunity.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private GitHubProvider gitHubProvider;

    public void createOrUpdate(User user) {
//        User dbUser = userMapper.findByAccountId(user.getAccountId());
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);

        GitHubUser gitHubUser = gitHubProvider.getUser(user.getAccountId());
        if (users.size() == 0){
            //插入
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            //更新
            User dbUser = users.get(0);


            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());

            User updateUser = new User();

            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            UserExample example = new UserExample();
            example.createCriteria().andIdEqualTo(dbUser.getId());
            userMapper.updateByExampleSelective(updateUser, example);
//            userMapper.update(dbUser);
        }
    }
}
