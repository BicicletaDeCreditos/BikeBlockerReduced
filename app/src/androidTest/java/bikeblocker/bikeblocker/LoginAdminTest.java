package bikeblocker.bikeblocker;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

import org.junit.After;

public class LoginAdminTest extends InstrumentationTestCase {
    private UiDevice device;
    @Override
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
    }

    @Override
    @After
    public void tearDown() throws Exception {
        for (int i = 0; i<5; i++) {device.pressBack();}
    }

    public void testFirstLoginAdminSuccessfully() throws Exception {
        device.findObject(new UiSelector().description("signIn")).click();
        device.wait(Until.hasObject(By.desc("confirmRegister")), 5000);
        UiObject password = device.findObject(new UiSelector().description("insertAdminPassword"));
        UiObject confirmPassword = device.findObject(new UiSelector().description("confirmAdminPassword"));
        UiObject registerButton = device.findObject(new UiSelector().description("confirmRegister"));

        password.setText("123456");
        confirmPassword.setText("123456");
        registerButton.click();

        device.wait(Until.hasObject(By.desc("addUser")), 5000);
        UiObject addUserButton = device.findObject(new UiSelector().description("addUser"));
        UiObject changeAdminPasswordButton = device.findObject(new UiSelector().description("changeAdminPassword"));

        assertTrue(addUserButton.exists());
        assertTrue(changeAdminPasswordButton.exists());
    }

    public void testLoginAdminSuccessfully() throws Exception {
        //Senha padrao = '123456'
        device.wait(Until.hasObject(By.desc("signIn")), 5000);
        device.findObject(By.desc("adminPassword")).setText("123456");
        device.findObject(By.desc("signIn")).click();

        device.wait(Until.hasObject(By.desc("addUser")), 5000);
        UiObject addUserButton = device.findObject(new UiSelector().description("addUser"));
        UiObject changeAdminPasswordButton = device.findObject(new UiSelector().description("changeAdminPassword"));

        assertTrue(addUserButton.exists());
        assertTrue(changeAdminPasswordButton.exists());
    }

    public void testWrongAdminPassword() throws Exception{
        device.wait(Until.hasObject(By.desc("signIn")), 5000);
        UiObject password = device.findObject(new UiSelector().description("adminPassword"));
        password.setText("12345");
        device.findObject(By.desc("signIn")).click();

        assertTrue(password.exists());
    }
}