package com.youhajun.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.youhajun.core.database.dao.ConversationCursorDao
import com.youhajun.core.database.dao.ConversationDao
import com.youhajun.core.database.dao.ConversationMetaDao
import com.youhajun.core.database.dao.ParticipantDao
import com.youhajun.core.database.entity.ConversationCursorEntity
import com.youhajun.core.database.entity.ConversationEntity
import com.youhajun.core.database.entity.ConversationMetaEntity
import com.youhajun.core.database.entity.ParticipantEntity

@Database(
    entities = [ConversationEntity::class, ConversationMetaEntity::class, ConversationCursorEntity::class, ParticipantEntity::class],
    version = 5
)
@TypeConverters
internal abstract class TransCallRoomDataBase : RoomDatabase() {
    abstract fun conversationMetaDao(): ConversationMetaDao
    abstract fun conversationCursorDao(): ConversationCursorDao
    abstract fun conversationDao(): ConversationDao
    abstract fun participantDao(): ParticipantDao
}
