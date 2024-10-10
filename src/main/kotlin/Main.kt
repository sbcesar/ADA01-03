package org.example

import org.example.FileManager.FileManager
import org.example.Output.Console
import java.nio.file.Path


fun main() {
    val console = Console()
    val fileManager = FileManager(console)

    val employeeFileCsv = Path.of("src/main/resources/employees.csv")

    val directory = Path.of("src/main/resources")
    fileManager.verifyAndCreateDirectory(directory)
    val employeeFileXml = directory.resolve("empleados.xml")

    fileManager.menu(employeeFileCsv, employeeFileXml, directory)
}