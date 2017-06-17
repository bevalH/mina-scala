package com.wh.game

/**
  *
  * @author beval(bevalbiz@126.com) 2017/6/17
  * @version
  */

class GameMsg {
    var cmd: Short = 0
    var content: String = ""

    def this(cmd: Short, content: String) {
        this()
        this.cmd = cmd
        this.content = content

    }
}
