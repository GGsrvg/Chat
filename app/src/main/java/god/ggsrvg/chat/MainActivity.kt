package god.ggsrvg.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import god.ggsrvg.chat.ui.ChatFragment
import god.ggsrvg.chat.util.Binding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Binding.life = this
        Binding.context = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ChatFragment.newInstance())
                .commitNow()
        }
    }
}
