package org.penella.store

import io.vertx.core.DeploymentOptions
import io.vertx.core.Vertx
import io.vertx.core.json.JsonObject
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.penella.codecs.StatusMessageCodec
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
 * Created by alisle on 1/24/17.
 */

@RunWith(VertxUnitRunner::class)
class VerticalStoreWrapperTest : StoreTest() {
    val vertx = Vertx.vertx()

    @Before
    fun setUp(context: TestContext) {
        val config = JsonObject()
        config.apply {
            put(StoreVertical.STORE_TYPE, "BTreeCompressedStore")
            put(StoreVertical.SEED, 665445839L)
            put(StoreVertical.MAX_STRING, 1024)
        }

        val deploymentOptions = DeploymentOptions().setConfig(config)
        vertx.deployVerticle(StoreVertical::class.java.name, deploymentOptions, context.asyncAssertSuccess())
        vertx.eventBus().registerDefaultCodec(StatusMessage::class.java, StatusMessageCodec())
    }

    @After
    fun tearDown(context: TestContext) {
        vertx.close(context.asyncAssertSuccess())
    }

    override fun createStore(): IStore {
        return VerticalStoreWrapper(vertx)
    }

    override fun storeType(): String {
        return "VerticalStoreWrapper"
    }
}
