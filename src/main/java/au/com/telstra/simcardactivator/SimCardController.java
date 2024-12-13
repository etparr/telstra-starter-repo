package au.com.telstra.simcardactivator;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class SimCardController {

    private static final String ACTUATOR_URL = "http://localhost:8444/actuate";

    static class SimCardRequest {
        private String iccid;
        private String customerEmail;

        public String getIccid() {
            return iccid;
        }

        public void setIccid(String iccid) {
            this.iccid = iccid;
        }

        public String getCustomerEmail() {
            return customerEmail;
        }

        public void setCustomerEmail(String customerEmail) {
            this.customerEmail = customerEmail;
        }
    }

    static class ActuatorRequest {
        private String iccid;

        public ActuatorRequest(String iccid) {
            this.iccid = iccid;
        }

        // Getter
        public String getIccid() {
            return iccid;
        }
    }

    static class ActuatorResponse {
        private boolean success;

        // Getter
        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<String> activateSimCard(@RequestBody SimCardRequest request) {
        System.out.println("Received request: " + request);

        ActuatorRequest actuatorRequest = new ActuatorRequest(request.getIccid());

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<ActuatorResponse> response = restTemplate.postForEntity(
                    ACTUATOR_URL,
                    actuatorRequest,
                    ActuatorResponse.class
            );

            if (response.getBody() != null && response.getBody().isSuccess()) {
                System.out.println("SIM card activation successful for ICCID: " + request.getIccid());
                return ResponseEntity.ok("SIM card activation successful");
            } else {
                System.out.println("SIM card activation failed for ICCID: " + request.getIccid());
                return ResponseEntity.status(500).body("SIM card activation failed");
            }
        } catch (Exception e) {
            System.err.println("Error communicating with actuator: " + e.getMessage());
            return ResponseEntity.status(500).body("Error communicating with actuator");
        }
    }
    @GetMapping("/query")
    public ResponseEntity<?> querySimCard(@RequestParam long simCardId) {
        SimCardTransaction transaction = simCardService.getTransactionById(simCardId);
        if (transaction != null) {
            return ResponseEntity.ok(transaction);
        } else {
            return ResponseEntity.status(404).body("Transaction not found");
        }
    }
}

}
