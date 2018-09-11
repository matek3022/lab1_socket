import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import javax.imageio.ImageIO

fun main(argv: Array<String>) {
    println("Welcome to Server side")

    lateinit var servers: ServerSocket
    lateinit var fromclient: Socket
    try {
        servers = ServerSocket(4444)
    } catch (e: IOException) {
        println("Couldn't listen to port 4444")
        System.exit(-1)
    }

    val fos = FileOutputStream("C:\\Users\\User\\Desktop\\inputImgNoise.jpg")

    try {
        print("Waiting for a client...")
        fromclient = servers.accept()
        println("Client connected")
    } catch (e: IOException) {
        println("Can't accept")
        System.exit(-1)
    }


    val inpBuff = fromclient.getInputStream()
    val out = fromclient.getOutputStream()
    var inputByteCount: Int
    var byteArray = ByteArray(64)

    println("Wait for image")
    byteArray = inpBuff.readAllBytes()
//    var readBytes = inputByteCount
//    while (inputByteCount != -1) {
//        println("readBytes = $readBytes")
//        fos.write(byteArray)
//        inputByteCount = inpBuff.read(byteArray)
//        readBytes += inputByteCount
//    }
    fos.write(byteArray)
    print("Image received")
    out.close()
    inpBuff.close()
    fromclient.close()
    servers.close()
    fos.close()

    val fosNormal = FileOutputStream("C:\\Users\\User\\Desktop\\inputImg.jpg")
    val buffImg = ImageIO.read(ByteArrayInputStream(byteArray))
    val newImg = buffImg
    for (i in 1 until buffImg.width - 1) {
        for (j in 1 until buffImg.height - 1) {
            val listPixels = arrayListOf(
                    buffImg.getRGB(i - 1, j - 1),
                    buffImg.getRGB(i - 1, j),
                    buffImg.getRGB(i + 1, j),
                    buffImg.getRGB(i + 1, j + 1),
                    buffImg.getRGB(i, j - 1),
                    buffImg.getRGB(i, j + 1),
                    buffImg.getRGB(i - 1, j + 1),
                    buffImg.getRGB(i + 1, j - 1))
            var avrg = listPixels.sorted()[4]
            newImg.setRGB(i, j, avrg)
        }
    }
    val noisesOUTSTREAM = ByteArrayOutputStream()
    ImageIO.write(newImg, "jpeg", noisesOUTSTREAM)
    fosNormal.write(noisesOUTSTREAM.toByteArray())
    fosNormal.close()
}