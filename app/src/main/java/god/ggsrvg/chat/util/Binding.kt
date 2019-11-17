package god.ggsrvg.chat.util

import android.annotation.SuppressLint
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

@SuppressLint("StaticFieldLeak")
object Binding {

    lateinit var life: LifecycleOwner

    @JvmStatic
    @BindingAdapter("adapter")
    fun setAdapter(listView: ListView, mutableStrs: MutableLiveData<MutableList<String>>){
        mutableStrs.observe(life, Observer<MutableList<String>> {
            if(listView.adapter != null){
                val adapter = listView.adapter as ArrayAdapter<String>
                adapter.clear()
                adapter.addAll(it)
            }
        })
    }
}