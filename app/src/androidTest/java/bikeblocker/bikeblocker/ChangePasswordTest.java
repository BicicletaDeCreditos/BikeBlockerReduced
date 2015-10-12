package bikeblocker.bikeblocker;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

import org.junit.After;
import org.junit.BeforeClass;

public class ChangePasswordTest extends InstrumentationTestCase {
    private UiDevice device;
    private UiObject password;
    private UiObject confirmPassword;
    private UiObject registerButton;
    @Override
    @BeforeClass
    public void setUp() throws Exception {
        device = UiDevice.getInstance(getInstrumentation());

        device.pressHome();
        device.wait(Until.hasObject(By.desc("Apps")), 3000);
        device.findObject(new UiSelector().description("Apps")).click();
        device.wait(Until.hasObject(By.text("BikeBlocker")), 3000);
        device.findObject(new UiSelector().description("BikeBlocker")).click();
        device.wait(Until.hasObject(By.desc("settings")), 5000);
        UiObject2 settings = device.findObject(By.desc("settings"));
        settings.click();

        device.wait(Until.hasObject(By.desc("signIn")), 5000);
        device.findObject(By.desc("adminPassword")).setText("123456");
        device.findObject(By.desc("signIn")).click();

        device.wait(Until.hasObject(By.desc("changeAdminPassword")), 5000);
        device.findObject(new UiSelector().description("changeAdminPassword")).click();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        for (int i = 0; i<10; i++) {device.pressBack();}
    }

    public void testFirstLoginAdminNoConfirmPassword() throws Exception {

        device.wait(Until.hasObject(By.desc("confirmRegister")), 5000);
        password = device.findObject(new UiSelector().description("insertAdminPassword"));
        confirmPassword = device.findObject(new UiSelector().description("confirmAdminPassword"));
        registerButton = device.findObject(new UiSelector().description("confirmRegister"));

        password.setText("123456");
        registerButton.click();

        assertTrue(registerButton.exists());
    }

    public void testChangePasswordDoesNotMatch() throws Exception {
        device.wait(Until.hasObject(By.desc("confirmRegister")), 5000);
        password = device.findObject(new UiSelector().description("insertAdminPassword"));
        confirmPassword = device.findObject(new UiSelector().description("confirmAdminPassword"));
        registerButton = device.findObject(new UiSelector().description("confirmRegister"));

        password.setText("123456");
        confirmPassword.setText("1234567");
        registerButton.click();

        assertTrue(registerButton.exists());
    }

    public void testChangePasswordShortPassword() throws Exception {
        device.wait(Until.hasObject(By.desc("confirmRegister")), 5000);
        password = device.findObject(new UiSelector().description("insertAdminPassword"));
        confirmPassword = device.findObject(new UiSelector().description("confirmAdminPassword"));
        registerButton = device.findObject(new UiSelector().description("confirmRegister"));

        password.setText("123");
        confirmPassword.setText("123");
        registerButton.click();

        assertTrue(registerButton.exists());
    }

    public void testChangePasswordSuccessfully() throws Exception {
        device.wait(Until.hasObject(By.desc("confirmRegister")), 5000);
        password = device.findObject(new UiSelector().description("insertAdminPassword"));
        confirmPassword = device.findObject(new UiSelector().description("confirmAdminPassword"));
        registerButton = device.findObject(new UiSelector().description("confirmRegister"));

        password.setText("123456");
        confirmPassword.setText("123456");
        registerButton.click();

        device.wait(Until.hasObject(By.desc("listUsers")), 5000);
        UiObject addUserButton = device.findObject(new UiSelector().description("addUser"));
        UiObject changeAdminPasswordButton = device.findObject(new UiSelector().description("changeAdminPassword"));

        assertTrue(addUserButton.exists());
        assertTrue(changeAdminPasswordButton.exists());
    }
}
