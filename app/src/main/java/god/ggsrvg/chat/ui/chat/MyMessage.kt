package god.ggsrvg.chat.ui.chat

import androidx.databinding.Observable
import androidx.databinding.ObservableField

class MyMessage(private val message: String) {

    val text: ObservableField<String> = ObservableField(message)
}