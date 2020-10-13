import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class SberbankTravelInsuranceTest {
    WebDriver driver;
    WebDriverWait wait;
    String baseUrl = "https://www.sberbank.ru/ru/person";

    @BeforeEach
    void beforeSberbankTravelInsuranceTest() {
        System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 5);
    }

    @Test
    void sberbankTravelInsuranceTest() {

        // 1. Перейти на страницу http://www.sberbank.ru/ru/person.
        driver.get(baseUrl);

        // 2. Нажать на "Страхование".
        click(By.xpath("//a[contains(@aria-label, 'Страхование')]"));

        // 3. Выбрать "СберСтрахование", в блоке "Страхование путешественников" нажать "Подробнее".
        // Шаг изменён по текущему дизайну сайта.
        click(By.xpath("//a[contains(@class, 'kitt-top-menu__link') and contains(text(), 'СберСтрахование')]"));
        WebElement element = driver.findElement(By.xpath("//h2[text()='Страхование путешественников']/..//a[text()='Подробнее']"));
        ((JavascriptExecutor) driver).executeScript("return arguments[0].scrollIntoView(true);", element);
        click(element);

        // 4. Проверить наличие на странице заголовка "Страхование путешественников".
        Assertions.assertEquals("Страхование путешественников", driver.findElement(By.xpath("//nav/..//h1")).getText());

        // 5. Нажать на "Оформить Онлайн".
        click(By.xpath("//span[text()='Оформить онлайн']"));

        // 6. На вкладке "Выбор полиса" выбрать сумму страховой защиты "Минимальная".
        click(By.xpath("//legend[text()='Выберите сумму страховой защиты']/..//online-card-program"));

        // 7. Нажать "Оформить".
        click(By.xpath("//button[text()='Оформить']"));

        // 8. На вкладке "Оформить" заполнить фамилию, имя, дату рождения застрахованных,
        // фамилию, имя, отчество, дату рождения, пол страхователя, паспортные данные.
        // Контактные данные не заполнять.
        fillField("surname_vzr_ins_0", "Иванов");
        fillField("name_vzr_ins_0", "Иван");
        fillField("birthDate_vzr_ins_0", "01.01.1960");
        fillField("person_lastName", "Петров");
        fillField("person_firstName", "Петр");
        fillField("person_middleName", "Петрович");
        fillField("person_birthDate", "01.01.2000");
        fillField("passportSeries", "1010");
        fillField("passportNumber", "101010");
        fillField("documentDate", "01.01.2020");
        fillField("documentIssue", "МВД России");
        WebElement genderButton = driver.findElement(By.xpath("//label[text()='Мужской']"));
        click(genderButton);

        // 9. Проверить, что все поля заполнены правильно.
        Assertions.assertAll(
                () -> checkField("surname_vzr_ins_0", "Иванов"),
                () -> checkField("name_vzr_ins_0", "Иван"),
                () -> checkField("birthDate_vzr_ins_0", "01.01.1960"),
                () -> checkField("person_lastName", "Петров"),
                () -> checkField("person_firstName", "Петр"),
                () -> checkField("person_middleName", "Петрович"),
                () -> checkField("person_birthDate", "01.01.2000"),
                () -> checkField("passportSeries", "1010"),
                () -> checkField("passportNumber", "101010"),
                () -> checkField("documentDate", "01.01.2020"),
                () -> checkField("documentIssue", "МВД России"),
                () -> Assertions.assertTrue(genderButton.getAttribute("class").contains("active")));

        // 10. Нажать "Продолжить".
        click(By.xpath("//button[contains(text(), 'Продолжить')]"));

        // 11. Проверить, что появилось сообщение "При заполнении данных произошла ошибка".
        // Текст сообщения изменён по текущему дизайну сайта.
        Assertions.assertEquals("При заполнении данных произошла ошибка",
                driver.findElement(By.xpath("//div[contains(@class, 'alert-form')]")).getText());
    }

    @AfterEach
    void afterSberbankTravelInsuranceTest() {
        driver.quit();
    }

    WebElement getField(String id) {
        return wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id(id))));
    }

    void fillField(String id, String value) {
        WebElement element = getField(id);
        element.click();
        element.clear();
        element.sendKeys(value);
    }

    void checkField(String id, String value) {
        Assertions.assertEquals(value, getField(id).getAttribute("value"));
    }

    void click(By locator) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
    }

    void click(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element)).click();
    }
}
