package com.wh.game

import java.net.InetSocketAddress

import com.wh.game.protocol.GameCodecFactory
import org.apache.mina.core.session.IdleStatus
import org.apache.mina.filter.codec.ProtocolCodecFilter
import org.apache.mina.transport.socket.SocketAcceptor
import org.apache.mina.transport.socket.nio.NioSocketAcceptor

/**
  *
  * @author beval(bevalbiz@126.com) 2017/6/17
  * @version
  */
object Bootstrap extends App {
    val port = 9998
    val acceptor: SocketAcceptor = new NioSocketAcceptor();

    acceptor.setReuseAddress(true)

    acceptor.getFilterChain.addLast("codec", new ProtocolCodecFilter(new GameCodecFactory))
    acceptor.setHandler(new GameMsgHandler)

    acceptor.getSessionConfig.setReadBufferSize(2048)
    acceptor.getSessionConfig.setIdleTime(IdleStatus.BOTH_IDLE, 10)

    acceptor.bind(new InetSocketAddress(port))


    println("game server is start ok.....!")
}
