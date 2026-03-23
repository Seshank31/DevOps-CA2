package com.selenium.test;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.time.Duration;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestPro {

    private static final Duration WAIT_TIME = Duration.ofSeconds(5);
    private static int passed = 0;
    private static int failed = 0;
    private static HttpServer server;
    private static String baseUrl;

    public static void main(String[] args) {
        WebDriver driver = null;

        try {
            startLocalServer();

            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1440,1000");
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
            options.addArguments("--allow-file-access-from-files");
            options.addArguments("--disable-web-security");

            driver = new ChromeDriver(options);
            driver.manage().timeouts().implicitlyWait(WAIT_TIME);

            runTest(driver, "Form page opens successfully", TestPro::testPageLoads);
            runTest(driver, "Valid data submits successfully", TestPro::testValidSubmission);
            runTest(driver, "Mandatory name field validation", TestPro::testBlankNameValidation);
            runTest(driver, "Invalid email validation", TestPro::testInvalidEmailValidation);
            runTest(driver, "Invalid mobile validation", TestPro::testInvalidMobileValidation);
            runTest(driver, "Dropdown selection works", TestPro::testDropdownSelection);
            runTest(driver, "Submit and reset buttons work", TestPro::testButtonsWork);

            System.out.println();
            System.out.println("TOTAL PASSED: " + passed);
            System.out.println("TOTAL FAILED: " + failed);

            if (failed > 0) {
                throw new RuntimeException("One or more Selenium tests failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        } finally {
            if (driver != null) {
                driver.quit();
            }
            stopLocalServer();
        }
    }

    private static void testPageLoads(WebDriver driver) {
        openForm(driver);
        logPageState(driver);
        assertTrue(driver.getCurrentUrl().contains("index.html"), "Form page URL should open correctly.");
        assertEquals("Student Feedback Registration Form",
                driver.findElement(By.tagName("h2")).getText(),
                "Form heading should be visible on the page.");
        assertTrue(driver.findElement(By.tagName("form")).isDisplayed(), "Form should be visible on the page.");
    }

    private static void testValidSubmission(WebDriver driver) {
        openForm(driver);
        fillValidForm(driver);
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        String alertText = readAndAcceptAlert(driver);
        assertEquals("Form submitted successfully", alertText, "Valid form should submit successfully.");
    }

    private static void testBlankNameValidation(WebDriver driver) {
        openForm(driver);
        fillValidForm(driver);
        driver.findElement(By.id("name")).clear();
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        String alertText = readAndAcceptAlert(driver);
        assertEquals("Name cannot be empty", alertText, "Blank name should show required-field validation.");
    }

    private static void testInvalidEmailValidation(WebDriver driver) {
        openForm(driver);
        fillValidForm(driver);
        WebElement email = driver.findElement(By.id("email"));
        email.clear();
        email.sendKeys("wrong-email-format");
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        String alertText = readAndAcceptAlert(driver);
        assertEquals("Invalid email format", alertText, "Invalid email should show validation.");
    }

    private static void testInvalidMobileValidation(WebDriver driver) {
        openForm(driver);
        fillValidForm(driver);
        WebElement mobile = driver.findElement(By.id("mobile"));
        mobile.clear();
        mobile.sendKeys("12345abc");
        driver.findElement(By.cssSelector("input[type='submit']")).click();

        String alertText = readAndAcceptAlert(driver);
        assertEquals("Mobile must be 10 digits", alertText, "Invalid mobile number should show validation.");
    }

    private static void testDropdownSelection(WebDriver driver) {
        openForm(driver);
        Select department = new Select(driver.findElement(By.id("department")));
        department.selectByVisibleText("Computer Science");

        assertEquals("Computer Science", department.getFirstSelectedOption().getText(),
                "Dropdown should select the requested department.");
    }

    private static void testButtonsWork(WebDriver driver) {
        openForm(driver);
        fillValidForm(driver);

        driver.findElement(By.cssSelector("input[type='reset']")).click();

        assertEquals("", driver.findElement(By.id("name")).getAttribute("value"), "Reset should clear the name field.");
        assertEquals("", driver.findElement(By.id("email")).getAttribute("value"), "Reset should clear the email field.");
        assertEquals("", driver.findElement(By.id("mobile")).getAttribute("value"), "Reset should clear the mobile field.");
        assertEquals("", driver.findElement(By.id("department")).getAttribute("value"),
                "Reset should clear the department field.");
        assertEquals("", driver.findElement(By.id("feedback")).getAttribute("value"),
                "Reset should clear the feedback field.");
        assertTrue(driver.findElements(By.cssSelector("input[name='gender']:checked")).isEmpty(),
                "Reset should clear the selected gender.");
    }

    private static void fillValidForm(WebDriver driver) {
        driver.findElement(By.id("name")).sendKeys("Seshank");
        driver.findElement(By.id("email")).sendKeys("seshank@example.com");
        driver.findElement(By.id("mobile")).sendKeys("9876543210");

        Select department = new Select(driver.findElement(By.id("department")));
        department.selectByVisibleText("Computer Science");

        driver.findElement(By.cssSelector("input[name='gender'][value='Male']")).click();
        driver.findElement(By.id("feedback")).sendKeys(
                "This feedback form is clear easy to use well styled and validates every input correctly.");
    }

    private static void openForm(WebDriver driver) {
        File formFile = new File(System.getProperty("user.dir"), "index.html").getAbsoluteFile();

        if (!formFile.exists()) {
            throw new RuntimeException("Form file not found at: " + formFile.getAbsolutePath());
        }

        String formUrl = baseUrl + "/index.html";
        System.out.println("Opening form: " + formUrl);
        driver.get(formUrl);

        new WebDriverWait(driver, WAIT_TIME).until(d ->
                "complete".equals(((JavascriptExecutor) d).executeScript("return document.readyState")));
    }

    private static void logPageState(WebDriver driver) {
        System.out.println("Page title: " + driver.getTitle());
        System.out.println("Current URL: " + driver.getCurrentUrl());
        String pageSource = driver.getPageSource();
        int previewLength = Math.min(pageSource.length(), 400);
        System.out.println("Page source preview: " + pageSource.substring(0, previewLength));
    }

    private static void startLocalServer() throws IOException {
        File projectRoot = new File(System.getProperty("user.dir")).getAbsoluteFile();

        server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
        server.createContext("/", new StaticFileHandler(projectRoot));
        server.start();

        baseUrl = "http://127.0.0.1:" + server.getAddress().getPort();
        System.out.println("Local test server started at: " + baseUrl);
    }

    private static void stopLocalServer() {
        if (server != null) {
            server.stop(0);
        }
    }

    private static String readAndAcceptAlert(WebDriver driver) {
        Alert alert = new WebDriverWait(driver, WAIT_TIME).until(d -> d.switchTo().alert());
        String text = alert.getText();
        alert.accept();
        return text;
    }

    private static void runTest(WebDriver driver, String testName, SeleniumTest testCase) {
        try {
            testCase.run(driver);
            passed++;
            System.out.println("[PASS] " + testName);
        } catch (Exception e) {
            failed++;
            System.out.println("[FAIL] " + testName + " -> " + e.getMessage());
        }
    }

    private static void assertEquals(String expected, String actual, String message) {
        if (!expected.equals(actual)) {
            throw new AssertionError(message + " Expected: " + expected + ", Actual: " + actual);
        }
    }

    private static void assertTrue(boolean condition, String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }

    @FunctionalInterface
    private interface SeleniumTest {
        void run(WebDriver driver);
    }

    private static class StaticFileHandler implements HttpHandler {
        private final File rootDirectory;

        StaticFileHandler(File rootDirectory) {
            this.rootDirectory = rootDirectory;
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestPath = exchange.getRequestURI().getPath();
            if ("/".equals(requestPath)) {
                requestPath = "/index.html";
            }

            File requestedFile = new File(rootDirectory, requestPath.substring(1)).getCanonicalFile();
            if (!requestedFile.getPath().startsWith(rootDirectory.getCanonicalPath()) || !requestedFile.exists()
                    || requestedFile.isDirectory()) {
                byte[] notFound = "404 Not Found".getBytes();
                exchange.sendResponseHeaders(404, notFound.length);
                try (OutputStream outputStream = exchange.getResponseBody()) {
                    outputStream.write(notFound);
                }
                return;
            }

            String contentType = guessContentType(requestedFile.getName());
            exchange.getResponseHeaders().set("Content-Type", contentType);

            byte[] fileBytes = Files.readAllBytes(requestedFile.toPath());
            exchange.sendResponseHeaders(200, fileBytes.length);
            try (OutputStream outputStream = exchange.getResponseBody()) {
                outputStream.write(fileBytes);
            }
        }

        private String guessContentType(String fileName) {
            if (fileName.endsWith(".html")) {
                return "text/html; charset=UTF-8";
            }
            if (fileName.endsWith(".css")) {
                return "text/css; charset=UTF-8";
            }
            if (fileName.endsWith(".js")) {
                return "application/javascript; charset=UTF-8";
            }
            if (fileName.endsWith(".png")) {
                return "image/png";
            }
            return "text/plain; charset=UTF-8";
        }
    }
}
