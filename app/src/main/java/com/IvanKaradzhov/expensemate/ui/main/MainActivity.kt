package com.IvanKaradzhov.expensemate.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.IvanKaradzhov.expensemate.data.model.Expense
import com.IvanKaradzhov.expensemate.databinding.ActivityMainBinding
import com.IvanKaradzhov.expensemate.ui.add.AddExpenseActivity
import com.IvanKaradzhov.expensemate.ui.adapter.ExpenseAdapter
import com.IvanKaradzhov.expensemate.ui.viewmodel.ExpenseViewModel

class MainActivity : AppCompatActivity() {

    // ViewBinding
    private lateinit var binding: ActivityMainBinding

    // ViewModel (MVVM)
    private val viewModel: ExpenseViewModel by viewModels()

    // ActivityResult launcher – за връщане на резултат от AddExpenseActivity
    private val addLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult

            val title = data.getStringExtra(AddExpenseActivity.EXTRA_TITLE) ?: return@registerForActivityResult
            val amount = data.getDoubleExtra(AddExpenseActivity.EXTRA_AMOUNT, 0.0)
            val category = data.getStringExtra(AddExpenseActivity.EXTRA_CATEGORY) ?: "Други"
            val date = data.getLongExtra(AddExpenseActivity.EXTRA_DATE, System.currentTimeMillis())
            val note = data.getStringExtra(AddExpenseActivity.EXTRA_NOTE)

            val expense = Expense(
                title = title,
                amount = amount,
                category = category,
                date = date,
                note = note
            )

            // добавяме в базата чрез ViewModel
            viewModel.addExpense(expense)

            Snackbar.make(binding.root, "Добави се успешно ✅", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Зареждаме layout чрез ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настройваме RecyclerView и адаптера
        val adapter = ExpenseAdapter { expense ->
            // TODO: тук ще направим екран за редакция/изтриване по-късно
            Snackbar.make(binding.root, "Избра: ${expense.title}", Snackbar.LENGTH_SHORT).show()
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Наблюдаваме LiveData от ViewModel
        viewModel.expenses.observe(this) { list ->
            adapter.submitList(list)
            binding.swipeRefresh.isRefreshing = false
        }

        // FAB бутон – отваря AddExpenseActivity
        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            addLauncher.launch(intent)
        }

        // SwipeRefresh (ако искаш да презареждаш)
        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = true
            // Room LiveData автоматично се обновява, така че само изключваме
            binding.swipeRefresh.isRefreshing = false
        }
    }
}
