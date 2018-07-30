package com.a5.joshe.locationassign

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_all_entries.*

// Author:       Joshua Esquilin
// Date:         4/25/2018
// Description:  AllEntries displays all the current saved diary entries
//

class AllEntries : AppCompatActivity() {

    var EntryList = ArrayList<Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_entries)

    }

    override fun onResume() {
        super.onResume()

        var DB = DbHandler(this)
        EntryList = DB.readData("%")

        // Populates the list with the saved entries with their appropriate data
        if(EntryList.size>0) {

            var anEntry = EntryListAdapter(this, EntryList)
            Entry_list.adapter = anEntry

            // Looks for when a user touches an entry on the list sends the information with an intention
            // to the New Entry activity to populate it
            Entry_list.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->

                var dDate = EntryList[position].dateOfCreation;
                var dSubject = EntryList[position].subject;
                var dLat = EntryList[position].diaryLat;
                var dLon = EntryList[position].diaryLon;
                var dLoc = EntryList[position].diarySource;
                var dEntry = EntryList[position].diaryEntry;
                var dId = EntryList[position].idOfDiaryEntry

                var intent = Intent(this, NewEntry::class.java)

                intent.putExtra("dId", dId)
                intent.putExtra("dDate", dDate)
                intent.putExtra("dSubject", dSubject)
                intent.putExtra("dLat", dLat)
                intent.putExtra("dLon", dLon)
                intent.putExtra("dSource", dLoc)
                intent.putExtra("dEntry", dEntry)
                intent.putExtra("action", "edit")

                startActivity(intent)

            }
        }else{
            Toast.makeText(this, "Could not find any Entries", Toast.LENGTH_SHORT).show()
        }

        button_goto_main_menu.setOnClickListener{
            var intent= Intent(this,MainMenu::class.java)
            startActivity(intent)
        }

        button_make_new_entry.setOnClickListener{
            var intent= Intent(this,NewEntry::class.java)
            startActivity(intent)
        }

    }
}