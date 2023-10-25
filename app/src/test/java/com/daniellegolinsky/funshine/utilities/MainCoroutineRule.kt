package com.daniellegolinsky.funshine.utilities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * Sets up a TestDispatcher so we can test suspending functions in view models
 * See: https://github.com/Kotlin/kotlinx.coroutines/blob/master/kotlinx-coroutines-test/MIGRATION.md
 * And see MainCoroutineRule provided by Google here:
 *  https://developer.android.com/codelabs/advanced-android-kotlin-training-testing-survey#3
 * Add
 *  `@get:Rule
 *   var mainCoroutineRule = MainCoroutineRule()`
 *  to your tests.
 */
class MainCoroutineRule(
    private val dispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    // Runs with @Before
    override fun starting(description: Description?) {
        super.starting(description)
        Dispatchers.setMain(dispatcher)
    }

    // Runs with @After
    override fun finished(description: Description?) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}
