package god.ggsrvg.chat.ui

import android.database.Observable
import androidx.lifecycle.ViewModel
import android.widget.TextView
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import okio.ByteString
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import okhttp3.*
import java.util.*
import java.util.concurrent.TimeUnit


class ChatViewModel : ViewModel() {

    lateinit var navigator: ChatNavigator

    val textMessage = ObservableField<String>();

    val messages: MutableLiveData<MutableList<String>> = MutableLiveData(mutableListOf())

    private val messagesP: MutableList<String> = mutableListOf()

    private lateinit var webSocket: WebSocket

    fun onStart(){
        connectWebSocket()
    }

    fun onStop(){
        disconnectWebSocket()
    }

    public fun sendMessage(){
        if(!textMessage.get().isNullOrEmpty()) {
            webSocket.send(textMessage.get())
            addMessage(textMessage.get()!!)
            textMessage.set(null)
        }
    }

    private fun connectWebSocket() {
        val client = OkHttpClient.Builder()
            .readTimeout(0, TimeUnit.MILLISECONDS)
            .build()

        val request = Request.Builder()
            .url("wss://connect.websocket.in/v2/1?token=" + token)
            .build()
        webSocket = client.newWebSocket(request, getWebSocketListener())
    }

    private fun disconnectWebSocket() {
        webSocket.cancel()
    }

    private fun addMessage(text: String){
        messagesP.add(text!!)
        messages.postValue(messagesP)
    }

    private fun getWebSocketListener(): WebSocketListener {
        return object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                //navigator.toast(response.message())
            }

            override fun onMessage(webSocket: WebSocket?, text: String?) {
                addMessage(text!!)
            }

            override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
                addMessage(bytes!!.hex())
            }

            override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
                webSocket!!.close(1000, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
//                navigator.toast(response?.message()!!)
            }
        }
    }

    companion object{
        private var token: String = "eyJhdWQiOiI2IiwianRpIjoiMjMyMmIwOWQwMDAxNWRlNTgwNjIzMTI2NjFkZmE1YTg2MTU2YjBiYzY3ZmZmOGQ5NDMyNmUzMTRmNDZiMzVkZjhmMzgxN2YyNzk4Yzk0OGIiLCJpYXQiOjE1NzM5MjEwMjQsIm5iZiI6MTU3MzkyMTAyNCwiZXhwIjoxNjA1NTQzNDI0LCJzdWIiOiI4Iiwic2NvcGVzIjpbXX0.nd4x3S1MbvibzTBmY4t9wG5uTi1qWvFGn0izmHq05EMhdVkgif3lM94g1bO3ul9fYHrdRgO6WMoyR3dKqSQoyj82RmH72c72Lx8WTJigG6ymMFIUXLnt7Wh2J_yJ0hoI0kLyzyamCy69Ka8UZ-m7baD45hKGZze7SAzR9tsYxBqTEi32e3p4-tp2ntnY2gcR5AQ-Lx__E0LzhQuNInK9O4j7g4sPZcM2OyU4FHE5Y1fyT9oNQ8k0CxjZKLv0DBpqrwJGHL2ZMTOCy2XMtawabXzCvdFOS1oL-deI9HTaItIGM-tpi26ZsFzU_JVImVv6aWgfNC5AuWu4Gx_GUQLGNo_3iinvbi0vaLvP672t9G0bSTWSSWs6gSIrLl_CjAurZDUoi0TcJCgaYvKsRsCYYfGzSthPx-JjiU_xqiEWF8fcvu4HVb6_Zne7N5ECriy5zEdmAQQFT9bkj7y7XFyiYW2ZQ-2rFj18_ScFHue-akmXp7E_0UdjinsVfr1BrqwkmhXIpPlED8EsK3JA9HHfTPilHnz3EThZayAWN1uOwoW__PJr94KOjyxdvEHg9wm18RB0qj9JRzduv460TubXUTnxIPtlrE_qrApDVs6byFPTzEFrg55jc8o1c1I6SkWXi0oL6xeYBVpDtwbcVuQzaQEh0wkS0WabQKMe-9rII7Y"
    }
}
