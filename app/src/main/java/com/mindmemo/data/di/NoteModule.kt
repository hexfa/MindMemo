package com.mindmemo.data.di

import android.content.Context
import androidx.room.Room
import com.example.noteappcleanarchitecture.data.db.NoteDatabase
import com.example.noteappcleanarchitecture.data.entity.NoteEntity
import com.example.noteappcleanarchitecture.data.repository.HomeRepositoryImpl
import com.example.noteappcleanarchitecture.data.repository.NoteRepositoryImpl
import com.mindmemo.data.utils.NOTE_DATABASE
import com.example.noteappcleanarchitecture.domain.repository.HomeRepository
import com.example.noteappcleanarchitecture.domain.repository.NoteRepository
import com.mindmemo.data.db.MemoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NoteModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context, MemoDatabase::class.java, NOTE_DATABASE
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(db: NoteDatabase) = db.noteDao()

    @Provides
    @Singleton
    fun provideEntity() = NoteEntity()

    @Provides
    @Singleton
    fun provideNoteRepository(noteDataBase: NoteDatabase): NoteRepository {
        return NoteRepositoryImpl(noteDataBase.noteDao())
    }

    @Provides
    @Singleton
    fun provideMainRepository(noteDataBase: NoteDatabase):HomeRepository{
        return HomeRepositoryImpl(noteDataBase.noteDao())
    }

}