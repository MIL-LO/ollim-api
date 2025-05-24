package com.millo.ollim.common.controller

import com.millo.ollim.common.util.ImageCompressor
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/images")
class ImageController {
    val imageCompressor = ImageCompressor()

    @PostMapping("")
    fun test(@RequestBody img: MultipartFile, @RequestParam quality:Int): ResponseEntity<ByteArray> {
        val res= imageCompressor.compressImage(img, quality)

        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(res)
    }
}
