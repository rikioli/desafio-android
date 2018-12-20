package br.com.henriqueoliveira.desafioandroidconcrete.utils

import android.content.Context
import br.com.henriqueoliveira.desafioandroidconcrete.utils.Utils.readFileFromAssets
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer

class MockServer {

    private var mockWebServer = MockWebServer()
    var context: Context? = null

    fun startTestCase() {
        mockWebServer = MockWebServer()
        mockWebServer.start(8080)
    }

    fun endTestCase() {
        mockWebServer.shutdown()
    }

    fun enqueue(responseCode: Int, responseBodyFileName: String? = null) {
        val mockResponse = MockResponse().setResponseCode(responseCode)

        responseBodyFileName?.let {
            if (it == "empty")
                mockResponse.setBody("[]")
            else
                mockResponse.setBody(readFileFromAssets(context, it))
        }

        mockWebServer.enqueue(mockResponse)
    }
}