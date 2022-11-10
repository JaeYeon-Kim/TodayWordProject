package com.kjy.todaywordproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotePagerAdapter(
    private val quotes: List<Quote>
): RecyclerView.Adapter<QuotePagerAdapter.QuoteViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        QuoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_quote, parent, false)

        )
    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(quotes[position])
    }

    override fun getItemCount(): Int {
        // 리스트의 사이즈 리턴
        return quotes.size
    }

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // 아이템 뷰에 들어갈 텍스트뷰 프로퍼티 선언
        private val quoteTextView: TextView = itemView.findViewById(R.id.quoteTextView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        fun bind(quote: Quote) {
            quoteTextView.text = quote.quote
            nameTextView.text = quote.name
        }
    }
}


