package stepDefinitions;

import au.com.telstra.simcardactivator.SimCardTransaction;
import io.cucumber.java.en.*;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class SimCardActivatorStepDefinitions {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String ICCID_SUCCESS = "1255789453849037777";
    private static final String ICCID_FAILURE = "8944500102198304826";
    private ResponseEntity<String> response;
    private long lastInsertedId = 1;

    @Autowired
    private RestTemplate restTemplate;

    @Given("the actuator service is running")
    public void theActuatorServiceIsRunning() {

    }

    @When("I send an activation request with ICCID {string} and customer email {string}")
    public void iSendAnActivationRequestWithICCIDAndCustomerEmail(String iccid, String customerEmail) {
        String requestUrl = BASE_URL + "/activate";
        var requestBody = new SimCardController.SimCardRequest();
        requestBody.setIccid(iccid);
        requestBody.setCustomerEmail(customerEmail);

        response = restTemplate.postForEntity(requestUrl, requestBody, String.class);
    }

    @Then("the response should indicate success")
    public void theResponseShouldIndicateSuccess() {
        Assertions.assertEquals(200, response.getStatusCodeValue());
        Assertions.assertEquals("SIM card activation successful", response.getBody());
    }

    @Then("the response should indicate failure")
    public void theResponseShouldIndicateFailure() {
        Assertions.assertEquals(500, response.getStatusCodeValue());
        Assertions.assertEquals("SIM card activation failed", response.getBody());
    }

    @And("the database should record the transaction with ICCID {string}, customer email {string}, and active {string}")
    public void theDatabaseShouldRecordTheTransactionWithICCIDCustomerEmailAndActive(String iccid, String customerEmail, String active) {
        String queryUrl = BASE_URL + "/query?simCardId=" + lastInsertedId++;
        ResponseEntity<SimCardTransaction> queryResponse = restTemplate.getForEntity(queryUrl, SimCardTransaction.class);

        SimCardTransaction transaction = queryResponse.getBody();
        Assertions.assertNotNull(transaction);
        Assertions.assertEquals(iccid, transaction.getIccid());
        Assertions.assertEquals(customerEmail, transaction.getCustomerEmail());
        Assertions.assertEquals(Boolean.parseBoolean(active), transaction.isActive());
    }
}
