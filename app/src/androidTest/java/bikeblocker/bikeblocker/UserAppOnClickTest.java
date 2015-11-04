package bikeblocker.bikeblocker;

import android.content.Intent;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiScrollable;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

import org.junit.After;
import org.junit.BeforeClass;

import bikeblocker.bikeblocker.Control.AppsListActivity;
import bikeblocker.bikeblocker.Database.AppDAO;
import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.App;
import bikeblocker.bikeblocker.Model.User;

public class UserAppOnClickTest extends InstrumentationTestCase{
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

        App app = new App();
        app.setUser("Test1");
        app.setCreditsPerHour(10);
        app.setAppName("Teste");
        AppDAO.getInstance(getInstrumentation().getContext()).saveApp(app);


        Intent viewUser = new Intent(getInstrumentation().getContext(), AppsListActivity.class);
        viewUser.putExtra("user_name", "Test1");
        launchActivityWithIntent("bikeblocker.bikeblocker", AppsListActivity.class, viewUser);


    }

    @Override
    @After
    public void tearDown() throws Exception {
        for (int i = 0; i<10; i++) {device.pressBack();}
    }

    public void testClickOnListItem() throws Exception{
        device.wait(Until.hasObject(By.desc("listUserApps")), 5000);

        UiScrollable listView = new UiScrollable(new UiSelector());
        listView.scrollTextIntoView("Teste");
        listView.waitForExists(5000);

        UiObject listItem = listView.getChildByText(new UiSelector().className(android.widget.TextView.class.getName()), "Teste");
        listItem.click();

        assertTrue(device.findObject(new UiSelector().text("Choose one option")).exists());
        assertTrue(device.findObject(new UiSelector().text("Edit")).exists());
        assertTrue(device.findObject(new UiSelector().text("Cancel")).exists());
        assertTrue(device.findObject(new UiSelector().text("Delete")).exists());
    }

    public void testClickOnEditOption() throws Exception{
        device.wait(Until.hasObject(By.desc("listUserApps")), 5000);

        UiScrollable listView = new UiScrollable(new UiSelector());
        listView.scrollTextIntoView("Teste");
        listView.waitForExists(5000);

        UiObject listItem = listView.getChildByText(new UiSelector().className(android.widget.TextView.class.getName()), "Teste");
        listItem.click();

        device.findObject(new UiSelector().text("Edit")).click();

        device.wait(Until.hasObject(By.text("OK")), 5000);

        assertTrue(device.findObject(new UiSelector().textContains("Select the number of credits needed to access this app for 1 hour.")).exists());
        assertTrue(device.findObject(new UiSelector().className("android.widget.CheckedTextView")).exists());
        assertTrue(device.findObject(new UiSelector().text("Cancel")).exists());
        assertTrue(device.findObject(new UiSelector().text("OK")).exists());
    }

    public void testEditClickedItem() throws Exception{
        device.wait(Until.hasObject(By.desc("listUserApps")), 5000);

        UiScrollable listView = new UiScrollable(new UiSelector());
        listView.scrollTextIntoView("Teste");
        listView.waitForExists(5000);

        UiObject listItem = listView.getChildByText(new UiSelector().className(android.widget.TextView.class.getName()), "Teste");
        listItem.click();

        device.findObject(new UiSelector().text("Edit")).click();

        device.wait(Until.hasObject(By.text("OK")), 5000);

        device.findObject(new UiSelector().className("android.widget.CheckedTextView").text("20")).click();
        device.findObject(new UiSelector().text("OK")).click();

        device.wait(Until.hasObject(By.desc("listUserApps")), 5000);

        assertTrue(device.findObject(new UiSelector().text("Teste")).exists());
        assertTrue(device.findObject(new UiSelector().text("Credits per hour: ")).exists());
        assertTrue(device.findObject(new UiSelector().text("20")).exists());
    }

    public void testClickOnDeleteOption() throws Exception{
        device.wait(Until.hasObject(By.desc("listUserApps")), 5000);

        UiScrollable listView = new UiScrollable(new UiSelector());
        listView.scrollTextIntoView("Teste");
        listView.waitForExists(5000);

        UiObject listItem = listView.getChildByText(new UiSelector().className(android.widget.TextView.class.getName()), "Teste");
        listItem.click();

        device.findObject(new UiSelector().text("Delete")).click();

        device.wait(Until.hasObject(By.text("Delete")), 5000);

        assertTrue(device.findObject(new UiSelector().textContains("Are You Sure?")).exists());
        assertTrue(device.findObject(new UiSelector().text("This will permanently delete this app from your list.")).exists());
        assertTrue(device.findObject(new UiSelector().text("Cancel")).exists());
        assertTrue(device.findObject(new UiSelector().text("Delete")).exists());
    }

    public void testClickOnDelete() throws Exception{
        device.wait(Until.hasObject(By.desc("listUserApps")), 5000);

        UiScrollable listView = new UiScrollable(new UiSelector());
        listView.scrollTextIntoView("Teste");
        listView.waitForExists(5000);

        UiObject listItem = listView.getChildByText(new UiSelector().className(android.widget.TextView.class.getName()), "Teste");
        listItem.click();

        device.findObject(new UiSelector().text("Delete")).click();

        device.wait(Until.hasObject(By.text("Are You Sure?")), 5000);

        device.findObject(new UiSelector().text("Delete")).click();

        device.wait(Until.hasObject(By.desc("listUserApps")), 5000);

        assertFalse(device.findObject(new UiSelector().text("Teste")).exists());
    }

}

