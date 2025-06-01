package com.youhajun.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.youhajun.core.database.dao.ConversationDao
import com.youhajun.core.database.entity.ConversationEntity

@Database(entities = [ConversationEntity::class], version = 1)
@TypeConverters
internal abstract class RoomDataBase : RoomDatabase() {
    abstract fun conversationDao(): ConversationDao
}
