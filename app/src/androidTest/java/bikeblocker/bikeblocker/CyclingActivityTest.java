package bikeblocker.bikeblocker;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

import org.junit.After;

public class CyclingActivityTest extends InstrumentationTestCase {
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

        device.wait(Until.hasObject(By.text("It is turned on")), 3000);
        UiObject2 buttonTurnedOn = device.findObject(By.text("It is turned on"));
        buttonTurnedOn.click();
        device.wait(Until.hasObject(By.desc("Allow")), 5000);
        UiObject2 buttonAllow = device.findObject(By.text("Allow"));
        buttonAllow.click();

        device.wait(Until.hasObject(By.desc("startbiking")), 5000);

        UiObject2 buttonStartBiking = device.findObject(By.desc("startbiking"));
        buttonStartBiking.click();

    }
    @Override
    @After
    public void tearDown() throws Exception {
        for (int i = 0; i<10; i++) {device.pressBack();}
    }


    public void testBackButton() {

        device.wait(Until.hasObject(By.desc("finishCyclingButton")), 5000);
        UiObject2 buttonBack = device.findObject(By.text("finishCyclingButton"));
        buttonBack.click();

        device.wait(Until.hasObject(By.desc("startbiking")), 5000);
        assertTrue(device.findObject(new UiSelector().description("startbiking")).exists());
    }
}
