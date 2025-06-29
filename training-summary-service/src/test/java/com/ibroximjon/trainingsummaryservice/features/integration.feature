Feature: Integration between management and summary services

  Scenario: Training event is sent and saved
    Given training summary does not exist for "trainerX"
    When training-management-service sends training event for "trainerX"
    Then training-summary-service should store 3 hours for "trainerX" in June 2025
