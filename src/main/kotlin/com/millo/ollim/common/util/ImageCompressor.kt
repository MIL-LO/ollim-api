package com.millo.ollim.common.util

import org.springframework.web.multipart.MultipartFile
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.ImageWriter
import javax.imageio.stream.ImageOutputStream
import javax.imageio.stream.MemoryCacheImageOutputStream


class ImageCompressor {

    fun compressImage(mpFile: MultipartFile, q:Int): ByteArray {

        // 품질
        val quality = q*0.01f
        // 이미지명, 확장자 확인
        val imageName = mpFile.originalFilename
        // jpg, jpeg ...
        val imageExtension = imageName!!.substring(imageName.lastIndexOf(".") + 1)

        // 요청 이미지의 확장자를 가지는 ImageWriter 호출
        val imageWriter:ImageWriter = ImageIO.getImageWritersByFormatName(imageExtension).next()
        val imageWriteParam = imageWriter.defaultWriteParam

        // explicit: 압축 수준을 명시적으로 제어 가능해짐
        imageWriteParam.compressionMode = ImageWriteParam.MODE_EXPLICIT
        // 압축 진행
        // 0.0f: 최대 압축     1.0: 최고 품질
        imageWriteParam.compressionQuality = quality

        // 출력 설정
        val baos= ByteArrayOutputStream()
        val imageOutputStream: ImageOutputStream = MemoryCacheImageOutputStream(baos)
        imageWriter.output = imageOutputStream

        // 이미지 크기 조정
        val origin = ImageIO.read(mpFile.inputStream).getScaledInstance(300,300, Image.SCALE_DEFAULT)
        val resizedImage = BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB)
        val graphics = resizedImage.createGraphics()
        graphics.drawImage(origin.getScaledInstance(300, 300, Image.SCALE_SMOOTH), 0, 0, null)
        graphics.dispose()

        val image = IIOImage(resizedImage, null, null)
        imageWriter.write(null, image, imageWriteParam)

        val file = File("C:\\Users\\daers\\Documents\\GitHub\\ollim-api\\src\\main\\kotlin\\com\\millo\\ollim\\common\\util\\images\\"+mpFile.originalFilename)
        mpFile.transferTo(file)

        imageOutputStream.close()
        baos.close()
        imageWriter.dispose()

        val file2 = File("C:\\Users\\daers\\Documents\\GitHub\\ollim-api\\src\\main\\kotlin\\com\\millo\\ollim\\common\\util\\images\\after\\"+mpFile.originalFilename)
        file2.writeBytes(baos.toByteArray())

        println("원본 파일 크기: ${file.length()} bytes")
        println("압축된 파일 크기: ${file2.length()} bytes")

        return baos.toByteArray()
    }
}
