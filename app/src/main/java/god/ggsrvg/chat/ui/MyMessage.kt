package god.ggsrvg.chat.ui

import androidx.databinding.Observable
import androidx.databinding.ObservableField

class MyMessage(private val message: String) {

    val text: ObservableField<String> = ObservableField(message)
}