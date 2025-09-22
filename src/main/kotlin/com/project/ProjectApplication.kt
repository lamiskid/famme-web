package com.project

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class ProjectApplication

fun main(args: Array<String>) {
	runApplication<ProjectApplication>(*args)
}
