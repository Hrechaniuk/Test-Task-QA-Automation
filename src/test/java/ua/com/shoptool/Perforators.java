package ua.com.shoptool;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Random;

public class Perforators {

    public static final int NUM_PERFORATORS = 3;

    @Test
    public void testOne() {
        System.setProperty(Constants.PROPERTY_DRIVER, Constants.ADDRESS);
        ChromeDriver driver = new ChromeDriver();
        driver.get(Constants.URL);
        driver.findElement(By.xpath("//a[text()='Каталог']")).click();
        driver.findElement(By.xpath("//a[text()='Электроинструмент']")).click();
        driver.findElement(By.xpath("//a[text()='Перфораторы']")).click();

        Random random = new Random();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        for (int i = 0; i < NUM_PERFORATORS; i++) {
            WebElement boxOfPerforators = driver.findElement(By.id("categories_view_pagination_contents"));
            List<WebElement> allPerforatorsOnPage = boxOfPerforators.findElements(By.className("ty-column3"));
            int randomPerforator = random.nextInt(allPerforatorsOnPage.size());

            WebElement item = allPerforatorsOnPage.get(randomPerforator);
            wait.until(ExpectedConditions.elementToBeClickable(item));
            item.click();

            boolean isPresent = driver.findElements(By.className("action-title")).size() > 0;
            if (isPresent) {
                System.out.println(driver.findElement(By.xpath("//h1[@class='ut2-pb__title']/bdi")).getText());
                System.out.print("Old price is: ");
                System.out.println(driver.findElement
                        (By.xpath("//div[@class='ty-product-prices']/span/span/span/bdi/span")).getText());
                System.out.print("Present price is ");
                System.out.println(driver.findElement
                        (By.xpath("//div[@class='ty-product-prices']/div/span/span/bdi/span")).getText());

            }

            driver.navigate().back();
            if (i < NUM_PERFORATORS - 1) {
                WebElement pageNext = driver.findElement(By.className("ty-pagination__right-arrow"));
                wait.until(ExpectedConditions.elementToBeClickable(pageNext));
                pageNext.click();
                wait.until(ExpectedConditions.urlContains("page-" + (i + 2)));
            }

        }
    }
}
