package br.com.newtonpaiva.pi5receiveCloud

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Pi5ReceiveCloudApplication

fun main(args: Array<String>) {
	runApplication<Pi5ReceiveCloudApplication>(*args)
}
