package com.jessyend.sheare;

import android.support.test.filters.MediumTest;
import android.test.InstrumentationTestCase;
import android.app.*;
import android.content.*;
import android.test.*;
import android.view.*;
import android.widget.*;

import com.jessyend.sheare.PlaylistView.PlaylistActivity;
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
public class PlaylistTest extends InstrumentationTestCase {

    /**
     * Tests for clicking on the first playlist of the Home Page
     */
    @MediumTest
    public void testPlaylistClick() {
        Instrumentation instrumentation = getInstrumentation();
        Instrumentation.ActivityMonitor monitor = instrumentation.addMonitor(HomeActivity.class.getName(), null, false);

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(instrumentation.getTargetContext(), HomeActivity.class.getName());
        instrumentation.startActivitySync(intent);

        Activity currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
        assertThat(currentActivity, is(notNullValue()));

        // Click a playlist in the listview
        ListView currentView = currentActivity.findViewById(com.jessyend.sheare.R.id.listview);
        assertThat(currentView, is(notNullValue()));
        assertThat(currentView, instanceOf(ListView.class));
        assertThat(currentView.getChildAt(0), is(nullValue()));

        instrumentation.removeMonitor(monitor);
        monitor = instrumentation.addMonitor(PlaylistActivity.class.getName(), null, false);

        currentActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5);
        assertThat(currentActivity, is(nullValue()));
    }
}