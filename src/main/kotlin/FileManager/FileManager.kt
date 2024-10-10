package org.example.FileManager

import org.example.Model.Employee
import org.example.Output.IConsole
import org.w3c.dom.Document
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class FileManager(
    private val console: IConsole
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

    fun verifyAndCreateXML(directory: Path) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory)
            } catch (e: Exception) {
                console.showMessage("Error creating directory ${e.message}")
            }
        }
    }

     fun createXml(destinationFile: Path, employeeList: List<Employee>) {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val impl = builder.domImplementation

        val document = impl.createDocument(null, "empleados", null)

        for (employee in employeeList) {
            val employeeElement = document.createElement("empleado")
            employeeElement.setAttribute("id", employee.ID.toString())
            document.documentElement.appendChild(employeeElement)

            val surnameElement = document.createElement("apellido")
            val departmentElement  = document.createElement("departamento")
            val salaryElement  = document.createElement("salario")

            val surnameText = document.createTextNode(employee.surname)
            val departmentText = document.createTextNode(employee.department)
            val salaryText = document.createTextNode(employee.salary.toString())

            surnameElement.appendChild(surnameText)
            departmentElement.appendChild(departmentText)
            salaryElement.appendChild(salaryText)

            employeeElement.appendChild(surnameElement)
            employeeElement.appendChild(departmentElement)
            employeeElement.appendChild(salaryElement)
        }

        val transformer = TransformerFactory.newInstance().newTransformer()
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")

        val source = DOMSource(document)
        val result = StreamResult(destinationFile.toFile())
        transformer.transform(source,result)
    }

    fun editSalaryXml(employeeFile: Path, employeeId: Int, newSalary: Double) {
        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val document = builder.parse(employeeFile.toFile())

            val employee = document.getElementsByTagName("empleado")
            TODO("TERMINAR EL METODO, HACER BUCLE QUE RECORRA TODOS LOS EMPLEADOS PARA QUE ENCUENTRE EL ID, COMPRUEBE SI EL ATRIBUTO COINCIDE CON EL ID, IMPORTANTE GUARDAR CAMBIOS (TRANSFORMER)")
        } catch (e: Exception) {
            console.showMessage("Error changing the salary of the employee n${employeeId}/nError: ${e.message}")
        }
    }

    fun xmlReader() {

    }


}