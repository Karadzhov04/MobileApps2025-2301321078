package com.IvanKaradzhov.expensemate.ui.add

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.IvanKaradzhov.expensemate.databinding.ActivityAddExpenseBinding
import java.util.*

class AddExpenseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddExpenseBinding

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_AMOUNT = "extra_amount"
        const val EXTRA_CATEGORY = "extra_category"
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_NOTE = "extra_note"
    }

    private var pickedDate: Long = System.currentTimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPickDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                val c = Calendar.getInstance()
                c.set(y, m, d, 0, 0, 0)
                pickedDate = c.timeInMillis
                binding.btnPickDate.text = "${d}.${m+1}.${y}"
            }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.btnSave.setOnClickListener {
            val title = binding.etTitle.text.toString().trim()
            val amount = binding.etAmount.text.toString().toDoubleOrNull() ?: 0.0
            val category = binding.spinnerCategory.selectedItem?.toString() ?: "Други"
            val note = binding.etNote.text?.toString()

            if (title.isEmpty()) {
                binding.etTitle.error = "Заглавие е задължително"
                return@setOnClickListener
            }

            val out = Intent().apply {
                putExtra(EXTRA_TITLE, title)
                putExtra(EXTRA_AMOUNT, amount)
                putExtra(EXTRA_CATEGORY, category)
                putExtra(EXTRA_DATE, pickedDate)
                putExtra(EXTRA_NOTE, note)
            }
            setResult(RESULT_OK, out)
            finish()
        }
    }
}
