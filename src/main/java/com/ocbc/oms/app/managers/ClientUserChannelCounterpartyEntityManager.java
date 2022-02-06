package com.ocbc.oms.app.managers;

import com.ocbc.oms.app.dbservice.ChannelService;
import com.ocbc.oms.app.dbservice.ClientService;
import com.ocbc.oms.app.dbservice.CounterpartyService;
import com.ocbc.oms.app.dbservice.EntityService;
import com.ocbc.oms.app.dbservice.UserChannelService;
import com.ocbc.oms.app.dbservice.UserService;
import com.ocbc.oms.app.error.application.ApplicationErrorConstant;
import com.ocbc.oms.app.error.application.InvalidInputMapException;
import com.ocbc.oms.app.model.TChannel;
import com.ocbc.oms.app.model.TClient;
import com.ocbc.oms.app.model.TCounterparty;
import com.ocbc.oms.app.model.TEntity;
import com.ocbc.oms.app.model.TUser;
import com.ocbc.oms.app.model.TUserChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@DependsOn("flywayInitializer")
@Slf4j
public class ClientUserChannelCounterpartyEntityManager {
    private final ClientService clientService;
    private final UserService userService;
    private final ChannelService channelService;
    private final CounterpartyService counterpartyService;
    private final UserChannelService userChannelService;
    private final EntityService entityService;

    private Map<String, TClient> clientMap;
    private Map<String, TUser> userMap;
    private Map<String, TChannel> channelMap;
    private Map<String, TCounterparty> counterpartyMap;
    private Map<Integer, TUserChannel> userChannelMap;
    private Map<Integer, TEntity> entityMap;
    private Map<Integer,TUser> userIdMap;


    public ClientUserChannelCounterpartyEntityManager(ClientService clientService, UserService userService,
                                                      ChannelService channelService,
                                                      CounterpartyService counterpartyService,
                                                      UserChannelService userChannelService,
                                                      EntityService entityService) {
        this.clientService = clientService;
        this.userService = userService;
        this.channelService = channelService;
        this.counterpartyService = counterpartyService;
        this.userChannelService = userChannelService;
        this.entityService = entityService;
    }

    public Integer getUserIdFromMap(String user) {
        if (StringUtils.isNotBlank(user) && userMap.containsKey(user)) {
            return userMap.get(user).getId();
        }
        throw new InvalidInputMapException(ApplicationErrorConstant.InvalidTypeExceptionMessage +
            " -> userMap",
            ApplicationErrorConstant.InvalidTypeExceptionCode);
    }

    public String getUserNameFromMap(Integer userId){
        if (userId != null && userId > 0 && userIdMap.containsKey(userId)) {
            return userIdMap.get(userId).getFirstName() + " " + userIdMap.get(userId).getLastName();
        }
        throw new InvalidInputMapException(ApplicationErrorConstant.InvalidTypeExceptionMessage +
                " -> userIdMap",
                ApplicationErrorConstant.InvalidTypeExceptionCode);
    }

    public Integer getChannelIdFromMap(Integer userId) {
        if (userId != null && userId > 0 && userChannelMap.containsKey(userId)) {
            return userChannelMap.get(userId).getChannelId();
        }
        throw new InvalidInputMapException(ApplicationErrorConstant.InvalidTypeExceptionMessage +
            " -> userChannelMap",
            ApplicationErrorConstant.InvalidTypeExceptionCode);
    }

    public String getChannelNameFromMap(Integer channelId) {
        if (channelId != null && channelId > 0) {
            for (String key : channelMap.keySet()) {
                TChannel tChannel = channelMap.get(key);
                if (tChannel.getId().equals(channelId)) {
                    return tChannel.getName();
                }
            }
        }
        throw new InvalidInputMapException(ApplicationErrorConstant.InvalidTypeExceptionMessage +
            " -> channelMap",
            ApplicationErrorConstant.InvalidTypeExceptionCode);
    }

