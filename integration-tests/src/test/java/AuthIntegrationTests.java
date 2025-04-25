import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class AuthIntegrationTests {
    @BeforeAll
    static void setup() {
        RestAssured.baseURI="http://localhost:4009";
    }
    @Test
    public void shouldReturnOkWithValidToken(){
        // 1. Arrange
        // 2 Act
        // 3 Assert

        String loginPayLoad ="""
                {
                    "email":"asif@gmail.com",
                    "password":"asifebrahim"
                }
                """;

        Response response = given()
                .contentType("application/json")
                .body(loginPayLoad)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().response();

        System.out.println("Token : "+response.jsonPath().getString("token"));
    }



    @Test
    public void shouldReturnUnAuthorizedWithInValidLogin(){
        // 1. Arrange
        // 2 Act
        // 3 Assert
        // Wrong password right one is asifebrahim
        String loginPayLoad ="""
                {
                    "email":"asif@gmail.com",
                    "password":"asifebrahim1"             
                }
                """;

        given()
                .contentType("application/json")
                .body(loginPayLoad)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);

            // We removed so many things because we are not working on the response here so first removed Response response
            // and then removed the System.out.println() statement then the .body() as we are not storeing the token and extract as we dont need to extract the token


    }

    @Test
    public void shouldReturnOkWIthValidToken1(){
        String Payload= """
                {
                    "email":"asifebrahim13@gmail.com",
                    "password":"asifebrahim"
                }
                
                """;

        Response response=given()
                .contentType("application/json")
                .body(Payload)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token",notNullValue())
                .extract().response();

        System.out.println("Token :"+response.jsonPath().getString("token"));
    }
    @Test
    public void shouldReturnUnAuthorizedWIthInValidLogin1(){
        String payLoad= """
                {
                    "email":"lawraisGreat69@lawra.com",
                    "password":"WhyaskingAss"
                }
                
                """;

        given().
                contentType("application/json")
                .body(payLoad)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401);
    }


}
