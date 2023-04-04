package com.automation.Pages;


import org.jbehave.web.selenium.FluentWebDriverPage;
import org.jbehave.web.selenium.WebDriverProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.manager.MYSQLConnectionManager;
import com.automation.utils.UtilProperties;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

public class BasePage extends FluentWebDriverPage {

	public BasePage(WebDriverProvider driverProvider) {
		super(driverProvider);
	}
	
		
	public void go(String url) {
			get(url);
			getDriverProvider().get().manage().window().maximize();
	}

	/*
	 * The following function will be used to fill the data in a text box using
	 * xpath or css which already exist in xpath.properties file or css.properties
	 */
	public void fillTextBox(String element, String value) {
		savedWebElement(element).clear();
		savedWebElement(element).sendKeys(value);
	}
	/*
	 * End of function
	 */

	/*
	 * The following function will be used to click on using xpath or css which
	 * already exist in xpath.properties file or css.properties
	 */

	public void clickBy(String selector) {

		// Click on element
		waitElementToBeClickable(selector);
		savedWebElement(selector).click();
	}
	/*
	 * End of function
	 */

	/*
	 * The following function will be used to click on using JS which already
	 * exist in xpath.properties file or css.properties
	 */
	public void clickByJS(String selector) {

		String elementLocation = UtilProperties.getInstance().getProperty(selector);
		// Save CSS WebEelement in string
		By condition = By.cssSelector(elementLocation);
		WebElement foundElement = findElement(condition);
		// Click on element using JS
//		JavascriptExecutor executor = (JavascriptExecutor) getDriverProvider().get();
//		executor.executeScript("arguments[0].click();", foundElement);
		
		
		
		Actions actions = new Actions(getDriverProvider().get());
		actions.moveToElement(foundElement).click().perform();
	}

	/*
	 * End of function
	 */
	
	/*
	 * Wiat till page fully load
	 *
	 */
	public void waitPageToLoad() {
		while (true) {

			if (executeScript("return document.readyState").toString().equals("complete")) {
				break;
			}
			if( (Boolean) executeScript("return jQuery.active == 0")) {
				break;
			}

			try {
				Thread.sleep(6000);
			} catch (InterruptedException e) {
				// Restore interrupted state...
				Thread.currentThread().interrupt();
			}
		}
	}
	/*
	 * End of function
	 */

	/*
	 * Check if the element is displayed
	 */

	public Boolean elementDisplays(String selector) {

		WebElement foundElement = savedWebElement(selector);

		// if element is displayed return True else False
		boolean isDisplayed = (foundElement == null) ? false : foundElement.isDisplayed();
		return isDisplayed;
	}

	/*
	 * End of function
	 */

	/*
	 * The following function can get the text by css or xpath
	 */
	public String getText(String selector) {

		return savedWebElement(selector).getText();

	}
	/*
	 * End of function
	 */

	/*
	 * The following function can get the attribute
	 */
	public String getAttribute(String selector) {

		return savedWebElement(selector).getAttribute("onclick");
	}
	/*
	 * End of function
	 */

	/*
	 * The following function can get the attribute value
	 */
	public String getAttributeValue(String selector) {

		return savedWebElement(selector).getAttribute("value").toLowerCase();
	}
	/*
	 * End of function
	 */

	
	
	/*
	 * The following function can get the attribute value
	 */
	public boolean IsSelected(String selector) {

		return savedWebElement(selector).getAttribute("class").contains("selected");
			
	}
	/*
	 * End of function
	 */	
	
	
	
