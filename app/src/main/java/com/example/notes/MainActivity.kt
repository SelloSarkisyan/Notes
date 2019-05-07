package com.example.notes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.row.view.*

class MainActivity : AppCompatActivity() {
    var listNotes=ArrayList<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LoadQuery("%")
    }

    private fun LoadQuery(title: String) {
        var dbManager=DbManager(this)
        val projections= arrayOf("ID","Title","Description")
        val selectionArgs= arrayOf(title)
        val cursor=dbManager.Query(projections,"Title like ?",selectionArgs,"Title")
        if(cursor.moveToFirst()){
            do {
                val  ID=cursor.getInt(cursor.getColumnIndex("ID"))
                val Title=cursor.getString(cursor.getColumnIndex("Title"))
                val Description=cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Note(ID,Title,Description))
            }while (cursor.moveToNext())
        }
        var myNotesAdapter=MyNotesAdapter(this,listNotes)

    }
    inner class MyNotesAdapter:BaseAdapter {
        var  listNotesAdapter=ArrayList<Note>()
        var  context:Context?=null

        constructor( context: Context,listNotesAdapter:  ArrayList<Note>) : super() {
            this.listNotesAdapter=listNotesAdapter
            this.context = context
        }


        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var myView=layoutInflater.inflate(R.layout.row,null)
            var myNote=listNotesAdapter[position]
            myView.titleTv.text=myNote.nodeName
            myView.descTv.text=myNote.nodeDes
            myView.deleteBtn.setOnClickListener{
                var dbManager=DbManager(this.context!!)
                val  selectionArgs= arrayOf(myNote.nodeId.toString())
                dbManager.delete("ID?",selectionArgs)
                LoadQuery("%")
            }
            myView.editBtn.setOnClickListener{
                GoToUpdateFun(myNote)
            }
            //copy
            //share
            return myView
        }

        override fun getItem(position: Int): Any {
            return  listNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
          return  position.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }
    }

    private fun GoToUpdateFun(myNote: Note){
        var intent= Intent(this,AddNoteActivity::class.java)
        intent.putExtra("ID",myNote.nodeId)
        intent.putExtra("name",myNote.nodeName)
        intent.putExtra("des",myNote.nodeDes)
        startActivity(intent)

    }


}
