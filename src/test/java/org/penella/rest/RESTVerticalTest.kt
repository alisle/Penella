package org.penella.rest

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.vertx.core.Handler
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.ext.unit.TestContext
import org.junit.runner.RunWith
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.penella.MailBoxes
import org.penella.codecs.CreateDBCodec
import org.penella.codecs.StatusMessageCodec
import org.penella.messages.CreateDB
import org.penella.messages.Status
import org.penella.messages.StatusMessage

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
        Json.mapper.registerModule(KotlinModule())
        vertx.eventBus().registerDefaultCodec(CreateDB::class.java, CreateDBCodec())
        vertx.eventBus().registerDefaultCodec(StatusMessage::class.java, StatusMessageCodec())
        vertx.deployVerticle(RESTVertical::class.java.name, context.asyncAssertSuccess())
    }

    @After
    fun tearDown(context: TestContext) {
        vertx.close(context.asyncAssertSuccess())
    }

    @Test
    fun testCreateDBFailed(context: TestContext) {
        val createDB = CreateDB("CreateDBTest", 10)
        val query = Json.encode(createDB)
        val async = context.async()

        vertx.eventBus().consumer<CreateDB>(MailBoxes.NODE_CREATE_DB.mailbox).handler { msg ->
            val msgCreateDB = msg.body()
            Assert.assertEquals(createDB, msgCreateDB)
            msg.reply(StatusMessage(Status.FAILED, "NO!"))
        }


        val request = vertx.createHttpClient().post(8080,"localhost","/_add", Handler { response ->
            if(response.statusCode() == 200) {
                context.fail()
            }

            async.complete()
        })

        request.putHeader("content-type", "application/json")
                .putHeader("content-length", query.length.toString())
                .write(query).end()

        async.await(2 * 10000)

    }

    @Test
    fun testCreateDBOK(context: TestContext) {
        val createDB = CreateDB("CreateDBTest", 10)
        val query = Json.encode(createDB)
        val async = context.async()

        vertx.eventBus().consumer<CreateDB>(MailBoxes.NODE_CREATE_DB.mailbox).handler { msg ->
            val msgCreateDB = msg.body()
            Assert.assertEquals(createDB, msgCreateDB)
            msg.reply(StatusMessage(Status.SUCESSFUL, "OK"))
        }


        val request = vertx.createHttpClient().post(8080,"localhost","/_add", Handler { response ->
            if(response.statusCode() != 200) {
                context.fail()
            }

            async.complete()
        })

        request.putHeader("content-type", "application/json")
                .putHeader("content-length", query.length.toString())
                .write(query).end()

        async.await(2 * 10000)

    }


}
