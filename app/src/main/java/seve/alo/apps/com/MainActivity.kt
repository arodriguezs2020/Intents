package seve.alo.apps.com

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import java.util.*

class MainActivity : AppCompatActivity() {

    val SALUDO = "Hola desde el Activity main"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Forzar icono en el Action Bar
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setIcon(R.mipmap.ic_launcher)

        val buttonCapturar = findViewById<Button>(R.id.buttonCalcular)
        val buttonSiguiente = findViewById<Button>(R.id.buttonSiguiente)
        val editText = findViewById<EditText>(R.id.editTextNumber)
        val textView = findViewById<TextView>(R.id.textView)

        buttonCapturar.text = "Calcula tu edad"

        buttonCapturar.setOnClickListener {
            val anoNacimiento : Int = editText.text.toString().toInt()
            val anoActual = Calendar.getInstance().get(Calendar.YEAR)
            val miEdad = anoActual - anoNacimiento
            textView.text = "Tu edad es $miEdad años"
        }

        buttonSiguiente.setOnClickListener {
            startActivity(this, SecondActivity::class.java)
        }

    }

    private fun startActivity(activity: Activity, nextActivity: Class<*>) {
        val intent = Intent(activity, nextActivity)
        intent.putExtra("saludo", SALUDO)
        activity.startActivity(intent)
    }
}