package com.library.automation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class LibraryAutomationTest {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "D:\\\\Downloads(application)\\\\Softwares\\\\chromedriver-win64\\\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.get("file:///E:/projects/LibraryManagement/deepseek.html"); // update path

        WebElement username = driver.findElement(By.xpath("//input[@placeholder='Enter your email']"));
        WebElement password = driver.findElement(By.xpath("//input[@placeholder='Enter password']"));
        WebElement loginButton = driver.findElement(By.xpath("//button[text()='Login']"));

        username.sendKeys("admin");
        password.sendKeys("admin123");
        loginButton.click();

        Thread.sleep(2000);

        if (driver.getPageSource().contains("Welcome")) {
            System.out.println("✅ Login successful!");
        } else {
            System.out.println("❌ Login failed!");
        }

        driver.quit();
    }
}
