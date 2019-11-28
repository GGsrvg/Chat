package god.ggsrvg.chat.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import god.ggsrvg.chat.databinding.ItemMyMessageBinding
import god.ggsrvg.chat.databinding.ItemYourMessageBinding
import io.reactivex.rxkotlin.toObservable

class ChatRecyclerAdapter(val list: ObservableList<String>) : RecyclerView.Adapter<BaseViewHolder>() {

    init {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val blogViewBinding = ItemYourMessageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ChatViewHolder(blogViewBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    inner class ChatViewHolder(private val itemBinding: ViewDataBinding) : BaseViewHolder(itemBinding.root){
        override fun onBind(position: Int) {
            val item = list[position]
            val itemMessage: MyMessage = MyMessage(item)

            if(itemBinding is ItemMyMessageBinding){
                itemBinding.viewModel = itemMessage
            }
            else if(itemBinding is ItemYourMessageBinding){
                itemBinding.viewModel = itemMessage
            }

            itemBinding.executePendingBindings()
        }

    }
}