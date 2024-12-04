package com.amaurysdm.codequest.controllers.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.amaurysdm.codequest.model.Level
import com.amaurysdm.codequest.model.ParentChild
import com.amaurysdm.codequest.model.User

@Database(
    entities = [User::class, Level::class, ParentChild::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun levelDao(): LevelDao
    abstract fun parentChildDao(): ParentChildDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context = context,
                        klass = AppDatabase::class.java,
                        name = "user_database"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}
