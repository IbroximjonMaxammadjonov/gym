Feature: Trainer summary calculation

  Scenario: Add training duration to a new trainer
    Given trainer "trainer1" has no summary
    When a training event with 3 hours in June 2025 is received
    Then trainer "trainer1" should have 3 hours for June 2025

  Scenario: Add multiple durations to same month
    Given trainer "trainer2" has summary with 2 hours in June 2025
    When a training event with 4 hours in June 2025 is received
    Then trainer "trainer2" should have 6 hours for June 2025
