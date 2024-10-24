package com.example.patitasvivas

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun mensaje(show: Boolean, cerrar: () -> Unit, dialogTitle: String, dialogText: String) {
    if (show) {

        AlertDialog(onDismissRequest = {cerrar()},
            confirmButton = {
                TextButton(onClick = { cerrar() }){
                    Text(text="Confirmar")
                }
            },
            title = {
                Text(text = dialogTitle)
            },
            text = {
                Text(text = dialogText)
            }
        )
    }

}