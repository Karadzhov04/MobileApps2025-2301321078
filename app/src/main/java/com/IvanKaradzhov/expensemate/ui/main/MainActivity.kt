package com.IvanKaradzhov.expensemate.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.IvanKaradzhov.expensemate.databinding.ActivityMainBinding
import com.IvanKaradzhov.expensemate.ui.adapter.ExpenseAdapter
import com.IvanKaradzhov.expensemate.ui.add.AddExpenseActivity
import com.IvanKaradzhov.expensemate.ui.viewmodel.ExpenseViewModel
import com.IvanKaradzhov.expensemate.data.model.Expense
import java.sql.Date

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: ExpenseViewModel by viewModels()

    private val addLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult
            val id = data.getLongExtra(AddExpenseActivity.EXTRA_ID, -1L)
            val title = data.getStringExtra(AddExpenseActivity.EXTRA_TITLE) ?: return@registerForActivityResult
            val amount = data.getDoubleExtra(AddExpenseActivity.EXTRA_AMOUNT, 0.0)
            val category = data.getStringExtra(AddExpenseActivity.EXTRA_CATEGORY) ?: "Други"
            val date = data.getLongExtra(AddExpenseActivity.EXTRA_DATE, System.currentTimeMillis())
            val note = data.getStringExtra(AddExpenseActivity.EXTRA_NOTE)

            val expense = Expense(
                id = if (id != -1L) id else 0, // ако има id → редакция
                title = title,
                amount = amount,
                category = category,
                date = date,
                note = note
            )

            if (id != -1L) {
                viewModel.updateExpense(expense)
            } else {
                viewModel.addExpense(expense)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "💸Track Your Flow"

        val adapter = ExpenseAdapter { expense ->
            // При клик върху елемент → избор дали да редактираш или изтриеш
            AlertDialog.Builder(this)
                .setTitle("Опции за разход")
                .setItems(arrayOf("Редактирай", "Изтрий", "Сподели")) { _, which ->
                    when (which) {
                        0 -> { // Редакция
                            val intent = Intent(this, AddExpenseActivity::class.java).apply {
                                putExtra(AddExpenseActivity.EXTRA_ID, expense.id)
                                putExtra(AddExpenseActivity.EXTRA_TITLE, expense.title)
                                putExtra(AddExpenseActivity.EXTRA_AMOUNT, expense.amount)
                                putExtra(AddExpenseActivity.EXTRA_CATEGORY, expense.category)
                                putExtra(AddExpenseActivity.EXTRA_DATE, expense.date)
                                putExtra(AddExpenseActivity.EXTRA_NOTE, expense.note)
                            }
                            addLauncher.launch(intent)
                        }

                        1 -> { // Изтриване
                            viewModel.deleteExpense(expense)
                        }

                        2 -> {
                            var shareIntent = Intent().apply {
                                action = Intent.ACTION_SEND
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Разход: ${expense.title}\n" +
                                            "Сума: ${expense.amount} лв\n" +
                                            "Категория: ${expense.category}\n" +
                                            "Дата: ${Date(expense.date)}\n" +
                                            "Бележка: ${expense.note ?: ""}"
                                )
                                type = "text/plain"
                            }
                            this.startActivity(Intent.createChooser(shareIntent, "Сподели чрез"))
                        }
                    }
                }.show()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.expenses.observe(this) { list ->
            adapter.submitList(list)
            binding.swipeRefresh.isRefreshing = false
        }

        binding.fabAdd.setOnClickListener {
            val i = Intent(this, AddExpenseActivity::class.java)
            addLauncher.launch(i)
        }

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
        }
    }
}
