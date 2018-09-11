
import java.io.FileInputStream
import java.net.Socket

fun main(argv: Array<String>) {
    println("Welcome to Client side")

    val serverName = "localHost"

    System.out.println("Connecting to... $serverName")

    val fromserver = Socket(serverName, 4443)
    val out = fromserver.getOutputStream()

    val fis = FileInputStream("C:\\Users\\User\\Desktop\\output.jpg")
    val buff = ByteArray(64)

    var byteReads = fis.read(buff)

    while (byteReads != -1) {
        out.write(buff)
        byteReads = fis.read(buff)
    }

    out.close()
    fis.close()
    fromserver.close()
}