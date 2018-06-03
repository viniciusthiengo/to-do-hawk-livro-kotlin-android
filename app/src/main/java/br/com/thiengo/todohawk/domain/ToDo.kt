package br.com.thiengo.todohawk.domain

import br.com.thiengo.todohawk.R
import java.util.*


data class ToDo(
        val date: Long,
        val task: String,
        val duration : Int,
        val priority: Int) {

    companion object {
        @JvmField val TO_DO_LIST_KEY = "to_do_list"
    }


    fun getDateFormatted(): String{
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date

        var date = """
            ${getNumDate(calendar.get(Calendar.DAY_OF_MONTH))}/
            ${getNumDate(calendar.get(Calendar.MONTH))}/
            ${getNumDate(calendar.get(Calendar.YEAR))}
        """
        return date.replace("\n", "").replace(" ", "").trim()
    }


    fun getDateInSeconds() = date / 1000


    private fun getNumDate( num: Int )
        = if( num < 10 ){
            "0$num"
        }
        else{
            "$num"
        }


    fun getPriorityIcon()
        = if( priority == 1 ){
            R.drawable.ic_priority_low
        }
        else if( priority == 2 ){
            R.drawable.ic_priority_medium
        }
        else{
            R.drawable.ic_priority_high
        }
}