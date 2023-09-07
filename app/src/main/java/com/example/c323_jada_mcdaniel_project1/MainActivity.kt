package com.example.c323_jada_mcdaniel_project1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    private var canAddOperation = false
    private var canAddDecimal = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //I was following a tutorial for most of this and the plugin they used is no longer supported.
    //I don't know how to work around that issue.

    val workingTV = findViewById<TextView>(R.id.workingTV)
    val resultsTV = findViewById<TextView>(R.id.resultsTV)

    fun allClearAction(view: View) {
        workingTV.text = ""
        resultsTV.text = ""
    }
    fun equalsAction(view: View) {
        resultsTV.text = calculateResults()
    }


    fun numberAction(view: View){
        if(view is Button){
            if(view.text == "."){
                if(canAddDecimal){
                    workingTV.append(view.text)
                    canAddDecimal = false
                }
                else{
                    workingTV.append(view.text)
                    canAddOperation = true
                }
            }
        }
    }

    fun operationAction(view: View){
        if(view is Button){
            workingTV.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }


    private fun calculateResults(): String {
        val digitsOperators = digitsOperators()
        if(digitsOperators.isEmpty()){
            return ""
        }

        val timesDivision = timesDivisionCalculate(digitsOperators)

        if(timesDivision.isEmpty()){
            return ""
        }

        val result = addSubtractCalculate(timesDivision)
        return result.toString()
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex){
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if(operator == '+'){
                    result += nextDigit
                }
                if(operator == '-'){
                    result -= nextDigit
                }
            }
        }
        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while(list.contains('X') || list.contains ('/')){
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex){
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when(operator){
                    'X' ->
                    {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' ->
                    {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else ->
                    {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if(i > restartIndex){
                newList.add(passedList[i])
            }
        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any>{
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in workingTV.text){
            if(character.isDigit() || character == '.'){
                currentDigit += character
            }
            else{
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if(currentDigit != ""){
            list.add(currentDigit.toFloat())
        }

        return list
    }
}