package com.client.controller.api;

import com.client.domain.UpdateRequest;
import com.client.domain.db.ClientVersion;
import com.client.domain.enums.UpdatePolicy;
import com.client.domain.enums.VersionCheckResult;
import com.client.domain.responses.Response;
import com.client.domain.responses.UpdateResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author sdaskaliesku
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientVersionCheck extends ApiControllerTest {
    protected static UpdateRequest getTestUpdateRequest(double userVersion) {
        return new UpdateRequest(userVersion);
    }

    /*
     * input data: userVersion, isBetta, updateToBeta
     * Test cases:
     * 1. isBetta = true, updateToBeta = true
     *   a. userVersion is banned, required update
     *   b. userVersion is not banned, update is optional
     *   c. userVersion is upToDate, no update
     *  Expected return data:
     *   a. VersionCheckResult = Required
     *   b. VersionCheckResult = Optional
     *   c. VersionCheckResult
     * 2. isBetta = true, updateToBeta = false
     *   a. VersionCheckResult = Required
     *   b. VersionCheckResult = Optional
     *   c. VersionCheckResult = UpToDate
     * 3. isBetta = false, updateToBeta = true
     *   a. VersionCheckResult = Required
     *   b. VersionCheckResult = Optional
     *   c. VersionCheckResult = UpToDate
     * 4. isBetta = false, updateToBeta = false
     *   a. VersionCheckResult = Required
     *   b. VersionCheckResult = Optional
     *   c. VersionCheckResult = UpToDate
     * */
    protected ClientVersion getTestClientVersion(Double version, UpdatePolicy updatePolicy, Boolean banned, String link) {
        return new ClientVersion(version, updatePolicy, banned, "", link);
    }

    @Before
    public void setUp() throws Exception {
        allClientVersionList = new ArrayList<>();
        apiController = new ClientVersionApiController();
    }

    private void assertUpdateResponse(UpdateResponse response, VersionCheckResult expectedVersionCheckResult, double expectedVersion) {
        assertNotNull(response);
        assertEquals(expectedVersionCheckResult, response.getVersionCheckResult());
        assertNotNull(response.getUrl());
        assertEquals(expectedVersion, response.getVersion(), 0.0);
    }

    @Test
    public void testBannedVersion() throws Exception {
        List<String> list = new ArrayList<>();
        list.add(null);
        Double userVersion = 1.0;
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, false, "1b"));
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, true, "1c"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Required, false, "2"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, false, "4"));
        setUpClientVersionRepository();

        mockGetClientVersion(userVersion);
        mockGetCurrentVersion(userVersion);
        UpdateResponse response = getActualUpdateResponse(userVersion);
        // current version is banned
        assertUpdateResponse(response, VersionCheckResult.Required, 4.0);
    }

    @Test
    public void testBannedVersionWithNoRequiredUpdate() throws Exception {
        Double userVersion = 1.0;
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, false, "1b"));
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, true, "1c"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, "2"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Required, false, "4"));
        setUpClientVersionRepository();

        mockGetClientVersion(userVersion);
        mockGetCurrentVersion(userVersion);
        UpdateResponse response = getActualUpdateResponse(userVersion);
        // current version is banned
        assertUpdateResponse(response, VersionCheckResult.Required, 4.0);
    }

    @Test
    public void testBannedVersionWithNoRequiredUpdateNoBetta() throws Exception {
        Double userVersion = 1.0;
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, true, "1c"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, "2"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, false, "4"));
        setUpClientVersionRepository();

        mockGetClientVersion(userVersion);
        mockGetCurrentVersion(userVersion);
        UpdateResponse response = getActualUpdateResponse(userVersion);
        // current version is banned
        assertUpdateResponse(response, VersionCheckResult.Required, 4.0);
    }

    @Test
    public void testOldVersionWithNewRequiredUpdateAllowBetta() throws Exception {
        Double userVersion = 1.0;
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, false, "1b"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Required, false, "2"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, false, "4"));
        setUpClientVersionRepository();

        mockGetClientVersion(userVersion);
        mockGetCurrentVersion(userVersion);
        UpdateResponse response = getActualUpdateResponse(userVersion);
        assertUpdateResponse(response, VersionCheckResult.Required, 4.0);
    }

    @Test
    public void testOldVersionWithNewRequiredUpdateOnlyStable() throws Exception {
        Double userVersion = 1.0;
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, false, "1b"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Required, false, "2"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, false, "4"));
        setUpClientVersionRepository();

        mockGetClientVersion(userVersion);
        mockGetCurrentVersion(userVersion);
        UpdateResponse response = getActualUpdateResponse(userVersion);
        assertUpdateResponse(response, VersionCheckResult.Required, 4.0);
    }

    @Test
    public void testUpToDateCheck() throws Exception {
        Double userVersion = 4.0;
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, false, "1b"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, false, "4"));
        setUpClientVersionRepository();

        mockGetClientVersion(userVersion);
        mockGetCurrentVersion(userVersion);
        UpdateResponse response = getActualUpdateResponse(userVersion);
        assertUpdateResponse(response, VersionCheckResult.UpToDate, 4.0);
    }

    @Test
    public void testUpToDateBettaCheck() throws Exception {

        Double userVersion = 4.0;
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, false, "1b"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, false, "4"));
        setUpClientVersionRepository();

        mockGetClientVersion(userVersion);
        mockGetCurrentVersion(userVersion);
        UpdateResponse response = getActualUpdateResponse(userVersion);
        assertUpdateResponse(response, VersionCheckResult.UpToDate, 4.0);
    }

    @Test
    public void testGetLastVersion() throws Exception {
        ClientVersion lastStableVersion = getTestClientVersion(4.0, UpdatePolicy.Optional, false, "4");
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, false, "1b"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, "3"));
        allClientVersionList.add(lastStableVersion);
        setUpClientVersionRepository();
        Response actualResponse = apiController.getLastVersion();
        Response expectedResponse = new Response();
        expectedResponse.setRecord(lastStableVersion);
        assertEquals(expectedResponse.toString(), actualResponse.toString());

        actualResponse = apiController.getLastVersion();
        expectedResponse = new Response();
        expectedResponse.setRecord(lastStableVersion);
        assertEquals(expectedResponse.toString(), actualResponse.toString());
    }

    @Test
    public void testGetVersion() throws Exception {
        double version = 1.0;
        ClientVersion expectedStableVersion = getTestClientVersion(1.0, UpdatePolicy.Optional, false, "1b");

        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(expectedStableVersion);
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, false, "4"));
        setUpClientVersionRepository();