    public Integer getCounterpartyFromMap(Integer userId) {
        if (userId != null && userId > 0) {
            Integer entityId = getEntityIdFromUserMap(userId);
            for (String key : counterpartyMap.keySet()) {
                if (counterpartyMap.get(key).getEntityId().intValue() == entityId.intValue()) {
                    return counterpartyMap.get(key).getId();
                }
            }
        }
        throw new InvalidInputMapException(ApplicationErrorConstant.InvalidTypeExceptionMessage +
            " -> counterpartyMap",
            ApplicationErrorConstant.InvalidTypeExceptionCode);
    }

    private Integer getEntityIdFromUserMap(Integer userId) {
        if (userId != null && userId > 0) {
            for (String key : userMap.keySet()) {
                if (userId.intValue() == userMap.get(key).getId().intValue()) {
                    return userMap.get(key).getEntityId();
                }
            }
        }
        throw new InvalidInputMapException(ApplicationErrorConstant.InvalidTypeExceptionMessage +
            " -> userMap",
            ApplicationErrorConstant.InvalidTypeExceptionCode);
    }

    public Integer getClientIdFromMap(String client) {
        if (StringUtils.isNotBlank(client) && clientMap.containsKey(client)) {
            return clientMap.get(client).getId();
        }
        throw new InvalidInputMapException(ApplicationErrorConstant.InvalidTypeExceptionMessage +
            " -> clientMap",
            ApplicationErrorConstant.InvalidTypeExceptionCode);
    }

    public String getClientNameFromMap(Integer clientId) {
        if (clientId != null && clientId > 0) {
            for (String key : clientMap.keySet()) {
                TClient tClient = clientMap.get(key);
                if (tClient.getId().equals(clientId)) {
                    return tClient.getName();
                }
            }
        }
        throw new InvalidInputMapException(ApplicationErrorConstant.InvalidTypeExceptionMessage +
            " -> clientMap",
            ApplicationErrorConstant.InvalidTypeExceptionCode);
    }

    public TUser getUserById(Integer userId){
        return userService.getUserById(userId);
    }

    private void initializeClientMap() {
        clientMap = new HashMap<>();
        List<TClient> fromDatabase = clientService.getAllClients();
        fromDatabase.forEach(client -> {
            log.debug("Adding client:{}", client);
            clientMap.put(client.getName(), client);
        });
    }

    private void initializeUserMap() {
        userMap = new HashMap<>();
        userIdMap = new HashMap<>();
        List<TUser> fromDatabase = userService.getAllUsers();
        fromDatabase.forEach(user -> {
            log.debug("Adding user:{}", user);
            userMap.put(user.getFirstName() +
                " " + user.getLastName(), user);
            userIdMap.put(user.getId(),user);
        });
    }

    private void initializeChannelMap() {
        channelMap = new HashMap<>();
        List<TChannel> fromDatabase = channelService.getAllChannels();
        fromDatabase.forEach(channel -> {
            log.debug("Adding channel:{}", channel);
            channelMap.put(channel.getCode(), channel);
        });
    }

    private void initializeCounterpartyMap() {
        counterpartyMap = new HashMap<>();
        List<TCounterparty> fromDatabase = counterpartyService.getAllCounterparties();
        fromDatabase.forEach(counterparty -> {
            log.debug("Adding counterparty:{}", counterparty);
            counterpartyMap.put(counterparty.getCode(), counterparty);
        });
    }

    private void initializeUserChannelMap() {
        userChannelMap = new HashMap<>();
        List<TUserChannel> fromDatabase = userChannelService.getAllUserChannelMappings();
        fromDatabase.forEach(userChannelMapping -> {
            log.debug("Adding userChannelMapping:{}", userChannelMapping);
            userChannelMap.put(userChannelMapping.getUserId(), userChannelMapping);
        });
    }

    private void initializeEntityMap() {
        entityMap = new HashMap<>();
        List<TEntity> fromDatabase = entityService.getAllEntities();
        fromDatabase.forEach(entity -> {
            log.debug("Adding entity:{}", entity);
            entityMap.put(entity.getId(), entity);
        });
    }

    @PostConstruct
    private void initialize() {
        initializeClientMap();
        initializeUserMap();
        initializeChannelMap();
        initializeCounterpartyMap();
        initializeUserChannelMap();
        initializeEntityMap();
    }
}
