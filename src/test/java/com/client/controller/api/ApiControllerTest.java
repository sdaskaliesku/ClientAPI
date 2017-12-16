package com.client.controller.api;

import com.client.domain.db.AccessList;
import com.client.domain.db.ActivateRequest;
import com.client.domain.db.ClientVersion;
import com.client.repository.AccessListRepository;
import com.client.repository.ActivateRequestRepository;
import com.client.repository.BlackListRepository;
import com.client.repository.ClientVersionRepository;
import com.client.service.AccessListService;
import com.client.service.ActivateRequestService;
import com.client.service.ClientVersionService;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * @author sdaskaliesku
 */
public abstract class ApiControllerTest {
    protected List<AccessList> allAccessList;

//    protected List<BlackList> allBlackList;

    protected List<ClientVersion> allClientVersionList;

    @Mock
    protected AccessListRepository accessListRepository;

    @Mock
    protected BlackListRepository blackListRepository;

    @Mock
    protected ClientVersionRepository clientVersionRepository;

    protected ApiController apiController;



    /*
    * Get all entities
    */
    protected List<AccessList> getAllAccessList() {
        return allAccessList;
    }

//    protected List<BlackList> getAllBlackList() {
//        return allBlackList;
//    }

    protected List<ClientVersion> getAllClientVersionList() {
        return allClientVersionList;
    }

    /*
    * Helper methods for entities (analogues to sql queries)
    */
    protected List<AccessList> getAllClansOrUsers(boolean value) {
        List<AccessList> result = new ArrayList<>();
        for (AccessList list : allAccessList) {
            if (list.getClan() == value) {
                result.add(list);
            }
        }
        return result;
    }

    protected List<AccessList> getAccessByClanOrUserName(String name, String clan) {
        List<AccessList> result = new ArrayList<>();
        for (AccessList list : allAccessList) {
            if (list.getName().toLowerCase().equals(name.toLowerCase())
                    || list.getName().toLowerCase().equals(clan.toLowerCase())) {
                result.add(list);
            }
        }
        return result;
    }

    protected List<ClientVersion> getCurrentVersion(double version) {
        List<ClientVersion> result = new ArrayList<>();
        for (ClientVersion clientVersion : allClientVersionList) {
            if (clientVersion.getVersion().equals(version)) {
                result.add(clientVersion);
            }
        }
        return result;
    }

    protected ClientVersion getLastVersion(boolean isBetta) {
        List<ClientVersion> list = new ArrayList<>(allClientVersionList);
        Collections.sort(list);
        for (ClientVersion clientVersion : list) {
            if (clientVersion.getBetta() == isBetta) {
                return clientVersion;
            }
        }
        return null;
    }

//    protected List<BlackList> getAllBlackListClansOrUsers(boolean isClan) {
//        List<BlackList> result = new ArrayList<>();
//        for (BlackList list : allBlackList) {
//            if (list.getClan() == isClan) {
//                result.add(list);
//            }
//        }
//        return result;
//    }

//    protected BlackList isUserOrClanInBlackList(String userOrClan) {
//        for (BlackList blackList : allBlackList) {
//            if (blackList.getNickOrClanName().toLowerCase().equals(userOrClan.toLowerCase())) {
//                return blackList;
//            }
//        }
//        return null;
//    }

    /*
    * Mock some requests
    */
    protected void mockGetAccessByClanOrUserName(String clan, String name) {
        when(accessListRepository.getAccessByClanOrUserName(name, clan)).thenReturn(getAccessByClanOrUserName(name, clan));
    }

//    protected void mockIsUserOrClanInBlackList(String userOrClan) {
//        when(blackListRepository.isUserOrClanInBlackList(userOrClan)).thenReturn(isUserOrClanInBlackList(userOrClan));
//    }

    protected void mockGetClientVersion(double version, boolean updateToBetta) {
        ClientVersion result = null;
        for (ClientVersion clientVersion : allClientVersionList) {
            if (clientVersion.getVersion().equals(version) && clientVersion.getBetta().equals(updateToBetta)) {
                result = clientVersion;
                break;
            }
        }
        when(clientVersionRepository.getClientVersion(version, updateToBetta)).thenReturn(result);
    }

    protected void mockGetCurrentVersion(double version) {
        when(clientVersionRepository.getCurrentVersion(version)).thenReturn(getCurrentVersion(version));
    }


    /*
    * Set up mock repositories and services
    * */
    protected void setUpAccessListRepository() {
        allAccessList = getAllAccessList();
        when(accessListRepository.getAll()).thenReturn(allAccessList);
        boolean isClan = true;
        when(accessListRepository.getAllClansOrUsers(isClan)).thenReturn(getAllClansOrUsers(isClan));
        isClan = false;
        when(accessListRepository.getAllClansOrUsers(isClan)).thenReturn(getAllClansOrUsers(isClan));
        AccessListService accessListService = new AccessListService();
        accessListService.setRepository(accessListRepository);
        apiController.setAccessListService(accessListService);
    }

//    protected void setUpBlackListRepository() {
//        allBlackList = getAllBlackList();
//        when(blackListRepository.getAll()).thenReturn(allBlackList);
//        when(blackListRepository.getAllClans()).thenReturn(getAllBlackListClansOrUsers(true));
//        when(blackListRepository.getAllUsers()).thenReturn(getAllBlackListClansOrUsers(false));
//
//        BlackListService blackListService = new BlackListService();
//        blackListService.setRepository(blackListRepository);
//        apiController.setBlackListService(blackListService);
//    }

    protected void setUpClientVersionRepository() {
        reset(clientVersionRepository);
        when(clientVersionRepository.getAll()).thenReturn(getAllClientVersionList());
        boolean isBetta = true;
        when(clientVersionRepository.getLastVersion(isBetta)).thenReturn(getLastVersion(isBetta));
        isBetta = false;
        when(clientVersionRepository.getLastVersion(isBetta)).thenReturn(getLastVersion(isBetta));
        when(clientVersionRepository.getAllAllowedVersions()).thenReturn(getAllClientVersionList());

        ClientVersionService clientVersionService = new ClientVersionService();
        clientVersionService.setRepository(clientVersionRepository);
        apiController.setClientVersionService(clientVersionService);
    }

    protected void setUpActivateRequestRepository() {
        ActivateRequestRepository activateRequestRepository = mock(ActivateRequestRepository.class);
        // do not need to log
        when(activateRequestRepository.create(any(ActivateRequest.class))).thenReturn(null);
        ActivateRequestService activateRequestService = new ActivateRequestService();
        activateRequestService.setRepository(activateRequestRepository);
        apiController.setActivateRequestService(activateRequestService);
    }

}
