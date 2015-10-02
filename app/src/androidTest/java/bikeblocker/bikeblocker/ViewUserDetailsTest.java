package bikeblocker.bikeblocker;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

import org.junit.After;

import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.User;

public class ViewUserDetailsTest extends InstrumentationTestCase {
    private UiDevice device;
    User user = new User();
    UserDAO dao = UserDAO.getInstance(getInstrumentation().getContext().getApplicationContext());
    @Override
    public void setUp() throws Exception {
        user.setName("Teste");
        user.setUsername("teste");
        user.setPassword("123456");
        user.setCredits(0);
        dao.saveUser(user);
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

        device.wait(Until.hasObject(By.desc("listUsers")), 5000);
        device.findObject(new UiSelector().description("listUsers")).click();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        for (int i = 0; i<10; i++) {device.pressBack();}
    }

    public void testViewUserDetails(){

    }

}
