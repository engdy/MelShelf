package net.engdy.melshelf.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RGBColor::class], version = 1, exportSchema = false)
abstract class RGBColorDatabase : RoomDatabase() {
    abstract fun rgbColorDao(): RGBColorDao

    companion object {
        @Volatile
        private var INSTANCE: RGBColorDatabase? = null

        fun getDatabase(context: Context): RGBColorDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RGBColorDatabase::class.java,
                    "rgb_color_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}