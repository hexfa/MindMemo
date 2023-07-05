package com.mindmemo.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mindmemo.R
import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.utils.DELETE
import com.mindmemo.data.utils.EDIT
import com.mindmemo.data.utils.EDUCATION
import com.mindmemo.data.utils.HEALTH
import com.mindmemo.data.utils.HIGH
import com.mindmemo.data.utils.HOME
import com.mindmemo.data.utils.LOW
import com.mindmemo.data.utils.NORMAL
import com.mindmemo.data.utils.WORK
import com.mindmemo.databinding.ItemNoteBinding
import javax.inject.Inject

class MemoAdapter @Inject constructor() : RecyclerView.Adapter<MemoAdapter.ViewHolder>() {

    private lateinit var binding: ItemNoteBinding
    private lateinit var context: Context
    private var moviesList = emptyList<MemoEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //getItem from PagingDataAdapter
        holder.bind(moviesList[position])
        //Not duplicate items
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = moviesList.size

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(item: MemoEntity) {
            binding.apply {
                titleTxt.text = item.title
                descTxt.text = item.disc
                //Category
                when (item.category) {
                    HOME -> categoryImg.setImageResource(R.drawable.home)
                    WORK -> categoryImg.setImageResource(R.drawable.work)
                    EDUCATION -> categoryImg.setImageResource(R.drawable.education)
                    HEALTH -> categoryImg.setImageResource(R.drawable.healthcare)
                }
                //Priority
                when (item.priority) {
                    HIGH -> priorityColor.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
                    NORMAL -> priorityColor.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow))
                    LOW -> priorityColor.setBackgroundColor(ContextCompat.getColor(context, R.color.aqua))
                }
                //Menu
                menuImg.setOnClickListener {
                    val popupMenu = PopupMenu(context, it)
                    popupMenu.menuInflater.inflate(R.menu.menu_items, popupMenu.menu)
                    popupMenu.show()
                    //Select
                    popupMenu.setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.itemEdit -> {
                                onItemClickListener?.let {
                                    it(item, EDIT)
                                }
                            }
                            R.id.itemDelete -> {
                                onItemClickListener?.let {
                                    it(item, DELETE)
                                }
                            }
                        }
                        return@setOnMenuItemClickListener true
                    }
                }
            }
        }
    }

    private var onItemClickListener: ((MemoEntity, String) -> Unit)? = null

    fun setOnItemClickListener(listener: (MemoEntity, String) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<MemoEntity>) {
        val moviesDiffUtil = NotesDiffUtils(moviesList, data)
        val diffUtils = DiffUtil.calculateDiff(moviesDiffUtil)
        moviesList = data
        diffUtils.dispatchUpdatesTo(this)
    }

    class NotesDiffUtils(private val oldItem: List<MemoEntity>, private val newItem: List<MemoEntity>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }
    }
}