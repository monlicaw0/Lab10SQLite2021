package com.myweb.lab10sqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.myweb.lab10sqlite.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var dbHandler:DatabaseHelper
    private var studentList = arrayListOf<Student>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root )

        dbHandler = DatabaseHelper.getInstance(this)
        dbHandler.writableDatabase
        callStudentData()
        binding.recyclerView.adapter = StudentsAdapter(studentList,applicationContext)
        binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(applicationContext,
                LinearLayoutManager.VERTICAL)
        )
    }
    override fun onResume() {
        super.onResume()
        callStudentData()
    }
    fun callStudentData(){
        studentList.clear()
        studentList.addAll(dbHandler.getAllStudents())
        binding.recyclerView.adapter?.notifyDataSetChanged()
    }
    fun addStudentDialog(v: View){
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.insert_layout, null)
        val myBuilder = AlertDialog.Builder(this)
        myBuilder.setView(mDialogView)
        ///Save Button
        myBuilder.setPositiveButton("Save" ) { dialog, which ->
            val id = mDialogView.findViewById(R.id.edt_id) as EditText
            val name = mDialogView.findViewById(R.id.edt_name) as EditText
            val age = mDialogView.findViewById(R.id.edt_age) as EditText
            var result = dbHandler.insertStudent(Student(id= id.text.toString(),name=name.text.toString(),
                age=age.text.toString().toInt()))
            if(result > -1){
                Toast.makeText(applicationContext,"The student is inserted successfully",
                    Toast.LENGTH_LONG).show()
                dialog.dismiss()
                callStudentData()
            }else{
                Toast.makeText(applicationContext,"Insert Failure", Toast.LENGTH_LONG).show()}
        }
        ///Cancel Button
        myBuilder.setNegativeButton("Cancel",) { dialog, which ->
            dialog.dismiss()    }
        myBuilder.show()
    }
}


