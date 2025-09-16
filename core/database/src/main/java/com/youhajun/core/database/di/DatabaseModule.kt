package com.youhajun.core.database.di

import android.content.Context
import androidx.room.Room
import com.youhajun.core.database.BuildConfig
import com.youhajun.core.database.TransCallRoomDataBase
import com.youhajun.core.database.dao.ConversationCursorDao
import com.youhajun.core.database.dao.ConversationDao
import com.youhajun.core.database.dao.ConversationMetaDao
import com.youhajun.core.database.dao.ParticipantDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    private const val DB_NAME = "database_transcall"

    @Provides
    @Singleton
    fun provideRoomDataBase(
        @ApplicationContext context: Context
    ): TransCallRoomDataBase {
        val builder = Room.databaseBuilder(context, TransCallRoomDataBase::class.java, DB_NAME)
        if(BuildConfig.DEBUG) {
            builder.setQueryCallback(Dispatchers.IO) { sqlQuery, bindArgs ->
                Timber.d("SQL Query: $sqlQuery | Args: $bindArgs")
            }
        }
        return builder.build()
    }

    @Provides
    fun provideConversationDao(db: TransCallRoomDataBase): ConversationDao = db.conversationDao()

    @Provides
    fun provideConversationMetaDao(db: TransCallRoomDataBase): ConversationMetaDao = db.conversationMetaDao()

    @Provides
    fun provideConversationCursorDao(db: TransCallRoomDataBase): ConversationCursorDao = db.conversationCursorDao()

    @Provides
    fun provideParticipantDao(db: TransCallRoomDataBase): ParticipantDao = db.participantDao()
}
