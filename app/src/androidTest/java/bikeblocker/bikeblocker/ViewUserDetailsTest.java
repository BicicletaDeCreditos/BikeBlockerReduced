package bikeblocker.bikeblocker;

import android.content.Intent;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

import org.junit.After;

import bikeblocker.bikeblocker.Control.ViewUserActivity;
import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.User;

public class ViewUserDetailsTest extends InstrumentationTestCase {
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
        device.findObject(By.desc("settings")).click();

        device.wait(Until.hasObject(By.desc("signIn")), 5000);
        device.findObject(By.desc("username")).setText("jose");
        device.findObject(By.desc("password")).setText("1234");
        device.findObject(By.desc("signIn")).click();

        device.wait(Until.hasObject(By.desc("viewProfile")), 5000);

        Intent viewUser = new Intent(getInstrumentation().getContext(), ViewUserActivity.class);
        launchActivityWithIntent("bikeblocker.bikeblocker", ViewUserActivity.class, viewUser);

    }

    @Override
    @After
    public void tearDown() throws Exception {
        for (int i = 0; i<10; i++) {device.pressBack();}
    }

    public void testViewUserDetails() throws Exception {
        assertTrue(device.findObject(new UiSelector().description("userNameTextView")).exists());
        assertTrue(device.findObject(new UiSelector().description("userNameTextView")).getText().length() > 0);
        assertTrue(device.findObject(new UiSelector().description("usernameLabel")).exists());
        assertTrue(device.findObject(new UiSelector().description("usernameText")).exists());
        assertTrue(device.findObject(new UiSelector().description("usernameText")).getText().length() > 0);
        assertTrue(device.findObject(new UiSelector().description("creditsLabel")).exists());
        assertTrue(device.findObject(new UiSelector().description("creditsText")).exists());
        assertTrue(device.findObject(new UiSelector().description("changePassword")).exists());
    }
}
