package god.ggsrvg.chat.ui.chat

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ObservableList
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import god.ggsrvg.chat.databinding.ItemMyMessageBinding
import god.ggsrvg.chat.databinding.ItemYourMessageBinding
import god.ggsrvg.chat.models.Message
import god.ggsrvg.chat.ui.base.BaseViewHolder

class ChatRecyclerAdapter(val list: ObservableList<Message>) : RecyclerView.Adapter<BaseViewHolder>() {

    init {
        list.addOnListChangedCallback(object : ObservableList.OnListChangedCallback<ObservableList<Message>>() {
            val TAG = "[CALLBACK]"
            override fun onChanged(sender: ObservableList<Message>?) {
                Log.e(TAG, "onChanged")
                notifyDataSetChanged()
            }

            override fun onItemRangeRemoved(
                sender: ObservableList<Message>?,
                positionStart: Int,
                itemCount: Int
            ) {
                Log.e(TAG, "onItemRangeRemoved")
                notifyDataSetChanged()
            }

            override fun onItemRangeMoved(
                sender: ObservableList<Message>?,
                fromPosition: Int,
                toPosition: Int,
                itemCount: Int
            ) {
                Log.e(TAG, "onItemRangeMoved")
                notifyDataSetChanged()
            }

            override fun onItemRangeInserted(
                sender: ObservableList<Message>?,
                positionStart: Int,
                itemCount: Int
            ) {
                Log.e(TAG, "onItemRangeInserted")
                notifyItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeChanged(
                sender: ObservableList<Message>?,
                positionStart: Int,
                itemCount: Int
            ) {
                Log.e(TAG, "onItemRangeChanged")
                notifyDataSetChanged()
            }

        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val blogViewBinding: ViewDataBinding = if (list[viewType].itsMime){
            ItemMyMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        }else {
            ItemYourMessageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        }
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
            val itemMessage: MyMessage =
                MyMessage(item.text)

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