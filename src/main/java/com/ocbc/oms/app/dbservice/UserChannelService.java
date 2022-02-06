package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.TUserChannel;
import com.ocbc.oms.app.repository.TUserChannelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserChannelService {
    private final TUserChannelMapper tUserChannelMapper;

    public UserChannelService(TUserChannelMapper tUserChannelMapper) {this.tUserChannelMapper=tUserChannelMapper;}

    public List<TUserChannel> getAllUserChannelMappings() {return tUserChannelMapper.selectList(null);}
}
