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
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_AMOUNT = "extra_amount"
        const val EXTRA_CATEGORY = "extra_category"
        const val EXTRA_DATE = "extra_date"
        const val EXTRA_NOTE = "extra_note"
    }

    private var pickedDate: Long = System.currentTimeMillis()
    private var editingId: Long = -1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Проверка дали сме в режим "редакция"
        intent?.let {
            editingId = it.getLongExtra(EXTRA_ID, -1L)
            val incomingTitle = it.getStringExtra(EXTRA_TITLE)
            val incomingAmount = it.getDoubleExtra(EXTRA_AMOUNT, 0.0)
            val incomingCategory = it.getStringExtra(EXTRA_CATEGORY)
            val incomingDate = it.getLongExtra(EXTRA_DATE, System.currentTimeMillis())
            val incomingNote = it.getStringExtra(EXTRA_NOTE)

            if (incomingTitle != null) {
                binding.etTitle.setText(incomingTitle)
                binding.etAmount.setText(incomingAmount.toString())
                pickedDate = incomingDate
                val cal = Calendar.getInstance().apply { timeInMillis = incomingDate }
                binding.btnPickDate.text =
                    "${cal.get(Calendar.DAY_OF_MONTH)}.${cal.get(Calendar.MONTH) + 1}.${cal.get(Calendar.YEAR)}"
                binding.etNote.setText(incomingNote)

            }
        }

        binding.btnPickDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this, { _, y, m, d ->
                val c = Calendar.getInstance()
                c.set(y, m, d, 0, 0, 0)
                pickedDate = c.timeInMillis
                binding.btnPickDate.text = "${d}.${m + 1}.${y}"
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
                putExtra(EXTRA_ID, editingId)
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
