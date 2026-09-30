@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import myapplication.shared.generated.resources.Res
import myapplication.shared.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

/**
 * Aplicación de Registro Académico mejorada con componentes de Material Design 3.
 * Incluye Scaffold, TopAppBar, Imágenes y Menús.
 */
@Composable
fun App() {
    // --- ESTADOS DEL FORMULARIO ---
    var nombre by remember { mutableStateOf("") }
    var matricula by remember { mutableStateOf("") }
    var asignatura by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") } 
    var fecha by remember { mutableStateOf("") }

    // --- ESTADOS DE ERROR ---
    var errorNombre by remember { mutableStateOf(false) }
    var errorMatricula by remember { mutableStateOf(false) }
    var errorHora by remember { mutableStateOf(false) }
    var errorFecha by remember { mutableStateOf(false) }

    // --- ESTADOS DE GUARDADO ---
    var nombreGuardado by remember { mutableStateOf("") }
    var matriculaGuardada by remember { mutableStateOf("") }
    var asignaturaGuardada by remember { mutableStateOf("") }
    var horaGuardada by remember { mutableStateOf("") }
    var fechaGuardada by remember { mutableStateOf("") }
    
    var mostrarTarjeta by remember { mutableStateOf(false) }
    var menuExpandido by remember { mutableStateOf(false) }

    // Estructura Principal: Scaffold
    Scaffold(
        topBar = {
            // Barra de herramientas (TopAppBar)
            TopAppBar(
                title = { Text("Registro Académico") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    // Menú de opciones (DropdownMenu)
                    TextButton(onClick = { menuExpandido = true }) {
                        Text("Opciones", color = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                    DropdownMenu(
                        expanded = menuExpandido,
                        onDismissRequest = { menuExpandido = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Limpiar campos") },
                            onClick = { 
                                nombre = ""; matricula = ""; asignatura = ""; hora = ""; fecha = ""
                                mostrarTarjeta = false
                                menuExpandido = false 
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        // Contenedor con Scroll Responsivo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            
            // Imagen de cabecera (Logo)
            Image(
                painter = painterResource(Res.drawable.compose_multiplatform),
                contentDescription = "Logo de la app",
                modifier = Modifier.size(80.dp).padding(bottom = 8.dp)
            )

            // Etiqueta de Título
            Text(
                text = "Formulario de Entrega",
                style = MaterialTheme.typography.headlineSmall
            )

            // Cuadro de texto: Nombre
            TextField(
                value = nombre,
                onValueChange = { nombre = it; errorNombre = false },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth(),
                isError = errorNombre,
                supportingText = { if (errorNombre) Text("Ingrese solo letras") },
                singleLine = true
            )

            // Cuadro de texto: Matrícula
            TextField(
                value = matricula,
                onValueChange = { matricula = it; errorMatricula = false },
                label = { Text("Matrícula") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = errorMatricula,
                supportingText = { if (errorMatricula) Text("Solo números") },
                singleLine = true
            )

            TextField(
                value = asignatura,
                onValueChange = { asignatura = it },
                label = { Text("Asignatura") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Campo Hora con VisualTransformation
            TextField(
                value = hora,
                onValueChange = { if (it.length <= 4) { hora = it; errorHora = false } },
                label = { Text("Hora (HH:MM)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = TimeTransformation(),
                isError = errorHora,
                supportingText = { if (errorHora) Text("Formato 24h inválido") },
                singleLine = true
            )

            // Campo Fecha con VisualTransformation
            TextField(
                value = fecha,
                onValueChange = { if (it.length <= 8) { fecha = it; errorFecha = false } },
                label = { Text("Fecha (DD/MM/AAAA)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = DateTransformation(),
                isError = errorFecha,
                supportingText = { if (errorFecha) Text("Fecha inválida") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón de Acción
            Button(
                onClick = {
                    val nValido = nombre.isNotBlank() && nombre.all { it.isLetter() || it.isWhitespace() }
                    val mValida = matricula.isNotBlank() && matricula.all { it.isDigit() }
                    
                    val hValida = try {
                        hora.length == 4 && 
                        hora.all { it.isDigit() } &&
                        hora.substring(0, 2).toInt() in 0..23 && 
                        hora.substring(2, 4).toInt() in 0..59
                    } catch (e: Exception) { false }
                    
                    val fValida = try {
                        fecha.length == 8 && 
                        fecha.all { it.isDigit() } &&
                        fecha.substring(0, 2).toInt() in 1..31 && 
                        fecha.substring(2, 4).toInt() in 1..12
                    } catch (e: Exception) { false }
                    
                    if (nValido && mValida && asignatura.isNotBlank() && hValida && fValida) {
                        nombreGuardado = nombre
                        matriculaGuardada = matricula
                        asignaturaGuardada = asignatura
                        horaGuardada = "${hora.substring(0, 2)}:${hora.substring(2, 4)}"
                        fechaGuardada = "${fecha.substring(0, 2)}/${fecha.substring(2, 4)}/${fecha.substring(4)}"
                        mostrarTarjeta = true
                    } else {
                        errorNombre = !nValido
                        errorMatricula = !mValida
                        errorHora = !hValida
                        errorFecha = !fValida
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Registro")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de resultados
            if (mostrarTarjeta) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    elevation = CardDefaults.cardElevation(6.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(text = "Información del Registro", style = MaterialTheme.typography.titleLarge)
                        HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                        
                        DatoFila("Nombre", nombreGuardado)
                        DatoFila("Matrícula", matriculaGuardada)
                        DatoFila("Asignatura", asignaturaGuardada)
                        DatoFila("Hora", horaGuardada)
                        DatoFila("Entrega", fechaGuardada)
                    }
                }
            }
        }
    }
}

@Composable
fun DatoFila(etiqueta: String, valor: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Text(text = "$etiqueta: ", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Text(text = valor)
    }
}

class TimeTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val out = buildAnnotatedString {
            for (i in text.indices) {
                append(text[i])
                if (i == 1) append(":")
            }
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int) = if (offset <= 1) offset else offset + 1
            override fun transformedToOriginal(offset: Int) = if (offset <= 2) offset else offset - 1
        }
        return TransformedText(out, offsetMapping)
    }
}

class DateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val out = buildAnnotatedString {
            for (i in text.indices) {
                append(text[i])
                if (i == 1 || i == 3) append("/")
            }
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 3) return offset + 1
                return offset + 2
            }
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                return offset - 2
            }
        }
        return TransformedText(out, offsetMapping)
    }
}
