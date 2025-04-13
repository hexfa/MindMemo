package com.mindmemo.data.di

import android.content.Context
import androidx.room.Room
import com.mindmemo.data.datastore.SettingDataSource
import com.mindmemo.data.db.MemoDatabase
import com.mindmemo.data.entity.MemoEntity
import com.mindmemo.data.repository.HomeRepositoryImpl
import com.mindmemo.data.repository.NoteRepositoryImpl
import com.mindmemo.data.repository.SettingRepositoryImp
import com.mindmemo.data.utils.MEMO_DATABASE
import com.mindmemo.domain.repository.HomeRepository
import com.mindmemo.domain.repository.NoteRepository
import com.mindmemo.domain.repository.SettingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object MemoModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, MemoDatabase::class.java, MEMO_DATABASE
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(db: MemoDatabase) = db.memoDao()

    @Provides
    @Singleton
    fun provideEntity() = MemoEntity()

    @Provides
    @Singleton
    fun provideNoteRepository(memoDataBase: MemoDatabase): NoteRepository {
        return NoteRepositoryImpl(memoDataBase.memoDao())
    }

    @Provides
    @Singleton
    fun provideMainRepository(memoDataBase: MemoDatabase): HomeRepository {
        return HomeRepositoryImpl(memoDataBase.memoDao())
    }

    @Provides
    @Singleton
    fun provideSettingRepository(settingDataSource: SettingDataSource): SettingRepository {
        return SettingRepositoryImp(settingDataSource)
    }

    @Provides
    @Singleton
    fun provideThemeDataSource(@ApplicationContext context: Context): SettingDataSource {
        return SettingDataSource(context)
    }
}