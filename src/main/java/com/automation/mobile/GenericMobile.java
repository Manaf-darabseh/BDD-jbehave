package com.automation.mobile;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GenericMobile {

	public static String sharedData = "";
	MobileDriverProviderCreator mobiledriverProvider;
	int timeoutInSeconds;

	/**
	 * Initialize the webdriver. Must be called before using any genericMobile
	 * methods. *
	 */
	public GenericMobile(MobileDriverProviderCreator driverPorvider) {
		this.mobiledriverProvider = driverPorvider;
		// driver =mainTest.driver;
		timeoutInSeconds = 60;

		// driverWait = new WebDriverWait(driver, timeoutInSeconds);

	}

	/**
	 * Wrap WebElement in MobileElement *
	 */
	public void openApp() {
		currentDriver();
	}

	public void scrollTo(String elementSelector) {
		boolean found = false;
		int screenHeight = currentDriver().manage().window().getSize().getHeight();
		int screenWidth = currentDriver().manage().window().getSize().getWidth();
		int previousValue;

		while (!found) {
			currentDriver().swipe((int) (screenWidth * 0.5), (int) (screenHeight * 0.9), (int) (screenWidth * 0.5), 0,
					2000);

			try {
				if (find(elementSelector).isDisplayed()) {
					found = true;
					WebElement ele = find(elementSelector);

					while (ele.getLocation().getY() > screenHeight * 0.20) {
						previousValue = ele.getLocation().getY();
						currentDriver().swipe((int) (screenWidth * 0.5), (int) (screenHeight * 0.5),
								(int) (screenWidth * 0.5), (int) (screenHeight * 0.4), 2000);
						ele = find(elementSelector);
						if (previousValue == ele.getLocation().getY()) {
							break;
						}

					}

				}
			} catch (Exception e) {
			}
		}

	}

	public void scrollToWithCount(String elementSelector, int count) {
		boolean found = false;
		int screenHeight = currentDriver().manage().window().getSize().getHeight();
		int screenWidth = currentDriver().manage().window().getSize().getWidth();
		int previousValue;
		int timesEntered = 0;
		while (!found) {
			timesEntered++;
			if (timesEntered - 1 == count) {
				throw new ElementNotVisibleException(elementSelector);
				
			}

			currentDriver().swipe((int) (screenWidth * 0.5), (int) (screenHeight * 0.9), (int) (screenWidth * 0.5), 0,
					2000);

			try {
				if (find(elementSelector).isDisplayed()) {
					found = true;
					WebElement ele = find(elementSelector);

					while (ele.getLocation().getY() > screenHeight * 0.20) {
						previousValue = ele.getLocation().getY();
						currentDriver().swipe((int) (screenWidth * 0.5), (int) (screenHeight * 0.5),
								(int) (screenWidth * 0.5), (int) (screenHeight * 0.4), 2000);
						ele = find(elementSelector);
						if (previousValue == ele.getLocation().getY()) {
							break;
						}

					}

				}
			} catch (Exception e) {
			}
		}

	}

	public void SwipeRight() {

		int screenHeight = currentDriver().manage().window().getSize().getHeight();
		int screenWidth = currentDriver().manage().window().getSize().getWidth();
		currentDriver().swipe((int) (screenWidth * 0.1), (int) (screenHeight * 0.5), (int) (screenWidth * 0.99),
				(int) (screenHeight * 0.5), 1000);
	}

	public void SwipeLeft() {

		int screenHeight = currentDriver().manage().window().getSize().getHeight();
		int screenWidth = currentDriver().manage().window().getSize().getWidth();
		currentDriver().swipe((int) (screenWidth * 0.8), (int) (screenHeight * 0.5), 0, (int) (screenHeight * 0.5),
				500);
	}

	private MobileElement w(WebElement element) {
		return (MobileElement) element;
	}

	public void sleepTime(int value) {
		try {
			Thread.currentThread();
			Thread.sleep(value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AppiumDriver currentDriver() {
		return mobiledriverProvider.getCurrentDriver();
	}

	/**
	 * Wrap WebElement in MobileElement *
	 */
	private List<MobileElement> w(List<WebElement> elements) {
		List<MobileElement> list = new ArrayList<MobileElement>(elements.size());
		for (WebElement element : elements) {
			list.add(w(element));
		}

		return list;
	}

	/**
	 * Set implicit wait in seconds *
	 */
	public void setWait(int seconds) {
		mobiledriverProvider.getCurrentDriver().manage().timeouts().implicitlyWait(seconds, TimeUnit.SECONDS);
	}

	/**
	 * Return an element by locator *
	 */
	public MobileElement element(By locator) {
		return w(mobiledriverProvider.getCurrentDriver().findElement(locator));
	}

	/**
	 * Return a list of elements by locator *
	 */
	public List<MobileElement> elements(By locator) {
		return w(mobiledriverProvider.getCurrentDriver().findElements(locator));
	}

	/**
	 * Press the back button *
	 */
	public void back() {
		mobiledriverProvider.getCurrentDriver().navigate().back();
	}

	/**
	 * Return a list of elements by tag name *
	 */
	public List<MobileElement> tags(String tagName) {
		return elements(for_tags(tagName));
	}

	/**
	 * Return a tag name locator *
	 */
	public By for_tags(String tagName) {
		return By.className(tagName);
	}

	/**
	 * Return a text element by xpath index *
	 */
	public MobileElement s_text(int xpathIndex) {
		return element(for_text(xpathIndex));
	}

	/**
	 * Return a text locator by xpath index *
	 */
	public By for_text(int xpathIndex) {
		return By.xpath("//android.widget.TextView[" + xpathIndex + "]");
	}

	/**
	 * Return a text element that contains text *
	 */
	public MobileElement text(String text) {
		return element(for_text(text));
	}

	/**
	 * Return a text locator that contains text *
	 */
	public By for_text(String text) {
		return By.xpath("//android.widget.TextView[contains(@text, '" + text + "')]");
	}

	/**
	 * Return a text element by exact text *
	 */
	public MobileElement text_exact(String text) {
		return element(for_text_exact(text));
	}

	/**
	 * Return a text locator by exact text *
	 */
	public By for_text_exact(String text) {
		return By.xpath("//android.widget.TextView[@text='" + text + "']");
	}

	public By for_find(String value) {
		return By.xpath("//*[@content-desc=\"" + value + "\" or @type=\"" + value + "\" or @class=\"" + value
				+ "\" or @package=\"" + value + "\" or @resource-id=\"" + value + "\" or @text=\"" + value
				+ "\"] | //*[contains(translate(@content-desc,\"" + value + "\",\"" + value + "\"), \"" + value
				+ "\") or contains(translate(@text,\"" + value + "\",\"" + value + "\"), \"" + value
				+ "\") or @resource-id=\"" + value + "\"]");
	}

	public By for_first_attribute(String value) {
		return By.xpath("(//*[@*=\"" + value + "\"])[1]");
	}

	public By for_attribute(String value) {
		return By.xpath("(//*[@*=\"" + value + "\"])");
	}

	public By for_path(String value) {
		return By.xpath(value);
	}

	public MobileElement find(String value) {
		return element(for_find(value));
	}

	public MobileElement for_tag(String value, String tag) {
		try {
			List<MobileElement> elements = waitAll(for_attribute(value));

			for (MobileElement mobileElement : elements) {
				if (mobileElement.getTagName().equals(tag)) {
					mobileElement.click();
					return mobileElement;
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}

	public WebElement findElement(String value) {
		return mobiledriverProvider.getCurrentDriver().findElement(for_find(value));
	}

	public WebElement findElementByName(String value) {
		return mobiledriverProvider.getCurrentDriver().findElement(By.name(value));
	}

	public void hideKeyboard() {
		try {
			mobiledriverProvider.getCurrentDriver().hideKeyboard();
		} catch (Exception e) {
		}

	}

	/**
	 * Wait 30 seconds for locator to find an element *
	 */
	public MobileElement wait(By locator) {
		WebDriverWait driverWait = new WebDriverWait(mobiledriverProvider.getCurrentDriver(), timeoutInSeconds);
		return w(driverWait.until(ExpectedConditions.visibilityOfElementLocated(locator)));
	}

	/**
	 * Wait 60 seconds for locator to find all elements *
	 */
	public List<MobileElement> waitAll(By locator) {
		WebDriverWait driverWait = new WebDriverWait(mobiledriverProvider.getCurrentDriver(), timeoutInSeconds);
		return w(driverWait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator)));
	}

	/**
	 * Wait 60 seconds for locator to not find a visible element *
	 */
	public boolean waitInvisible(By locator) {
		WebDriverWait driverWait = new WebDriverWait(mobiledriverProvider.getCurrentDriver(), timeoutInSeconds);
		return driverWait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
	}

	/**
	 * Return an element that contains name or text *
	 */
	// public MobileElement scroll_to(String value) {
	// return (MobileElement)driverProvider.getCurrentDriver().scrollTo(value);
	// }

	/**
	 * Return an element that exactly matches name or text *
	 */
	// public MobileElement scroll_to_exact(String value) {
	// return (MobileElement)driverProvider.getCurrentDriver().scrollToExact(value);
	// }

	/**
	 * Return a list of elements by locator *
	 */
	public List<WebElement> webElements(By locator) {
		return mobiledriverProvider.getCurrentDriver().findElements(locator);
	}

	public int findElementsCount(String value) {

		return mobiledriverProvider.getCurrentDriver().findElements(for_find(value)).size();
	}

	public void setSharedData(String value) {
		sharedData = value;
	}

	public String getSharedData() {
		return sharedData;
	}

	public static BufferedImage resize(BufferedImage img, int newW, int newH) {
		Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
		BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = dimg.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return dimg;
	}

	public BufferedImage takeShot() {
		for (int i = 0; i < 5; i++) {
			if (currentDriver().manage().window().getSize().getHeight() > currentDriver().manage().window().getSize()
					.getWidth()) {
				System.out.println(
						"   takeShot: portrait mode w: " + currentDriver().manage().window().getSize().getWidth()
								+ ", h: " + currentDriver().manage().window().getSize().getHeight());
				try {
					Dimension dat = currentDriver().manage().window().getSize();
					BufferedImage img = ImageIO.read(((AppiumDriver) currentDriver()).getScreenshotAs(OutputType.FILE));
					img = resize(img, currentDriver().manage().window().getSize().getWidth(),
							currentDriver().manage().window().getSize().getHeight());
					File outputfile = new File("saved.png");
					ImageIO.write(img, "png", outputfile);
					return img;
				} catch (Exception e) {
				}
			} else {
				System.out.println(
						"   takeShot: landscape mode w: " + currentDriver().manage().window().getSize().getWidth()
								+ ", h: " + currentDriver().manage().window().getSize().getHeight());
				BufferedImage image = null;
				try {
					image = ImageIO.read(((AppiumDriver) currentDriver()).getScreenshotAs(OutputType.FILE));
				} catch (Exception e) {
					System.err.println("    get screenshot failed.");
				}
				if (image != null) {
					// System.out.println(" original image height: "+image.getHeight()+", width:
					// "+image.getWidth());
					int diff = (image.getHeight() - image.getWidth());
					diff = diff / 2;
					// System.out.println(" diff: "+diff);

					AffineTransform tx = new AffineTransform();
					tx.rotate(Math.PI * 1.5, image.getHeight() / 2, image.getWidth() / 2);// (radian,arbit_X,arbit_Y)
					AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
					image = op.filter(image, null);// (source,destination)
					// System.out.println(" rotated image height: "+image.getHeight()+", width:
					// "+image.getWidth());

					image = image.getSubimage(diff, diff, image.getWidth() - diff, image.getHeight() - diff);
					// System.out.println(" result image height: "+image.getHeight()+", width:
					// "+image.getWidth());
					return image;
				}
			}
		}
		return null;
	}

	// public void AndroidWiFiEnabled(boolean Status){
	//
	// NetworkConnectionSetting networkConnection = new
	// NetworkConnectionSetting(false,true,false);
	// networkConnection.setWifi(Status); // Change WIFI Status
	// ((AndroidDriver)currentDriver()).setNetworkConnection(networkConnection);
	// }

	public void iOSWiFiswitch() {

		Dimension IOSdimension = currentDriver().manage().window().getSize();
		int width = IOSdimension.getWidth();
		int height = IOSdimension.getHeight();
		currentDriver().swipe((int) (width * 0.5), (int) (height), (int) (width * 0.5), 0, 500);
		sleepTime(2000);
		currentDriver().findElementByAccessibilityId("Wi-Fi").click();
		currentDriver().swipe((int) (width * 0.5), (int) (0), (int) (width * 0.5), height, 500);

	}

}