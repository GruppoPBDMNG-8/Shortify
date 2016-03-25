package junitTests;

import dao.RedisDAO;
import exceptions.*;
import logic.Services;
import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

import static org.junit.Assert.*;

/**
 * Created by Giuseppe on 18/03/2016.
 */
public class ServicesTest {

    String longURL = "http://www.testinglongurljunit.ju";
    String hashedShortURL = "e70e9";
    String customURL = "testlink";
    String statisticsURL = customURL + "+";
    String ip = "80.80.80.80";
    String userAgent = "Chrome";
    String badword = "cunt";

    //execute before class
    @BeforeClass
    public static void beforeClass() {
        System.out.println("----- Testing logic.Services.class -----");
    }


    //execute for each test, before executing test
    @Before
    public void before() {
        Services.deleteURL(hashedShortURL);
        Services.deleteURL(customURL);
        Services.deleteURL(badword);
    }

    //execute for each test, after executing test
    @After
    public void after() {
        Services.deleteURL(hashedShortURL);
        Services.deleteURL(customURL);
        Services.deleteURL(badword);
    }


    /**
     * Tests if given a longURL the function returns the correct shortURL in Json string format after memorizing it in the db
     * @throws Exception
     * @throws MaxAttemptsException
     * @throws CustomUrlUnavailableException
     * @throws BadWordException
     * @throws BlankUrlException
     */
    @Test
    public void testSetShortURL() throws Exception, MaxAttemptsException, CustomUrlUnavailableException, BadWordException, BlankUrlException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = Services.setShortURL(longURL, "", ip, userAgent);
        String expectedResult = "{\"shortURL\":\"" + hashedShortURL + "\"}";
        assertEquals(expectedResult,result);
    }

    /**
     * Tests if given a longURL and a customURL the function returns back the customURL in Json string format after memorizing it in the db
     * @throws Exception
     * @throws MaxAttemptsException
     * @throws CustomUrlUnavailableException
     * @throws BadWordException
     * @throws BlankUrlException
     */
    @Test
    public void testSetShortURL_customURL() throws Exception, MaxAttemptsException, CustomUrlUnavailableException, BadWordException, BlankUrlException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = Services.setShortURL(longURL, customURL, ip, userAgent);
        String expectedResult = "{\"shortURL\":\""+ customURL + "\"}";
        assertEquals(expectedResult,result);
    }

    /**
     * Tests if given a customURL already present in the database the function returns the correct exception.
     * @throws Exception
     * @throws MaxAttemptsException
     * @throws CustomUrlUnavailableException
     * @throws BadWordException
     * @throws BlankUrlException
     */
    @Test (expected = CustomUrlUnavailableException.class)
    public void testSetShortURL_CustomUrlUnavailable() throws Exception, MaxAttemptsException, CustomUrlUnavailableException, BadWordException, BlankUrlException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = Services.setShortURL(longURL, customURL, ip, userAgent);
        result = Services.setShortURL(longURL, customURL, ip, userAgent);
    }

    /**
     * Tests if given a forbidden customURL the function returns the correct exception.
     * @throws Exception
     * @throws MaxAttemptsException
     * @throws CustomUrlUnavailableException
     * @throws BadWordException
     * @throws BlankUrlException
     */
    @Test (expected = BadWordException.class)
    public void testSetShortURL_BadWord() throws Exception, MaxAttemptsException, CustomUrlUnavailableException, BadWordException, BlankUrlException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = Services.setShortURL(longURL, badword, ip, userAgent);
    }

    /**
     * Tests if given an empty longURL the function returns the correct exception.
     * @throws Exception
     * @throws MaxAttemptsException
     * @throws CustomUrlUnavailableException
     * @throws BadWordException
     * @throws BlankUrlException
     */
    @Test (expected = BlankUrlException.class)
    public void testSetShortURL_BlankUrl() throws Exception, MaxAttemptsException, CustomUrlUnavailableException, BadWordException, BlankUrlException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = Services.setShortURL("", "", ip, userAgent);
    }

    /**
     * Tests if given a shortURL the function fetch the correct related longURL from the database and returns it in Json string format.
     * @throws Exception
     * @throws EmptyClicksException
     * @throws UrlNotPresentException
     */
    @Test
    public void testRedirectURL_getLongURL() throws Exception, EmptyClicksException, UrlNotPresentException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        RedisDAO.setString(customURL, longURL);

        String result = Services.redirectURL(customURL,ip,userAgent);
        String expectedResult = "{\"redirectURL\":\"" + longURL + "\"}";
        assertEquals(expectedResult,result);
    }

    /**
     * Tests if given a shortURL with + at the end, the function fetch the clicks from the database, calculates the statistics and returns
     * them
     * @throws Exception
     * @throws EmptyClicksException
     * @throws UrlNotPresentException
     * @throws MaxAttemptsException
     * @throws CustomUrlUnavailableException
     * @throws BadWordException
     * @throws BlankUrlException
     */
    @Test
    public void testRedirectURL_getStatistics() throws Exception, EmptyClicksException, UrlNotPresentException,
            MaxAttemptsException, CustomUrlUnavailableException, BadWordException, BlankUrlException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        Services.setShortURL(longURL,customURL,ip,userAgent);
        String result = Services.redirectURL(statisticsURL,ip,userAgent);
        assertNotNull(result);
    }

    /**
     * Tests if given a shortURL not present in the database the function returns the correct exception.
     * @throws Exception
     * @throws EmptyClicksException
     * @throws UrlNotPresentException
     */
    @Test (expected = UrlNotPresentException.class)
    public void testRedirectURL_UrlNotPresent() throws Exception, EmptyClicksException, UrlNotPresentException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = Services.redirectURL(customURL,ip,userAgent);
    }

    /**
     * Tests if given a shortURL with no clicks associated the function returns the correct exception.
     * NOTE: This is an error because a memorized shortURL must always have at least one click (information about
     * the user who created the shortURL)
     * @throws Exception
     * @throws EmptyClicksException
     * @throws UrlNotPresentException
     */
    @Test (expected = EmptyClicksException.class)
    public void testRedirectURL_EmptyClicks() throws Exception, EmptyClicksException, UrlNotPresentException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        RedisDAO.setString(customURL, longURL);
        String result = Services.redirectURL(statisticsURL,ip,userAgent);
    }


}