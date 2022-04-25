# Equal Experts: Java offline exercise

## Solved by: *please replace this with your full name*

## Pre-requisites
Candidates should have a recent version of the following installed:
- [java](https://java.com/en/download/help/download_options.html) (project tested with OpenJdk v11.0.13)
- [docker](https://docs.docker.com/get-docker/)

## Starting the Application
Execute `./run.sh` from the project root.  If you are using Windows then run `docker run -d --rm -p 3000:3000 bkimminich/juice-shop:v13.2.0`.  The service can be reached via a browser [here](http://localhost:3000/)

## Running the tests
Unfortunately because GMail won't allow us to include files with certain extensions we can't include the Gradle wrapper with our code. This means before running the test you will need to:
* Install Gradle on your machine. Instructions can be found [here](https://gradle.org/install/).
* After Gradle is installed run `gradle wrapper` from the root of this project.

After this you should be able to run the test with `./gradlew test` from the project root.

## Tasks to complete
Assume that this is an existing test framework within a company that leans heavily on Selenium WebDriver and RestAssured. You are required to complete the **three** tasks described below to the best your abilities.  We ask that you take a couple of hours to complete the exercise, but feel free to take a little more time if necessary.  Your code should be well-structured and easy to follow.  Don't forget to include assertions.

When you review the project you will find:
* `JuiceTest.java` which contains a basic template for the below three tasks. 
* `Customer.java` which is a basic Customer object to represent a user.

The project is already configured with [Selenium](https://www.selenium.dev/), [WebDriverManager](https://bonigarcia.dev/webdrivermanager/) and [REST Assured](https://github.com/rest-assured/rest-assured/wiki/Usage). Feel free to add dependencies for any additional libraries that you require to complete the exercise.

### Things we expect to see
* Tests that run **successfully** out of the box.
* Be sure to include assertions in the tests.
* Tidy code.
* Well commented code if required, particularly if you made interesting choices.

### Task 1: Create customer
Start the JuiceShop application and access it via a browser.  Manually register a user taking note of the email and password values you provide.  Record those value in the Customer object in the setup method of `JuiceTest.java`.

_Note: the application only persists users in memory.  If the container dies you will need to repeat this step to get your tests passing._

### Task 2: UI test
Using the credentials you recorded in Task 1, automate the Review Posting journey (Login is provided) via the UI.  Don't forget to assert that your review appears on the appropriate screen.

### Task 3: API test
Using the credentials you recorded in Task 1, automate the Juice Shop Login (to get a authentication token) and Review Posting journeys via the API.  We've provided some [API documentation](JuiceShopApiReference.md) to help.

## Ways to submit your solution
The solution you submit should:
* Include the original code that we sent you.
* Not include the dependencies downloaded by `gradle`.
* Include your solution to the tasks.
* Any instructions/comments that you would like to include. If you do include any additional documentation please make us aware of it so it doesn't get missed. For example, it could be worth mentioning if you have built a solution that runs on Windows or requires a specific version of Java. 

### Method 1
Push your solution to a private repository on Github or Gitlab and give access to the following email addresses:
* lim.sim@equalexperts.com
* adrian.rapan@equalexperts.com
* dan.prokopiwskyi@equalexperts.com

### Method 2
Zip up your solution and email it to the EE People Team that you are in contact with.

Do remember to check that you included/excluded the things mentioned above when you are zipping up your solution. 

