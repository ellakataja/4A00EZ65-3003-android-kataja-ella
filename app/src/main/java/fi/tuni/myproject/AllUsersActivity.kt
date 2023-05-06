package fi.tuni.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.Request
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

class AllUsersActivity : AppCompatActivity() {
    private lateinit var userList : ListView
    private lateinit var adapter : ArrayAdapter<User>
    private lateinit var newUser : User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allusers)

        //var newUserIntent : Intent = intent
        //var newUserBundle : Bundle? = newUserIntent.extras
        //var firstName = newUserBundle?.getString("firstName")
        //var lastName = newUserBundle?.getString("lastName")
        //newUser = User(firstName, lastName)

        userList = findViewById<ListView>(R.id.userList)
        adapter = ArrayAdapter<User>(this, R.layout.user, R.id.userText,
            mutableListOf<User>())
        //adapter.add(newUser)
        userList.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        val url = "https://dummyjson.com/users"

        thread {
            var json : String? = getUrl(url)

            if(json != null) {
                val result : UserList =
                    ObjectMapper().readValue(json, UserList::class.java)
                val users : MutableList<User>? = result.users

                if (users != null) {
                    runOnUiThread {
                        users.forEach {
                            adapter.add(it)
                        }
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