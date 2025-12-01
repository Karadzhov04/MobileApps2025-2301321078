package com.IvanKaradzhov.expensemate

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.IvanKaradzhov.expensemate.data.model.Expense
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
class ExpenseViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Задаваме фалшив Main Dispatcher за тестовете
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        // Връщаме нормалното състояние
        Dispatchers.resetMain()
    }

    @Test
    fun addExpense_shouldIncreaseListSize() = runTest {
        val fakeRepo = FakeRepository()
        val viewModel = TestExpenseViewModel(fakeRepo)

        val initialSize = viewModel.expenses.value?.size ?: 0
        val expense = Expense(1L, "Тест", 10.0, "Food", Date().time, "Бележка")

        viewModel.addExpense(expense)
        testDispatcher.scheduler.advanceUntilIdle()

        val newSize = viewModel.expenses.value?.size ?: 0
        Assert.assertEquals(initialSize + 1, newSize)
    }

    @Test
    fun deleteExpense_shouldDecreaseListSize() = runTest {
        val fakeRepo = FakeRepository()
        val viewModel = TestExpenseViewModel(fakeRepo)

        val expense = Expense(1L, "Тест", 10.0, "Food", Date().time, null)
        viewModel.addExpense(expense)
        testDispatcher.scheduler.advanceUntilIdle()

        val sizeBefore = viewModel.expenses.value?.size ?: 0
        viewModel.deleteExpense(expense)
        testDispatcher.scheduler.advanceUntilIdle()

        val sizeAfter = viewModel.expenses.value?.size ?: 0
        Assert.assertEquals(sizeBefore - 1, sizeAfter)
    }

    @Test
    fun updateExpense_shouldChangeTitle() = runTest {
        val fakeRepo = FakeRepository()
        val viewModel = TestExpenseViewModel(fakeRepo)

        val expense = Expense(1L, "Старо име", 10.0, "Food", Date().time, null)
        viewModel.addExpense(expense)
        testDispatcher.scheduler.advanceUntilIdle()

        val updated = expense.copy(title = "Ново име")
        viewModel.updateExpense(updated)
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.expenses.value?.firstOrNull { it.id == 1L }
        Assert.assertEquals("Ново име", result?.title)
    }
}
