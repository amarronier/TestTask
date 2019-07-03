package com.alina.booking_test;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class BookingTest {

    @Test
    public void startBookingTest() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\ChromeDriver\\chromedriver.exe");
        ChromeDriver driver = new ChromeDriver();
        driver.navigate().to("https://www.booking.com");

        WebElement city = driver.findElementById("ss");
        city.sendKeys("Copenhagen");

        WebElement dateBox = driver.findElementByClassName("sb-date-field__icon");
        dateBox.click();
        Thread.sleep(100);

        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        LocalDate today = LocalDate.now();
        String todayString = formatter.format(today);
        WebElement todayElement = driver.findElementByXPath(
                String.format("//td[@data-date='%s']", todayString));
        todayElement.click();
        Thread.sleep(100);

        LocalDate tomorrow = today.plusDays(1L);
        String tomorrowString = formatter.format(tomorrow);
        WebElement tomorrowElement = driver.findElementByXPath(
                String.format("//td[@data-date='%s']", tomorrowString));
        tomorrowElement.click();
        Thread.sleep(100);

        WebElement searchButton = driver
                .findElementByXPath("//button[@class='sb-searchbox__button  ']");
        searchButton.click();

        WebElement lowPrice = driver
                .findElementByXPath("//li[@class=' sort_category   sort_price ']");
        lowPrice.click();

        Thread.sleep(3000); // wait until Booking sorts hotels

        List<WebElement> priceElements = driver.findElementsByClassName("bui-price-display__value");
        Assert.assertTrue(priceElements.size() != 0, "No prices!");

        LinkedList<Integer> prices = new LinkedList<>();
        for (WebElement element : priceElements) {
            String text = element.getText();
            if (text.length() == 0) continue;
            text = text.replaceAll("[^0-9]", "");
            Integer price = Integer.parseInt(text);
            prices.add(price);
        }

        for (int i = 1; i < prices.size(); i++) {
            Assert.assertTrue(prices.get(i) >= prices.get(i - 1),
                    "Prices are not sorted correctly! "
                            + prices.get(i) + " < " + prices.get(i - 1));
        }

        driver.close();

    }
}

