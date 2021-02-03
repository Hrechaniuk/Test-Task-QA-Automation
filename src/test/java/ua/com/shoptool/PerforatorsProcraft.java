package ua.com.shoptool;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class PerforatorsProcraft {

    int currentPage = 1;

    @Test
    public void threeTest() {
        System.setProperty(Constants.PROPERTY_DRIVER, Constants.ADDRESS);
        ChromeDriver driver = new ChromeDriver();
        driver.get(Constants.URL);
        driver.findElement(By.xpath("//a[text()='Каталог']")).click();
        driver.findElement(By.xpath("//a[text()='Электроинструмент']")).click();
        driver.findElement(By.xpath("//a[text()='Перфораторы']")).click();


        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

        while (true) {
            List<WebElement> allProcraftOnPage = driver.findElements
                    (By.xpath("//img[@title = 'PROCRAFT']/../../../../../.."));

            for (WebElement webElement : allProcraftOnPage) {
                WebElement element = webElement.findElement(By.className("product-title"));
                String title = element.getAttribute("title");
                System.out.println("Product title: " + title);

                if (currentPage == 3 || currentPage == 1) {
                    if (!(title.contains("PROCRAFT") || title.contains("PRO-CRAFT"))) {
                        System.out.println("The title does not contains Procraft");
                    }
                }

            }

            if (driver.findElement(By.className("ty-pagination__right-arrow")).getAttribute("href") != null) {
                currentPage++;
                WebElement pageNext = driver.findElement(By.className("ty-pagination__right-arrow"));
                wait.until(ExpectedConditions.elementToBeClickable(pageNext));
                pageNext.click();
                wait.until(ExpectedConditions.urlContains("page-" + currentPage));
            } else {
                break;
            }
        }


    }

}
