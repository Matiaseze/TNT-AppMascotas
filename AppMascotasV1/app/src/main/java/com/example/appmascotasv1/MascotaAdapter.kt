package com.example.appmascotasv1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class MascotaAdapter(
    private val context: Context,
    private val mascotas: List<Mascota>
) : ArrayAdapter<Mascota>(context, 0, mascotas) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_mascota, parent, false)

        val mascota = mascotas[position]

        val nombre = view.findViewById<TextView>(R.id.txtNombre)
        val info = view.findViewById<TextView>(R.id.txtInfo)

        nombre.text = mascota.nombre
        info.text = "${mascota.tipo} - ${mascota.edad}"

        return view
    }
}