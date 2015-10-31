package bikeblocker.bikeblocker;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiSelector;
import android.support.test.uiautomator.Until;
import android.test.InstrumentationTestCase;

import org.junit.After;

import bikeblocker.bikeblocker.Database.UserDAO;
import bikeblocker.bikeblocker.Model.User;


/**
 * These tests were performed manually due to the fact some key ui objects could not be found by uiautomator
 * */
public class ViewUserDetailsTest extends InstrumentationTestCase {
    private UiDevice device;
    User user = new User();
    UserDAO dao = UserDAO.getInstance(getInstrumentation().getContext().getApplicationContext());
    @Override
    public void setUp() throws Exception {
    }

    @Override
    @After
    public void tearDown() throws Exception {
        for (int i = 0; i<10; i++) {device.pressBack();}
    }

    public void testViewUserDetails(){

    }

    public void testShowAlertDialogDeleteButtonOnClick(){

    }

    public void testDeleteUser(){

    }
}
