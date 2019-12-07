package god.ggsrvg.chat.ui.chat

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import god.ggsrvg.chat.R
import god.ggsrvg.chat.databinding.ChatFragmentBinding
import god.ggsrvg.chat.models.Message


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

        return viewDataBinding!!.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewDataBinding!!.setVariable(bindingVariable, viewModel)
        viewDataBinding!!.lifecycleOwner = this
        viewDataBinding!!.executePendingBindings()

        val chatAdapter = ChatRecyclerAdapter(viewModel.messages)
        viewDataBinding!!.list.adapter = chatAdapter
        viewDataBinding!!.list.itemAnimator = DefaultItemAnimator()

        val manager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        manager.stackFromEnd = true
        viewDataBinding!!.list.layoutManager = manager

        chatAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                manager.smoothScrollToPosition(viewDataBinding!!.list, null, chatAdapter.itemCount)
            }
        })

        viewDataBinding!!.edittextChatbox.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event != null) {
                    if (event.action == KeyEvent.ACTION_DOWN &&
                        keyCode == KeyEvent.KEYCODE_ENTER
                    ) { // Perform action on key press
                        viewModel.sendMessage()
                        return true
                    }
                }
                return false
            }

        })
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
