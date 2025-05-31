Feature: End-to-End API Testing for Object Management

  Background:
    Given the API is available
    And I login with email "drajattt@gmail.com" and password "admin123!"

  Scenario: Add a new object and verify its details
    When I add a new object with the following data:
      | name              | Lenovo ThinkBook 7i |
      | year              | 2023                |
      | price             | 1849.99             |
      | cpuModel          | Intel Core i7       |
      | hardDiskSize      | 1 TB                |
      | color             | silver              |
      | capacity          | 2 cpu               |
      | screenSize        | 14 Inch             |
    Then the response status code should be 200
    And the object should be present in the object list
    And I can retrieve the object details and verify it

  Scenario: Delete the object and verify it is removed
    When I delete the object
    Then the response status code should be 200
    And retrieving the object should return an empty response
