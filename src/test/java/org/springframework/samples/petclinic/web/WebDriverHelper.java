package org.springframework.samples.petclinic.web;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.InetAddress;

public class WebDriverHelper {


    public static WebDriver getWebDriver() {
        WebDriver driver;
        // driver = setupRemoteWebDriver(OperaDriver.class);
        // driver = setupRemoteWebDriver(ChromeDriver.class);
        driver = setupChrome();
        // driver = setupFirefox();

        return driver;

    }

    protected static WebDriver setupChrome() {
        WebDriver driver;
        WebDriverManager instance = WebDriverManager.getInstance(ChromeDriver.class);
        instance.setup();
        driver = new ChromeDriver();
        return driver;
    }

    protected static WebDriver setupFirefox() {
        WebDriver driver;
        System.setProperty("webdriver.firefox.bin", "/apps/sit/firefox54/firefox");
        WebDriverManager instance = WebDriverManager.getInstance(FirefoxDriver.class);
        instance.setup();
        driver = new FirefoxDriver();
        return driver;
    }

    private static RemoteWebDriver setupRemoteWebDriver(Class driverClass) {
        RemoteWebDriver driver;
        WebDriverManager instance = WebDriverManager.getInstance(driverClass);
        instance.setup();
        try {
            if (driverClass.equals(ChromeDriver.class)) {
                ChromeOptions options = new ChromeOptions();
                if (true || !"jety-17".equals(InetAddress.getLocalHost().getHostName())) {
                    options.addArguments("window-size=1200x600","--headless");
                    System.out.println("Running chrome in headless mode");
                }

                driver = new ChromeDriver(options);
            } else {
                driver = (RemoteWebDriver) driverClass.newInstance();
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        return driver;
    }
}
