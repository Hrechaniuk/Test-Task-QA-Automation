package ua.com.shoptool;


import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DiscountTest {

    public static final int MAX_ITEMS = 5;

    @Test
    public void testFour() {
        System.setProperty(Constants.PROPERTY_DRIVER, Constants.ADDRESS);
        ChromeDriver driver = new ChromeDriver();
        driver.get(Constants.URL);
        driver.findElement(By.xpath("//a[text()='Каталог']")).click();
        driver.findElement(By.xpath("//a[text()='Автоинструмент']")).click();
        driver.findElement(By.xpath("//a[text()='Наборы ключей']")).click();


        int currentPage = 1;

        final List<Item> items = new ArrayList<>();

        while (true) {
            WebElement content = driver.findElement(By.id("categories_view_pagination_contents"));

            List<WebElement> discountProducts = content.findElements(
                    By.xpath(".//div[contains(@class, 'ty-product-labels__item--discount')]/../../.."));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

            for (WebElement product : discountProducts) {

                WebElement element = product.findElement(By.className("product-title"));
                String name = element.getAttribute("title");
                String newPriceText = product.findElement(By.className("ty-price-num")).getText();
                String oldPriceText = product.findElement(By.className("ty-strike")).getText();
                oldPriceText = oldPriceText.replace("грн", "");

                String discountText = product.findElement
                        (By.xpath(".//div[contains(@class, 'ty-product-labels__item--discount')]/div/em")).getText();

                if (newPriceText.contains(",")) {
                    newPriceText = newPriceText.replace(",", "");
                }
                if (oldPriceText.contains(",")) {
                    oldPriceText = oldPriceText.replace(",", "");
                }

                double newPrice = Double.parseDouble(newPriceText);
                double oldPrice = Double.parseDouble(oldPriceText);
                double discount = Double.parseDouble(discountText.replace("%", ""));


                items.add(new Item(name, oldPrice, newPrice, discount));
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

        List<Item> resultList;
        if (items.size() > MAX_ITEMS) {
            Collections.shuffle(items);
            resultList = items.subList(0, MAX_ITEMS);
        } else {
            resultList = items;
        }

        if (resultList.isEmpty()) {
            System.out.println("Can not find products with discount");
        }

        for (Item item : resultList) {
            double expectedNewPrice = item.oldPrice * (1 - (item.discount / 100));
            System.out.println(item);
            System.out.println("expectedNewPrice: " + expectedNewPrice);
            assert item.newPrice == expectedNewPrice;
        }
    }

    static class Item {

        final String name;
        final double oldPrice;
        final double newPrice;
        final double discount;

        public Item(String name, double oldPrice, double newPrice, double discount) {
            this.name = name;
            this.oldPrice = oldPrice;
            this.newPrice = newPrice;
            this.discount = discount;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "name='" + name + '\'' +
                    ", oldPrice=" + oldPrice +
                    ", newPrice=" + newPrice +
                    ", discount=" + discount +
                    '}';
        }
    }
}

