package com.example.demo.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class TaskService {
    fun execute(task: String) {
        logger.info("do $task")
    }
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TaskService::class.java)
    }
