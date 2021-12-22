package com.bykea;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class LoginPage extends BasePage {
    By ENTER_IP = By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.widget.EditText");
    By OKAY_BUTTON = By.id("android:id/button1");
    By ENGLISH_RADIO_BUTTON = By.id("com.bykea.pk:id/rbEnglish");
    By URDU_RADIO_BUTTON = By.id("com.bykea.pk:id/rbUrdu");
    By TICK_BUTTON = By.id("com.bykea.pk:id/ivPositive");
    By INSERT_MOBILENUMBER = By.id("com.bykea.pk:id/phoneNumberEt");
    By MOBILE_NUMBER = By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.view.ViewGroup/android.widget.EditText");
    By LOGIN_BUTTON = By.id("com.bykea.pk:id/loginBtn");
    By ENTER_OTP = By.id("com.bykea.pk:id/verificationCodeEt_1");
    By RESEND_OTP = By.id("com.bykea.pk:id/resendTv");
    By LOGIN_BUTTON_PARTNER = By.id("com.bykea.pk.partner:id/tv_login");
    By ENTER_PHONE_NUMBER_PARTNER = By.id("com.bykea.pk.partner:id/phoneNumberEt");
    By PROCEED_BUTTON_PARTNER = By.id("com.bykea.pk.partner:id/loginBtn");
    By PARTNER_OTP_FIELD = By.id("com.bykea.pk.partner:id/verificationCodeEt_1");

    public WebElement enterIp() {
        return driver.findElement(ENTER_IP);
    }

    public WebElement getOkayButton() {
        return driver.findElement(OKAY_BUTTON);
    }

    public WebElement getEnglishRadioButton() {
        return driver.findElement(ENGLISH_RADIO_BUTTON);
    }

    public WebElement getUrduRadioButton() {
        return driver.findElement(URDU_RADIO_BUTTON);
    }

    public WebElement getTickButton() {
        return driver.findElement(TICK_BUTTON);
    }

    public WebElement insertMobileNumber() {
        return driver.findElement(INSERT_MOBILENUMBER);
    }

    public WebElement getLoginButton() {
        return driver.findElement(LOGIN_BUTTON);
    }

    public WebElement enterOtp() {
        return driver.findElement(ENTER_OTP);
    }

    public WebElement resendOtp() {
        return driver.findElement(RESEND_OTP);
    }

    public WebElement getLoginPartner() {
        return driver.findElement(LOGIN_BUTTON_PARTNER);
    }

    public WebElement getPhoneNumberPartner() {
        return driver.findElement(ENTER_PHONE_NUMBER_PARTNER);
    }

    public WebElement getNextButton() {
        return driver.findElement(PROCEED_BUTTON_PARTNER);
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
