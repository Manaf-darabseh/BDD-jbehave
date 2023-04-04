package com.automation.Pages;

import org.jbehave.web.selenium.WebDriverProvider;

/**
 * Factory that is used to instantiate desired page object
 */
public class PageFactory {

	private final WebDriverProvider webDriverProvider;

	public PageFactory(WebDriverProvider webDriverProvider) {
		this.webDriverProvider = webDriverProvider;
	}
	
	public BasePage NewGenericWeb() {
		return new BasePage(webDriverProvider);
	}
	
	
}
