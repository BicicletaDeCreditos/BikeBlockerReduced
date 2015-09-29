package bikeblocker.bikeblocker;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

public class MainActivityTest extends InstrumentationTestCase {
    private UiDevice device;
    @Override
    public void setUp() throws Exception {
        device = UiDevice.getInstance(getInstrumentation());
        device.pressHome();
        device.wait(Until.hasObject(By.desc("Apps")), 3000);
        UiObject2 appsButton = device.findObject(By.desc("Apps"));
        appsButton.click();
        device.wait(Until.hasObject(By.text("BikeBlocker")), 3000);
        UiObject2 bikeblockerButton = device.findObject(By.desc("Apps"));
        bikeblockerButton.click();
    }

    public void testGoToLogin() throws Exception {


    }
}