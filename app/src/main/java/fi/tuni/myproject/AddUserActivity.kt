package fi.tuni.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.concurrent.thread

class AddUserActivity : AppCompatActivity() {
    lateinit var firstNameText : TextView
    lateinit var lastNameText : TextView
    lateinit var newUserIntent : Intent
    lateinit var newUserBundle : Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adduser)

        firstNameText = findViewById(R.id.firstNameText)
        lastNameText = findViewById(R.id.lastNameText)
        newUserIntent = intent
        newUserBundle = newUserIntent.extras!!

        if (newUserBundle != null) {
            runOnUiThread {
                firstNameText.text = newUserBundle.getString("firstName")
                lastNameText.text = newUserBundle.getString("lastName")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val url = "https://dummyjson.com/users/add"

        //val addUserIntent = Intent(this, AddUserActivity::class.java)
        //addUserIntent.putExtra("firstName", firstName)
        //addUserIntent.putExtra("lastName", lastName)

        thread {
            val firstName = newUserBundle.getString("firstName")
            val lastName = newUserBundle.getString("lastName")
            val newUser = User(firstName, lastName)
            val json = ObjectMapper().writeValueAsString(newUser)
            addUser(url, json)
        }
    }

    private fun addUser(url : String, json : String) : String? {
        val client = OkHttpClient()
        val requestBody = json.toRequestBody("application/json".toMediaTypeOrNull())
        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseBody = response.body!!.string()
            println(responseBody)
            return responseBody
        }
    }
}