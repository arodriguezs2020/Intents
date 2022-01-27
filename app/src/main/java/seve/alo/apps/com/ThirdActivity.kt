package seve.alo.apps.com

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.ActivityCompat

class ThirdActivity : AppCompatActivity() {

    private val PHONE_CODE = 4000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        // Flecha para regresar al Activity
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val imageButtonPhone = findViewById<ImageButton>(R.id.imageButtonPhone)
        val imageButtonWeb = findViewById<ImageButton>(R.id.imageButtonWeb)
        val imageButtonCamera = findViewById<ImageButton>(R.id.imageButtonCamera)
        val buttonEmail = findViewById<Button>(R.id.buttonEmail)
        val buttonContactPhone = findViewById<Button>(R.id.buttonContactPhone)
        val editTextPhone = findViewById<EditText>(R.id.editTextPhone)
        val editTextWeb = findViewById<EditText>(R.id.editTextWeb)

        // --- Botón para la llamada --- //
        imageButtonPhone!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                val phoneNumber = editTextPhone!!.text.toString()
                if (phoneNumber.isNotEmpty()) {
                    // --- Comprobar versión actual VS version MarshMallow --- //
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        // --- Comprobar el permiso --- //
                        if (checkedPermission(android.Manifest.permission.CALL_PHONE)){
                            // --- Si está el permiso en el manifest ha aceptado --- //
                            val intentAceptado = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                            if (ActivityCompat.checkSelfPermission(this@ThirdActivity, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                                return
                            }
                            startActivity(intentAceptado)
                        }else{ // --- Preguntarle por el permiso --- //
                            if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CALL_PHONE)){
                                requestPermissions(arrayOf(android.Manifest.permission.CALL_PHONE), PHONE_CODE)
                            } else {
                                /* --- Si ya denegó el permiso y quiere acceder nuevamente
                                        lo dirigimos a los Ajustes para que habilite el permiso */
                                Toast.makeText(this@ThirdActivity, "Porfavor habilita el permiso correspondiente para continuar", Toast.LENGTH_LONG).show()
                                val intentSetting = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                intentSetting.addCategory(Intent.CATEGORY_DEFAULT)
                                intentSetting.data = Uri.parse("package:$packageName")
                                intentSetting.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                intentSetting.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                                intentSetting.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                                startActivity(intentSetting)
                            }
                        }
                    }else {
                        versionAntigua(phoneNumber)
                    }
                } else {
                    Toast.makeText(this@ThirdActivity, "Debes marcar un número, intenta nuevamente", Toast.LENGTH_LONG).show()
                }
            }
            fun versionAntigua(phoneNumber: String) {
                val intentCall = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                if (checkedPermission(android.Manifest.permission.CALL_PHONE)) {
                    if (ActivityCompat.checkSelfPermission(this@ThirdActivity, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                        return
                    }
                    startActivity(intentCall)
                }
            }
        })

        // --- Botón para la Web --- //
        imageButtonWeb!!.setOnClickListener {
            val url = editTextWeb!!.text.toString()
            val intentWeb = Intent()
            intentWeb.action = Intent.ACTION_VIEW
            intentWeb.data = Uri.parse("http://$url")
            startActivity(intentWeb)
        }
        // --- Botón para el Email --- //
        buttonEmail.setOnClickListener{
            val email = "miemail@gmail.com"
            val intentEmail = Intent(Intent.ACTION_SEND, Uri.parse(email))
            intentEmail.type = "plain/text"
            intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Titulo del email")
            intentEmail.putExtra(Intent.EXTRA_TEXT, "Hola, estoy esperando la respuesta...")
            intentEmail.putExtra(Intent.EXTRA_EMAIL, arrayOf("alguien@gmail.com", "alguienmas@gmail.com"))
            startActivity(Intent.createChooser(intentEmail, "Elige cliente de correo"))

        }
        // --- Botón para la llamada sin permisos --- //
        buttonContactPhone!!.setOnClickListener {
            val intentCall = Intent(Intent.ACTION_DIAL, Uri.parse("tel:634593315"))
            startActivity(intentCall)
        }

        // --- Botón para la cámara --- //
        imageButtonCamera!!.setOnClickListener {
            val intentCamera = Intent("android.media.action.IMAGE_CAPTURE")
            startActivity(intentCamera)
        }
    }

    // Implementar menú
    override fun onCreateOptionsMenu(menu: Menu) : Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    // TODO: Esto es para probar que funciona el TODO en Android Studio
    // Implementar funcion para cuando selecciona una de las opciones del menú
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menuContactos -> {
                val intentContact = Intent(Intent.ACTION_VIEW, Uri.parse("content://contact/people"))
                startActivity(intentContact)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // --- Método asíncrono para comprobar permisos --- //
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        val editTextPhone = findViewById<EditText>(R.id.editTextPhone)
        when (requestCode) {
            // Caso de uso
            PHONE_CODE ->{
                val permiso = permissions[0]
                val resultado = grantResults[0]

                if (permiso == android.Manifest.permission.CALL_PHONE){
                    // --- Comprobar si ha sido aceptado o denegado la peticion de permiso --- //
                    if (resultado == PackageManager.PERMISSION_GRANTED){
                        // --- Concedió su permiso --- //
                        val phoneNumber = editTextPhone!!.text.toString()
                        val intentCall = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                        // --- Debemos verificar que exista el permiso en el manifest explicitamente --- //
                        // --- ya que el usuario puede rechazar esta petición de permiso --- //
                        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            return
                        }
                        startActivity(intentCall)
                    }else{
                        // --- Denegó el permiso --- //
                        Toast.makeText(this, "Has denegado el permiso", Toast.LENGTH_LONG).show()
                    }
                }
            }
            else ->super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    // --- Verificamos que el permiso esté aceptado --- //
    fun checkedPermission(permission: String): Boolean {
        val result = this.checkCallingOrSelfPermission(permission)
        return result == PackageManager.PERMISSION_GRANTED
    }
}