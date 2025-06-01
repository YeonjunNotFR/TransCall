package com.youhajun.core.database.di

import android.content.Context
import androidx.room.Room
import com.youhajun.core.database.RoomDataBase
import com.youhajun.core.database.dao.ConversationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    private const val DB_NAME = "database_transcall"

    @Provides
    @Singleton
    fun provideRoomDataBase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        RoomDataBase::class.java,
        DB_NAME
    ).build()

    @Provides
    fun provideConversationDao(db: RoomDataBase): ConversationDao = db.conversationDao()
}