	/*
	 * The following function can get the attribute value Method is duplicated
	 */
	public WebElement getElementById(String selector) {
		WebDriverWait wait = new WebDriverWait(getDriverProvider().get(), 60);

		if (UtilProperties.getInstance().getProperty(selector).contains("//")) {
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.xpath(UtilProperties.getInstance().getProperty(selector))));
			return findElement(By.xpath(UtilProperties.getInstance().getProperty(selector)));
		} else {
			wait.until(ExpectedConditions
					.visibilityOfElementLocated(By.cssSelector(UtilProperties.getInstance().getProperty(selector))));
			return findElement(By.cssSelector(UtilProperties.getInstance().getProperty(selector)));
		}
	}
	/*
	 * End of function
	 */

	/*
	 * The following function is used to switch between tabs
	 */
	public void switchTab() {

		ArrayList<String> tabs2 = new ArrayList<String>(getWindowHandles());
		switchTo().window(tabs2.get(1));
	}
	/*
	 * End of function
	 */

	/*
	 * The following function is used to scroll down
	 */
	public void scrollDown(String selector) {

		// Scroll page Down until find element
		((JavascriptExecutor) getDriverProvider().get()).executeScript("arguments[0].scrollIntoView(true);",
				savedWebElement(selector));
	}
	/*
	 * End of function
	 */

	/*
	 * The following function is used to check if table is dispalyed
	 */
	public Boolean tableDisplayed(String selector) {

		try {
			findElement(By.cssSelector(UtilProperties.getInstance().getProperty(selector)));
			return true;
		} catch (org.openqa.selenium.NoSuchElementException e) {
			return false;
		}
	}
	/*
	 * End of function
	 */

	/*
	 * The following function is used to get query value from Database for dynamic
	 * selector
	 */
	public String testDBCPDataSource(String query, String selector, String column) {

		String[] selectors = checkString(selector).split(" -");
		String selector1 = selectors[0];
		// Inside query , replace XX with selector value
		String content = UtilProperties.getInstance().getProperty(query).replace("XX", selector1);

		// Save query value into list
		List<String> r = MYSQLConnectionManager.getInstance(UtilProperties.getInstance().getProperty("Country")).getListData(content, column);
		if (r != null) {
			return String.join(",", r);
		}
		return null;
	}
	/*
	 * End of function
	 */

	/*
	 * The following function is used to get value or text of webelement
	 */
	public String checkString(String Element1) {

		if ((savedWebElement(Element1)).getText().equals("")) {
			return getValue(Element1);
		} else {

			return bringText(Element1);
		}
	}
	/*
	 * End of function
	 */

	/*
	 * The following function is used to get value of webelement
	 */
	public String getValue(String element1) {

		return savedWebElement(element1).getAttribute("value");
	}
	/*
	 * End of function
	 */

	/*
	 * The following function is used to get text of webelement
	 */
	public String bringText(String element1) {

		return savedWebElement(element1).getText();
	}
	/*
	 * End of function
	 */

	/*
	 * The following function is used to retrieve value from database
	 */
	public String testDBCPDataSource(String query, String coulmn) {
		String content = UtilProperties.getInstance().getProperty(query);
		List<String> r = MYSQLConnectionManager.getInstance(UtilProperties.getInstance().getProperty("Country")).getListData(content, coulmn);
		if (r != null) {
			return String.join(",", r).replace("[", "").replace("]", "");
		}
		return null;
	}
	/*
	 * End of function
	 */

	/*
	 * The following function will be used to select item from (Li-ul) list this
	 * method is duplicated
	 */
	public void selectlistItem(String Text, String selector) {
		List<WebElement> listItems;

		// Save found elements in list
		listItems = listwebelement(selector);
		String Zones;
		String selector2 [];
		// Check all list elements
		for (WebElement item : listItems) {
			Zones = item.getText().toString();
			//String[] selectors = (item.getText());
			String selectors = Zones;
			String selector1 = selectors;
			selector2 = selector1.split(" ");
			String splittedValue = selector2[0];
		
			// check if given text match one of the list items ,then select it
			if (Text.equalsIgnoreCase(splittedValue)) {
				try {
					item.click();
					break;
				} catch (StaleElementReferenceException e) {
					item.click();
				}
			}
		}
	}
	/*
	 * End of function
	 */

	/*
	 * The following function will be used to select the splitted value from list
	 */
	public void splitlistItemValue(String Text, String selector) {
		List<WebElement> listItems;

		// Save found elements in list
		listItems = listwebelement(selector);
		String [] selectors;
		// Check all list element
		for (WebElement item : listItems) {
			selectors = item.getText().split(" ");
			String splittedValue = selectors[0];
		
			// check if given text match one of the list items ,then select it
			if (Text.contains(splittedValue))
			{
				try {
					item.click();
					break;
				} catch (StaleElementReferenceException e) {
					item.click();
				}
			}
		}
	}
	/*
	 * End of function
	 */

	
	
	/*
	 * The following function will be used to select item from (select/option) list
	 */
	public void selectListElement(String Text, String selector) {
		Select dropdown = null;

		// Save found element in drop down list
		dropdown = new Select(savedWebElement(selector));

		// Check all drop down list elements
		for (WebElement item : (dropdown.getOptions())) {

			// split the item text after space and save it in string
			String[] selectors = (item.getText()).split(" ");
			String selector1 = selectors[0];

			// If list element equals text , select text
			// if (Text.equalsIgnoreCase(selector1)) {
			if (selector1.toLowerCase().contains(Text.toLowerCase()))
				dropdown.selectByValue(Text);
			break;
		}
	}

	/*
	 * End of function
	 */

	/*
	 * The following function will be used to check item if not checked
	 */
	public void itemchecked(String selector) {

		// check item if item is not checked
		if (savedWebElement(selector).isSelected()) {
			return;
		} else {
			savedWebElement(selector).click();
		}
	}
	/*
	 * End of function
	 */



	public boolean referenceMatched(String element, String value) throws SQLException {
		List<WebElement> tableRecord = new ArrayList<WebElement>();
		List<String> records = new ArrayList<String>();
		WebElement debTable = getElementById(element);

		// Store the rows
		List<WebElement> rows = debTable.findElements(By.tagName("tr"));
		for (WebElement row : rows) {

			// Store the columns
			List<WebElement> cols = row.findElements(By.tagName("td"));

			// Check if the column contains value
			if (cols != null && cols.size() > 1) {
				// Add the column to TableRecord list
				tableRecord.add(cols.get(0));
			}
		}
		for (WebElement ColNumber : tableRecord) {
			// Add all AWBs to records list to use in assertion
			records.add(ColNumber.getAttribute("value"));
		}

		
		
		// Run this query to return the result with the sent ref.ID from database
		// to compare them with the result from the same page
		String query = UtilProperties.getInstance().getProperty("REFERENCE_QUERY");

		// To replace the value REF_ID in the query with the sent value
		query = query.replace("REF_ID", value);

		// To run the query and store the result in variable r
		List<String> r = MYSQLConnectionManager.getInstance(UtilProperties.getInstance().getProperty("Country")).getListData(query, "TRACKING_CODE");
		
		// Check if the retrieved result from the query are the same as result
			for (String record : records) {
				if(!r.contains(record)) {
					return false;	
				}
			}
		return true;
	}

	// Check the shipment if rejected
	public boolean checkRejection(String value, String actionValue) throws SQLException {

		// Run this query to return the result with the sent AWB
		String query = UtilProperties.getInstance().getProperty("CHECK_IF_REJECTED_QUERY");

		// To replace the value AWB in the query with the sent value
		query = query.replace("AWB", value);

		// To run the query and store the result in variable r
		List<String> r = MYSQLConnectionManager.getInstance(UtilProperties.getInstance().getProperty("Country")).getListData(query, "TERM_TYPE_ID");

		for (int i = 0; i < r.size(); i++) {
			// Check if the result from query are equals to the sent result
			if (r.get(i).equals(actionValue)) {
				return true;
			}
		}
		return false;
	}

	// Check the shipment if updated
	public boolean checkUpdate(String value) throws SQLException {

		// Run this query to return the result with the sent AWB
		String query = UtilProperties.getInstance().getProperty("CHECK_IF_UPDATED_QUERY");

		// To replace the value AWB in the query with the sent value
		query = query.replace("AWB", value);

		// To run the query and store the result in variable r
		List<String> r = MYSQLConnectionManager.getInstance(UtilProperties.getInstance().getProperty("Country")).getListData(query, "STATUS_ID");

		// Check if the result from query are equals to the sent result
		if (r.get(0).equals("CC_HOLD")) {
			return true;
		}
		return false;
	}

	public boolean checkNewRoute(String value, String destination) throws SQLException {

		// Run this query to return the result with the sent AWB
		String query = UtilProperties.getInstance().getProperty("CHECK_NEW_ROUTE_QUERY");

		// To replace the value AWB in the query with the sent value
		query = query.replace("CC_AWB", value);

		// To run the query and store the result in variable r
		List<String> r = MYSQLConnectionManager.getInstance(UtilProperties.getInstance().getProperty("Country")).getListData(query, "DELIVERY_FACILITY_ID");

		// Check if the result from query are equals to the sent result
		if (r.get(0).equals(destination)) {
			return true;
		}
		return false;
	}

	/*
	 * The following function will be used to fill textbox with value from Database
	 */
	public String fillTextboxFromDB(String selector, String value, String column) {
		// To get the query
		String query = UtilProperties.getInstance().getProperty(value);

		// To run the query and store the result in variable r
		List<String> r = MYSQLConnectionManager.getInstance(UtilProperties.getInstance().getProperty("Country")).getListData(query, column);

		// To get first AWB
		String result = r.get(0);

		savedWebElement(selector).clear();
		savedWebElement(selector).sendKeys(result);
		return result;
	}
	/*
	 * End of function
	 */

	/*
	 * The following function will be used to pick date
	 */
	public void pikDate(String selector, String value) {
		WebElement dateWidget;
		List<WebElement> columns;

		dateWidget = savedWebElement(selector);
		columns = dateWidget.findElements(By.tagName("a"));

		for (WebElement cell : columns) {
			// Select Date
			if (cell.getText().equals(value)) {
				cell.click();
				break;
			}
		}
	}
	/*
	 * End of function
	 */

	/*
	 * The following function will be used to close the new tap
	 */
	public void CloseNewTap() {

		// Add windows into array, then close the new window and switch back to
		// the
		// previous window
		ArrayList<String> tabs2 = new ArrayList<String>(getDriverProvider().get().getWindowHandles());
		getDriverProvider().get().switchTo().window(tabs2.get(tabs2.size() - 1));
		getDriverProvider().get().close();
		getDriverProvider().get().switchTo().window(tabs2.get(0));
	}
	/*
	 * End of function
	 */

	/*
	 * The following function will be used to switch to another frame
	 */
	public void switchToFrame() {

		// Get all frames(tabs) and save them in array list
		try {
			Thread.sleep(600);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<String> tabs2 = new ArrayList<String>(getDriverProvider().get().getWindowHandles());

		// Switch to the previous Frame(tab)
		getDriverProvider().get().switchTo().window(tabs2.get(tabs2.size() - 1));
	}
	/*
	 * End of function
	 */
	
	/*
	 * The following function will be used to switch to another pop up
	 */
	public void switchToPopUp() {

		String parentWindowHandler = getDriverProvider().get().getWindowHandle(); // Store your parent window
		String subWindowHandler = null;

		Set<String> handles = getDriverProvider().get().getWindowHandles(); // get all window handles
		Iterator<String> iterator = handles.iterator();
		while (iterator.hasNext()){
		    subWindowHandler = iterator.next();
		}
		getDriverProvider().get().switchTo().window(subWindowHandler); // switch to popup window

		// Now you are in the popup window, perform necessary actions here

		getDriverProvider().get().switchTo().window(parentWindowHandler);  // switch back to parent window
	}
	/*
	 * End of function
	 */
	

	/*
	 * The following function will be used to check if WebElement is displayed or
	 * not
	 */
	public WebElement savedWebElement(String selector) {
		WebDriverWait Wait = new WebDriverWait(getDriverProvider().get(), 60);

		String elementLocation = UtilProperties.getInstance().getProperty(selector);

		// Save CSS WebEelement in string
		By condition = By.cssSelector(elementLocation);

		// Override the condition string with xpath webElement
		if (elementLocation.contains("//")) {
			condition = By.xpath(elementLocation);
		}

		// Wait until element is visible
		Wait.until(ExpectedConditions.visibilityOfElementLocated(condition));

		// Save found element in WebElement
		WebElement foundElement = findElement(condition);

		// boolean isDisplayed =(foundElement==null) ? false :
		// foundElement.isDisplayed();
		// return isDisplayed;
		return foundElement;
	}
	/*
	 * End of function
	 */

	/*
	 * The following function will be used to check if ListElements is displayed or
	 * not
	 */
	public List<WebElement> listwebelement(String selector) {
		WebDriverWait Wait = new WebDriverWait(getDriverProvider().get(), 60);
		List<WebElement> listItems;

		String elementLocation = UtilProperties.getInstance().getProperty(selector);

		// Save CSS WebEelement in string
		By condition = By.cssSelector(elementLocation);

		// Override the condition string with xpath webElement
		if (elementLocation.contains("//")) {
			condition = By.xpath(elementLocation);
		}

		// Wait until element is visible
		Wait.until(ExpectedConditions.visibilityOfElementLocated(condition));

		// Save found element in WebElement
		listItems = findElements(condition);

		return listItems;
	}
	/*
	 * The following function will be used to to wait element to be visible
	 * not
	 */
	public void waitVisibilityOfWebelement(String selector) {
		WebDriverWait Wait = new WebDriverWait(getDriverProvider().get(), 60);

		String elementLocation = UtilProperties.getInstance().getProperty(selector);

		// Save CSS WebEelement in string
		By condition = By.cssSelector(elementLocation);

		// Override the condition string with xpath webElement
		if (elementLocation.contains("//")) {
			condition = By.xpath(elementLocation);
		}

		// Wait until element is visible
		Wait.until(ExpectedConditions.visibilityOfElementLocated(condition));
	}
	/*
	 * End of function
	 */
	/*
	 * The following function will be used to to wait element to be Clickable
	 * not
	 */
	public void waitElementToBeClickable(String selector) {
		WebDriverWait Wait = new WebDriverWait(getDriverProvider().get(), 60);

		String elementLocation = UtilProperties.getInstance().getProperty(selector);

		// Save CSS WebEelement in string
		By condition = By.cssSelector(elementLocation);

		// Override the condition string with xpath webElement
		if (elementLocation.contains("//")) {
			condition = By.xpath(elementLocation);
		}

		// Wait until element is visible
		Wait.until(ExpectedConditions.elementToBeClickable(condition));
	}
	/*
	 * End of function
	 */
	
	/*
	 * The following function will be used to to wait element to be Clickable
	 * not
	 */
	public void waitElementToBeInvisible(String selector) {
		WebDriverWait Wait = new WebDriverWait(getDriverProvider().get(), 60);

		String elementLocation = UtilProperties.getInstance().getProperty(selector);

		// Save CSS WebEelement in string
		By condition = By.cssSelector(elementLocation);

		// Override the condition string with xpath webElement
		if (elementLocation.contains("//")) {
			condition = By.xpath(elementLocation);
		}

		// Wait until element is visible
		Wait.until(ExpectedConditions.invisibilityOfElementLocated(condition));
	}
	/*
	 * End of function
	 */
	
	 public void datePickerTest(String datepickerSelector) {
	        //Get Today's number
	         String today = getCurrentDay();
	 
	        //Click and open the datepickers
	        clickBy(datepickerSelector);
	 
	        //This is from date picker table
	        WebElement dateWidgetFrom = savedWebElement("DatePickerTable");
	 
	        //This are the rows of the from date picker table
//	        List<WebElement> rows = dateWidgetFrom.findElements(By.tagName("tr"));
	 
	        //This are the columns of the from date picker table
	        List<WebElement> columns = dateWidgetFrom.findElements(By.tagName("td"));
	 
	        //DatePicker is a table. Thus we can navigate to each cell
	        //and if a cell matches with the current date then we will click it.
	        for (WebElement cell: columns) {
	            /*
	            //If you want to click 18th Date
	            if (cell.getText().equals("18")) {
	            */
	            //Select Today's Date
	            if (cell.getText().equals(today)) {
	                cell.click();
	                break;
	            }
	        }
	 
	    }
	 
	//Get The Current Day
	    public String getCurrentDay(){
	        //Create a Calendar Object
	        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
	 
	        //Get Current Day as a number
	        int todayInt = calendar.get(Calendar.DAY_OF_MONTH);
	        //System.out.println("Today Int: " + todayInt +"\n");
	 
	        //Integer to String Conversion
	        String todayStr = Integer.toString(todayInt);
	        //System.out.println("Today Str: " + todayStr + "\n");
	 
	        return todayStr;
	    }
	    
	    /*
		 * The following function is used to get query List from Database for dynamic
		 * selector
		 */
		public List<String> GetQueryList(String query, String value, String column) {
			
			String content = UtilProperties.getInstance().getProperty(query).replace("XX", value);

			// Save query value into list
			List<String> r = MYSQLConnectionManager.getInstance(UtilProperties.getInstance().getProperty("Country")).getListData(content, column);
			if (r != null) {
				

			}
			return r;
		}
		/*
		 * End of function
		 */
		
	public Boolean elementDisplaysSimple(String selector) {

		WebElement foundElement = savedWebElement(selector);

		// if element is displayed return True else False

		try {
			return foundElement.isDisplayed();
		} catch (NoSuchElementException ignored) {
			return false;
		} catch (StaleElementReferenceException ignored) {
			return false;
		}
	}
	
	public void selctRandonFromDropDownList(String dropDown) {
		// Getting list of options
		List<WebElement> itemsInDropdown =  listwebelement(dropDown);

		// Getting size of options available
		int size = itemsInDropdown.size();

		// Generate a random number with in range
		int randnMumber = ThreadLocalRandom.current().nextInt(0, size);

		
		// Selecting random value
		itemsInDropdown.get(randnMumber).click();
		
	}
		
}
