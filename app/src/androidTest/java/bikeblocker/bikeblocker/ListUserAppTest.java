package bikeblocker.bikeblocker;

import android.content.Intent;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

import org.junit.After;
import org.junit.BeforeClass;

import bikeblocker.bikeblocker.Control.ViewUserActivity;
import bikeblocker.bikeblocker.Database.AppDAO;
import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.App;
import bikeblocker.bikeblocker.Model.User;

public class ListUserAppTest extends InstrumentationTestCase {
    private UiDevice device;
    User user = new User();
    UserDAO dao;
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

    public void testEmptyAppsList() throws Exception{
        device.wait(Until.hasObject(By.desc("apps")), 5000);

        device.findObject(new UiSelector().description("apps")).click();

        device.wait(Until.hasObject(By.desc("listUserApps")), 5000);

        assertEquals(device.findObject(new UiSelector().description("listUserApps")).getChildCount(), 0);
    }

    public void testListUserApps() throws Exception{
        App app = new App();
        app.setUser("Test1");
        app.setCreditsPerHour(10);
        app.setAppName("Google+");
        AppDAO.getInstance(getInstrumentation().getContext()).saveApp(app);

        device.wait(Until.hasObject(By.desc("apps")), 5000);

        device.findObject(new UiSelector().description("apps")).click();

        device.wait(Until.hasObject(By.desc("listUserApps")), 5000);

        assertEquals(device.findObject(new UiSelector().description("listUserApps")).getChildCount(), 1);
        assertTrue(device.findObject(new UiSelector().text("Google+")).exists());
        assertTrue(device.findObject(new UiSelector().text("Credits per hour: ")).exists());
        assertTrue(device.findObject(new UiSelector().text("10")).exists());

    }

}
