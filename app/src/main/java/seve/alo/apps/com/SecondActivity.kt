package seve.alo.apps.com

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        // Flecha para regresar al Activity
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val textView = findViewById<TextView>(R.id.textViewIntent)
        val btnThirdActivity = findViewById<Button>(R.id.btnThirdActivity)

        val bundle = intent.extras
        if (bundle?.getString("saludo") != null) {
            val saludo = bundle.getString("saludo")
            textView.text = saludo
        } else{
            Toast.makeText(this, "Está vacio", Toast.LENGTH_SHORT).show()
        }

        btnThirdActivity.setOnClickListener{
            startActivity(this, ThirdActivity::class.java)
        }
    }

    private fun startActivity(activity: Activity, nextActivity: Class<*>) {
        val intent = Intent(activity, nextActivity)
        activity.startActivity(intent)
    }
}