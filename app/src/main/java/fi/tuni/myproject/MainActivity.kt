package fi.tuni.myproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.concurrent.thread

@JsonIgnoreProperties(ignoreUnknown = true)
data class User (val firstName: String? = null, val lastName: String? = null) {
    override fun toString(): String {
        return "$firstName $lastName"
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserList(var users: MutableList<User>? = null)
class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var addUserButton : Button
    private lateinit var userList : ListView
    private lateinit var adapter : ArrayAdapter<User>
    private lateinit var newUserIntent : Intent
    private lateinit var firstName : String
    private lateinit var lastName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addUserButton = findViewById(R.id.addUserButton)
        addUserButton.setOnClickListener(this)

        newUserIntent = intent
        var newUserBundle : Bundle? = newUserIntent.extras
        firstName = newUserBundle?.getString("firstName").toString()
        lastName = newUserBundle?.getString("lastName").toString()
        var newUser = User(firstName, lastName)

        userList = findViewById(R.id.userList)
        adapter = ArrayAdapter<User>(this, R.layout.user, R.id.userText,
            mutableListOf<User>())
        if (newUserBundle != null) {
            adapter.add(newUser)
        }
        userList.adapter = adapter
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.addUserButton -> {
                val addUserIntent = Intent(this, AddUserActivity::class.java)
                startActivity(addUserIntent)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val url = "https://dummyjson.com/users"

        thread {
            var json : String? = getUrl(url)

            if(json != null) {
                val result : UserList =
                    ObjectMapper().readValue(json, UserList::class.java)
                val users : MutableList<User> = result.users!!

                runOnUiThread {
                    users.forEach {
                        adapter.add(it)
                    }
                }
            }
        }
    }

    private fun getUrl(url: String) : String? {
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