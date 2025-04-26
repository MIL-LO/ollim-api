package com.millo.ollim.diary.request

data class TagRequest(
    val name: String,
    val description: String,
    val color: String,
    val category: String,
    val group: String,

) {

}
