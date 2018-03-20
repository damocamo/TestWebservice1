package cucumber.launchers;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(glue = {"cucumber.stepDefinitions"}, features = {"src/main/resources/features/WebTest"}, tags = {
        "@Login,@Logout,@PlaceOrder,@UpdateOrder,@RemoveOrder"})
public class RunFeatures {
}