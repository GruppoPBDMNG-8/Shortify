package junitTests;

import exceptions.BadWordException;
import exceptions.BlankUrlException;
import exceptions.CustomUrlUnavailableException;
import exceptions.MaxAttemptsException;
import logic.Services;
import logic.URLServices;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Giuseppe on 18/03/2016.
 */
public class URLServicesTest {

    String longURL = "http://www.testinglongurljunit.ju";
    String hashedShortURL = "e70e9";
    String ip = "80.80.80.80";
    String userAgent = "Chrome";


    //execute before class
    @BeforeClass
    public static void beforeClass() {
        System.out.println("----- Testing logic.URLServices.class -----");
    }


    //execute for each test, before executing test
    @Before
    public void before() {
        Services.deleteURL(hashedShortURL);
    }

    //execute for each test, after executing test
    @After
    public void after() {
        Services.deleteURL(hashedShortURL);
    }

    @Test
    public void testCreateShortURL() throws Exception {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = URLServices.createShortURL(longURL);
        String expectedResult = hashedShortURL;
        assertEquals(expectedResult,result);
    }

    @Test
    public void testCreateShortURL_alreadyExistent() throws Exception, MaxAttemptsException, CustomUrlUnavailableException,
            BadWordException, BlankUrlException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = Services.setShortURL(longURL,"",ip,userAgent);
        result = URLServices.createShortURL(longURL);
        String expectedResult = hashedShortURL;
        assertEquals(expectedResult,result);
    }

}