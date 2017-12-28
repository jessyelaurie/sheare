package com.jessyend.sheare;

import android.support.test.filters.MediumTest;
import android.test.InstrumentationTestCase;
import android.app.*;
import android.content.*;
import android.test.*;
import android.view.*;
import android.widget.*;

import com.jessyend.sheare.UserManagement.LoginActivity;
import com.jessyend.sheare.HomeView.HomeActivity;

import static org.hamcrest.core.Is.*;
import static org.hamcrest.core.IsNull.*;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;
import static com.jessyend.sheare.R.id.*;

/**
 * The LoginTest class tests login for credentials that are valid and invalid.
 * @author Jessye Nana Davies <nanadav2@illinois.edu>
 */
public class LoginTest extends InstrumentationTestCase {

    /**
     * Tests for login with valid credentials
     */
    @MediumTest
    public void testValidLogIn() {
        Instrumentation instrumentation = getInstrumentation();
        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(LoginActivity.class.getName(), null, false);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(instrumentation.getTargetContext(), LoginActivity.class.getName());
        instrumentation.startActivitySync(intent);

        Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
        assertThat(currentActivity, is(notNullValue()));

        // Type the username
        View currentView = currentActivity.findViewById(email);
        assertThat(currentView, is(notNullValue()));
        assertThat(currentView, instanceOf(EditText.class));
        TouchUtils.clickView(this, currentView);
        instrumentation.sendStringSync("jessye@gmail.com");

        // Type the password
        currentView = currentActivity.findViewById(password);
        assertThat(currentView, is(notNullValue()));
        assertThat(currentView, instanceOf(EditText.class));
        TouchUtils.clickView(this, currentView);
        instrumentation.sendStringSync("password");

        instrumentation.removeMonitor(monitor);
        monitor = instrumentation.addMonitor(HomeActivity.class.getName(), null, false);

        currentView = currentActivity.findViewById(email_sign_in_button);
        assertThat(currentView, is(notNullValue()));
        assertThat(currentView, instanceOf(Button.class));
        TouchUtils.clickView(this, currentView);

        currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
        assertThat(currentActivity, is(nullValue()));
    }

    /**
     * Tests for login with invalid credentials
     */
    @MediumTest
    public void testInvalidLogIn() {
        Instrumentation instrumentation = getInstrumentation();
        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(LoginActivity.class.getName(), null, false);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(instrumentation.getTargetContext(), LoginActivity.class.getName());
        instrumentation.startActivitySync(intent);

        Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
        assertThat(currentActivity, is(notNullValue()));

        // Type the username
        View currentView = currentActivity.findViewById(email);
        assertThat(currentView, is(notNullValue()));
        assertThat(currentView, instanceOf(EditText.class));
        TouchUtils.clickView(this, currentView);
        instrumentation.sendStringSync("jessye@gmail.com");

        // Type the password
        currentView = currentActivity.findViewById(password);
        assertThat(currentView, is(notNullValue()));
        assertThat(currentView, instanceOf(EditText.class));
        TouchUtils.clickView(this, currentView);
        instrumentation.sendStringSync("wrongpassword");

        instrumentation.removeMonitor(monitor);
        monitor = instrumentation.addMonitor(HomeActivity.class.getName(), null, false);

        currentView = currentActivity.findViewById(email_sign_in_button);
        assertThat(currentView, is(notNullValue()));
        assertThat(currentView, instanceOf(Button.class));
        TouchUtils.clickView(this, currentView);

        currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
        assertThat(currentActivity, is(nullValue()));
    }
}