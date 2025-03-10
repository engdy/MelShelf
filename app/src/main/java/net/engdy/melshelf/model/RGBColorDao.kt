package net.engdy.melshelf.model

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RGBColorDao {
    @Insert
    suspend fun insert(rgbColor: RGBColor)

    @Update
    suspend fun update(rgbColor: RGBColor)

    @Delete
    suspend fun delete(rgbColor: RGBColor)

    @Query("SELECT * FROM rgb_colors")
    fun getAllColors(): Flow<List<RGBColor>>

    @Query("SELECT * FROM rgb_colors WHERE id = :id")
    fun getColorById(id: Int): Flow<RGBColor>
}