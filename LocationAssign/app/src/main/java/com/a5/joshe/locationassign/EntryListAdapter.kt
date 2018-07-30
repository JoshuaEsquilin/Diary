package com.a5.joshe.locationassign

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.list_entry_block.view.*

// Author:       Joshua Esquilin
// Date:         4/25/2018
// Description:  This is the Entry List Adapter that handles displaying and listing the saved entries

class EntryListAdapter(con:Context, entryArrayList:ArrayList<Entry>): BaseAdapter() {

    var arrayList = ArrayList<Entry>()
    var context: Context? = null
    var myInflater: LayoutInflater? = null

    init {
        this.context    = con
        this.myInflater = LayoutInflater.from(context)
        this.arrayList  = entryArrayList
    }

    // Inflates the view and shows the description for each saved entry
    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, savedView: View?, parent: ViewGroup?): View {

        var myView = myInflater!!.inflate(R.layout.list_entry_block,null)
        var dataOnEntry = arrayList[position]

        var description : String = "Date:  " + dataOnEntry.dateOfCreation.toString() +"  \n" + "Subject:   " + dataOnEntry.subject.toString()
        myView.entry_info.text = description
        return myView
    }

    override fun getItem(position: Int): Any {
        return arrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return arrayList.size
    }
}