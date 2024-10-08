package org.example

import java.nio.file.Files
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

fun fileReader(employeesFile: Path): List<Employee> {
    val employeeList = mutableListOf<Employee>()

    var isFirstLine = true

    val bufferedReader = Files.newBufferedReader(employeesFile)
    bufferedReader.use { br ->
        br.forEachLine { line ->
            if (isFirstLine) {
                line.split(",")
                isFirstLine = false
            } else {
                val employeeInfo = line.split(",")
                for (i in employeeInfo.indices) {
                    val employee = Employee(employeeInfo[i])
                }
            }
        }
    }
}

fun main() {

}