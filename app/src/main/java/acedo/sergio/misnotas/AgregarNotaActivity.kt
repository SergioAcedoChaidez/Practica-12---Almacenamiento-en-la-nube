package acedo.sergio.misnotas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ListView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.*

class AgregarNotaActivity : AppCompatActivity() {
    var listaNotas = ArrayList<Nota>()
    var adaptador: AdaptadorNotas?  = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_nota)
        setSupportActionBar(findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar))

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            var intent = Intent(this, MainActivity::class.java)
            startActivityForResult(intent,123)
        }

        leerNotas()

        adaptador = AdaptadorNotas(this, listaNotas)
        findViewById<ListView>(R.id.listview).adapter = adaptador
    }

    fun leerNotas(){
        listaNotas.clear()
        var carpeta = File(ubicacion().absolutePath)

        if (carpeta.exists()){
            var archivos = carpeta.listFiles()
            if(archivos != null){
                for (archivo in archivos){
                    leerArchivo(archivo)
                }
            }

        }
    }

    fun leerArchivo(archivo: File){
        val fis = FileInputStream(archivo)

        val di = DataInputStream(fis)
        val br = BufferedReader(InputStreamReader(di))
        var strLine: String? = br.readLine()
        var myData = ""
        while (strLine != null ){
            myData = myData + strLine

            strLine = br.readLine()
        }

        br.close()

        di.close()
        fis.close()

        var nombre = archivo.name.substring(0, archivo.name.length-4)
        var nota = Nota(nombre,myData)

        listaNotas.add(nota)
    }

    private fun ubicacion(): File {
        val folder = File(Environment.getExternalStorageDirectory(), "notas")
        if(!folder.exists()){
            folder.mkdir()
        }

        return folder
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        leerNotas()
        adaptador?.notifyDataSetChanged()
    }

}