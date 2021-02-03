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


public class DrillsAddToCart {

    public static final int NUM_DRILLS = 3;

    @Test
    public void testTwo() {
        System.setProperty(Constants.PROPERTY_DRIVER, Constants.ADDRESS);
        ChromeDriver driver = new ChromeDriver();
        driver.get(Constants.URL);
        driver.findElement(By.xpath("//a[text()='Каталог']")).click();
        driver.findElement(By.xpath("//a[text()='Электроинструмент']")).click();
        driver.findElement(By.xpath("//a[text()='Дрели']")).click();

        Random random = new Random();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        for (int i = 0; i < NUM_DRILLS; i++) {
            WebElement boxOfDrills = driver.findElement(By.id("categories_view_pagination_contents"));
            List<WebElement> allDrillsOnPage = boxOfDrills.findElements(By.className("ut2-gl__body"));
            int randomDrill = random.nextInt(allDrillsOnPage.size());

            System.out.println("Page " + (i + 1) + " has " + allDrillsOnPage.size() +
                    " elements, position=" + randomDrill);

            WebElement item = allDrillsOnPage.get(randomDrill);
            wait.until(ExpectedConditions.elementToBeClickable(item));
            item.click();

            driver.findElement(By.className("ty-btn__add-to-cart")).click();

            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.className("ty-product-notification__item")));

            driver.navigate().back();

            if (i < NUM_DRILLS - 1) {
                WebElement pageNext = driver.findElement(By.className("ty-pagination__right-arrow"));
                wait.until(ExpectedConditions.elementToBeClickable(pageNext));
                pageNext.click();
                wait.until(ExpectedConditions.urlContains("page-" + (i + 2)));
            }
        }

        driver.navigate().refresh();

        driver.findElement(By.id("cart_status_3312")).click();
        driver.findElement(By.className("ty-float-left")).click();

        List<WebElement> cartElements = driver.findElements
                (By.xpath("//li[@class='ty-cart-items__list-item']"));
        assert (cartElements.size() == NUM_DRILLS);

        String oldTotalPrice = driver.findElement(By.id("sec_cart_total")).getText();
        System.out.println("Old total price = " + oldTotalPrice);
        driver.findElement(By.className("ty-delete-big__icon")).click();
        driver.navigate().refresh();
        String newTotalPrice = driver.findElement(By.id("sec_cart_total")).getText();
        System.out.println("New total price = " + newTotalPrice);
        assert (!oldTotalPrice.equals(newTotalPrice));

    }
}
