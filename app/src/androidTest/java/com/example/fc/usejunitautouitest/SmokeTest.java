package com.example.fc.usejunitautouitest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by fc on 17-1-16.
 * smokeTest
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        StartMainActivity.class,
        ExampleInstrumentedTest.class
})

public class SmokeTest {
}
