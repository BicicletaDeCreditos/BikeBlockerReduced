package bikeblocker.bikeblocker;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

import org.junit.After;

public class LoginTest extends InstrumentationTestCase {
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
    }

    @Override
    @After
    public void tearDown() throws Exception {
        for (int i = 0; i<5; i++) {device.pressBack();}
    }

    public void testFirstLoginRegisterUserNoPassword() throws Exception {
        device.wait(Until.hasObject(By.desc("signIn")), 5000);
        device.findObject(new UiSelector().description("signIn")).click();

        device.wait(Until.hasObject(By.text("Save")), 5000);
        device.findObject(new UiSelector().description("userName")).setText("Jose");
        device.findObject(new UiSelector().description("userUsername")).setText("jose");
        device.findObject(new UiSelector().description("userConfirmPassword")).setText("1234");
        device.pressBack();
        device.findObject(new UiSelector().text("Save")).click();

        assertTrue(device.findObject(new UiSelector().description("userPassword")).isFocused());
    }

    public void testFirstLoginRegisterUserPasswordDoesNotMatch() throws Exception {
        device.wait(Until.hasObject(By.desc("signIn")), 5000);
        device.findObject(new UiSelector().description("signIn")).click();

        device.wait(Until.hasObject(By.text("Save")), 5000);
        device.findObject(new UiSelector().description("userName")).setText("Jose");
        device.findObject(new UiSelector().description("userUsername")).setText("jose");
        device.findObject(new UiSelector().description("userPassword")).setText("123456");
        device.findObject(new UiSelector().description("userConfirmPassword")).setText("1234");
        device.pressBack();
        device.findObject(new UiSelector().text("Save")).click();

        assertTrue(device.findObject(new UiSelector().description("userConfirmPassword")).isFocused());
    }

    public void testFirstLoginRegisterUserNoName() throws Exception {
        device.wait(Until.hasObject(By.desc("signIn")), 5000);
        device.findObject(new UiSelector().description("signIn")).click();

        device.wait(Until.hasObject(By.text("Save")), 5000);
        device.findObject(new UiSelector().description("userUsername")).setText("jose");
        device.findObject(new UiSelector().description("userPassword")).setText("123456");
        device.findObject(new UiSelector().description("userConfirmPassword")).setText("123456");
        device.pressBack();
        device.findObject(new UiSelector().text("Save")).click();

        assertTrue(device.findObject(new UiSelector().description("userName")).isFocused());
    }

    public void testFirstLoginRegisterUserNoUsername() throws Exception {
        device.wait(Until.hasObject(By.desc("signIn")), 5000);
        device.findObject(new UiSelector().description("signIn")).click();

        device.wait(Until.hasObject(By.text("Save")), 5000);
        device.findObject(new UiSelector().description("userName")).setText("Jose");
        device.findObject(new UiSelector().description("userPassword")).setText("123456");
        device.findObject(new UiSelector().description("userConfirmPassword")).setText("123456");
        device.pressBack();
        device.findObject(new UiSelector().text("Save")).click();

        assertTrue(device.findObject(new UiSelector().description("userUsername")).isFocused());
    }

    public void testFirstLoginSuccessfully() throws Exception {
        device.wait(Until.hasObject(By.desc("signIn")), 5000);
        device.findObject(new UiSelector().description("signIn")).click();

        device.wait(Until.hasObject(By.text("Save")), 5000);
        device.findObject(new UiSelector().description("userName")).setText("Jose");
        device.findObject(new UiSelector().description("userUsername")).setText("jose");
        device.findObject(new UiSelector().description("userPassword")).setText("1234");
        device.findObject(new UiSelector().description("userConfirmPassword")).setText("1234");
        device.pressBack();
        device.findObject(new UiSelector().text("Save")).click();

        device.wait(Until.hasObject(By.desc("addNewApp")), 5000);

        assertTrue(device.findObject(new UiSelector().description("addNewApp")).exists());
        assertTrue(device.findObject(new UiSelector().description("changePassword")).exists());
    }

    public void testLoginSuccessfully() throws Exception {
        device.wait(Until.hasObject(By.desc("signIn")), 5000);
        device.findObject(By.desc("username")).setText("jose");
        device.findObject(By.desc("password")).setText("1234");
        device.findObject(By.desc("signIn")).click();

        device.wait(Until.hasObject(By.desc("changePassword")), 5000);

        assertTrue(device.findObject(new UiSelector().description("changePassword")).exists());
        assertTrue(device.findObject(new UiSelector().description("addNewApp")).exists());
    }

    public void testWrongAdminPassword() throws Exception{
        device.wait(Until.hasObject(By.desc("signIn")), 5000);
        UiObject password = device.findObject(new UiSelector().description("password"));
        password.setText("12345");
        device.findObject(By.desc("signIn")).click();

        assertTrue(password.exists());
    }
}