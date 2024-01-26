package com.example.vmlogin

object Constant {

    object Server {
        const val BASE_URL = "https://example.vividmindsoft.com/"
    }

    object App {
        const val GRANT_TYPE_PW = "password"
        const val CLIENT_ID = "69bfdce9-2c9f-4a12-aa7b-4fe15e1228dc"
    }

    object Timeout {
        const val CONNECT = 15L
        const val READ = 15L
        const val WRITE = 15L
    }
}