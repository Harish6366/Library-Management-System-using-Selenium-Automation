package com.library.automation;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class UserFlowTest extends BaseTest {

    @Test
    public void testUserBorrowAndReturnBook() throws InterruptedException {
        driver.findElement(By.xpath("//input[@placeholder='Enter your email']")).sendKeys("user");
        Thread.sleep(1500);
        driver.findElement(By.xpath("//input[@placeholder='Enter password']")).sendKeys("user123");
        Thread.sleep(1500);
        driver.findElement(By.xpath("//button[text()='Login']")).click();

        Thread.sleep(1500);
        driver.findElement(By.linkText("Browse Books")).click();
        Thread.sleep(1500);

        // ✅ Borrow first available book
        WebElement borrowBtn = driver.findElement(By.xpath("(//button[text()='Borrow Book' and not(@disabled)])[1]"));
        Thread.sleep(1000);
        String bookTitle = borrowBtn.findElement(By.xpath("ancestor::div[@class='book-card']//div[@class='book-title']")).getText();
        borrowBtn.click();

        Thread.sleep(1500);
        driver.switchTo().alert().accept();

        // ✅ Navigate to “My Books”
        driver.findElement(By.linkText("My Books")).click();
        Thread.sleep(2000);
        assertTrue(driver.getPageSource().contains(bookTitle));

        // ✅ Return the book
        WebElement returnBtn = driver.findElement(By.xpath("//button[text()='Return Book']"));
        returnBtn.click();
        driver.switchTo().alert().accept();

        Thread.sleep(1000);
        assertTrue(driver.getPageSource().contains("You haven't borrowed any books yet."));

        // ✅ Logout
        driver.findElement(By.xpath("//button[text()='Logout']")).click();
        Thread.sleep(1000);
        assertTrue(driver.getPageSource().contains("Library Management System"));
    }
}
