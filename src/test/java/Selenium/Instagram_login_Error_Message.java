package Selenium;

import com.github.javafaker.Faker;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Instagram_login_Error_Message {

    @Test

    public void warmUp() throws InterruptedException {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.instagram.com/");

        driver.manage().window().maximize();

        Faker faker = new Faker();

        Thread.sleep(1000);

        String userName = faker.name().username();
        WebElement usernameField = driver.findElement(By.name("username"));
        usernameField.sendKeys(userName);

        String password = faker.internet().password();
        WebElement passField = driver.findElement(By.name("password"));
        passField.sendKeys(password);

        WebElement loginB = driver.findElement(By.xpath("(//div[contains(@class,'_abak _abb8 _abbq _abb- _abcm')])[1]"));
        loginB.click();



//        WebElement homeButton = driver.findElement(By.id("slfErrorAlert"));
////        if (homeButton.isDisplayed()) {
////            System.out.println("Login failed");
////        } else {
////            System.out.println("Login successful!");
////        }
//        Assert.assertTrue(homeButton.isDisplayed(), "Login failed!");
        Thread.sleep(2000);

        Assert.assertEquals(driver.findElement(By.id("slfErrorAlert")).getText(),"Sorry, your password was incorrect. Please double-check your password.");

        driver.quit();
    }

}
