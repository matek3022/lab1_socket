import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.ServerSocket
import java.net.Socket
import java.util.*
import javax.imageio.ImageIO

fun main(argv: Array<String>) {
    println("Welcome to noise side")

    lateinit var noiseServer: ServerSocket
    lateinit var fromclient: Socket
    lateinit var toMainServer: Socket

    toMainServer = Socket("localHost", 4444)
    val outNoise = toMainServer.getOutputStream()


    try {
        noiseServer = ServerSocket(4443)
    } catch (e: IOException) {
        println("Couldn't listen to port 4444")
        System.exit(-1)
    }

    try {
        print("Waiting for a client...")
        fromclient = noiseServer.accept()
        println("Client connected")
    } catch (e: IOException) {
        println("Can't accept")
        System.exit(-1)
    }

    val inpBuff = fromclient.getInputStream()
    val out = fromclient.getOutputStream()
    var inputByteCount: Int
    val byteArray = ByteArray(64)

    println("Wait for image")
    val buffImg = ImageIO.read(inpBuff)
    for (i in 1 until buffImg.raster.width) {
        for (j in 1 until buffImg.raster.height) {
            val rnd = Random().nextInt(100)
            if ( (i+j) % (if (rnd == 0) 1 else rnd) == 0) {
                buffImg.setRGB(i,j, - Random().nextInt(Math.abs(buffImg.getRGB(i,j))))
            }
        }
    }
    val noisesOUTSTREAM = ByteArrayOutputStream()
    ImageIO.write(buffImg, "jpeg", noisesOUTSTREAM)
    outNoise.write(noisesOUTSTREAM.toByteArray())
    outNoise.close()
    print("Image received and sent to main server")
    out.close()
    inpBuff.close()
    fromclient.close()
}