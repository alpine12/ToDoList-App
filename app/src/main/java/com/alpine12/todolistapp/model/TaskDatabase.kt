package com.alpine12.todolistapp.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.alpine12.todolistapp.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
       @ApplicationScope private val applicationScope: CoroutineScope
    ) :
        RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            //db operations
            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task("Wash the dishes"))
                dao.insert(Task("Wash the pring", true))
                dao.insert(Task("makan the dishes"))
                dao.insert(Task("kucin g the dishes", true))
                dao.insert(Task("ahahha the dishes"))
                dao.insert(Task("Wash the dishes", completed = true))
                dao.insert(Task("Wash the dishes"))

            }

        }
    }

}