package gradle.junit.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import model.AuthResponse;
import model.Authentication;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import java.time.Duration;
import java.util.List;

import static constants.Endpoints.*;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.put;
import static org.hamcrest.Matchers.*;

class JuiceTest {

    static WebDriver driver;

    static Customer customer;
    public static RequestSpecification OWASP_RequestSpec;

    public static ResponseSpecification OWASP_ResponseSpec;


    @BeforeAll
    static void setup() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();

        //TODO Task1: Add your credentials to customer i.e. email, password and security answer.
        customer = new Customer.Builder().build();

        OWASP_RequestSpec = new RequestSpecBuilder().
                setBaseUri(baseUrl).
                addHeader("Content-Type", "application/json").
                addHeader("Accept", "application/json").
                build();
        RestAssured.requestSpecification = OWASP_RequestSpec;

        OWASP_ResponseSpec = new ResponseSpecBuilder().
                expectContentType(ContentType.JSON).
                build();
        RestAssured.responseSpecification = OWASP_ResponseSpec;
    }

    @Test
    void loginAndPostProductReviewViaUi() {
        dismissPopUp();
        registerCustomer();
        loginWithCredentials();
        pickAnItemForReview();
        addReviews();
    }

    public void dismissPopUp() {
        driver.get(baseUrl + "/#/login");
        driver.manage().window().fullscreen();
        WebElement welcomeBanner = driver.findElement(By.xpath("//*[@id=\"mat-dialog-0\"]/app-welcome-banner/div/div[2]/button[2]"));
        welcomeBanner.click();
    }

    public boolean myAccountDisplayed() {
        WebElement myAccount = driver.findElement(By.id("navbarAccount"));
        return myAccount.isDisplayed();
    }

    /*
     * Register a new customer
     *
     */
    public void registerCustomer() {

        WebElement newCustomerLink = driver.findElement(By.id("newCustomerLink"));
        newCustomerLink.click();

        WebElement customerRegistrationEmail = driver.findElement(By.id("emailControl"));
        customerRegistrationEmail.sendKeys(customer.getEmail());

        WebElement customerRegistrationPassword = driver.findElement(By.id("passwordControl"));
        customerRegistrationPassword.sendKeys(customer.getPassword());

        WebElement registrationRepeatPassword = driver.findElement(By.id("repeatPasswordControl"));
        registrationRepeatPassword.sendKeys(customer.getPassword());

        WebElement securityQuestionDropDown = driver.findElement(By.className("mat-select-arrow"));
        securityQuestionDropDown.click();

        WebElement selectOption = driver.findElement(By.xpath("//*[@id='mat-option-4']/span"));
        selectOption.click();

        WebElement securityAnswerTextField = driver.findElement(By.id("securityAnswerControl"));
        securityAnswerTextField.sendKeys("SampleTest");

        WebElement registerButton = driver.findElement(By.id("registerButton"));
        Actions actions = new Actions(driver);
        actions.moveToElement(registerButton);
        actions.perform();
        registerButton.click();
    }

    /*
     * Login with Credentials
     *
     */
    public void loginWithCredentials() {

        WebElement navigationBarAccount = driver.findElement(By.id("navbarAccount"));
        navigationBarAccount.click();

        WebElement navigationBarLogin = driver.findElement(By.id("navbarLoginButton"));
        navigationBarLogin.click();

        WebElement emailField = driver.findElement(By.name("email"));
        emailField.click();
        emailField.sendKeys(customer.getEmail());

        WebElement passwordField = driver.findElement(By.name("password"));
        WebElement loginButton = driver.findElement(By.id("loginButton"));

        passwordField.sendKeys(customer.getPassword());
        loginButton.click();
        Assertions.assertTrue(myAccountDisplayed());
    }

    // TODO Navigate to product and post review
    public void pickAnItemForReview() {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        List<WebElement> firstImage = driver.findElements(By.xpath("(//div[@class='item-name'])"));
        for (WebElement opt : firstImage) {
            if (opt.getText().equals("Apple Pomace")) {
                opt.click();
            }
        }
    }

    // TODO Navigate to product and post review
    public void addReviews() {
        String reviewComments = "Good One";

        List<WebElement> reviewCountInitial = driver.findElements(By.xpath("(//div[contains(@class,'comment ng-star-inserted')])"));
        int countBeforeReview = reviewCountInitial.size();

        WebElement reviewTextField = driver.findElement(By.xpath("(//textarea[contains(@aria-label,'Text field to review a product')])"));
        reviewTextField.sendKeys(reviewComments);
        WebElement submitReviewButton = driver.findElement(By.id("submitButton"));
        submitReviewButton.click();

        // TODO Assert that the review has been created successfully

        /*
        Asserting the review count before and after
         */
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        List<WebElement> reviewCountAfter = driver.findElements(By.xpath("(//div[contains(@class,'comment ng-star-inserted')])"));
        int countAfterReview = reviewCountAfter.size();
        Assertions.assertEquals(countAfterReview, countBeforeReview + 1);

        WebElement reviewPanel = driver.findElement(By.id("mat-expansion-panel-header-0"));
        reviewPanel.click();

        /*
        Asserting the review comments from my email id
         */
        List<WebElement> myReviewComments = driver.findElements(By.xpath("//div[contains(@class, 'mat-tooltip-trigger review-text')]"));
        for (WebElement opt : myReviewComments) {
            if (opt.getText().contains(customer.getEmail())) {
                Assertions.assertTrue(opt.getText().contains(reviewComments));
            }
        }
    }

    @AfterAll
    static void teardown() {
        driver.quit();
    }

    // TODO Task3: Login and post a product review using the Juice Shop API
    // TODO Retrieve token via login API
    /*
    Create Request body for a POST method , Using simple json dependency with needed params (email & password)
    and post it to the endpoint : http://localhost:3000/rest/users/login
    Save the json response onto a POJO class called AutResponse -> Authentication class for getting the token to the add review call
    Product id: Picking the first id from the response of http://localhost:300/rest/products/search
     */
    @Test
    void loginViaAPI() {
        String message = "Good One";
        JSONObject loginRequestBody = new JSONObject();
        loginRequestBody.put("email", customer.getEmail());
        loginRequestBody.put("password", customer.getPassword());
        Authentication authentication =
                given().spec(OWASP_RequestSpec)
                        .body(loginRequestBody)
                        .post(login)
                        .then().assertThat().statusCode(200)
                        .extract().response().as(AuthResponse.class).getAuthentication();

        // Product ID
        int productId = given().spec(OWASP_RequestSpec)
                .when()
                .get(searchAllProducts)
                .then()
                .assertThat().statusCode(200)
                .body("data", is(not(empty())))
                .extract().path("data[0].id");

        PostReviewViaAPI(productId, authentication.getToken(), message);
        validateReviewComment(productId, message);
    }

    // TODO Use token to post review to product
    private Response PostReviewViaAPI(int productId, String token, String message) {
        JSONObject postReviewRequestBody = new JSONObject();
        postReviewRequestBody.put("message", message);
        postReviewRequestBody.put("author", customer.getEmail());

        return given().spec(OWASP_RequestSpec)
                .header("Authorization", "Bearer" + token)
                .pathParam("product-id", productId)
                .body(postReviewRequestBody)
                .put(reviewEndpoint);
    }


    // TODO Assert that the product review has persisted
    private void validateReviewComment(int productId, String message) {
        given()
                .spec(OWASP_RequestSpec)
                .pathParam("product-id", productId)
                .when()
                .get(reviewEndpoint).then().assertThat()
                .body("data.message", hasItem(message), "data.author", hasItem(customer.getEmail()));
    }
}

