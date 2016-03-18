package junitTests;

import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;


/**
 * Created by Giuseppe on 18/03/2016.
 */
public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(ServicesTest.class);
        for (Failure failure : result.getFailures()) {
            System.out.println("FAILURE: " + failure.toString());
        }
        Result result2 = JUnitCore.runClasses(URLServicesTest.class);
        for (Failure failure : result2.getFailures()) {
            System.out.println("FAILURE: " + failure.toString());
        }
        System.out.println("Tests completed - All tests passed: "+(result.wasSuccessful() && result2.wasSuccessful()));
    }
}