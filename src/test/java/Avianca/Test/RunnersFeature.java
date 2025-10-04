package Avianca.Test;

import org.junit.runner.RunWith;
import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;


@RunWith(CucumberWithSerenity.class)
@CucumberOptions(features = { "src/test/resources/features" }, glue = { "Avianca.Definitions" }, tags ="@HU002")

public class RunnersFeature {

}