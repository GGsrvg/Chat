package god.ggsrvg.chat.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import god.ggsrvg.chat.ui.chat.ChatRecyclerAdapter

@SuppressLint("StaticFieldLeak")
object Binding {

    lateinit var life: LifecycleOwner
    lateinit var context: Context

    @JvmStatic
    @BindingAdapter("adapter")
    fun setAdapter(recyclerView: RecyclerView, observableArrayList: ObservableArrayList<String>){
//        val adapter =
//            ChatRecyclerAdapter(
//                observableArrayList)
//        recyclerView.adapter = adapter
    }
}