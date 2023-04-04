package com.automation.mobile;

import java.util.ArrayList;
import java.util.List;

import com.automation.utils.UtilProperties;

import io.appium.java_client.MobileElement;

public class MobilePages extends GenericMobile {
	public MobilePages(MobileDriverProviderCreator driverPorvider) {
		super(driverPorvider);
	}

	public void openApp() {
		// genericMobile.openApp();

	}

	 private String getProperty(String name) {
	 return UtilProperties.getInstance().getProperty(name);
	 }

	public Boolean mobileElementDisplay(String XPath) {
		return wait(for_path(getProperty(XPath))).isDisplayed();

	}

	public void fillMoblileTextbox(String XPath, String vlaue) {
		wait(for_path(getProperty(XPath))).clear();
		wait(for_path(getProperty(XPath))).sendKeys(vlaue);
		hideKeyboard();

	}

	public void tapOnmobileElement(String XPath) {
		wait(for_path(getProperty(XPath))).click();

	}

	public void elementClick(String Text, String Selector) {
		boolean findOption = false;
		int counter = 0;
		List<MobileElement> allElements = new ArrayList<MobileElement>();
		if (Selector == "") {
			wait(for_text(Text)).isDisplayed();
		} else {
			while (!findOption) {
				allElements.clear();
				allElements = elements(for_path(getProperty(Selector)));

				if (allElements.size() > 0) {

					for (int i = 0; i < allElements.size(); i++) {
						System.out.println("Searching For: " + "(" + Text.toLowerCase() + ")" + "   Actual Value: "
								+ "(" + allElements.get(i).getText().trim().toLowerCase() + ")");
						if (allElements.get(i).getText().toLowerCase().contains(Text.toLowerCase().trim())) {
							findOption = true;
							System.out.println(allElements.get(i).getText().trim().toLowerCase());
							allElements.get(i).click();

						}

						if (findOption == true) {
							break;
						}

						if (i == allElements.size() - 1) {
							counter++;
							int screenHeight = currentDriver().manage().window().getSize().getHeight();
							int screenWidth = currentDriver().manage().window().getSize().getWidth();
							currentDriver().swipe((int) (screenWidth * 0.3), (int) (screenHeight * 0.9),
									(int) (screenWidth * 0.3), 0, 400);
							sleepTime(2000);
						}

					}

				} else {
					sleepTime(3000);
					counter++;
				}

				if (counter == 15) {
					findOption = true;
					element(for_find(Text)).click();
					break;
				}
			}
		}

	}

	 public void SwipeSlider() {
		  int screenHeight = currentDriver().manage().window().getSize().getHeight();
		  int screenWidth = currentDriver().manage().window().getSize().getWidth();
		  currentDriver().swipe((int) (screenWidth * 0.05),(int) (screenHeight * 0.96), (int) (screenWidth * 0.98),(int) (screenHeight * 0.96) ,1000);  
		 }
	 
	public List<String> GetElementText(String Element) {
		List<String> awbs = new ArrayList<String>();
		
		List<MobileElement> allElements = new ArrayList<MobileElement>();
		allElements = elements(for_path(getProperty(Element)));
		for (MobileElement elemnt : allElements) {
			awbs.add(elemnt.getText().trim());
		}
		return awbs;

	}

}
