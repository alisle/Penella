package org.penella

import net.openhft.hashing.LongHashFunction
import java.io.Serializable

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
 *
 * Created by alisle on 9/30/16.
 */
class Triple(val subject: String, val property: String, val obj: String) : Serializable {
    val seed : Long = 665445839
    val hashSubject = LongHashFunction.xx_r39(seed).hashChars(subject)
    val hashProperty = LongHashFunction.xx_r39(seed).hashChars(property)
    val hashObj = LongHashFunction.xx_r39(seed).hashChars(obj)
    val hash = LongHashFunction.xx_r39(seed).hashChars(this.toString())


    override fun toString(): String {
        return "'$subject'-'$property'-'$obj'."
    }

    override fun equals(other: Any?): Boolean {
        if (other is Triple && other.hash == this.hash) {
            if (other.hashSubject == this.hashSubject &&
                    other.hashProperty == this.hashProperty &&
                    other.hashObj == this.hashObj) {
                return true
            }
        }

        return false
    }


}