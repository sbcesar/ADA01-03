package org.example

import org.example.FileManager.FileManager
import org.example.Output.Console
import java.nio.file.Path

/*
- Funcion leer fichero (cada linea representa un empleado y separados por coma)
- Funcion crear XML (usar DOM)
- Funcion modificar que permita modificar el salario en funcion del ID
- Funcion leer XML que obtiene los datos del XML y la muestra por la terminal (ID: 101, Apellido: García, Departamento: Recursos Humanos, Salario: 3000)

QUE DEBES USAR
---------------
clases Path, Files, BufferedReader
bibliotecas javax.xml.* org.w3c.*
manejo de excepciones
cerrar flujos
 */

//si el xml esta vacio que la ejecute y si no que no
fun main() {
    val console = Console()
    val fileManager = FileManager(console)

    val employeeFileCsv = Path.of("src/main/resources/employees.csv")

    val directory = Path.of("src/main/resources")
    fileManager.verifyAndCreateDirectory(directory)
    val employeeFileXml = directory.resolve("empleados.xml")

    fileManager.menu(employeeFileCsv, employeeFileXml, directory)
}