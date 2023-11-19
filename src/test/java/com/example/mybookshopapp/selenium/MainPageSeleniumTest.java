package com.example.mybookshopapp.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MainPageSeleniumTest {

    private static FirefoxDriver driver;

    @BeforeAll
    static void setup() {
        System.setProperty("webdriver.gecko.driver", "D:\\Personal projects\\JavaSpring\\Code\\spring-book-shop-app\\geckodriver.exe");
        driver = new FirefoxDriver();
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }

    @Test
    void PagesAccessTest() throws InterruptedException {

        // Access main page
        MainPageSelenium mainPage = new MainPageSelenium(driver);
        mainPage
                .callMainPage()
                .pause();
        assertTrue(driver.getPageSource().contains("BOOKSHOP"));

        // Access first book page on main page
        String bookTitle = driver.findElements(By.className("Card-title")).get(0).getText();
        driver.findElements(By.className("Card-picture")).get(0).click();
        mainPage.pause();
        assertTrue(driver.getPageSource().contains(bookTitle));
        driver.navigate().back();

        // Access genres page
        mainPage
                .goToGenresPage()
                .pause();
        assertTrue(driver.getPageSource().contains("Genres"));

        // Access first genre page on genres page
        WebElement tag = driver.findElements(By.className("Tag")).get(0);
        String tagTitle = tag.findElement(By.tagName("span")).getText();
        tag.click();
        mainPage.pause();
        assertTrue(driver.getPageSource().contains(tagTitle));
        driver.navigate().back();

        // Access news page
        mainPage
                .goToNewsPage()
                .pause();
        assertTrue(driver.getPageSource().contains("News"));

        WebElement fromDate = driver.findElement(By.id("fromdaterecent"));
        fromDate.clear();
        fromDate.sendKeys("01.01.2023");
        mainPage.pause();

        // Access first book page on news page
        bookTitle = driver.findElements(By.className("Card-title")).get(0).getText();
        driver.findElements(By.className("Card-picture")).get(0).click();
        mainPage.pause();
        assertTrue(driver.getPageSource().contains(bookTitle));
        driver.navigate().back();

        // Access popular page
        mainPage
                .goToPopularPage()
                .pause();
        assertTrue(driver.getPageSource().contains("Popular"));

        // Access first book page on popular page
        bookTitle = driver.findElements(By.className("Card-title")).get(0).getText();
        driver.findElements(By.className("Card-picture")).get(0).click();
        mainPage.pause();
        assertTrue(driver.getPageSource().contains(bookTitle));
        driver.navigate().back();

        // Access authors page
        mainPage
                .goToAuthorsPage()
                .pause();
        assertTrue(driver.getPageSource().contains("Authors"));

        // Access first author page on authors page
        WebElement author = driver.findElements(By.className("Authors-item")).get(0);
        String[] authorNames = author.getText().split(" ");
        author.findElement(By.tagName("a")).click();
        mainPage.pause();
        for (String name : authorNames) {
            assertTrue(driver.getPageSource().contains(name));
        }
    }
}