package acedo.sergio.misnotas

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.io.File
import java.lang.Exception

class AdaptadorNotas:BaseAdapter {
    var contexto: Context? = null
    var notas = ArrayList<Nota>()

    constructor(contexto: Context, notas: ArrayList<Nota>){
        this.contexto = contexto
        this.notas = notas
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var inflador = LayoutInflater.from(contexto)
        var vista = inflador.inflate(R.layout.nota_layout,null)
        var nota = notas[position]

        vista.findViewById<TextView>(R.id.tv_titulo_det).text = nota.titulo
        vista.findViewById<TextView>(R.id.tv_cotenido_det).text = nota.contenido

        vista.findViewById<Button>(R.id.btn_borrar).setOnClickListener{
            eliminarExterno(nota.titulo)
            notas.remove(nota)
            this.notifyDataSetChanged()
        }

        vista.findViewById<Button>(R.id.btn_editar).setOnClickListener{
            var intent = Intent(contexto, MainActivity::class.java)
            intent.putExtra("titulo", nota.titulo)
            intent.putExtra("contenido", nota.contenido)
            (contexto as Activity).startActivityForResult(intent,123)
        }

        return vista


    }

    private fun eliminarExterno(titulo: String){

        if (titulo == ""){
            Toast.makeText(contexto, "Error: título vacío", Toast.LENGTH_SHORT).show()
        }else{
            try{

                //val ofi = openFileInput(titulo + ".txt")
                val archivo = File(album(),titulo+".txt")
                archivo.delete()

                Toast.makeText(contexto, "Se eliminó el archivo", Toast.LENGTH_SHORT).show()
            }catch(e: Exception){

                Log.e("error", "error")
                Toast.makeText(contexto, "Error al eliminar el archivo", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun album(): String{
        val album = File(Environment.getExternalStorageDirectory(), "notas")
        if(!album.exists()){
            album.mkdir()
        }

        return album.absolutePath
    }

    override fun getItem(position: Int): Any {
        return notas[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return notas.size
    }
}