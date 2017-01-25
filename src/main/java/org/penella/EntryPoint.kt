package org.penella

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.core.*
import io.vertx.core.json.Json
import io.vertx.core.json.JsonObject
import io.vertx.core.logging.LoggerFactory
import org.penella.codecs.CreateDBCodec
import org.penella.codecs.ListDBCodec
import org.penella.codecs.ListDBResponseCodec
import org.penella.codecs.StatusMessageCodec
import org.penella.messages.CreateDB
import org.penella.messages.ListDB
import org.penella.messages.ListDBResponse
import org.penella.messages.StatusMessage
import org.penella.node.NodeVertical
import org.penella.rest.RESTVertical
import org.penella.store.StoreVertical


/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Created by alisle on 1/4/17.
 */
class EntryPoint {
    private val vertx : Vertx

    private val config : JsonObject by lazy {
        val url = javaClass.classLoader.getResource("config/default.json")
        val json = url.readText(Charsets.UTF_8)

        JsonObject(json)
    }

    init {
         vertx = Vertx.vertx(getVertxOptions())
    }

    private fun getVertxOptions() : VertxOptions {
        val options = VertxOptions()
        options.blockedThreadCheckInterval = 5000

        return options
    }


    fun start() {
        registerCodecs()
        vertx.deployVerticle(StoreVertical::class.java.name, DeploymentOptions().setConfig(config.getJsonObject("store")))
        vertx.deployVerticle(NodeVertical::class.java.name, DeploymentOptions().setConfig(config.getJsonObject("node")))
        startRest()
    }

    fun startRest() {
        val deploymentOptions = DeploymentOptions()
        val restHandler = Handler<AsyncResult<String>> {
            fun handle(result : AsyncResult<String>) {
                if(result.succeeded()) {
                    log.info("Succeeded in creating REST Vertical")
                } else {
                    log.error("Failed in creating the REST vVertical - " + result.cause().message)
                }
            }
        }

        vertx.deployVerticle("org.penella.rest.RESTVertical", deploymentOptions.setInstances(1), restHandler)

    }

    fun registerCodecs() {
        Json.mapper.registerModule(KotlinModule())
        vertx.eventBus().registerDefaultCodec(StatusMessage::class.java, StatusMessageCodec())
    }

    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(EntryPoint::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            println("Welcome to Penella 0.1 Alpha")
            val entry = EntryPoint()
            entry.start()
        }
    }
}