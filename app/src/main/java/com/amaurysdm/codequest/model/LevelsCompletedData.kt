package com.amaurysdm.codequest.model

// This is getting pushed to the firebase
// Keeps track of the user and an array of levels completed.
data class LevelsCompletedData(var userId: String, var levelsCompleted: List<Level>)