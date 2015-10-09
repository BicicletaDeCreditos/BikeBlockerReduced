package bikeblocker.bikeblocker;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

import org.junit.After;

/**
 * TO DO: testar se pode ser inserido um usuario com o mesmo username de um ja existente
 * Na classe "ViewUserDetailsTest": criar teste de selecao de usuario na lista e visualizacao do usuario selecionado.
 * */

public class AddUserTest extends InstrumentationTestCase {
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
        device.findObject(By.desc("adminPassword")).setText("123456");
        device.findObject(By.desc("signIn")).click();

        device.wait(Until.hasObject(By.desc("addUser")), 5000);
        UiObject addUserButton = device.findObject(new UiSelector().description("addUser"));
        addUserButton.click();
    }

    @Override
    @After
    public void tearDown() throws Exception {
        for (int i = 0; i<10; i++) {device.pressBack();}
    }

    public void testAddNewUserSuccessfully() throws Exception {
        device.wait(Until.hasObject(By.text("Save")), 5000);
        device.findObject(new UiSelector().description("userName")).setText("Jose");
        device.findObject(new UiSelector().description("userUsername")).setText("jose");;
        device.findObject(new UiSelector().description("userPassword")).setText("123456");
        device.findObject(new UiSelector().description("userConfirmPassword")).setText("123456");
        device.pressBack();
        device.findObject(new UiSelector().text("Save")).click();

        device.wait(Until.hasObject(By.desc("listUsers")), 5000);
        device.findObject(new UiSelector().description("listUsers")).click();
        device.wait(Until.hasObject(By.desc("userRow")), 5000);

        assertTrue(device.findObject(new UiSelector().text("Jose")).exists());
    }

    public void testAddNewUserNoPassword() throws Exception {
        device.wait(Until.hasObject(By.text("Save")), 5000);
        device.findObject(new UiSelector().description("userName")).setText("Jose");
        device.findObject(new UiSelector().description("userUsername")).setText("jose");
        device.pressBack();
        device.findObject(new UiSelector().text("Save")).click();

        assertTrue(device.findObject(new UiSelector().description("userPassword")).isFocused());
    }

    public void testAddNewUserPasswordDoesNotMatch() throws Exception {
        device.wait(Until.hasObject(By.text("Save")), 5000);
        device.findObject(new UiSelector().description("userName")).setText("Jose");
        device.findObject(new UiSelector().description("userUsername")).setText("jose");
        device.findObject(new UiSelector().description("userPassword")).setText("123456");
        device.findObject(new UiSelector().description("userConfirmPassword")).setText("1234");
        device.pressBack();
        device.findObject(new UiSelector().text("Save")).click();

        assertTrue(device.findObject(new UiSelector().description("userConfirmPassword")).isFocused());
    }

    public void testAddNewUserNoName() throws Exception {
        device.wait(Until.hasObject(By.text("Save")), 5000);
        device.findObject(new UiSelector().description("userUsername")).setText("jose");
        device.findObject(new UiSelector().description("userPassword")).setText("123456");
        device.findObject(new UiSelector().description("userConfirmPassword")).setText("123456");
        device.pressBack();
        device.findObject(new UiSelector().text("Save")).click();

        assertTrue(device.findObject(new UiSelector().description("userName")).isFocused());
    }

    public void testAddNewUserNoUsername() throws Exception {
        device.wait(Until.hasObject(By.text("Save")), 5000);
        device.findObject(new UiSelector().description("userName")).setText("Jose");
        device.findObject(new UiSelector().description("userPassword")).setText("123456");
        device.findObject(new UiSelector().description("userConfirmPassword")).setText("123456");
        device.pressBack();
        device.findObject(new UiSelector().text("Save")).click();

        assertTrue(device.findObject(new UiSelector().description("userUsername")).isFocused());
    }

}
