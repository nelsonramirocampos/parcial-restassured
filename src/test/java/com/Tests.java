package com;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.JsonFormatter;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;

public class Tests {
    static ExtentSparkReporter spark;
    static ExtentReports extent;

    @BeforeAll
    public static void init(){
        spark = new ExtentSparkReporter("target/reports/" + Tests.class.getName() + ".html");
        extent = new ExtentReports();

        extent.attachReporter(spark);
    }

    @Test
    @Tag("Smoke")
    @Tag("Integration")
    @DisplayName("TC - Registro")
    public void test01() {
        ExtentTest test = extent.createTest("TC - Registro");

        test.info("Inicio de test");

        Response response =
                RestAssured
                        .given()
                        .log().all()
                        .when()
                        .get("https://parabank.parasoft.com/parabank/register.htm");

        
        test.log(Status.INFO, "Response Body: " + response.body().print());

        if (HttpStatus.SC_OK == response.statusCode()) {
            test.pass("Status code 200");
        } else {
            test.fail("Status code distinto a 200 ==> Status Code: " + response.statusCode());
        }

        Assertions.assertTrue(HttpStatus.SC_OK ==response.statusCode(), "Status code is 200");
    }

    @Test
    @Tag("Smoke")
    @DisplayName("TC - Abrir una nueva cuenta")
    public void test02(){
        ExtentTest test = extent.createTest("TC - Abrir una nueva cuenta");
        test.info("Inicio test 02");
        
        //Falta autorizacion
        Response response =
        RestAssured
                .given()
                .log().all()
                //.auth().basic("pepe", "pepe")
                .queryParam("customerId", "12545")
                .queryParam("newAccountType", "1")
                .queryParam("fromAccountId", "124123")
                .when()
                .post("https://parabank.parasoft.com/parabank/services_proxy/bank/createAccount");


        test.log(Status.INFO, "Response Body: " + response.body().print());

        if (HttpStatus.SC_OK == response.statusCode()) {
            test.pass("Status code 200");
        } else {
            test.fail("Status code distinto a 200 ==> Status Code: " + response.statusCode());
        }

        Assertions.assertTrue(HttpStatus.SC_OK ==response.statusCode(), "Status code is 200");
    }

    @Test
    @Tag("Smoke")
    @Tag("Integration")
    @DisplayName("TC - Resumen de las cuentas")
    public void test03() {
        ExtentTest test = extent.createTest("TC - Resumen de las cuentas");

        test.info("Inicio de test");

        //Ver error 500
        Response response =
                RestAssured
                        .given()
                        .log().all()
                        .when()
                        .get("https://parabank.parasoft.com/parabank/overview.htm");

        test.log(Status.INFO, "Response Body: " + response.body().print());

        if (HttpStatus.SC_OK == response.statusCode()) {
            test.pass("Status code 200");
        } else {
            test.fail("Status code distinto a 200 ==> Status Code: " + response.statusCode());
        }

        Assertions.assertTrue(HttpStatus.SC_OK ==response.statusCode(), "Status code is 200");
    }

    @Test
    @Tag("Smoke")
    @DisplayName("TC - Descarga de fondos")
    public void test04() {
        ExtentTest test = extent.createTest("TC - Descarga de fondos");

        test.info("Inicio de test");

        //Falta autorizacion
        Response response =
                RestAssured
                        .given()
                        .log().all()
                        //.auth().basic("a", "a")
                        .queryParam("fromAccountId", "13566")
                        .queryParam("toAccountId", "13677")
                        .queryParam("amount","100")
                        .when()
                        .post("https://parabank.parasoft.com/parabank/services_proxy/bank/transfer");

        test.log(Status.INFO, "Response Body: " + response.body().print());

        if (HttpStatus.SC_OK == response.statusCode()) {
            test.pass("Status code 200");
        } else {
            test.fail("Status code distinto a 200 ==> Status Code: " + response.statusCode());
        }

        Assertions.assertTrue(HttpStatus.SC_OK ==response.statusCode(), "Status code is 200");
    }

    @Test
    @Tag("Integration")
    @DisplayName("TC - Actividad de la cuenta (cada mes)")
    public void test05(){
        ExtentTest test = extent.createTest("TC - Actividad de la cuenta (cada mes)");

        test.info("Inicio de test");

        //Falta autorizacion
        Response response =
                RestAssured
                        .given()
                        .log().all()
                        //.auth().basic("a", "a")
                        .when()
                        .get("https://parabank.parasoft.com/parabank/services_proxy/bank/accounts/13566/transactions/month/All/type/Al");

        test.log(Status.INFO, "Response Body: " + response.body().print());

        if (HttpStatus.SC_OK == response.statusCode()) {
            test.pass("Status code 200");
        } else {
            test.fail("Status code distinto a 200 ==> Status Code: " + response.statusCode());
        }

        Assertions.assertTrue(HttpStatus.SC_OK ==response.statusCode(), "Status code is 200");
    }

    @AfterAll
    public static void finalTest(){
        extent.flush();
    }
}