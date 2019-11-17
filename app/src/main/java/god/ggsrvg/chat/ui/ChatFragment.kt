package god.ggsrvg.chat.ui

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil

import god.ggsrvg.chat.R
import god.ggsrvg.chat.databinding.ChatFragmentBinding

class ChatFragment : Fragment(), ChatNavigator {
    private var viewDataBinding: ChatFragmentBinding? = null
    override fun toast(text: String) {
        Toast.makeText(activity, text, Toast.LENGTH_LONG).show()
    }
    val bindingVariable: Int = 1

    companion object {
        fun newInstance() = ChatFragment()
    }

    private lateinit var viewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        viewModel.navigator = this

        viewDataBinding = DataBindingUtil.inflate(inflater, R.layout.chat_fragment, container, false)


        val arrayAdapter = ArrayAdapter<String>(
            activity!!,
            android.R.layout.simple_list_item_1)
        viewDataBinding!!.list.adapter = arrayAdapter
        return viewDataBinding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding!!.setVariable(bindingVariable, viewModel)
        viewDataBinding!!.lifecycleOwner = this
        viewDataBinding!!.executePendingBindings()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

}
