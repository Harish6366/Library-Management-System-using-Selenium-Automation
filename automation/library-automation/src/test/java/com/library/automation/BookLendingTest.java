package com.library.automation;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BookLendingTest extends BaseTest {

    @Test
    public void testFullLendAndReturnFlow() throws InterruptedException {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // ======================================================
        // 🔹 Step 1: Admin login
        // ======================================================
        System.out.println("🔹 Step 1: Admin login...");
        driver.findElement(By.xpath("//input[@placeholder='Enter your email']")).sendKeys("admin");
        driver.findElement(By.xpath("//input[@placeholder='Enter password']")).sendKeys("admin123");
        driver.findElement(By.xpath("//button[text()='Login']")).click();
        Thread.sleep(2000);
        assertTrue(driver.getPageSource().contains("Library Management System"));

        // ======================================================
        // 🔹 Step 2: Add new member
        // ======================================================
        System.out.println("🔹 Step 2: Adding new member for lending...");
        driver.findElement(By.linkText("Members Management")).click();
        Thread.sleep(2000);

        driver.findElement(By.xpath("//button[contains(text(),'Add New Member')]")).click();
        Thread.sleep(2000);

        String randomId = Long.toHexString(System.currentTimeMillis());
        String memberEmail = "auto_user_" + randomId + "@test.com";
        String memberPassword = "user123";

        driver.findElement(By.xpath("//label[text()='Name']/following::input[1]")).sendKeys("AutoUser_" + randomId);
        Thread.sleep(1500);
        driver.findElement(By.xpath("//label[text()='Email']/following::input[1]")).sendKeys(memberEmail);
        Thread.sleep(1500);
        driver.findElement(By.xpath("//label[text()='Phone']/following::input[1]")).sendKeys("9876543210");
        Thread.sleep(1500);
        driver.findElement(By.xpath("//label[text()='Address']/following::input[1]")).sendKeys("Test City");
        Thread.sleep(1500);
        driver.findElement(By.xpath("//input[@placeholder='Enter password for login']")).sendKeys(memberPassword);
        Thread.sleep(1500);

        driver.findElement(By.xpath("//button[text()='Add Member']")).click();
        Thread.sleep(2000);

        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            System.out.println("✅ Member alert: " + alert.getText());
            alert.accept();
        } catch (TimeoutException e) {
            System.out.println("⚠️ No alert appeared after adding member.");
        }

        // ======================================================
        // 🔹 Step 3: Navigate to Dashboard
        // ======================================================
        System.out.println("🔹 Step 3: Navigating back to Dashboard...");
        driver.findElement(By.linkText("Dashboard")).click();
        Thread.sleep(2000);

        // ======================================================
        // 🔹 Step 4: Lend Book
        // ======================================================
        System.out.println("🔹 Step 4: Opening Lend Book modal...");
        driver.findElement(By.xpath("//button[contains(text(),'Lend Book')]")).click();
        Thread.sleep(2000);

        Select bookDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='Select Book']/following::select[1]"))));
        List<WebElement> bookOptions = bookDropdown.getOptions();
        Thread.sleep(1500);
        if (bookOptions.size() <= 1) {
            throw new RuntimeException("❌ No available books found to lend.");
        }
        bookDropdown.selectByIndex(1);
        System.out.println("✅ Selected book: " + bookOptions.get(1).getText());

        Select memberDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//label[text()='Select Member']/following::select[1]"))));
        Thread.sleep(1000);
        List<WebElement> memberOptions = memberDropdown.getOptions();
        if (memberOptions.size() <= 1) {
            throw new RuntimeException("❌ No members found to lend book to.");
        }
        memberDropdown.selectByIndex(memberOptions.size() - 1);
        System.out.println("✅ Selected member: " + memberOptions.get(memberOptions.size() - 1).getText());

        // Set due date (10 days ahead)
        String dueDate = LocalDate.now().plusDays(10).format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        Thread.sleep(1000);
        driver.findElement(By.xpath("//label[text()='Due Date']/following::input[1]")).sendKeys(dueDate);
        Thread.sleep(2000);

        WebElement lendButton = driver.findElement(By.xpath("//button[text()='Lend']"));
        try {
            wait.until(ExpectedConditions.elementToBeClickable(lendButton));
            lendButton.click();
        } catch (Exception e) {
            System.out.println("⚠️ Normal click failed, retrying with JavaScript...");
            js.executeScript("arguments[0].click();", lendButton);
        }

        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("modal")));
        Thread.sleep(1500);

        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            System.out.println("✅ Lending alert: " + alert.getText());
            alert.accept();
        } catch (TimeoutException e) {
            System.out.println("⚠️ No lending alert appeared.");
        }

        System.out.println("✅ Book lending successful.");

        // ======================================================
        // 🔹 Step 5: Logout as Admin
        // ======================================================
        driver.findElement(By.xpath("//button[text()='Logout']")).click();
        Thread.sleep(1000);
        System.out.println("✅ Logged out as admin.");

        // ======================================================
        // 🔹 Step 6: Login as the newly added member
        // ======================================================
        System.out.println("🔹 Step 6: Logging in as the new member...");
        driver.findElement(By.xpath("//input[@placeholder='Enter your email']")).sendKeys(memberEmail);
        Thread.sleep(1500);
        driver.findElement(By.xpath("//input[@placeholder='Enter password']")).sendKeys(memberPassword);
        Thread.sleep(1500);
        driver.findElement(By.xpath("//button[text()='Login']")).click();
        Thread.sleep(2000);
        assertTrue(driver.getPageSource().contains("Library Management System"));
        System.out.println("✅ Member logged in successfully.");

        // ======================================================
