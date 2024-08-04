package demoqa.com.test;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.List;

public class DemoqaTest {
    private WebDriver driver;

    @BeforeClass
    public void setup() {

        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-running-insecure-content");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(120));
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
        driver.manage().window().maximize();

        driver.get("https://demoqa.com/buttons");
    }

    @Test(priority = 0)
    public void testDoubleClickButton() {
        WebElement btnDoubleClick = driver.findElement(By.cssSelector("button#doubleClickBtn"));
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", btnDoubleClick);

        Actions actions = new Actions(driver);
        actions.doubleClick(btnDoubleClick).perform();

        WebElement result = driver.findElement(By.cssSelector("#doubleClickMessage"));
        Assert.assertEquals(result.getText(), "You have done a double click");
    }

    @Test(priority = 1)
    public void testClickButton() {
        List<WebElement> buttons = driver.findElements(By.cssSelector("button"));
        WebElement btnClick = null;
        for (WebElement button : buttons) {
            if (button.getText().equals("Click Me")) {
                btnClick = button;
                break;
            }
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", btnClick);

        btnClick.click();

        WebElement result = driver.findElement(By.cssSelector("#dynamicClickMessage"));
        Assert.assertEquals(result.getText(), "You have done a dynamic click");
    }

    @Test(priority = 2)
    public void testAddRecord() {
        driver.get("https://demoqa.com/webtables");
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement btnAdd = driver.findElement(By.cssSelector("button#addNewRecordButton"));
        js.executeScript("arguments[0].click();", btnAdd);

        WebElement textFirstName = driver.findElement(By.cssSelector("input#firstName"));
        WebElement textLastName = driver.findElement(By.cssSelector("input#lastName"));
        WebElement textEmail = driver.findElement(By.cssSelector("input#userEmail"));
        WebElement textAge = driver.findElement(By.cssSelector("input#age"));
        WebElement textSalary = driver.findElement(By.cssSelector("input#salary"));
        WebElement textDepartment = driver.findElement(By.cssSelector("input#department"));

        textFirstName.sendKeys("Demir Ege");
        textLastName.sendKeys("Karapinar");
        textEmail.sendKeys("demirege@gmail.com");
        textAge.sendKeys("0");
        textSalary.sendKeys("20000");
        textDepartment.sendKeys("Bebek");

        WebElement btnSubmit = driver.findElement(By.cssSelector("button#submit"));
        btnSubmit.click();

        List<WebElement> rows = driver.findElements(By.cssSelector("div.rt-tbody div.rt-tr-group"));
        WebElement btnEdit = null;
        for (WebElement row : rows) {
            if (row.getText().contains("Demir Ege")) {
                btnEdit = row.findElement(By.cssSelector("span[title='Edit']"));
                break;
            }
        }
        js.executeScript("arguments[0].click();", btnEdit);

        textSalary = driver.findElement(By.cssSelector("input#salary"));
        textSalary.clear();
        textSalary.sendKeys("25000");

        btnSubmit = driver.findElement(By.cssSelector("button#submit"));
        btnSubmit.click();

        WebElement salaryCell = null;
        for (WebElement row : rows) {
            if (row.getText().contains("Demir Ege")) {
                salaryCell = row.findElement(By.cssSelector("div.rt-td:nth-child(5)"));
                break;
            }
        }
        Assert.assertEquals(salaryCell.getText(), "25000");
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }
    }
}