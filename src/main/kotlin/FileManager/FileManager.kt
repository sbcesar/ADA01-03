package org.example.FileManager

import org.example.Model.Employee
import org.example.Output.IConsole
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
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

    fun menu(employeesFile: Path, directory: Path) {
        console.showMessage("Choose an option: \n\t- 1. Create xml.\n\t- 2. Change salary by id")
        val option = readln().toInt()
        var firstTime = false

        when (option) {
            1 -> {
                verifyAndCreateDirectory(directory)     //Verify if the directory exist
                val employeeList = csvFileReader(employeesFile)   //Read the csv and returns a list
                createXml(employeesFile,employeeList)   //Create the xml with csv info
            }
            2 -> {
                firstTime = true
                val employeeListCsv = csvFileReader(employeesFile)
                editSalaryXml(employeesFile,employeeListCsv)
                if (firstTime == true) {
                    val employeeListXml = xmlFileReader(employeesFile)
                    editSalaryXml(employeesFile,xmlFileReader(employeesFile))
                }

            }
            else -> {
                console.showMessage("Invalid option")
            }
        }
    }

    fun csvFileReader(employeesFileCsv: Path): List<Employee> {

        val employeeList = mutableListOf<Employee>()

        Files.newBufferedReader(employeesFileCsv).use { br ->

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

    fun xmlFileReader(employeesFileXml: Path): List<Employee> {
        val employeeList = mutableListOf<Employee>()

        val document = createDocument(employeesFileXml)

        val employeeElement = document.documentElement
        val employees = employeeElement.getElementsByTagName("empleado")

        for (i in 0..<employees.length) {
            val employeeNode = employees.item(i)

            if (employeeNode.nodeType == Node.ELEMENT_NODE) {
                val employee = employeeNode as Element

                val id = employee.getAttribute("id").toString()
                val surname = employee.getElementsByTagName("apellido").item(0).textContent
                val department = employee.getElementsByTagName("departamento").item(0).textContent
                val salary = employee.getElementsByTagName("salario").item(0).textContent.toDouble()

                val employeeInfo = Employee(id.toInt(),surname, department, salary)

                employeeList.add(employeeInfo)
            }
        }

        return employeeList
    }

    fun verifyAndCreateDirectory(directory: Path) {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory)
            } catch (e: Exception) {
                console.showMessage("Error creating directory ${e.message}")
            }
        }
    }

     fun createXml(employeesFile: Path, employeeList: List<Employee>) {
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

        saveXml(document,employeesFile)
    }

    fun editSalaryXml(employeesFile: Path, employeeList: List<Employee>) {

        console.showMessage("Introduce the employee's id that you want to change the salary: ")
        val employeeId = readln().toIntOrNull()

        console.showMessage("Introduce the new salary: ")
        val newSalary = readln().toDoubleOrNull()

        try {

            if (employeeId == null) {
                throw IllegalArgumentException("Invalid ID argument")
            }

            if (newSalary == null) {
                throw IllegalArgumentException("Invalid salary argument")
            }

            val finder = employeeList.find { employee -> employee.ID == employeeId }

            if (finder != null) {
                val document = createDocument(employeesFile)

                val employeeElement = document.documentElement
                val employees = employeeElement.getElementsByTagName("empleado")

                for (i in 0..<employees.length) {
                    val employeeNode = employees.item(i)

                    if (employeeNode.nodeType == Node.ELEMENT_NODE) {
                        val employee = employeeNode as Element

                        val id = employee.getAttribute("id").toInt()
                        if (id == employeeId) {
                            employee.getElementsByTagName("salario").item(0).textContent = newSalary.toString()
                            break
                        }
                    }
                }

                saveXml(document,employeesFile)
            } else {
                throw Exception("Employee not found")
            }
        } catch (e: Exception) {
            console.showMessage("Error changing the salary of the employee with id (${employeeId})\nError: ${e.message}")
        }
    }

    private fun createDocument(employeesFile: Path): Document {
        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.parse(employeesFile.toFile())
        document.documentElement.normalize()

        return document
    }

    private fun saveXml(document: Document?, employeesFile: Path) {
        val source = DOMSource(document)
        val result = StreamResult(employeesFile.toFile())
        val transformer = TransformerFactory.newInstance().newTransformer()

        transformer.setOutputProperty(OutputKeys.INDENT,"yes")
        transformer.transform(source,result)
    }

    fun xmlReader(employeesFile: Path) {
        val document = createDocument(employeesFile)

        val employeeElement = document.documentElement
        val employees = employeeElement.getElementsByTagName("empleado")

        for (i in 0..<employees.length) {
            val employeeNode = employees.item(i)

            if (employeeNode.nodeType == Node.ELEMENT_NODE) {
                val employee = employeeNode as Element

                val id = employee.getAttribute("id").toString()
                val surname = employee.getElementsByTagName("apellido").item(0).textContent
                val department = employee.getElementsByTagName("departamento").item(0).textContent
                val salary = employee.getElementsByTagName("salario").item(0).textContent.toDouble()

                console.showMessage("ID: $id, Apellido: $surname, Departamento: $department, Salario: $salary")
            }
        }
    }

}