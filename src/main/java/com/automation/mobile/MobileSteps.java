package com.automation.mobile;

import static org.junit.Assert.assertThat;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.hamcrest.Matchers;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import com.automation.steps.BaseSteps;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;

public class MobileSteps extends BaseSteps{
public List<String> AWB;


public MobileSteps(MobilePageFactory MobilePageFactory) throws ParserConfigurationException, TransformerException {
		super(MobilePageFactory);
		// TODO Auto-generated constructor stub
	}



	
}
