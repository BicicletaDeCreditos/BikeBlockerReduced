package bikeblocker.bikeblocker;

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


public class AddAppTest extends InstrumentationTestCase{
    private UiDevice device;

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
        device.findObject(By.desc("username")).setText("jose");
        device.findObject(By.desc("password")).setText("1234");
        device.findObject(By.desc("signIn")).click();

        device.wait(Until.hasObject(By.desc("addNewApp")), 5000);
        device.findObject(By.desc("addNewApp")).click();
        device.wait(Until.hasObject(By.clazz(android.widget.TextView.class.getName())), 10000);
    }

    @Override
    @After
    public void tearDown() throws Exception {
        for (int i = 0; i<10; i++) {device.pressBack();}
    }

    public void testSelectNewAppToList() throws Exception{
        UiScrollable listView = new UiScrollable(new UiSelector());

        UiObject listItem = listView.getChildByInstance(new UiSelector().className(android.widget.TextView.class.getName()), 1  );
        listItem.click();

        device.wait(Until.hasObject(By.text("OK")), 10000);

        assertTrue(device.findObject(new UiSelector().textContains("Select the number of credits needed to access this app for 1 hour.")).exists());
        assertTrue(device.findObject(new UiSelector().className("android.widget.CheckedTextView")).exists());
        assertTrue(device.findObject(new UiSelector().text("Cancel")).exists());
        assertTrue(device.findObject(new UiSelector().text("OK")).exists());
    }

    public void testAddNewAppToList() throws Exception{
        UiScrollable listView = new UiScrollable(new UiSelector());

        UiObject listItem = listView.getChildByInstance(new UiSelector().className(android.widget.TextView.class.getName()), 1);
        String app_name = listItem.getText();
        listItem.click();

        device.wait(Until.hasObject(By.text("OK")), 10000);

        device.findObject(new UiSelector().className("android.widget.CheckedTextView").text("20")).click();
        device.findObject(new UiSelector().text("OK")).click();

        device.wait(Until.hasObject(By.desc("listUserApps")), 5000);

        assertTrue(device.findObject(new UiSelector().text(app_name)).exists());
    }

}
