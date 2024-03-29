package com.example.mybookshopapp.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

public class MainPageSelenium {

    private static final String URL = "http://localhost:8085/";
    private final FirefoxDriver driver;

    public MainPageSelenium(FirefoxDriver driver) {
        this.driver = driver;
    }

    public MainPageSelenium callMainPage() {
        driver.get(URL);
        return this;
    }

    public MainPageSelenium pause() throws InterruptedException {
        Thread.sleep(2000);
        return this;
    }

    public MainPageSelenium goToGenresPage() {
        driver.findElements(By.className("menu-link")).get(1).click();
        return this;
    }

    public MainPageSelenium goToNewsPage() {
        driver.findElements(By.className("menu-link")).get(2).click();
        return this;
    }

    public MainPageSelenium goToPopularPage() {
        driver.findElements(By.className("menu-link")).get(3).click();
        return this;
    }
    
    public MainPageSelenium goToViewedPage(){
        driver.findElements(By.className("menu-link")).get(4).click();
        return this;
    }

    public MainPageSelenium goToAuthorsPage() {
        driver.findElements(By.className("menu-link")).get(5).click();
        return this;
    }
}
