package org.penella.rest

import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext

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
 * Created by alisle on 12/12/16.
 */
class RESTVertical : AbstractVerticle() {
    val router = createRouter()

    private fun createRouter() = Router.router(vertx).apply {
        get("/").handler(handlerCreate)
    }

    val handlerCreate = Handler<RoutingContext> { req ->
        req.response().end("CREATE DB!")
    }

    override fun start(startFuture: Future<Void>?) {
        vertx.createHttpServer()
                .requestHandler { request ->
                    request.response().end("CREATE DB!")
                    //router.accept(it)
                }.listen(7400) { result ->
                    if(result.succeeded()) startFuture?.complete() else startFuture?.fail(result.cause())
                }
    }
}