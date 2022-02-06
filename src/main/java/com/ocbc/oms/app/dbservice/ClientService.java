package com.ocbc.oms.app.dbservice;

import com.ocbc.oms.app.model.TClient;
import com.ocbc.oms.app.repository.TClientMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final TClientMapper tClientMapper;

    public ClientService(TClientMapper tClientMapper) {
        this.tClientMapper = tClientMapper;
    }

    public List<TClient> getAllClients() {
        return tClientMapper.selectList(null);
    }
}
