package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.TUser;
import com.ocbc.oms.app.repository.TUserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final TUserMapper tUserMapper;

    public UserService(TUserMapper tUserMapper) {
        this.tUserMapper = tUserMapper;
    }

    public List<TUser> getAllUsers() {
        return tUserMapper.selectList(null);
    }

    public TUser getUserById(Integer userId){
        return tUserMapper.selectById(userId);
    }
}
