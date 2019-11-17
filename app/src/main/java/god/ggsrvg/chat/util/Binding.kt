package god.ggsrvg.chat.util

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayList
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

@SuppressLint("StaticFieldLeak")
object Binding {

    lateinit var life: LifecycleOwner
    lateinit var context: Context

    @JvmStatic
    @BindingAdapter("adapter")
    fun setAdapter(listView: ListView, zxc: ObservableArrayList<String>){
        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1, zxc)
        listView.adapter = adapter
    }
}