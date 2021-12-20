package com.bykea;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {
    private String ENTER_IP_XPATH = "/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.EditText";
    private String OKAY_BUTTON_ID = "android:id/button1";
    private String ENGLISH_RADIO_BUTTON_ID = "com.bykea.pk:id/rbEnglish";
    private String URDU_RADIO_BUTTON_ID = "com.bykea.pk:id/rbUrdu";
    private String TICK_BUTTON_ID = "com.bykea.pk:id/ivPositive";
    private String INSERT_MOBILENUMBER_ID = "com.bykea.pk:id/phoneNumberEt";
    private String MOBILE_NUMBER_XPATH = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.EditText";
    private String LOGIN_BUTTON_ID = "com.bykea.pk:id/loginBtn";
    private String ENTER_OTP_ID = "com.bykea.pk:id/verificationCodeEt_1";
    private String RESEND_OTP_ID = "com.bykea.pk:id/resendTv";

    private String LOGIN_BUTTON_ID_PARTNER = "com.bykea.pk.partner:id/tv_login";
    private String ENTER_PHONE_NUMBER_PARTNER = "com.bykea.pk.partner:id/phoneNumberEt";
    private String PROCEED_BUTTON_PARTNER = "com.bykea.pk.partner:id/loginBtn";
    private By PARTNER_OTP_FIELD = By.id("com.bykea.pk.partner:id/verificationCodeEt_1");

    public WebElement enterIp() {
        return driver.findElement(By.xpath(ENTER_IP_XPATH));
    }

    public WebElement getOkayButton() {
        return driver.findElement(By.id(OKAY_BUTTON_ID));
    }

    public WebElement getEnglishRadioButton() {
        return driver.findElement(By.id(ENGLISH_RADIO_BUTTON_ID));
    }

    public WebElement getUrduRadioButton() {
        return driver.findElement(By.id(URDU_RADIO_BUTTON_ID));
    }

    public WebElement getTickButton() {
        return driver.findElement(By.id(TICK_BUTTON_ID));
    }

    public WebElement insertMobileNumber() {
        return driver.findElement(By.id(INSERT_MOBILENUMBER_ID));
    }

    public WebElement getLoginButton() {
        return driver.findElement(By.id(LOGIN_BUTTON_ID));
    }

    public WebElement enterOtp() {
        return driver.findElement(By.id(ENTER_OTP_ID));
    }

    public WebElement resendOtp() {
        return driver.findElement(By.id(RESEND_OTP_ID));
    }


    public WebElement getLoginPartner() {
        return driver.findElement(By.id(LOGIN_BUTTON_ID_PARTNER));
    }

    public WebElement getPhoneNumberPartner() {
        return driver.findElement(By.id(ENTER_PHONE_NUMBER_PARTNER));
    }

    public WebElement getNextButton() {
        return driver.findElement(By.id(PROCEED_BUTTON_PARTNER));
    }

    public WebElement getPartnerOtp() {
        return driver.findElement(PARTNER_OTP_FIELD);
    }

    public void loginApp(String phone) {
        enterIp().clear();
        enterIp().sendKeys("https://mars-talos.bykea.dev");
        getOkayButton().click();
        getEnglishRadioButton().click();
        getTickButton().click();
        insertMobileNumber().sendKeys(phone);
        getLoginButton().click();
        enterOtp().sendKeys("0000");
    }
}
