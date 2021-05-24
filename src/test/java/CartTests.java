import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class CartTests {

    @BeforeAll
    static void setup() {
        RestAssured.baseURI = "http://demowebshop.tricentis.com";
        Configuration.startMaximized = true;
        Configuration.baseUrl = "http://demowebshop.tricentis.com";
    }

    @Test
    void addItemToCartAsExistUserTest() {
        String response =
                given()
                        .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                        .body("product_attribute_74_5_26=81&product_attribute_74_6_27=85&product_attribute_74_3_28=" +
                        "86&product_attribute_74_8_29=88&addtocart_74.EnteredQuantity=1")
                        .cookie("Nop.customer", "e1ff71d8-07d7-48c0-abb7-b8d3121cc413")
                .when()
                        .post("/addproducttocart/details/74/1")
                .then()
                        .statusCode(200)
                        .log().body()
                        .body("success", is(true))
                        .extract().response()
                        .jsonPath().get("updatetopcartsectionhtml");

        int productSize = Integer.parseInt(response.substring(1, response.length() - 1));


        given()
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .body("product_attribute_74_5_26=81&product_attribute_74_6_27=85&product_attribute_74_3_28=" +
                        "86&product_attribute_74_8_29=88&addtocart_74.EnteredQuantity=1")
                    .cookie("Nop.customer", "e1ff71d8-07d7-48c0-abb7-b8d3121cc413")
            .when()
                    .post("/addproducttocart/details/74/1")
            .then()
                .statusCode(200)
                .log().body()
                .body("success", is(true))
                    .body("updatetopcartsectionhtml", is("(" + (productSize + 1) + ")"));
    }

    @Test
    void addItemToCartTest() {
        open("/build-your-own-expensive-computer-2");
        $("#add-to-cart-button-74").click();
        $("#topcartlink .ico-cart").shouldHave(text("(1)"));
        String nopCustomerCookie = WebDriverRunner.getWebDriver().manage().getCookieNamed("Nop.customer").getValue();

        given()
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .body("product_attribute_74_5_26=81&product_attribute_74_6_27=85&product_attribute_74_3_28=" +
                        "86&product_attribute_74_8_29=88&addtocart_74.EnteredQuantity=1")
                .cookie("Nop.customer", nopCustomerCookie)
        .when()
                .post("/addproducttocart/details/74/1")
        .then()
                .statusCode(200)
                .log().body()
                .body("success", is(true))
                .body("updatetopcartsectionhtml", is("(2)"));

        Selenide.refresh();
        $("#topcartlink .ico-cart").shouldHave(text("(2)"));
    }
}
