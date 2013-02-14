addSbtPlugin("org.xerial.sbt" % "sbt-pack" % "0.1.5")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.2.0")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.6")


resolvers += "GWT plugin repo" at "http://thunderklaus.github.com/maven"

//addSbtPlugin("net.thunderklaus" % "sbt-gwt-plugin" % "1.1-SNAPSHOT")


libraryDependencies += "com.github.siasia" % "xsbt-web-plugin_2.9.2" % "0.12.0-0.2.11.1"


addSbtPlugin("net.virtual-void" % "sbt-dependency-graph" % "0.7.0")