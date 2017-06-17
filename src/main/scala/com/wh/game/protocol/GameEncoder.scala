package com.wh.game.protocol

import com.wh.game.GameMsg
import org.apache.mina.core.buffer.IoBuffer
import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.{CumulativeProtocolDecoder, ProtocolDecoderOutput, ProtocolEncoderAdapter, ProtocolEncoderOutput}

/**
  *
  * @author beval(bevalbiz@126.com) 2017/6/17
  * @version
  */

object protocol {
    val headDataLength: Int = 4
    val headTypeLength: Int = 2
    val headLength: Int = 6
}

class GameEncoder extends ProtocolEncoderAdapter {

    def encode(session: IoSession, message: scala.Any, out: ProtocolEncoderOutput): Unit = {

        var msg=new GameMsg

        message match {
            case m:GameMsg => msg = m
            case _ => print(message)
        }

        val bytes = msg.content.getBytes("utf-8")
        val totalLength = bytes.length + protocol.headLength + 4

        val buffer = IoBuffer.allocate(totalLength).setAutoExpand(true)
        buffer.putInt(totalLength)
        buffer.putShort(msg.cmd)
        buffer.putInt(bytes.length)
        buffer.put(bytes)
        buffer.flip
        out.write(buffer)
        out.flush
    }
}


class GameDecoder extends CumulativeProtocolDecoder {
    def doDecode(session: IoSession, in: IoBuffer, out: ProtocolDecoderOutput): Boolean = {

        if (in.remaining > protocol.headDataLength) { //说明缓冲区中有数据
            in.mark//标记当前position，以便后继的reset操作能恢复position位置

            //获取数据包长度
            val len: Int = in.getInt
            //上面的get会改变remaining()的值
            if (in.remaining < len - protocol.headDataLength) { //内容不够， 重置position到操作前，进行下一轮接受新数据
                in.reset
                return false
            } else { //内容足够
                in.reset//重置回复position位置到操作前

                val packArray: Array[Byte] = new Array[Byte](len)
                in.get(packArray, 0, len)//获取整条报文

                val cmdArray: Array[Byte] = new Array[Byte](2)
                System.arraycopy(packArray, 4, cmdArray, 0, 2)
                val cmd: Short = byteArray2Short(cmdArray)

                val contentArray: Array[Byte] = new Array[Byte](len - 10)
                System.arraycopy(packArray, 10, contentArray, 0, len - 10)
                val content: String = new String(contentArray, "UTF-8")

                val msg = new GameMsg
                msg.cmd =cmd
                msg.content =content

                out.write(msg)
                if (in.remaining > 0) { //如果读取一个完整包内容后还粘了包，就让父类再调用一次，进行下一次解析
                    return true
                }
            }
        }
        false
    }

    def byteArrayToInt(b: Array[Byte]): Int = b(1) & 0xFF | (b(0) & 0xFF) << 8

    def byteArray2Short(b: Array[Byte]): Short = (((b(0) & 0xff) << 8) | (b(1) & 0xff)).toShort
}