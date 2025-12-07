package com.ghn.poker.core.testing

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest

/**
 * Base class for ViewModel tests that sets up the test dispatcher.
 *
 * Subclasses should use [testDispatcher] when creating ViewModels or running tests.
 *
 * Example usage:
 * ```kotlin
 * class MyViewModelTest : BaseViewModelTest() {
 *     private lateinit var myUseCase: MyUseCase
 *
 *     override fun onSetup() {
 *         myUseCase = mock<MyUseCase>()
 *     }
 *
 *     @Test
 *     fun myTest() = runTest(testDispatcher) {
 *         val viewModel = MyViewModel(myUseCase)
 *         advanceUntilIdle()
 *         // assertions
 *     }
 * }
 * ```
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseViewModelTest {

    protected val testDispatcher: TestDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        onSetup()
    }

    @AfterTest
    fun tearDown() {
        onTearDown()
        Dispatchers.resetMain()
    }

    /**
     * Override this to perform additional setup after [Dispatchers.setMain] is called.
     * This is where you should initialize mocks and other test dependencies.
     */
    protected open fun onSetup() {}

    /**
     * Override this to perform additional cleanup before [Dispatchers.resetMain] is called.
     */
    protected open fun onTearDown() {}
}
