package god.ggsrvg.chat.ui.chat

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import god.ggsrvg.chat.models.Message
import okhttp3.*
import okio.ByteString
import org.apache.http.conn.ssl.AbstractVerifier.getDNSSubjectAlts
import java.security.SecureRandom
import java.security.cert.Certificate
import java.security.cert.X509Certificate
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern.matches
import javax.net.ssl.*


class ChatViewModel : ViewModel() {

    lateinit var navigator: ChatNavigator

    val textMessage = ObservableField<String>();

    val messages = ObservableArrayList<Message>()

    private lateinit var webSocket: WebSocket

    fun onStart(){
        connectWebSocket()
    }

    fun onStop(){
        disconnectWebSocket()
    }

    fun sendMessage(){
        if(!textMessage.get().isNullOrEmpty()) {
            webSocket.send(textMessage.get()!!)
            addMessage(Message(textMessage.get()!!, true))
            textMessage.set(null)
        }
    }

    private fun connectWebSocket() {
        val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
            object : X509TrustManager {

                override fun checkClientTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun checkServerTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }
        )

        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())

        val sslSocketFactory: SSLSocketFactory = sslContext.socketFactory



        val client = OkHttpClient.Builder()
            .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            .hostnameVerifier { _, _ -> true }
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()

        val request = Request.Builder()
            .url(localserver)
            .build()

        webSocket = client.newWebSocket(request, getWebSocketListener())
    }

    private fun disconnectWebSocket() {
        webSocket.cancel()
    }

    fun addMessage(message: Message){
        Log.e("TEXT", "!${message.text}")
        messages.add(message)
    }

    private fun getWebSocketListener(): WebSocketListener {
        return object : WebSocketListener() {
            val TAG = "WebSocket"

            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.e(TAG, "onOpen")
                //navigator.toast(response.message())
            }

            override fun onMessage(webSocket: WebSocket?, text: String?) {
                Log.e(TAG, "onMessage $text")
                navigator.addMessage(Message(text!!, false))
            }

            override fun onMessage(webSocket: WebSocket?, bytes: ByteString?) {
                Log.e(TAG, "onMessageBytes ${bytes!!.hex()}")
                navigator.addMessage(Message(bytes.hex(), false))
            }

            override fun onClosing(webSocket: WebSocket?, code: Int, reason: String?) {
                Log.e(TAG, "onClossing")
                webSocket!!.close(1000, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                Log.e(TAG, "onFailure - message  ${t.message}")
                Log.e(TAG, "onFailure - response  ${response?.message()}")
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
            }
        }
    }

    private fun hostNameVerifier() : HostnameVerifier{
        return HostnameVerifier { hostname, session ->
            val certs: Array<Certificate>
            certs = try {
                session.peerCertificates
            } catch (e: SSLException) {
                return@HostnameVerifier false
            }
            val x509 =
                certs[0] as X509Certificate
            // We can be case-insensitive when comparing the host we used to
            // establish the socket to the hostname in the certificate.
            val hostName =
                hostname.trim { it <= ' ' }.toLowerCase(Locale.ENGLISH)
            // Verify the first CN provided. Other CNs are ignored. Firefox, wget,
            // curl, and Sun Java work this way.
            val firstCn = getFirstCn(x509)
            if (matches(hostName, firstCn)) {
                return@HostnameVerifier true
            }
            for (cn in getDNSSubjectAlts(x509)) {
                if (matches(hostName, cn)) {
                    return@HostnameVerifier true
                }
            }
            false
        }
    }


    private fun getFirstCn(cert: X509Certificate): String? {
        val subjectPrincipal = cert.subjectX500Principal.toString()
        for (token in subjectPrincipal.split(",").toTypedArray()) {
            val x = token.indexOf("CN=")
            if (x >= 0) {
                return token.substring(x + 3)
            }
        }
        return null
    }

    companion object{
        private const val token: String = "eyJhdWQiOiI2IiwianRpIjoiMjMy/MmIwOWQwMDAxNWRlNTgwNjIzMTI2NjFkZmE1YTg2MTU2YjBiYzY3ZmZmOGQ5NDMyNmUzMTRmNDZiMzVkZjhmMzgxN2YyNzk4Yzk0OGIiLCJpYXQiOjE1NzM5MjEwMjQsIm5iZiI6MTU3MzkyMTAyNCwiZXhwIjoxNjA1NTQzNDI0LCJzdWIiOiI4Iiwic2NvcGVzIjpbXX0.nd4x3S1MbvibzTBmY4t9wG5uTi1qWvFGn0izmHq05EMhdVkgif3lM94g1bO3ul9fYHrdRgO6WMoyR3dKqSQoyj82RmH72c72Lx8WTJigG6ymMFIUXLnt7Wh2J_yJ0hoI0kLyzyamCy69Ka8UZ-m7baD45hKGZze7SAzR9tsYxBqTEi32e3p4-tp2ntnY2gcR5AQ-Lx__E0LzhQuNInK9O4j7g4sPZcM2OyU4FHE5Y1fyT9oNQ8k0CxjZKLv0DBpqrwJGHL2ZMTOCy2XMtawabXzCvdFOS1oL-deI9HTaItIGM-tpi26ZsFzU_JVImVv6aWgfNC5AuWu4Gx_GUQLGNo_3iinvbi0vaLvP672t9G0bSTWSSWs6gSIrLl_CjAurZDUoi0TcJCgaYvKsRsCYYfGzSthPx-JjiU_xqiEWF8fcvu4HVb6_Zne7N5ECriy5zEdmAQQFT9bkj7y7XFyiYW2ZQ-2rFj18_ScFHue-akmXp7E_0UdjinsVfr1BrqwkmhXIpPlED8EsK3JA9HHfTPilHnz3EThZayAWN1uOwoW__PJr94KOjyxdvEHg9wm18RB0qj9JRzduv460TubXUTnxIPtlrE_qrApDVs6byFPTzEFrg55jc8o1c1I6SkWXi0oL6xeYBVpDtwbcVuQzaQEh0wkS0WabQKMe-9rII7Y"
        private const val server = "wss://connect.websocket.in/v2/1?token=$token"

        private const val localserver = "wss://10.20.0.75:5001/ws"
    }
}
