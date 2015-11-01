package bikeblocker.bikeblocker;

import android.content.Intent;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;
import android.widget.TextView;

import org.junit.After;

import bikeblocker.bikeblocker.Control.ListUsersActivity;
import bikeblocker.bikeblocker.Control.ViewUserActivity;
import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.User;

public class ViewUserDetailsTest extends InstrumentationTestCase {
    private UiDevice device;
    User user = new User();
    UserDAO dao;
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
        device.findObject(By.desc("adminPassword")).setText("123456");
        device.findObject(By.desc("signIn")).click();

        device.wait(Until.hasObject(By.desc("addUser")), 5000);

        user.setName("Test1");
        user.setPassword("123456");
        user.setCredits(0);
        dao = UserDAO.getInstance(getInstrumentation().getContext().getApplicationContext());
        dao.saveUser(user);

        Intent viewUser = new Intent(getInstrumentation().getContext(), ViewUserActivity.class);
        viewUser.putExtra("user_name", "Test1");
        launchActivityWithIntent("bikeblocker.bikeblocker", ViewUserActivity.class, viewUser);

    }

    @Override
    @After
    public void tearDown() throws Exception {
        for (int i = 0; i<10; i++) {device.pressBack();}
    }

    public void testViewUserDetails() throws Exception {
        assertTrue(device.findObject(new UiSelector().description("userNameTextView")).exists());
        assertTrue(device.findObject(new UiSelector().description("creditsLabel")).exists());
        assertTrue(device.findObject(new UiSelector().description("creditsText")).exists());
        assertTrue(device.findObject(new UiSelector().description("deleteUser")).exists());
        assertTrue(device.findObject(new UiSelector().description("apps")).exists());
    }

    public void testShowAlertDialogDeleteButtonOnClick() throws Exception{
        device.wait(Until.hasObject(By.desc("deleteUser")), 5000);

        device.findObject(new UiSelector().description("deleteUser")).click();

        device.wait(Until.hasObject(By.text("Are You Sure?")), 5000);

        assertTrue(device.findObject(new UiSelector().text("Are You Sure?")).exists());
        assertTrue(device.findObject(new UiSelector().text("This will permanently delete this user.")).exists());
        assertTrue(device.findObject(new UiSelector().text("Cancel")).exists());
        assertTrue(device.findObject(new UiSelector().text("Delete")).exists());
    }

    public void testDeleteUser() throws Exception{
        device.wait(Until.hasObject(By.desc("deleteUser")), 5000);

        device.findObject(new UiSelector().description("deleteUser")).click();

        device.wait(Until.hasObject(By.text("Are You Sure?")), 5000);

        device.findObject(new UiSelector().text("Delete")).click();

        device.wait(Until.hasObject(By.desc("addUser")), 5000);

        assertTrue(device.findObject(new UiSelector().description("addUser")).exists());
        assertTrue(device.findObject(new UiSelector().description("changeAdminPassword")).exists());
    }
}