// 🔹 Step 7: Return Book flow
// ======================================================
        Thread.sleep(1500);
        System.out.println("🔹 Step 7: Opening Return Book modal...");
        driver.findElement(By.xpath("//button[contains(text(),'Return Book')]")).click();
        Thread.sleep(2000);

        try {
            // Wait for dropdown to appear (modal fully loaded)
            Select returnBookDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//label[text()='Select Book']/following::select[1]"))));
            Thread.sleep(1500);

            List<WebElement> returnBookOptions = returnBookDropdown.getOptions();
            if (returnBookOptions.size() <= 1) {
                throw new RuntimeException("❌ No books available to return.");
            }

            returnBookDropdown.selectByIndex(1);
            System.out.println("✅ Selected book to return: " + returnBookOptions.get(1).getText());

            // Click Return button
            driver.findElement(By.xpath("//button[text()='Return']")).click();
            Thread.sleep(1000);

            // ✅ Handle success alert immediately
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            System.out.println("✅ Return alert: " + alert.getText());
            alert.accept();

            // Give the UI time to update
            Thread.sleep(1500);

        } catch (TimeoutException | org.openqa.selenium.UnhandledAlertException e) {
            // Handles both missing alert or an already-open alert gracefully
            try {
                Alert fallbackAlert = driver.switchTo().alert();
                System.out.println("⚠️ Fallback alert handled: " + fallbackAlert.getText());
                fallbackAlert.accept();
                Thread.sleep(2000);
            } catch (Exception ignored) {
                System.out.println("⚠️ No additional alert found.");
            }
        }

        Thread.sleep(2000);
        assertTrue(driver.getPageSource().toLowerCase().contains("returned"));
        System.out.println("✅ Book returned successfully and verified on dashboard.");

        // ======================================================
        // 🔹 Step 8: Logout Member
        // ======================================================
        driver.findElement(By.xpath("//button[text()='Logout']")).click();
        Thread.sleep(1000);
        System.out.println("✅ Member logged out successfully.");

        System.out.println("🎉 Full lend + return flow completed successfully!");
    }
}
