package org.penella.messages

/**
 * Created by alisle on 12/12/16.
 */
enum class Status {
    SUCESSFUL,
    FAILED,
    IN_PROGRESS
}

data class StatusMessage(val status: Status, val reason: String)