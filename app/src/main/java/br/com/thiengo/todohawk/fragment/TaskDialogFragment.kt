package br.com.thiengo.todohawk.fragment

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import br.com.thiengo.todohawk.MainActivity
import br.com.thiengo.todohawk.R
import br.com.thiengo.todohawk.domain.ToDo
import kotlinx.android.synthetic.main.fragment_dialog_task.*
import java.util.*


class TaskDialogFragment :
        DialogFragment(),
        View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    /*
     * PARA PERMITIR O ACESSO A CHAVE DO DIALOG NA ATIVIDADE,
     * SEM NECESSIDADE DE USO DE VALORES MÁGICOS (String SEM
     * ESTAR EM UMA DETERMINADA PROPRIEDADE).
     * */
    companion object {
        val KEY = "task_dialog_fragment"
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        /*
         * PARA QUE SEJA REMOVIDA A BARRA DE TOPO DO DIALOG EM
         * DEVICES COM O ANDROID ABAIXO DA API 21.
         * */
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }


    /*
     * SOMENTE PARA A DEFINIÇÃO DO LAYOUT DO DialogFragment
     * */
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.fragment_dialog_task, null, false)
    }


    /*
     * PARA QUE SEJA POSSÍVEL ACESSAR AS VIEWS COM A SINTAXE
     * PERMITIDA PELO kotlin-android-extensions, TEMOS DE
     * UTILIZAR O onResume() NO DIALOG OU QUALQUER OUTRO
     * MÉTODO DO CICLO DE VIDA DO FRAGMENT QUE VENHA DEPOIS
     * DE onCreateView(), POIS CASO CONTRÁRIO, MESMO ACESSANDO
     * A VIEW EM onCreateView(), SERÁ GERADA UMA NullPointerException.
     * ISSO, POIS O LAYOUT AINDA NÃO FOI INICIALIZADO.
     * */
    override fun onResume() {
        super.onResume()
        bt_create_task.setOnClickListener(this)
        sp_months.setOnItemSelectedListener(this)
    }


    /*
     * PARA QUE SEJA CRIADO UM NOVO OBJETO ToDo E ENTÃO SEJA
     * ELE ENVIADO A LISTA DE ITENS VINCULADOS AO ADAPTER DO
     * RecyclerView DA MainActivity.
     * */
    override fun onClick(view: View?) {
        /*
         * CONVERSÃO DE DADOS DE DATA EM String PARA MILLISECONDS.
         * */
        val calendar = Calendar.getInstance()
        calendar.set(
            getSelectedYear(),
            sp_months.selectedItemPosition + 1,
            sp_days.selectedItemPosition + 1,
            0,
            0,
            0
        )

        val toDo = ToDo(
            calendar.timeInMillis,
            et_task.text.toString(),
            sp_duration.selectedItemPosition,
            sp_priority.selectedItemPosition
        )

        (activity as MainActivity).addToList( toDo )
        dismiss()
    }


    /*
     * PARA OBTER O VALOR CORRETO DE ANO QUE ESTÁ EM CADA ITEM DO
     * Spinner DE ANOS DO FORMULÁRIO. ISSO, POIS O VALOR DO ITEM
     * SELECIONADO, APENAS, É PARTINDO DE 0 (ZERO), O QUE NÃO NOS
     * SERVE.
     * */
    private fun getSelectedYear() = (sp_years.selectedView as TextView).text.toString().toInt()


    /*
     * ATUALIZA O ARRAY DE DIAS VINCULADO AO Spinner DE DIAS DO
     * FORMULÁRIO, A ATUALIZAÇÃO OCORRE DE ACORDO COM A MUDANÇA DE
     * VALOR NO Spinner DE MÊS.
     * */
    override fun onItemSelected(
            parentView: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long) {

        val arrayDays = getArrayDaysResource( position )
        val adapter = ArrayAdapter.createFromResource(
            activity,
            arrayDays,
            android.R.layout.simple_spinner_item )

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        sp_days.setAdapter(adapter)
    }


    /*
     * RETORNA O RESOURCE DO ARRAY DE DIAS CORRETO DE ACORDO COM
     * O VALOR DE MÊS PASSADO COMO PARÂMETRO.
     * */
    private fun getArrayDaysResource( month: Int )
        = if( month in arrayOf(0,2,4,6,7,9,11) ){
            R.array.days_31
        }
        else if( month in arrayOf(3,5,8,10) ){
            R.array.days_30
        }
        else{
            if( isLeapYear( getSelectedYear() ) ){
                R.array.days_29
            }
            else{
                R.array.days_28
            }
        }


    /*
     * VERIICA SE O ANO É BISSEXTO PARA A ESCOLHA DO ARRAY DE DIAS
     * CORRETO NO MÊS DE FEVEREIRO.
     * */
    private fun isLeapYear(year: Int)
        = if (year % 4 == 0) {
            if (year % 100 == 0) {
                year % 400 == 0
            }
            else{
                true
            }
        }
        else{
            false
        }


    /*
     * SOBRESCRITA OBRIGATÓRIA DE MÉTODO DEVIDO A IMPLEMENTAÇÃO DA
     * INTERFACE OnItemSelectedListener. PORÉM O MÉTODO NÃO É UTILIZADO.
     * */
    override fun onNothingSelected(p0: AdapterView<*>?) {}
}
