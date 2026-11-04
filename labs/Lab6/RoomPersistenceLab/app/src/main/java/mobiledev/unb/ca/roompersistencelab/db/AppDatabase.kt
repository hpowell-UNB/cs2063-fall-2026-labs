package mobiledev.unb.ca.roompersistencelab.db

import android.content.Context
import androidx.room3.Database
import androidx.room3.Room
import androidx.room3.RoomDatabase
import mobiledev.unb.ca.roompersistencelab.db.dao.ItemDao
import mobiledev.unb.ca.roompersistencelab.db.entities.Item

private const val DB_NAME = "app_database.db"

@Database(entities = [Item::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val dbFile = context.getDatabasePath(DB_NAME)
                val instance = Room.databaseBuilder<AppDatabase>(
                    context = context.applicationContext,
                    name = dbFile.absolutePath
                )
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
