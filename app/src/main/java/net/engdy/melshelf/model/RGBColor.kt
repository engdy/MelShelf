package net.engdy.melshelf.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "rgb_colors", indices = [Index(value = ["name"], unique = true)])
data class RGBColor(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "red")
    val red: Int,
    @ColumnInfo(name = "green")
    val green: Int,
    @ColumnInfo(name = "blue")
    val blue: Int
)
