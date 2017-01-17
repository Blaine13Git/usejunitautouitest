package com.example.fc.usejunitautouitest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by fc on 17-1-16.
 * Uiatumator + Espress的组合有点在于：
 * 1、Uiautomator可以启动设备上的任意的app操作手机;
 * 2、Espresso可以在被测的app上操作app内部的任何资源。
 */
@RunWith(AndroidJUnit4.class)
public class StartMainActivity {
    private static final String BASIC_SAMPLE_PACKAGE = "com.example.fc.usejunitautouitest";
    private static final int LAUNCH_TIMEOUT = 5000;
    private UiDevice mDevice;

    /*10 seconds max per method tested*/
    @Rule
    public Timeout globalTimeout = Timeout.seconds(10);

    /*
    create and launch of the activity
    To get a reference to the activity you can use
    */
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void getTest() {
        onView(withId(R.id.textView01)).check(matches(withText("Hello World!")));
    }


    /*启动任意的指定的app*/
    // @Test //(timeout=1000)
    public void toLaunchAnyApp() {
        // Initialize UiDevice instance
        mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        // Start from the home screen
        mDevice.pressHome();

        // Launch the app
        Context context = InstrumentationRegistry.getContext();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(BASIC_SAMPLE_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        mDevice.wait(Until.hasObject(By.pkg(BASIC_SAMPLE_PACKAGE).depth(0)), LAUNCH_TIMEOUT);

        // Type text and then press the button.
        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "editTextUserInput")).setText("123321");
        mDevice.findObject(By.res(BASIC_SAMPLE_PACKAGE, "changeTextBt")).click();

        // Verify the test is displayed in the Ui
        UiObject2 changedText = mDevice.wait(
                Until.findObject(
                        By.res(BASIC_SAMPLE_PACKAGE, "textToBeChanged")), 500 /* wait 500ms */);

        assertThat(changedText.getText(), is(equalTo("123321")));
    }

    /*获取正在运行的包名*/
    public String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }
}
