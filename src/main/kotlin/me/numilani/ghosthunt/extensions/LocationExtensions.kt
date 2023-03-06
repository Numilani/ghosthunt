package me.numilani.ghosthunt.extensions

import org.bukkit.Location

class LocationExtensions {
    companion object{
        fun fromString(str: String) : Location{
            var deserialized: MutableMap<String, Any> = mutableMapOf()
            var kvps = str.removeSurrounding("{", "}").split(", ")
            for(kvp in kvps){
                deserialized.put(kvp.split("=")[0], kvp.split("=")[1])
            }
            return Location.deserialize(deserialized)
        }
    }
}