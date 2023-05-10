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



class AllUsersActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_allusers)

        //var newUserIntent : Intent = intent
        //var newUserBundle : Bundle? = newUserIntent.extras
        //var firstName = newUserBundle?.getString("firstName")
        //var lastName = newUserBundle?.getString("lastName")
        //newUser = User(firstName, lastName)
    }
}