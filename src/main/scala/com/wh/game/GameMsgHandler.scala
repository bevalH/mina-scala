package com.wh.game

import org.apache.mina.core.service.IoHandlerAdapter
import org.apache.mina.core.session.IoSession

/**
  *
  * @author beval(bevalbiz@126.com) 2017/6/17
  * @version
  */
class GameMsgHandler extends IoHandlerAdapter {

    override def sessionOpened(session: IoSession): Unit = {
        println("some one connected, id=" + session.getId)
    }

    override def exceptionCaught(session: IoSession, cause: Throwable): Unit = {

    }


    override def sessionClosed(session: IoSession): Unit = {

    }

    override def messageSent(session: IoSession, message: scala.Any): Unit = {

    }

    override def messageReceived(session: IoSession, message: scala.Any): Unit = {
        message match {
            case msg: GameMsg => {
                println(msg.content)
                msg.cmd match {
                    case Command.login =>
                        println("sent [login successful] to client")
                        session.write(new GameMsg(Command.login, "login successful"))
                    case Command.heart =>
                        println("sent [heart] to client")
                        session.write(new GameMsg(Command.heart, System.currentTimeMillis + ""))
                }
            }
            case _ => println(message)
        }
    }
}
