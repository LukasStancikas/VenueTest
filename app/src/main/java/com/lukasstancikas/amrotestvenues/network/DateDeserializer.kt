package com.lukasstancikas.amrotestvenues.network

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date

class DateDeserializer(private val formatter: SimpleDateFormat) : JsonDeserializer<Date> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Date {
        val dateString = json.asJsonPrimitive.asString
        return formatter.parse(dateString)
    }
}