package basic.asyncevents.common.event

abstract class Event (
    var timestamp: Long = System.currentTimeMillis()
) {
}