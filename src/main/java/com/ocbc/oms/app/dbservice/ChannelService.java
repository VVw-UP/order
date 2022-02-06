package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.TChannel;
import com.ocbc.oms.app.repository.TChannelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {
    private final TChannelMapper tChannelMapper;

    public ChannelService(TChannelMapper tChannelMapper) {
        this.tChannelMapper = tChannelMapper;
    }

    public List<TChannel> getAllChannels() {
        return tChannelMapper.selectList(null);
    }
}
