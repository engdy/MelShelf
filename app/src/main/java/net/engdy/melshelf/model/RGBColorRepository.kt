package net.engdy.melshelf.model

import kotlinx.coroutines.flow.Flow

class RGBColorRepository(private val rgbColorDao: RGBColorDao) {
    fun getAllColors(): Flow<List<RGBColor>> = rgbColorDao.getAllColors()
    fun getColorById(id: Int): Flow<RGBColor> = rgbColorDao.getColorById(id)
    suspend fun insert(color: RGBColor) = rgbColorDao.insert(color)
    suspend fun update(color: RGBColor) = rgbColorDao.update(color)
    suspend fun delete(color: RGBColor) = rgbColorDao.delete(color)
}