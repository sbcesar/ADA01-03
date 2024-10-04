package org.example.Output

class Console: IConsole {

    override fun showMessage(message: String, lineBreak: Boolean) {
        if (lineBreak) println(message) else print(message)
    }
}