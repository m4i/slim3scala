package org.slim3scala.sbt

class EnvString(val source: String) {
  protected lazy val string = {
    /* >= 2.8.0
    """\$\{(\w+)\}""".r.replaceAllIn(source, {
        val env = System.getenv(_.group(1))
        if (env == null) "" else env
    })
    */
    // <  2.8.0
    var buffer = new StringBuilder
    val rest = source.drop(
      (0 /:  """\$\{(\w+)\}""".r.findAllIn(source).matchData)((i, m) => {
        var env = System.getenv(m.group(1))
        env = if (env == null) "" else env
        buffer.append(m.before.toString.drop(i) + env)
        m.end
      })
    )
    buffer.toString + rest
  }

  override def toString = string
}
