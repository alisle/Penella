package org.penella.rest

import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.ext.unit.TestContext
import org.junit.runner.RunWith
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After
import org.junit.Before
import org.junit.Test

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

@RunWith(VertxUnitRunner::class)
class RESTVerticalTest {
    val vertx : Vertx = Vertx.vertx()

    @Before
    fun setUp(context: TestContext) {

        vertx.deployVerticle(RESTVertical::class.java.name, context.asyncAssertSuccess())
    }

    @After
    fun tearDown(context: TestContext) {
        vertx.close(context.asyncAssertSuccess())
    }

    @Test
    fun testCreateDB(context: TestContext) {
        val async = context.async()

        vertx.createHttpClient()
                .getNow(7400,
                        "localhost",
                        "/",
                        Handler { response ->
                            response.bodyHandler { body -> context.assertTrue(body.toString().contains("CREATE DB!")) }
                            async.complete()
                        })
    }

}
