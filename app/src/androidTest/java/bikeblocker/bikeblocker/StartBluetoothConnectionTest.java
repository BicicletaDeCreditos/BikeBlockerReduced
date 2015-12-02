package bikeblocker.bikeblocker;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

import org.junit.After;

public class StartBluetoothConnectionTest extends InstrumentationTestCase {
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
        UiObject2 settings = device.findObject(By.desc("perfis"));
        settings.click();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        for (int i = 0; i<10; i++) {device.pressBack();}
    }

    public void testConnectionCancel() throws Exception {

        device.wait(Until.hasObject(By.text("Cancel")), 3000);
        UiObject2 buttonCancel = device.findObject(By.text("Cancel"));
        buttonCancel.click();

        device.wait(Until.hasObject(By.desc("settings")), 5000);

        assertTrue(device.findObject(new UiSelector().description("settings")).exists());
        assertTrue(device.findObject(new UiSelector().description("perfis")).exists());

    }

    public void testConnectionDenyBluetooth() throws Exception {

        device.wait(Until.hasObject(By.text("It is turned on")), 3000);
        UiObject2 buttonTurnedOn = device.findObject(By.text("It is turned on"));
        buttonTurnedOn.click();

        device.wait(Until.hasObject(By.desc("Deny")), 5000);
        UiObject2 buttonDeny = device.findObject(By.text("Deny"));
        buttonDeny.click();

        device.wait(Until.hasObject(By.desc("startbiking")), 5000);

        UiObject2 buttonStartBiking = device.findObject(By.desc("startbiking"));
        buttonStartBiking.click();

        device.wait(Until.hasObject(By.desc("Deny")), 5000);

        assertTrue(device.findObject(new UiSelector().clickable(true)).exists());


    }
    public void testConnectionFull() throws Exception {

        device.wait(Until.hasObject(By.text("It is turned on")), 3000);
        UiObject2 buttonTurnedOn = device.findObject(By.text("It is turned on"));
        buttonTurnedOn.click();
        device.wait(Until.hasObject(By.desc("Allow")), 5000);
        UiObject2 buttonAllow = device.findObject(By.text("Allow"));
        buttonAllow.click();

        device.wait(Until.hasObject(By.desc("startbiking")), 5000);

        UiObject2 buttonStartBiking = device.findObject(By.desc("startbiking"));
        buttonStartBiking.click();

        device.wait(Until.hasObject(By.text("Connecting to BikeBlocker Bluetooth device...")), 50000);

        assertTrue(device.findObject(new UiSelector().text("Connecting to BikeBlocker Bluetooth device...")).exists());

    }
}
