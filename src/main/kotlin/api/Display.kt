package org.example.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import java.awt.BorderLayout
import java.util.concurrent.Executors
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.JTextField

object Display {
    private val dispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()
    private val scope = CoroutineScope(dispatcher)
    private val repository = Repository
    private val enterWordLabel = JLabel("Enter word here:")
    private val wordLabel = JTextField(20)
    private val searchButton = JButton("Search").apply {
        addActionListener{
            scope.launch {
                isEnabled = false
                resultArea.text = "loading.."
                val word = wordLabel.text.trim()
                val definition = repository.loadDefinition(word).joinToString("\n\n")
                if (definition.isNotEmpty()){
                    resultArea.text = definition
                }else resultArea.text = "word not found"
                isEnabled = true
            }
        }
    }
    private val resultArea = JTextArea(25,50).apply {
        isEditable = false
        lineWrap = true
        wrapStyleWord = true
    }
    private val complexTopField = JPanel().apply {
        add(enterWordLabel)
        add(wordLabel)
        add(searchButton)
        add(resultArea)
    }
    private val mainFrame = JFrame("DictionaryApp").apply {
        layout = BorderLayout()
        add(complexTopField, BorderLayout.NORTH)
        add(JScrollPane(resultArea), BorderLayout.CENTER)
        pack()
    }

    fun show(){
        mainFrame.isVisible = true
    }

}

fun main()
{
    Display.show()

}