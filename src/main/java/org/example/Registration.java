package org.example;

import com.mailosaur.MailosaurClient;
import com.mailosaur.MailosaurException;
import com.mailosaur.models.Message;
import com.mailosaur.models.SearchCriteria;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registration {
    String apiKey = "mbPmUL0G2rBn0dnx7ie1relA6Df2Ecxl";
    String serverId = "fx1ra45a";
    String serverDomain = "fx1ra45a.mailosaur.net";

    public String getRandomEmail() {
        return "user" + System.currentTimeMillis() + "@" + serverId + "." + serverDomain;
    }


    public void testMailExample() throws IOException, MailosaurException, InterruptedException {
        String emailId = getRandomEmail();

        WebDriver driver = new ChromeDriver();

        driver.get("https://authorized-partner.vercel.app/");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        driver.findElement(By.cssSelector("a[href='/register'] button")).click();

        driver.findElement(By.id("remember")).click();
        driver.findElement(By.cssSelector("a[href='register?step=setup'] button")).click();

        //Setup your account
        driver.findElement(By.id("_r_0_-form-item")).sendKeys("Rina");
        driver.findElement(By.id("_r_1_-form-item")).sendKeys("Shrestha");
        driver.findElement(By.id("_r_2_-form-item")).sendKeys(emailId);
        driver.findElement(By.id("_r_4_-form-item")).sendKeys("9809876532");
        driver.findElement(By.cssSelector("input[type='password'][name ='password']")).sendKeys("Password@123");
        driver.findElement(By.cssSelector("input[type='password'][name ='confirmPassword']")).sendKeys("Password@123");
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        MailosaurClient mailosaur = new MailosaurClient(apiKey);

        SearchCriteria criteria = new SearchCriteria();
        criteria.withSentTo(emailId);

        Message message = mailosaur.messages().get(serverId, criteria);

        String emailBody = message.text().body();
        Pattern otpPattern = Pattern.compile("\\b\\d{6}\\b");
        Matcher matcher = otpPattern.matcher(emailBody);

        String otp = null;
        if (matcher.find()) {
            otp = matcher.group(0);
            System.out.println("OTP found: " + otp);
        }else {
            System.out.println("OTP not found");
        }

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement otpInput = wait.until(ExpectedConditions.elementToBeClickable(
                By.cssSelector("input[data-input-otp='true']")
        ));
        otpInput.sendKeys(otp);

        driver.findElement(By.cssSelector("button[type='submit']")).click();
        System.out.println("Account created");

        //Agency Details
        driver.findElement(By.id("_r_7u_-form-item")).sendKeys("Rising Star");
        driver.findElement(By.id("_r_7v_-form-item")).sendKeys("Staff");
        driver.findElement(By.id("_r_80_-form-item")).sendKeys("RisingStar@gmail.com");
        driver.findElement(By.id("_r_81_-form-item")).sendKeys("RisingStar.com");
        driver.findElement(By.id("_r_82_-form-item")).sendKeys("Lalitpur");
        WebElement Country = driver.findElement(By.cssSelector("button[role='combobox']"));
        Country.click();
        WebElement searchInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("input[placeholder='Search...']")));
        searchInput.sendKeys("Nepal");
        WebElement nepalOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='flex cursor-pointer items-center justify-between p-2 space-y-1 hover:bg-accent']/span[text()='Nepal']")));
        nepalOption.click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        //Professional Experience
        WebElement experienceDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.id("_r_85_-form-item")));
        experienceDropdown.click();
        WebElement option6 = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@role='option' or @data-state='open']//span[contains(text(),'6 years')]")));
        option6.click();
        driver.findElement(By.id("_r_87_-form-item")).sendKeys("1000");
        driver.findElement(By.id("_r_88_-form-item")).sendKeys("Master admission in Canada");
        driver.findElement(By.id("_r_89_-form-item")).sendKeys("95%");
        driver.findElement(By.id("_r_8c_-form-item")).click();
        driver.findElement(By.id("_r_8d_-form-item")).click();
        driver.findElement(By.cssSelector("button[type='submit']")).click();

        //Verification and Preferences
        driver.findElement(By.id("_r_9d_-form-item")).sendKeys("029574329");
        WebElement preferredCountry = wait.until(ExpectedConditions.elementToBeClickable(By.id("_r_9e_-form-item")));
        preferredCountry.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'cursor-pointer') and .//span[text()='Canada']]")));
        WebElement canadaOption = driver.findElement(By.xpath("//div[contains(@class, 'cursor-pointer') and .//span[text()='Canada']]"));
        canadaOption.click();
        WebElement ukOption = driver.findElement(By.xpath("//div[contains(@class, 'cursor-pointer') and .//span[text()='United Kingdom']]"));
        ukOption.click();
        preferredCountry.click();
        driver.findElement(By.id("_r_9h_-form-item")).click();
        WebElement uploadFile = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[type='file']")));
        uploadFile.sendKeys("C:\\Users\\Acer\\Downloads\\thumb.jpg");
        driver.findElement(By.cssSelector("button[type='submit']")).click();
        System.out.println("Registration successfull!");
    }


    public static void main(String[] args) throws IOException, MailosaurException, InterruptedException {
        Registration otpTest = new Registration();
        otpTest.testMailExample();
    }
}
