package com.leandroperez.taf.sut.mobile.pom.step;

import com.leandroperez.taf.core.Session;
import com.leandroperez.taf.core.enumerator.PlatformInTest;
import com.leandroperez.taf.core.enumerator.TestExecutionStrategy;
import com.leandroperez.taf.sut.mobile.pom.page.ExampleMobilePage;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;


public class ExampleMobileStep {

    Session session = new Session();
    ExampleMobilePage exampleMobilePage;

    @Given("an example scenario of mobile app")
    public void anExampleScenarioOfMobileApp() {
        System.out.println("bbbbbb aaaaaaaa aaaaaaaaaa aaaaaaaaaaa aaaaaaaaaaaa aaaaaaaaaaaaaaaaa");
        System.out.println("bbbbbb aaaaaaaa aaaaaaaaaa aaaaaaaaaaa aaaaaaaaaaaa aaaaaaaaaaaaaaaaa");
        System.out.println("bbbbbb aaaaaaaa aaaaaaaaaa aaaaaaaaaaa aaaaaaaaaaaa aaaaaaaaaaaaaaaaa");
        System.out.println("bbbbbb aaaaaaaa aaaaaaaaaa aaaaaaaaaaa aaaaaaaaaaaa aaaaaaaaaaaaaaaaa");

    }

    @Before
    public void initSession() {
        System.out.println("aaaaaa aaaaaaaa aaaaaaaaaa aaaaaaaaaaa aaaaaaaaaaaa aaaaaaaaaaaaaaaaa");
        try{
            session.startSession(PlatformInTest.ANDROID, TestExecutionStrategy.LOCAL);
            exampleMobilePage = new ExampleMobilePage();
        } catch (Exception e) {
            System.out.println("initSession()" + "\n" + "Error: " + e);

        }
    }

    @After
    public void closeSession() {
        System.out.println("cccccccc aaaaaaaa aaaaaaaaaa aaaaaaaaaaa aaaaaaaaaaaa aaaaaaaaaaaaaaaaa");
        try{
            session.closeSession();
        } catch (Exception e){
            System.out.println("closeSession()" + "\n" + "Error: " + e);
        }
    }
}
