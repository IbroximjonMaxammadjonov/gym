package com.ibroximjon.trainingsummaryservice;


import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "com.ibroximjon.integration",
        plugin = {"pretty"}
)
public class IntegrationTest {
}
