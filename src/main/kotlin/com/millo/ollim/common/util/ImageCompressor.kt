package com.millo.ollim.common.util

import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.IIOImage
import javax.imageio.ImageIO
import javax.imageio.ImageWriteParam
import javax.imageio.stream.MemoryCacheImageOutputStream


/**
 * 이미지 압축
 *
 * 다이어리 생성 요청 시 이미지가 있는 경우 동작한다. 만약 이미지를 서버에 전달하기 전, 이미지 압축, 크기 조정하기 위함
 *
 * */
class ImageCompressor {

    fun compressImage(mpFile: MultipartFile, q:Int): ByteArray {

        // 품질, 이미지명, 이미지 확장자
        val quality= q * 0.01f // 0.0f: 최대 압축  1.0f: 최고 품질
        val name= mpFile.originalFilename
            ?: throw IllegalArgumentException("Image file name is null")
        val extension= name.substringAfterLast('.', "").lowercase()

        // ImageWriter - 이미지 설정 객체
        val writer= ImageIO.getImageWritersByFormatName(extension).asSequence().firstOrNull()
            ?: throw IllegalArgumentException("No suitable ImageWriter found for extension: ${extension}")

        // 출력 객체 (이미지 압축 결과 담을 곳)
        val baos= ByteArrayOutputStream()
        val imageOutputStream= MemoryCacheImageOutputStream(baos)

        // 결과 저장될 위치 설정
        writer.output = imageOutputStream

        try{
            // explicit: 압축 수준을 명시적으로 제어 가능해짐
            val param= writer.defaultWriteParam.apply {
                if(canWriteCompressed()){
                    compressionMode = ImageWriteParam.MODE_EXPLICIT
                    compressionQuality = quality
                }
            }

            // 이미지 압축 실행
            val image= IIOImage(resize(mpFile), null, null)
            writer.write(null, image, param)

        }catch (e:Exception){
            throw RuntimeException(e)

        }finally {
            imageOutputStream.close()
            baos.close()
            writer.dispose()
        }

        return baos.toByteArray()
    }

    // 이미지 크기 조정 로직 - 80%
    private fun resize(mpFile: MultipartFile):BufferedImage{

        val ratio = 0.8

        val image = ImageIO.read(mpFile.inputStream)
        val width = (image.width*ratio).toInt()
        val height = (image.height*ratio).toInt()

        val resizedImage= BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        resizedImage.createGraphics().apply {
            drawImage(image, 0, 0, null)
            dispose()
        }

        return resizedImage
    }

}
