package com.automation.steps;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.hamcrest.Matchers;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import com.automation.Pages.PageFactory;


public class Steps extends BaseSteps {

	public Steps(PageFactory pageFactory) throws ParserConfigurationException, TransformerException {
		super(pageFactory);
	}

	@Given("I Navigate to $URL")
	public  void gO (String URL) {
		genericWeb.go(URL);
	}

	@Given("Login Using Valid credential like : $UserName and $Password")
	@When("Login Using Valid credential like : $UserName and $Password")
	public void login(String userName, String password) {
		genericWeb.fillTextBox("UserName", userName);
		genericWeb.fillTextBox("Password", password);
		genericWeb.clickBy("Login_Button");
	}

	@When("Fill the $selector with: $value")
	public void fill_TextBox_ByCss(String selector, String value) {
		genericWeb.fillTextBox(selector, value);

	}

	@When("Click on $Element")
	@Given("Click on $Element")
	public void click(String element){
		genericWeb.waitPageToLoad();
		genericWeb.waitVisibilityOfWebelement(element);
		genericWeb.clickBy(element);
	}

	@When("Click On $Element")
	@Given("Click On $Element")
	@Then("Click On $Element")
	public void clickByCss(String element) {
		genericWeb.waitPageToLoad();
		genericWeb.clickBy(element);
	}
	
	@When("click On $Element")
	@Given("click On $Element")
	@Then("click On $Element")
	public void clickByJS(String element) {
		genericWeb.waitPageToLoad();
		genericWeb.clickByJS(element);
	}
	

	@Then("The $Element should displays")
	public void elementDisplays(String element) {

		assertTrue(genericWeb.elementDisplaysSimple(element));
	}

	@Then("The $Element should not displays")
	public void elementNotDisplays(String element) {

		assertFalse(genericWeb.tableDisplayed(element));
	}


	@When("Hold a while")
	@Given("Hold a while")
	public void hold() throws InterruptedException {
		Thread.sleep(2000);
	}
	
	@When("The $Element is Selected")
	@Given("The $Element is Selected")
	public void Is_Selected(String element){
		assertTrue(genericWeb.IsSelected(element));
	}
	
	//get element text and compare it with the expected 
	@Then("The $Element should be $Text")
	public void elementTextDisplays(String element , String Text) {
		assertThat(genericWeb.getText(element).contains(Text), Matchers.equalTo(true));
	}

}
