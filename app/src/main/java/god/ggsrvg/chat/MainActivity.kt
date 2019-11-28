package god.ggsrvg.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import god.ggsrvg.chat.ui.ChatFragment
import god.ggsrvg.chat.util.Binding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Binding.life = this
        Binding.context = this
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, ChatFragment.newInstance())
//                .commitNow()
//        }


        val a1 = A(1, "A");
        val a2 = A(1, "S");

        Log.e("A1", "${a1.hashCode()}")
        Log.e("A2", "${a2.hashCode()}")
        Log.e("Equals", a1.equals(a2).toString())
    }
}

class A(
    val id: Int,
    val name: String
){
    override fun hashCode(): Int {
        return id * name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return if(other != null && other is A && id == other.id && name.equals(other.name)) true else false
    }
}
