package br.com.thiengo.todohawk

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.ImageView
import android.widget.TextView
import br.com.thiengo.todohawk.domain.ToDo


class ToDoAdapter(
        private val context: Context,
        private val toDoList: List<ToDo>) :
        RecyclerView.Adapter<ToDoAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int) : ToDoAdapter.ViewHolder {

        val v = LayoutInflater
                .from(context)
                .inflate(R.layout.iten_todo, parent, false)

        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setData(toDoList[position])
    }


    override fun getItemCount(): Int {
        return toDoList.size
    }


    inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView),
            CompoundButton.OnCheckedChangeListener {

        var ivPriority: ImageView
        var tvDate: TextView
        var tvTask: TextView
        var tvDuration: TextView
        var cbDone: CheckBox

        init {
            ivPriority = itemView.findViewById(R.id.iv_ic_priority)
            tvDate = itemView.findViewById(R.id.tv_date)
            tvTask = itemView.findViewById(R.id.tv_task)
            tvDuration = itemView.findViewById(R.id.tv_duration)
            cbDone = itemView.findViewById(R.id.cb_done)
            cbDone.setOnCheckedChangeListener(this)
        }

        fun setData(toDo: ToDo) {
            ivPriority.setImageResource( toDo.getPriorityIcon() )
            tvDate.text = toDo.getDateFormatted()
            tvTask.text = toDo.task
            tvDuration.text = context.resources.getStringArray(R.array.durations)[toDo.duration]
            cbDone.isChecked = false
        }

        override fun onCheckedChanged(checkBox: CompoundButton?, status: Boolean) {
            val ma = (context as MainActivity)

            if( !ma.isRecyclerAnimationg() ){
                ma.removeFromList( adapterPosition )
            }
            else{
                (checkBox as CheckBox).isChecked = false
            }
        }
    }
}