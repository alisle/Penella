package org.penella

import io.vertx.core.*
import io.vertx.core.logging.LoggerFactory
import org.penella.rest.RESTVertical


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

    init {
         vertx = Vertx.vertx(getVertxOptions())
    }

    private fun getVertxOptions() : VertxOptions {
        val options = VertxOptions()
        options.blockedThreadCheckInterval = 5000

        return options
    }

    fun start() {
        val deploymentOptions = DeploymentOptions()
        val restHandler = Handler<AsyncResult<String>> {
            fun handle(result : AsyncResult<String>) {
                if(result.succeeded()) {
                    log.info("Succeeded in creating REST Vertical")
                } else {
                    log.info("Failed in creating the REST vVertical - " + result.cause().message)
                }
            }
        }
        vertx.deployVerticle("org.penella.rest.RESTVertical", deploymentOptions.setInstances(1), restHandler)

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