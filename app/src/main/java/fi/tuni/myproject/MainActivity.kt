package fi.tuni.myproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var firstNameEditText : EditText
    private lateinit var lastNameEditText : EditText
    private lateinit var addUserButton : Button
    private lateinit var getAllUsersButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstNameEditText = findViewById(R.id.firstNameInput)
        lastNameEditText = findViewById(R.id.lastNameInput)
        addUserButton = findViewById(R.id.addUserButton)
        getAllUsersButton = findViewById(R.id.allUsersButton)
        addUserButton.setOnClickListener(this)
        getAllUsersButton.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.allUsersButton -> {
                val allUsersIntent = Intent(this, AllUsersActivity::class.java)
                startActivity(allUsersIntent)
            }
            R.id.addUserButton -> {
                var firstNameInput = firstNameEditText.text.toString()
                var lastNameInput = lastNameEditText.text.toString()
                val addUserIntent = Intent(this, AddUserActivity::class.java)
                addUserIntent.putExtra("firstName", firstNameInput)
                addUserIntent.putExtra("lastName", lastNameInput)
                startActivity(addUserIntent)
            }
        }
    }
}