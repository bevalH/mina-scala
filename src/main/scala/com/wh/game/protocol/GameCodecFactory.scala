package com.wh.game.protocol

import org.apache.mina.core.session.IoSession
import org.apache.mina.filter.codec.{ProtocolCodecFactory, ProtocolDecoder, ProtocolEncoder}

/**
  *
  * @author beval(bevalbiz@126.com) 2017/6/17
  * @version
  */
class GameCodecFactory extends ProtocolCodecFactory {

    def getDecoder(session: IoSession): ProtocolDecoder = {
        new GameDecoder
    }

    def getEncoder(session: IoSession): ProtocolEncoder = {
        new GameEncoder
    }
}
