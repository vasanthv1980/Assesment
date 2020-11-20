package com.countries;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Countries {


    RequestSpecification request = null;
    Response response = null;
    public final String GET_BY_COUNTRY_NAME = "/name/{countryname}";
    public final String GET_BY_COUNTRY_CODE = "/alpha/{countrycode}";
    public String baseURI = "https://restcountries.eu/rest/v2/";

    @Test
    public void getInputFromUser() {

        List<String> inputType = new ArrayList<>();
        RestAssured.baseURI = "https://restcountries.eu/rest/v2/";
        inputType.add("All");
        inputType.add("Name");
        inputType.add("Code");
        inputType.add("Exit");

        printOptions(inputType);

        String countryName = "", countryCode = "";
        boolean loop = true;
        Scanner scanner = new Scanner(System.in);
        while (loop == true) {

            System.out.print("Please choose 1 to 4 for any one of the above option : ");
            String input = scanner.next();
            switch (input) {
                case "1":
                    System.out.println("You have selected 1.All : Getting data for all counties");
                    callRestEndPointForAll();
                    loop = true;
                    break;
                case "2":
                    getByname(countryName, scanner, inputType);
                    break;
                case "3":
                    getByCode(countryCode, scanner, inputType);
                    break;
                case "4":
                    System.out.println("User opted to exit");
                    loop = false;
                    break;
                default:
                    System.out.println("Invalid option. ");
                    printOptions(inputType);
                    break;
            }
        }
    }

    public void getByname(String countryName, Scanner scanner, List<String> inputType) {
        System.out.print("You have selected 2.Name : Please enter the country name : ");
        countryName = scanner.next();
        response = callRestEndPointByName(countryName);
        int i = 1;
        while (response.statusCode() == 404 && i < 3) {
            System.out.print("Nothing found for the given Input, please enter valid Country name : ");
            countryName = scanner.next();
            response = callRestEndPointByName(countryName);
            i++;
            if (i == 3) {
                System.out.println("Entered wrong input multiple times. Exiting this feature.... ");
                printOptions(inputType);
                break;
            }
        }
    }

    public void getByCode(String countryCode, Scanner scanner, List<String> inputType) {
        System.out.print("You have selected 3.Code : Please enter the country code : ");
        countryCode = scanner.next();
        response = callRestEndPointByCode(countryCode);
//        response.then().log().all();
        int i = 1;
        while (response.statusCode() == 404 && i < 3) {
            System.out.print("Nothing found for the given Input, please enter valid Country Code : ");
            countryCode = scanner.next();
            response = callRestEndPointByName(countryCode);
            i++;
            if (i == 3) {
                System.out.println("Entered wrong input multiple times. Exiting this feature.... ");
                printOptions(inputType);
                break;
            }
        }
    }

    public void callRestEndPointForAll() {
        request = RestAssured.given().baseUri(baseURI).basePath("all");
        callRestEndPoint(request);
    }

    public Response callRestEndPointByName(String countryName) {
        request = RestAssured.given().baseUri(baseURI).basePath(GET_BY_COUNTRY_NAME).pathParam("countryname", countryName);
        return callRestEndPoint(request);
    }
    public Response callRestEndPointByCode(String countryCode) {
        request = RestAssured.given().baseUri(baseURI).basePath(GET_BY_COUNTRY_CODE).pathParam("countrycode", countryCode);
        return callRestEndPoint(request);
    }

    public Response callRestEndPoint(RequestSpecification request) {
        Response response = request.get();
//        response.then().log().all();
        if (response.statusCode() == 200) {
            List<String> country = response.then().extract().path("name");
            List<String> capital = response.then().extract().path("capital");
            for (int i = 0; i < capital.size(); i++) {
                System.out.println("Capital of " + capital.get(i) + " is : " + capital.get(i));
            }
        }
        return response;
    }

    public static void printOptions(List<String> inputType) {
        for (int i = 0; i < inputType.size(); i++) {
            System.out.println(i + 1 + ". " + inputType.get(i));
        }
    }
}
