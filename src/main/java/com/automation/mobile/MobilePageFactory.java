package com.automation.mobile;

import com.automation.mobile.MobileDriverProviderCreator;
import com.automation.mobile.MobilePages;

/**
 * Factory that is used to instantiate desired page object
 */
public class MobilePageFactory {

	private final MobileDriverProviderCreator mobileDriverProvider;
	
	MobilePageFactory( MobileDriverProviderCreator mobileDriverProvider) {
		this.mobileDriverProvider = mobileDriverProvider;
	}


	 public MobilePages NewMobilePages() {
	 return new MobilePages(mobileDriverProvider);
	
	 }

}
