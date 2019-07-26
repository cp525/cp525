package com.czxy.servie.impl;

import com.czxy.dao.UserDao;
import com.czxy.domain.User;
import com.czxy.servie.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User login(User user) {

        List<User> list = userDao.findUserByNameAndPsw(user);
        if(list.isEmpty()){
            return null;
        }else{

            return  list.get(0);
        }
    }
}
