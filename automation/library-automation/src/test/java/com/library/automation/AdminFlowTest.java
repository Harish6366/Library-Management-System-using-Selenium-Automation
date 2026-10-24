package com.library.automation;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class AdminFlowTest extends BaseTest {

    @Test
    public void testAdminLoginAndBookFlow() throws InterruptedException {

        // 🕒 Global implicit wait – lets Selenium breathe between actions
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // ✅ Step 1: Login as admin
        driver.findElement(By.xpath("//input[@placeholder='Enter your email']")).sendKeys("admin");
        Thread.sleep(1500);

        driver.findElement(By.xpath("//input[@placeholder='Enter password']")).sendKeys("admin123");
        Thread.sleep(1500);

        driver.findElement(By.xpath("//button[text()='Login']")).click();
        Thread.sleep(2000);

        assertTrue(driver.getPageSource().contains("Library Management System"));

        // ✅ Step 2: Navigate to Books Management
        driver.findElement(By.linkText("Books Management")).click();
        Thread.sleep(1500);

        // ✅ Step 3: Click “Add New Book”
        driver.findElement(By.xpath("//button[contains(text(),'Add New Book')]")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h3[text()='Add New Book']")));
        Thread.sleep(1500);

        // ✅ Step 4: Fill in new book details (slow typing for visibility)
        driver.findElement(By.xpath("//label[text()='Title']/following::input[1]")).sendKeys("Selenium Guide");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//label[text()='Author']/following::input[1]")).sendKeys("Harish Naik");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//label[text()='ISBN']/following::input[1]")).sendKeys("1234567890");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//label[text()='Publication Year']/following::input[1]")).sendKeys("2025");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//label[text()='Category']/following::input[1]")).sendKeys("Automation");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//label[text()='Total Copies']/following::input[1]")).sendKeys("3");
        Thread.sleep(1000);

        driver.findElement(By.xpath("//button[text()='Add Book']")).click();
        Thread.sleep(1500);

        // ✅ Step 5: Verify the book appears in the list
        assertTrue(driver.getPageSource().contains("Selenium Guide"));

        // ✅ Step 6: Search for the new book
        WebElement search = driver.findElement(By.xpath("//input[@placeholder='Search books by title, author, ISBN, or genre...']"));
        search.sendKeys("Selenium");
        Thread.sleep(1500);
        assertTrue(driver.getPageSource().contains("Selenium Guide"));

        // ✅ Step 7: Logout
        driver.findElement(By.xpath("//button[text()='Logout']")).click();
        Thread.sleep(2000);
        assertTrue(driver.getPageSource().contains("Login"));
    }
}
