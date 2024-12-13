Feature: Sim card activation

  Scenario: Successful SIM card activation
    Given the actuator service is running
    When I send an activation request with ICCID "1255789453849037777" and customer email "success@example.com"
    Then the response should indicate success
    And the database should record the transaction with ICCID "1255789453849037777", customer email "success@example.com", and active true

  Scenario: Failed SIM card activation
    Given the actuator service is running
    When I send an activation request with ICCID "8944500102198304826" and customer email "fail@example.com"
    Then the response should indicate failure
    And the database should record the transaction with ICCID "8944500102198304826", customer email "fail@example.com", and active false
