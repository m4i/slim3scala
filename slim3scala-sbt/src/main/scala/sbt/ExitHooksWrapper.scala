package sbt

object ExitHooksWrapper {
  def register(hook: ExitHook) { ExitHooks.register(hook) }
}
