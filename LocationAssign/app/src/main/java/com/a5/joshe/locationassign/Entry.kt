package com.a5.joshe.locationassign

// Author:       Joshua Esquilin
// Date:         4/25/2018
// Description:  This is the Entry Data model for what a Diary entry should have

class Entry(dId: Int, dSubject: String, dEntry: String, dLat: String, dLon: String, dSource: String, dDate: String) {

    var idOfDiaryEntry: Int? = dId
    var subject: String? = dSubject
    var diaryEntry: String? = dEntry
    var diaryLat: String? = dLat
    var diaryLon: String? = dLon
    var diarySource: String? = dSource
    var dateOfCreation: String? = dDate

}