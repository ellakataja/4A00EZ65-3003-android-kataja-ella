package fi.tuni.myproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var getAllUsersButton : Button
    private lateinit var json: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getAllUsersButton = findViewById(R.id.allUsersButton)
        getAllUsersButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        val allUsersIntent = Intent(this, AllUsersActivity::class.java)
        allUsersIntent.putExtra("json", json)
        startActivity(allUsersIntent)
    }

    override fun onResume() {
        super.onResume()
        val url = "https://dummyjson.com/users"

        thread {
            json = getUrl(url)
        }
    }

    private fun getUrl(url: String) : String {
        val client = OkHttpClient()

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseBody = response.body!!.string()
            println(responseBody)
            return responseBody
        }
    }
}