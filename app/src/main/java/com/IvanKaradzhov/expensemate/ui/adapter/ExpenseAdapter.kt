package com.IvanKaradzhov.expensemate.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.IvanKaradzhov.expensemate.R
import com.IvanKaradzhov.expensemate.data.model.Expense
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter(private val onClick: (Expense) -> Unit) :
    ListAdapter<Expense, ExpenseAdapter.ExpenseVH>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseVH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseVH(v, onClick)
    }

    override fun onBindViewHolder(holder: ExpenseVH, position: Int) {
        holder.bind(getItem(position))
    }

    class ExpenseVH(itemView: View, val onClick: (Expense) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvAmount: TextView = itemView.findViewById(R.id.tvAmount)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvNote: TextView = itemView.findViewById(R.id.tvNote) // добавено
        private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

        private var current: Expense? = null

        fun bind(expense: Expense) {
            current = expense
            tvTitle.text = expense.title
            tvAmount.text = String.format(Locale.getDefault(), "%.2f лв", expense.amount)
            tvCategory.text = expense.category
            tvDate.text = dateFormat.format(Date(expense.date))

            // Бележка – показва се само ако има текст
            if (!expense.note.isNullOrEmpty()) {
                tvNote.text = expense.note
                tvNote.visibility = View.VISIBLE
            } else {
                tvNote.visibility = View.GONE
            }

            itemView.setOnClickListener { current?.let { onClick(it) } }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Expense, newItem: Expense) = oldItem == newItem
    }
}