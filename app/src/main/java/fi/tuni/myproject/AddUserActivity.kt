package fi.tuni.myproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import kotlin.concurrent.thread

/**
 * This class adds a new user in it's own view
 */
class AddUserActivity : AppCompatActivity(), View.OnClickListener {
    /**
     * This is an override function of onCreate.
     * It sets the content view with input fields
     * for new user's name and a button.
     */
    private lateinit var firstNameEditText : EditText
    private lateinit var lastNameEditText : EditText
    private lateinit var helpText : TextView
    private lateinit var addButton : Button
    private lateinit var firstNameInput : String
    private lateinit var lastNameInput : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adduser)

        firstNameEditText = findViewById(R.id.firstNameInput)
        lastNameEditText = findViewById(R.id.lastNameInput)
        helpText = findViewById(R.id.helpText)
        addButton = findViewById(R.id.addButton)
        //firstNameInput = firstNameEditText.text.toString()
        //lastNameInput = lastNameEditText.text.toString()

        helpText.text = ""
        addButton.setOnClickListener(this)
    }

    /**
     * This is a function that adds the new user to the database
     * @param url the database url address
     * @param json new user's name as json
     * @return responseBody the result of the connection
     */
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

    /**
     * Override function of onClick that turns the name inputs to
     * string and adds them to the new user. It turns the new users data to json
     * and passes it with the url to the database. It also checks if the names are
     * too short or missing.
     */
    override fun onClick(p0: View?) {
        firstNameInput = firstNameEditText.text.toString()
        lastNameInput = lastNameEditText.text.toString()
        val newUser = User(firstNameInput, lastNameInput)
        val json = ObjectMapper().writeValueAsString(newUser)
        val url = "https://dummyjson.com/users/add"

        thread {
            addUser(url, json)
            // runOnUiThread {
                // addedText.text = "User $firstNameInput $lastNameInput added"
            // }
        }

        val newUserIntent = Intent(this, MainActivity::class.java)

        if (firstNameInput != "" && lastNameInput != "") {
            newUserIntent.putExtra("firstName", firstNameInput)
            newUserIntent.putExtra("lastName", lastNameInput)
            startActivity(newUserIntent)
        } else if (firstNameInput.length < 2 && lastNameInput.length < 2) {
            runOnUiThread {
                helpText.text = "Name length must be at least 2 characters"
            }
        } else {
            runOnUiThread {
                helpText.text = "First name or last name missing"
            }
        }
    }
}