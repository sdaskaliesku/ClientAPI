package com.client.controller.api;

import com.client.domain.db.AccessList;
import com.client.domain.db.ActivateRequest;
import com.client.domain.enums.AccessType;
import com.client.domain.responses.ActivateResponse;
import com.client.service.BlackListService;
import com.client.service.ClientVersionService;
import com.client.utils.DateUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

/**
 * @author sdaskaliesku
 */
@RunWith(MockitoJUnitRunner.class)
public class AccessListApiControllerTest extends ApiControllerTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private
    ClientVersionService clientVersionService;

    @Mock
    private
    BlackListService blackListService;

    @Before
    public void setUp() throws Exception {
        apiController = new AccessListApiController();
        when(request.getHeader("X-FORWARDED-FOR")).thenReturn("headerIp");
        when(request.getRemoteAddr()).thenReturn("remoteIp");
        allAccessList = new ArrayList<>();
        setUpActivateRequestRepository();
        setUpAccessListRepository();
        apiController.setClientVersionService(clientVersionService);
        apiController.setBlackListService(blackListService);
    }

    private void mockIsClientVersionBanned(double version, boolean result) {
        when(clientVersionService.isVersionBanned(version)).thenReturn(result);
    }

    private void mockIsUserInBlackList(String nickname, boolean result) {
        when(blackListService.isUserInBlackList(nickname)).thenReturn(result);
    }

    private void mockIsClanInBlackList(String clan, boolean result) {
        when(blackListService.isClanInBlackList(clan)).thenReturn(result);
    }

    @Test
    public void testSingleActivateBasicNoDenied() throws Exception {
        ActivateRequest activateRequest;
        ActivateResponse response;

        allAccessList = new ArrayList<>();
        AccessList accessList = new AccessList();
        accessList.setAccessType(AccessType.Basic);
        accessList.setFromDate(DateUtils.addMonth(-2));
        accessList.setDueDate(DateUtils.addMonth(2));
        accessList.setClan(false);
        accessList.setName("manson");

        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("1");
        accessList.setFromDate(DateUtils.addMonth(-5));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("2");
        accessList.setFromDate(DateUtils.addMonth(-1));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("3");
        accessList.setFromDate(DateUtils.addMonth(-6));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);

        String nickName = "manson";
        String clanName = "pn";

        mockGetAccessByClanOrUserName(clanName, nickName);
        mockIsUserInBlackList(nickName, false);
        mockIsClanInBlackList(clanName, false);
        setUpAccessListRepository();
        activateRequest = getTestActivateRequest(clanName, nickName);
        response = getActivateResponse(activateRequest, request);
        assertNotNull(response);
        assertEquals(AccessType.Basic, response.getAccessType());
    }

    @Test
    public void testActivateClanBasicUserPro() throws Exception {
        ActivateRequest activateRequest;
        ActivateResponse response;
        Date lastDate = DateUtils.addMonth(10);


        allAccessList = new ArrayList<>();
        AccessList accessList = new AccessList();
        accessList.setAccessType(AccessType.Pro);
        accessList.setFromDate(DateUtils.addMonth(-2));
        accessList.setDueDate(DateUtils.addMonth(2));
        accessList.setClan(false);
        accessList.setName("manson");

        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("1");
        accessList.setFromDate(DateUtils.addMonth(-5));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("2");
        accessList.setFromDate(DateUtils.addMonth(-1));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("3");
        accessList.setFromDate(DateUtils.addMonth(-6));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setAccessType(AccessType.Basic);
        accessList.setFromDate(DateUtils.addMonth(-2));
        accessList.setDueDate(lastDate);
        accessList.setClan(true);
        accessList.setName("pn");

        allAccessList.add(accessList);

        String nickName = "manson";
        String clanName = "pn";

        mockGetAccessByClanOrUserName(clanName, nickName);
        mockIsUserInBlackList(nickName, false);
        mockIsClanInBlackList(clanName, false);
        setUpAccessListRepository();
        activateRequest = getTestActivateRequest(clanName, nickName);
        response = getActivateResponse(activateRequest, request);
        assertNotNull(response);
        assertEquals(AccessType.Pro, response.getAccessType());
        assertEquals(response.getAccessEndDate(), lastDate);

    }

    @Test
    public void testUserBasicClanDenied() throws Exception {
        ActivateRequest activateRequest;
        ActivateResponse response;

        allAccessList = new ArrayList<>();
        AccessList accessList = new AccessList();
        accessList.setAccessType(AccessType.Basic);
        accessList.setFromDate(DateUtils.addMonth(-2));
        accessList.setDueDate(DateUtils.addMonth(2));
        accessList.setClan(false);
        accessList.setName("manson");

        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("1");
        accessList.setFromDate(DateUtils.addMonth(-5));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("2");
        accessList.setFromDate(DateUtils.addMonth(-1));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("3");
        accessList.setFromDate(DateUtils.addMonth(-6));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);

        String nickName = "manson";
        String clanName = "pn";

        mockGetAccessByClanOrUserName(clanName, nickName);
        mockIsUserInBlackList(nickName, true);
        mockIsClanInBlackList(clanName, false);
        setUpAccessListRepository();
        activateRequest = getTestActivateRequest(clanName, nickName);
        response = getActivateResponse(activateRequest, request);
        assertNotNull(response);
        assertEquals(AccessType.Denied, response.getAccessType());
    }

    @Test
    public void testSingleActivateClanDenied() throws Exception {
        ActivateRequest activateRequest;
        ActivateResponse response;

        allAccessList = new ArrayList<>();
        AccessList accessList = new AccessList();
        accessList.setAccessType(AccessType.Basic);
        accessList.setFromDate(DateUtils.addMonth(-2));
        accessList.setDueDate(DateUtils.addMonth(2));
        accessList.setClan(false);
        accessList.setName("manson");

        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("1");
        accessList.setFromDate(DateUtils.addMonth(-5));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("2");
        accessList.setFromDate(DateUtils.addMonth(-1));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("3");
        accessList.setFromDate(DateUtils.addMonth(-6));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setAccessType(AccessType.Basic);
        accessList.setFromDate(DateUtils.addMonth(-2));
        accessList.setDueDate(DateUtils.addMonth(2));
        accessList.setClan(true);
        accessList.setName("pn");

        allAccessList.add(accessList);

        String nickName = "manson";
        String clanName = "pn";

        mockGetAccessByClanOrUserName(clanName, nickName);
        mockIsUserInBlackList(nickName, false);
        mockIsClanInBlackList(clanName, true);
        setUpAccessListRepository();
        activateRequest = getTestActivateRequest(clanName, nickName);
        response = getActivateResponse(activateRequest, request);
        assertNotNull(response);
        assertEquals(AccessType.Denied, response.getAccessType());
    }

    @Test
    public void testNoUserOrClanInAccessList() throws Exception {
        ActivateRequest activateRequest;
        ActivateResponse response;

        allAccessList = new ArrayList<>();
        AccessList accessList = new AccessList();
        accessList.setAccessType(AccessType.Basic);
        accessList.setFromDate(DateUtils.addMonth(-2));
        accessList.setDueDate(DateUtils.addMonth(2));
        accessList.setClan(false);
        accessList.setName("manson");

        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("1");
        accessList.setFromDate(DateUtils.addMonth(-5));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("2");
        accessList.setFromDate(DateUtils.addMonth(-1));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("3");
        accessList.setFromDate(DateUtils.addMonth(-6));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setAccessType(AccessType.Basic);
        accessList.setFromDate(DateUtils.addMonth(-2));
        accessList.setDueDate(DateUtils.addMonth(2));
        accessList.setClan(true);
        accessList.setName("pn");

        allAccessList.add(accessList);

        String nickName = "4234";
        String clanName = "23423423";

        mockGetAccessByClanOrUserName(clanName, nickName);
        mockIsUserInBlackList(nickName, false);
        mockIsClanInBlackList(clanName, false);
        setUpAccessListRepository();
        activateRequest = getTestActivateRequest(clanName, nickName);
        response = getActivateResponse(activateRequest, request);
        assertNotNull(response);
        assertEquals(response.getAccessType(), AccessType.NoAccess);
    }


    @Test
    public void testNoUserOrClanInAccessListAllowTestPeriod() throws Exception {
        ActivateRequest activateRequest;
        ActivateResponse response;

        allAccessList = new ArrayList<>();
        AccessList accessList = new AccessList();
        accessList.setAccessType(AccessType.Basic);
        accessList.setFromDate(DateUtils.addMonth(-2));
        accessList.setDueDate(DateUtils.addMonth(2));
        accessList.setClan(false);
        accessList.setName("manson");

        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("1");
        accessList.setFromDate(DateUtils.addMonth(-5));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("2");
        accessList.setFromDate(DateUtils.addMonth(-1));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setName("3");
        accessList.setFromDate(DateUtils.addMonth(-6));
        accessList.setDueDate(DateUtils.addMonth(5));
        allAccessList.add(accessList);
        accessList = new AccessList();
        accessList.setAccessType(AccessType.Basic);
        accessList.setFromDate(DateUtils.addMonth(-2));
        accessList.setDueDate(DateUtils.addMonth(2));
        accessList.setClan(true);
        accessList.setName("pn");
        allAccessList.add(accessList);

        String nickName = "4234";
        String clanName = "23423423";

        mockGetAccessByClanOrUserName(clanName, nickName);
        mockIsUserInBlackList(nickName, false);
        mockIsClanInBlackList(clanName, false);
        setUpAccessListRepository();

        apiController.setAllowFreeFirstTimeUsage(true);
        Date lastDate = DateUtils.addDay(apiController.getFreeDaysPeriod());

        activateRequest = getTestActivateRequest(clanName, nickName);
        response = getActivateResponse(activateRequest, request);
        assertNotNull(response);
        assertEquals(AccessType.Basic, response.getAccessType());
        String endDate = ApiController.getDateFormat().format(response.getAccessEndDate());
        String expectedEndDate = ApiController.getDateFormat().format(lastDate);
        assertEquals(expectedEndDate, endDate);
    }

    private ActivateRequest getTestActivateRequest(String clanName, String nickName) {
        double version = 1.0;
        ActivateRequest activateRequest = new ActivateRequest();
        activateRequest.setClanName(clanName);
        activateRequest.setNickName(nickName);
        activateRequest.setClientVersion(version);
        mockIsClientVersionBanned(version, false);
        return activateRequest;
    }


    private ActivateResponse getActivateResponse(ActivateRequest activateRequest, HttpServletRequest httpServletRequest) throws Exception {
        return (ActivateResponse) apiController.activate(activateRequest, httpServletRequest);
    }
}