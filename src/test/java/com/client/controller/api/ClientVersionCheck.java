package com.client.controller.api;

import com.client.domain.UpdateRequest;
import com.client.domain.db.ClientVersion;
import com.client.domain.enums.VersionCheckResult;
import com.client.domain.enums.UpdatePolicy;
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
import static org.junit.Assert.assertNull;

/**
 * @author sdaskaliesku
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientVersionCheck extends ApiControllerTest {
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
    protected ClientVersion getTestClientVersion(Double version, UpdatePolicy updatePolicy, Boolean betta, Boolean banned, String releaseNotes, String link) {
        return new ClientVersion(version, updatePolicy, betta, banned, releaseNotes, link);
    }

    @Before
    public void setUp() throws Exception {
        allClientVersionList = new ArrayList<>();
        apiController = new ClientVersionApiController();
    }

    private void assertUpdateResponse(UpdateResponse response, VersionCheckResult expectedVersionCheckResult, double expectedVersion) {
        assertUpdateResponse(response, expectedVersionCheckResult, expectedVersion, null);
    }

    private void assertUpdateResponse(UpdateResponse response, VersionCheckResult expectedVersionCheckResult, double expectedVersion, Boolean isBetta) {
        assertNotNull(response);
        assertNotNull(response.getBetta());
        assertEquals(expectedVersionCheckResult, response.getVersionCheckResult());
        assertNotNull(response.getUrl());
        assertEquals(expectedVersion, response.getVersion(), 0.0);
        if (isBetta != null) {
            assertEquals(isBetta, response.getBetta());
        }
    }

    @Test
    public void testBannedVersion() throws Exception {
        List<String> list = new ArrayList<>();
        list.add(null);
        Boolean isBetta = true;
        Boolean updateToBetta = true;
        Double userVersion = 1.0;
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, false, false, "", "1b"));
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, true, true, "", "1c"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Required, true, false, "", "2"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, false, "", "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, false, "", "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, true, false, "", "4"));
        setUpClientVersionRepository();

        mockGetClientVersion(userVersion, isBetta);
        mockGetCurrentVersion(userVersion);
        UpdateResponse response = getActualUpdateResponse(isBetta, updateToBetta, userVersion);
        // current version is banned
        assertUpdateResponse(response, VersionCheckResult.Required, 4.0);
    }

    @Test
    public void testBannedVersionWithNoRequiredUpdate() throws Exception {
        Boolean isBetta = true;
        Boolean updateToBetta = true;
        Double userVersion = 1.0;
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, false, false, "", "1b"));
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, true, true, "", "1c"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, true, false, "", "2"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, false, "", "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, false, "", "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, true, false, "", "4"));
        setUpClientVersionRepository();

        mockGetClientVersion(userVersion, isBetta);
        mockGetCurrentVersion(userVersion);
        UpdateResponse response = getActualUpdateResponse(isBetta, updateToBetta, userVersion);
        // current version is banned
        assertUpdateResponse(response, VersionCheckResult.Required, 4.0);
    }

    @Test
    public void testBannedVersionWithNoRequiredUpdateNoBetta() throws Exception {
        Boolean isBetta = true;
        Boolean updateToBetta = false;
        Double userVersion = 1.0;
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, false, false, "", "1b"));
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, true, true, "", "1c"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, true, false, "", "2"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, false, "", "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, false, "", "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, true, false, "", "4"));
        setUpClientVersionRepository();

        mockGetClientVersion(userVersion, isBetta);
        mockGetCurrentVersion(userVersion);
        UpdateResponse response = getActualUpdateResponse(isBetta, updateToBetta, userVersion);
        // current version is banned
        assertUpdateResponse(response, VersionCheckResult.Required, 3.0);
    }

    @Test
    public void testOldVersionWithNewRequiredUpdateAllowBetta() throws Exception {
        Boolean isBetta = true;
        Boolean updateToBetta = true;
        Double userVersion = 1.0;
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, false, false, "", "1b"));
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, true, false, "", "1c"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Required, true, false, "", "2"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, false, "", "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, false, "", "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, true, false, "", "4"));
        setUpClientVersionRepository();

        mockGetClientVersion(userVersion, isBetta);
        mockGetCurrentVersion(userVersion);
        UpdateResponse response = getActualUpdateResponse(isBetta, updateToBetta, userVersion);
        assertUpdateResponse(response, VersionCheckResult.Required, 4.0);
    }

    @Test
    public void testOldVersionWithNewRequiredUpdateOnlyStable() throws Exception {
        Boolean isBetta = false;
        Boolean updateToBetta = false;
        Double userVersion = 1.0;
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, false, false, "", "1b"));
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, true, false, "", "1c"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Required, false, false, "", "2"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, false, "", "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, false, "", "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, true, false, "", "4"));
        setUpClientVersionRepository();

        mockGetClientVersion(userVersion, isBetta);
        mockGetCurrentVersion(userVersion);
        UpdateResponse response = getActualUpdateResponse(isBetta, updateToBetta, userVersion);
        assertUpdateResponse(response, VersionCheckResult.Required, 3.0);
    }

    @Test
    public void testUpToDateCheck() throws Exception {
        Boolean isBetta = true;
        Boolean updateToBetta = false;
        Double userVersion = 4.0;
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, false, false, "", "1b"));
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, true, false, "", "1c"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Required, false, false, "", "2"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, false, "", "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, false, "", "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, true, false, "", "4"));
        setUpClientVersionRepository();

        mockGetClientVersion(userVersion, isBetta);
        mockGetCurrentVersion(userVersion);
        UpdateResponse response = getActualUpdateResponse(isBetta, updateToBetta, userVersion);
        assertUpdateResponse(response, VersionCheckResult.UpToDateBeta, 4.0);
    }

    @Test
    public void testUpToDateBettaCheck() throws Exception {
        Boolean isBetta = true;
        Boolean updateToBetta = true;
        Double userVersion = 4.0;
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, false, false, "", "1b"));
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, true, false, "", "1c"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Required, false, false, "", "2"));
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, false, "", "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, false, "", "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, true, false, "", "4"));
        setUpClientVersionRepository();

        mockGetClientVersion(userVersion, isBetta);
        mockGetCurrentVersion(userVersion);
        UpdateResponse response = getActualUpdateResponse(isBetta, updateToBetta, userVersion);
        assertUpdateResponse(response, VersionCheckResult.UpToDateBeta, 4.0);
    }

    @Test
    public void testGetLastVersion() throws Exception {
        ClientVersion lastBettaVersion = getTestClientVersion(5.0, UpdatePolicy.Optional, true, false, "", "2");
        ClientVersion lastStableVersion = getTestClientVersion(4.0, UpdatePolicy.Optional, false, false, "", "4");
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, false, false, "", "1b"));
        allClientVersionList.add(getTestClientVersion(1.0, UpdatePolicy.Optional, true, false, "", "1c"));
        allClientVersionList.add(lastBettaVersion);
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, false, "", "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, false, "", "3"));
        allClientVersionList.add(lastStableVersion);
        setUpClientVersionRepository();
        boolean allowBetta = true;
        Response actualResponse = apiController.getLastVersion(allowBetta);
        Response expectedResponse = new Response();
        expectedResponse.setRecord(lastBettaVersion);
        assertEquals(expectedResponse.toString(), actualResponse.toString());

        allowBetta = false;
        actualResponse = apiController.getLastVersion(allowBetta);
        expectedResponse = new Response();
        expectedResponse.setRecord(lastStableVersion);
        assertEquals(expectedResponse.toString(), actualResponse.toString());
    }

    @Test
    public void testGetVersion() throws Exception {
        boolean isBetta = true;
        double version = 1.0;
        ClientVersion expectedBettaVersion = getTestClientVersion(2.0, UpdatePolicy.Optional, true, false, "", "2");
        ClientVersion expectedStableVersion = getTestClientVersion(1.0, UpdatePolicy.Optional, false, false, "", "1b");

        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(expectedStableVersion);
        allClientVersionList.add(expectedBettaVersion);
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, false, "", "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, false, "", "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, true, false, "", "4"));
        setUpClientVersionRepository();

        mockGetClientVersion(version, isBetta);
        Response actualResponse = apiController.getVersion(version, isBetta);
        assertNotNull(actualResponse);
        assertNull(actualResponse.getRecord());
        assertEquals(actualResponse.getMessage(), "Version: " + version + " with type: " + isBetta + " does not exists. Try again.");

        isBetta = false;
        mockGetClientVersion(version, isBetta);
        actualResponse = apiController.getVersion(version, isBetta);
        assertNotNull(actualResponse);
        assertEquals(expectedStableVersion.toString(), actualResponse.getRecord().toString());

        isBetta = false;
        version = 2.0;

        expectedStableVersion = getTestClientVersion(version, UpdatePolicy.Optional, false, false, "", "2b");
        mockGetClientVersion(version, isBetta);
        actualResponse = apiController.getVersion(version, isBetta);
        assertNotNull(actualResponse);
        assertEquals(expectedStableVersion.toString(), actualResponse.getRecord().toString());

        isBetta = true;
        version = 2.0;
        mockGetClientVersion(version, isBetta);
        actualResponse = apiController.getVersion(version, isBetta);
        assertNotNull(actualResponse);
        assertEquals(expectedBettaVersion.toString(), actualResponse.getRecord().toString());

    }

    @Test
    public void testGetAllVersions() throws Exception {
        allClientVersionList = new ArrayList<>();
        allClientVersionList.add(getTestClientVersion(2.0, UpdatePolicy.Optional, false, false, "", "2b"));
        allClientVersionList.add(getTestClientVersion(3.0, UpdatePolicy.Optional, false, false, "", "3"));
        allClientVersionList.add(getTestClientVersion(4.0, UpdatePolicy.Optional, true, false, "", "4"));

        setUpClientVersionRepository();
        Response response = apiController.getAllVersions();
        assertNotNull(response);
        assertNotNull(response.getRecords());
        assertEquals(allClientVersionList, response.getRecords());
    }

    protected static UpdateRequest getTestUpdateRequest(boolean isBetta, boolean updateToBetta, double userVersion) {
        return new UpdateRequest(userVersion, isBetta, updateToBetta);
    }

    protected UpdateResponse getActualUpdateResponse(boolean isBetta, boolean updateToBetta, double userVersion) throws Exception {
        return (UpdateResponse) apiController.checkForUpdates(getTestUpdateRequest(isBetta, updateToBetta, userVersion));
    }

}