//        mockGetClientVersion(version);
        Response actualResponse = apiController.getVersion(version);
//        assertNotNull(actualResponse);
//        assertNotNull(actualResponse.getRecord());
//        assertEquals(actualResponse.getMessage(), "Version: " + version + " does not exists. Try again.");

        mockGetClientVersion(version);
        actualResponse = apiController.getVersion(version);
        assertNotNull(actualResponse);
        assertEquals(expectedStableVersion.toString(), actualResponse.getRecord().toString());

        version = 2.0;

        expectedStableVersion = getTestClientVersion(version, UpdatePolicy.Optional, false, "2b");
        mockGetClientVersion(version);
        actualResponse = apiController.getVersion(version);
        assertNotNull(actualResponse);
        assertEquals(expectedStableVersion.toString(), actualResponse.getRecord().toString());

        version = 2.0;
        mockGetClientVersion(version);
        actualResponse = apiController.getVersion(version);
        assertNotNull(actualResponse);
        assertEquals(expectedStableVersion.toString(), actualResponse.getRecord().toString());

    }

    @Test
    public void testGetAllVersions() throws Exception {
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, false, "4"));

        setUpClientVersionRepository();
        Response response = apiController.getAllVersions();
        assertNotNull(response);
        assertNotNull(response.getRecords());
        assertEquals(allClientVersionList, response.getRecords());
    }

    protected UpdateResponse getActualUpdateResponse(double userVersion) throws Exception {
        return (UpdateResponse) apiController.checkForUpdates(getTestUpdateRequest(userVersion));
    }

}