package com.automation.steps;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.automation.Pages.BasePage;
import com.automation.Pages.PageFactory;
import com.automation.mobile.MobilePageFactory;
import com.automation.mobile.MobilePages;


public class BaseSteps {

	public BasePage genericWeb;
	public MobilePages mobilePages;

	



	public BaseSteps(PageFactory PageFactory) throws ParserConfigurationException, TransformerException {
		genericWeb = PageFactory.NewGenericWeb();

	}
	public BaseSteps(MobilePageFactory mobilePageFactory) throws ParserConfigurationException, TransformerException {
		mobilePages = mobilePageFactory.NewMobilePages();
	}
}
