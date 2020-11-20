package com.countries;

import org.junit.experimental.categories.Category;
import org.testng.annotations.Test;

public class TestRunner {

    @Test
    public void testSample(){
        Countries countries = new Countries();
		countries.getInputFromUser();
    }

//    public static void main(String[] args) {
//        Countries countries = new Countries();
//		countries.getInputFromUser();
//    }
}
