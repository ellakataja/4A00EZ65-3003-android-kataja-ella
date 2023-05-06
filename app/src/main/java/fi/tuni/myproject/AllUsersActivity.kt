package fi.tuni.myproject

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper

@JsonIgnoreProperties(ignoreUnknown = true)
data class User (var firstName: String? = null, var lastName: String? = null) {
    override fun toString(): String {
        return "$firstName $lastName"
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserList(var users: MutableList<User>? = null)

class AllUsersActivity : AppCompatActivity() {
    private lateinit var userList : ListView
    private lateinit var adapter : ArrayAdapter<User>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allusers)

        userList = findViewById<ListView>(R.id.userList)
        adapter = ArrayAdapter<User>(this, R.layout.user, R.id.userText,
            mutableListOf<User>())
        userList.adapter = adapter

        val allUsersIntent = intent
        val json = allUsersIntent.getStringExtra("json")

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