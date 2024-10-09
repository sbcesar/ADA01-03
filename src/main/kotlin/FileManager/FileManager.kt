package org.example.FileManager

import org.example.Model.Employee
import org.example.Output.IConsole
import org.w3c.dom.Document
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class FileManager(
    val console: IConsole
) {

    fun fileReader(employeesFile: Path): List<Employee> {

        val employeeList = mutableListOf<Employee>()


        Files.newBufferedReader(employeesFile).use { br ->

            br.readLine()

            br.forEachLine { line ->
                val employeeInfo = line.split(",")
                val employee = Employee(
                    ID = employeeInfo[0].toInt(),
                    surname = employeeInfo[1],
                    department = employeeInfo[2],
                    salary = employeeInfo[3].toDouble()
                )
                employeeList.add(employee)
            }
        }

        return employeeList
    }

    fun verifyAndCreateXML(directory: String) {
        val path: Path = Path.of(directory)


        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path)
            } catch (e: Exception) {
                console.showMessage("Error creating directory ${e.message}")
            }
        }
    }

    fun createXmlFile(file: String): Document {
        val docFactory = DocumentBuilderFactory.newInstance().newDocumentBuilder()
        return docFactory.newDocument()
    }

    fun xmlWriter(empleados: List<Employee>, archivoXml: String) {
        try {
            val doc: Document = createXmlFile(archivoXml)

            val rootElement = doc.createElement("Empleados")
            doc.appendChild(rootElement)

            for (empleado in empleados) {
                val empleadoElement = doc.createElement("Empleado")

                val id = doc.createElement("ID")
                id.appendChild(doc.createTextNode(empleado.ID.toString()))
                empleadoElement.appendChild(id)

                val apellido = doc.createElement("Apellido")
                apellido.appendChild(doc.createTextNode(empleado.surname))
                empleadoElement.appendChild(apellido)

                val departamento = doc.createElement("Departamento")
                departamento.appendChild(doc.createTextNode(empleado.department))
                empleadoElement.appendChild(departamento)

                val salario = doc.createElement("Salario")
                salario.appendChild(doc.createTextNode(empleado.salary.toString()))
                empleadoElement.appendChild(salario)

                rootElement.appendChild(empleadoElement)
            }

            val transformer = TransformerFactory.newInstance().newTransformer()
            val source = DOMSource(doc)
            val result = StreamResult(File(archivoXml))
            transformer.transform(source, result)

            console.showMessage("XML file created sucessfully in: $archivoXml")

        } catch (e: Exception) {
            console.showMessage("Error writing in the xml file ${e.message}")
        }
    }

}