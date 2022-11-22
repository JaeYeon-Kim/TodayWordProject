package com.kjy.todaywordproject

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class QuotePagerAdapter(
    private val quotes: List<Quote>,
    private val isNameRevealed: Boolean
): RecyclerView.Adapter<QuotePagerAdapter.QuoteViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        QuoteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_quote, parent, false)
        )
    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        holder.bind(quotes[position], isNameRevealed)
    }

    override fun getItemCount(): Int {
        // 리스트의 사이즈 리턴
        return quotes.size
    }

    class QuoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // 아이템 뷰에 들어갈 텍스트뷰 프로퍼티 선언
        private val quoteTextView: TextView = itemView.findViewById(R.id.quoteTextView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)

        @SuppressLint("SetTextI18n")
        fun bind(quote: Quote, isNameRevealed: Boolean) {
            // 각 따옴표를 붙여줄때 역슬래시로 구분을 줘야함.
            quoteTextView.text = "\"${quote.quote}\""


            // 설정에 따라 이름이 가려지거나 노출되거나를 설정해주는 기능(remoteConfig에서 설정한 것에 따라서)
            if(isNameRevealed) {
                // 인물 이름앞에 -을 붙여줌.
                nameTextView.text = "- ${quote.name}"
                nameTextView.visibility = View.VISIBLE
            }else {
                nameTextView.visibility = View.GONE
            }

        }
    }
}


