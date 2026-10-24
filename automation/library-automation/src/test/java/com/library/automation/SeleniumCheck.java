package com.library.automation;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class SeleniumCheck {
    public static void main(String[] args) throws InterruptedException {
        WebDriverManager.chromedriver().setup();  // Automatically manages ChromeDriver
        WebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.get("https://www.google.com");

        System.out.println("✅ Selenium is working — Google opened successfully!");
        Thread.sleep(3000);  // keep the window open briefly
        driver.quit();
    }
}
