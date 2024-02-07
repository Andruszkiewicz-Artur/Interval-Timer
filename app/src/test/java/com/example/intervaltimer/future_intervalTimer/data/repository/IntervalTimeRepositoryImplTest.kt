package com.example.intervaltimer.future_intervalTimer.data.repository

import com.example.intervaltimer.future_intervalTimer.data.data_source.IntervalTimeDao
import com.example.intervaltimer.future_intervalTimer.domain.model.IntervalTime
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class IntervalTimeRepositoryImplTest {

    private lateinit var repository: IntervalTimeRepositoryImpl
    private lateinit var mockDao: IntervalTimeDao

    private val timer1 = IntervalTime(
        1,
        "timer1",
        10,
        10,
        10,
        10,
        2000
    )

    private val timer2 = IntervalTime(
        2,
        "timer2",
        10,
        10,
        10,
        10,
        2000
    )

    @Before
    fun setUp() {
        mockDao = mockk()
        repository = IntervalTimeRepositoryImpl(mockDao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getAllIntervalTimes returns Flow of IntervalTimes`() = runBlocking {
        val intervalTimes = listOf(timer1, timer2)
        coEvery { mockDao.getAllIntervalTimes() } returns flowOf(intervalTimes)

        val result = repository.getAllIntervalTimes().first()

        assertEquals(intervalTimes, result)
    }

    @Test
    fun `getIntervalTime returns IntervalTime`() = runBlocking {
        val id = timer1.id ?: 1
        coEvery { mockDao.getIntervalTime(id) } returns timer1

        val result = repository.getIntervalTime(id)

        assertEquals(timer1, result)
    }

    @Test
    fun `insertIntervalTime calls Dao method`() = runBlocking {
        coEvery { mockDao.insertIntervalTime(any()) } just runs

        repository.insertIntervalTime(timer1)

        coVerify { mockDao.insertIntervalTime(timer1) }
    }
}