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


    @Test
    public void testSetShortURL() throws Exception, MaxAttemptsException, CustomUrlUnavailableException, BadWordException, BlankUrlException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = Services.setShortURL(longURL, "", ip, userAgent);
        String expectedResult = "{\"shortURL\":\"" + hashedShortURL + "\"}";
        assertEquals(expectedResult,result);
    }

    @Test
    public void testSetShortURL_customURL() throws Exception, MaxAttemptsException, CustomUrlUnavailableException, BadWordException, BlankUrlException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = Services.setShortURL(longURL, customURL, ip, userAgent);
        String expectedResult = "{\"shortURL\":\""+ customURL + "\"}";
        assertEquals(expectedResult,result);
    }

    @Test (expected = CustomUrlUnavailableException.class)
    public void testSetShortURL_CustomUrlUnavailable() throws Exception, MaxAttemptsException, CustomUrlUnavailableException, BadWordException, BlankUrlException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = Services.setShortURL(longURL, customURL, ip, userAgent);
        result = Services.setShortURL(longURL, customURL, ip, userAgent);
    }

    @Test (expected = BadWordException.class)
    public void testSetShortURL_BadWord() throws Exception, MaxAttemptsException, CustomUrlUnavailableException, BadWordException, BlankUrlException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = Services.setShortURL(longURL, badword, ip, userAgent);
    }

    @Test (expected = BlankUrlException.class)
    public void testSetShortURL_BlankUrl() throws Exception, MaxAttemptsException, CustomUrlUnavailableException, BadWordException, BlankUrlException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = Services.setShortURL("", "", ip, userAgent);
    }



    @Test
    public void testRedirectURL_getLongURL() throws Exception, EmptyClicksException, UrlNotPresentException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        RedisDAO.setString(customURL, longURL);

        String result = Services.redirectURL(customURL,ip,userAgent);
        String expectedResult = "{\"redirectURL\":\"" + longURL + "\"}";
        assertEquals(expectedResult,result);
    }

    @Test
    public void testRedirectURL_getStatistics() throws Exception, EmptyClicksException, UrlNotPresentException,
            MaxAttemptsException, CustomUrlUnavailableException, BadWordException, BlankUrlException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        Services.setShortURL(longURL,customURL,ip,userAgent);
        String result = Services.redirectURL(statisticsURL,ip,userAgent);
        assertNotNull(result);
    }

    @Test (expected = UrlNotPresentException.class)
    public void testRedirectURL_UrlNotPresent() throws Exception, EmptyClicksException, UrlNotPresentException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = Services.redirectURL(customURL,ip,userAgent);
    }

    @Test (expected = EmptyClicksException.class)
    public void testRedirectURL_EmptyClicks() throws Exception, EmptyClicksException, UrlNotPresentException {
        System.out.println( "Executing " +
                Thread.currentThread().getStackTrace()[1].getMethodName());
        RedisDAO.setString(customURL, longURL);
        String result = Services.redirectURL(statisticsURL,ip,userAgent);
    }


}