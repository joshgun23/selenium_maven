package Selenium_Project_3;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CarGurus_Test {
    @Test
    public void carGurus() throws InterruptedException {
//step 1
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.cargurus.com/");
driver.findElement(By.xpath("//label[@class=\"ft-homepage-search__tabs__used-car \"]")).click();
//Step 3
        // verify that the default selected option in Makes dropdown is All Makes
        Thread.sleep(1000);
       WebElement selectedOptionMakes = new Select(driver.findElement(By.xpath("//select[@id=\"carPickerUsed_makerSelect\"]"))).getFirstSelectedOption();
        Assert.assertEquals(selectedOptionMakes.getText(), "All Makes", "Default selected option");

       // choose Lamborghini in Makes dropdown
        Thread.sleep(2000);
        new Select(driver.findElement(By.xpath("//select[@id=\"carPickerUsed_makerSelect\"]"))).selectByVisibleText("Lamborghini");

        // verify that the default selected option in Models dropdown is All Models
        WebElement selectedOptionMakes1 = new Select(driver.findElement(By.xpath("//select[@id='carPickerUsed_modelSelect']"))).getFirstSelectedOption();
        Assert.assertEquals(selectedOptionMakes1.getText(), "All Models", "Default selected option");

//        // verify Models dropdown options
        List<String> expectedModels = List.of("All Models", "Aventador", "Huracan", "Urus", "400GT",
                "Centenario", "Countach", "Diablo", "Espada", "Gallardo", "Murcielago");
        List<WebElement> modelsOptions = selectedOptionMakes1.findElements(By.xpath("//select[@id='carPickerUsed_modelSelect']//option"));
        List<String> actualModels = new ArrayList<>();
        for (WebElement option : modelsOptions) {
            actualModels.add(option.getText());
        }
        Assert.assertEquals(actualModels, expectedModels);
//
//        // choose Gallardo in Models dropdown
            new Select(driver.findElement(By.xpath("//select[@id='carPickerUsed_modelSelect']"))).selectByVisibleText("Gallardo");
//
//        // enter zip code and search
         driver.findElement(By.xpath("//input[@id=\"dealFinderZipUsedId_dealFinderForm\"]")).sendKeys("22031");

         driver.findElement(By.xpath("//input[@id=\"dealFinderForm_0\"]")).click();

         // verify that there are 15 search results, excluding the first sponsored result
        List<WebElement> searchResults = driver.findElements(By.xpath("//a[@data-cg-ft='car-blade-link'][not(contains(@href, 'FEATURED'))]"));
        Assert.assertEquals(searchResults.size(), 15);

         // verify that all 15 result's title text contains "Lamborghini Gallardo"
        for (WebElement result : searchResults) {
            String title = result.findElement(By.xpath("//h4[@class=\"vO42pn\"]")).getText();
            Assert.assertTrue(title.contains("Lamborghini Gallardo"));
        }

          // choose "Lowest price first" option from dropdown and verify correct sorting
        driver.findElement(By.xpath("//select[@id='sort-listing']/option[text()='Lowest price first']"))
                .click();
        List<WebElement> prices = driver.findElements(By.className("cg-dealFinder-result-stats"));
        List<Integer> actualPrices = new ArrayList<>();
        for (WebElement price : prices) {
            String priceText = price.findElement(By.className("cg-dealFinder-result-price")).getText().replaceAll("[^\\d.]", "");
            actualPrices.add(Integer.parseInt(priceText));
        }
        List<Integer> sortedPrices = new ArrayList<>(actualPrices);
        Collections.sort(sortedPrices);
        Assert.assertEquals(actualPrices, sortedPrices);

       // Click on "Highest mileage first" option in the sort dropdown
        WebElement sortDropdown = driver.findElement(By.xpath("//select[@id='sort-listing']"));
        Select sortSelect = new Select(sortDropdown);
        sortSelect.selectByVisibleText("Highest mileage first");

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.listingResultsPageContent")));

// Get a list of all search result elements that have the mileage information
        List<WebElement> searchResults1 = driver.findElements(By.cssSelector("div#listingResults article"));
        List<WebElement> mileageElements = new ArrayList<>();
        for (WebElement result : searchResults1) {
            List<WebElement> mileage = result.findElements(By.cssSelector("div.vehicle-card-features li"));
            if (mileage.size() > 1) {
                mileageElements.add(mileage.get(1));
            }
        }

// Extract the mileage information from each element and convert it to integers
        List<Integer> mileages = new ArrayList<>();
        for (WebElement element : mileageElements) {
            String mileageText = element.getText().replaceAll("[^\\d]", "");
            if (!mileageText.isEmpty()) {
                mileages.add(Integer.parseInt(mileageText));
            }
        }

// Check that the extracted mileage values are sorted in descending order
        boolean isSorted = true;
        for (int i = 0; i < mileages.size() - 1; i++) {
            if (mileages.get(i) < mileages.get(i+1)) {
                isSorted = false;
                break;
            }
        }
        Assert.assertTrue(isSorted, "The search results are not sorted by highest mileage first.");


    }
}
