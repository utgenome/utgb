
import java.net.InetAddress
import sbt._
import sbt.ExclusionRule
import sbt.Keys._

import xerial.sbt.Pack._
import net.thunderklaus.GwtPlugin._
import com.earldouglas.xsbtwebplugin.PluginKeys._
import com.earldouglas.xsbtwebplugin.Container

object Build extends sbt.Build {

  val SCALA_VERSION = "2.10.2"

  private def profile = System.getProperty("profile", "default")

  def releaseResolver(v: String): Option[Resolver] = {
    profile match {
      case "default" => {
        val nexus = "https://oss.sonatype.org/"
        if (v.trim.endsWith("SNAPSHOT"))
          Some("snapshots" at nexus + "content/repositories/snapshots")
        else
          Some("releases" at nexus + "service/local/staging/deploy/maven2")
      }
      case p => {
        scala.Console.err.println("unknown xerial.profile '%s'".format(p))
        None
      }
    }
  }

  lazy val defaultJavacOptions = Seq("-encoding", "UTF-8")

  lazy val buildSettings = Defaults.defaultSettings ++ net.virtualvoid.sbt.graph.Plugin.graphSettings ++
    Seq(
    organization := "org.utgenome",
    organizationName := "utgenome.org",
    organizationHomepage := Some(new URL("http://utgenome.org/")),
    description := "University of Tokyo Genome Browser",
    scalaVersion := SCALA_VERSION,
    javacOptions in Compile ++= defaultJavacOptions ++ Seq("-Xlint:unchecked", "-Xlint:deprecation", "-encoding", "UTF-8"),
    javacOptions in Compile in doc := defaultJavacOptions ++ Seq("-windowtitle", "utgb API", "-linkoffline", "http://docs.oracle.com/javase/6/docs/api/", "http://docs.oracle.com/javase/6/docs/api/"),
    scalacOptions ++= Seq("-encoding", "UTF-8", "-unchecked", "-deprecation", "-feature", "-target:jvm-1.6"),
    crossPaths := false,
//    publishMavenStyle := true,
    publishArtifact in Test := false,
    publishTo <<= version {
      v => releaseResolver(v)
    },
    pomIncludeRepository := {
      _ => false
    },
    //resolvers ++= Seq(
    //  "UTGB Repository" at "http://maven.utgenome.org/repository/artifact"),
    parallelExecution := true,
    parallelExecution in Test := false,
    pomExtra := {
      <url>http://utgenome.org/</url>
        <licenses>
          <license>
            <name>Apache 2</name>
            <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
          </license>
        </licenses>
        <scm>
          <connection>scm:git:github.com/xerial/utgb.git</connection>
          <developerConnection>scm:git:git@github.com:xerial/utgb.git</developerConnection>
          <url>github.com/xerial/utgb.git</url>
        </scm>
        <properties>
          <scala.version>
            {SCALA_VERSION}
          </scala.version>
          <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        </properties>
        <developers>
          <developer>
            <id>leo</id>
            <name>Taro L. Saito</name>
            <url>http://xerial.org/leo</url>
          </developer>
        </developers>
    }
  )


  lazy val container = Container("container")



  object Dependency {
    val JETTY_VERSION = "7.0.2.v20100331"
    val jetty = "org.mortbay.jetty" % "jetty-runner" % JETTY_VERSION excludeAll (
      // Exclude JSP modules if necessary
      ExclusionRule(organization="org.mortbay.jetty", name="jsp-2.1-glassfish"),
      ExclusionRule(organization="org.eclipse.jdtj"),
      ExclusionRule(organization = "org.slf4j")
      )


    val GWT_VERSION = "2.5.1"

    // We need to use an older version of jetty because newer version of jetty embeds ASM3 library,
    // which conflicts with ASM4 used in ClosureSerializer
    val jettyContainer = Seq("org.mortbay.jetty" % "jetty-runner" % JETTY_VERSION % "container" )


    val servletLib = Seq("javax.servlet" % "servlet-api" % "2.5" % "provided")

    val gwtLib = Seq(
      "com.google.gwt" % "gwt-user" % GWT_VERSION % "provided",
      "com.google.gwt" % "gwt-dev" % GWT_VERSION % "provided",
      "com.google.gwt" % "gwt-servlet" % GWT_VERSION,
      "com.google.gwt" % "gwt-incubator" % "2.0.1",
      //"org.utgenome.thirdparty" % "gwt-incubator" % "20101117-r1766",
      //"com.google.gwt.gears" % "gwt-google-apis" % "1.0.0",
      "com.allen-sauer.gwt.dnd" % "gwt-dnd" % "3.1.2"
    )

    val tomcatVersion = "7.0.21"
    val tomcatLib = Seq(
      "org.apache.tomcat.embed" % "tomcat-embed-core" % tomcatVersion,
      "org.apache.tomcat.embed" % "tomcat-embed-jasper" % tomcatVersion,
      "org.apache.tomcat.embed" % "tomcat-embed-logging-juli" % tomcatVersion,
      "org.apache.tomcat" % "tomcat-catalina" % tomcatVersion,
      "org.apache.tomcat" % "tomcat-jasper" % tomcatVersion excludeAll (
        ExclusionRule(organization = "org.eclipse.jdt.core.compiler")
        ),
      "org.apache.tomcat" % "tomcat-el-api" % tomcatVersion,
      "org.apache.tomcat" % "tomcat-juli" % tomcatVersion
    )

    val xerialVersion = "3.2.1"

    val xerialLib = Seq(
      "org.xerial" % "xerial-lens" % xerialVersion
    )
  }


  import Dependency._

  lazy val root = Project(
    id = "utgb",
    base = file("."),
    settings = buildSettings ++ packSettings ++ Seq(
      description := "UTGB Project",
      // Mapping from program name -> Main class
      packMain := Map("utgb" -> "org.utgenome.shell.UTGBShell"),
      packExclude := Seq("utgb"),
      publish := {},
      publishLocal := {},
      libraryDependencies ++= jettyContainer
    ) ++ container.deploy("/" -> web.project)
  ) aggregate(core, shell, web)

  private val cpuToUse : Int = {
    math.max((java.lang.Runtime.getRuntime.availableProcessors() * 0.9).toInt, 1)
  }

  lazy val core = Project(
    id = "utgb-core",
    base = file("utgb-core"),
    settings = buildSettings ++ Seq(
      description := "UTGB Core library",
      libraryDependencies ++= gwtLib ++ servletLib ++ xerialLib ++ Seq(
        // Add dependent jars here
        "org.xerial.java" % "xerial-lens" % "2.1",
        "org.xerial.java" % "xerial-storage" % "2.1",
        "junit" % "junit" % "4.8.1" % "test",
        "org.scalatest" % "scalatest_2.10" % "2.0.M5b" % "test",
        "org.xerial.snappy" % "snappy-java" % "1.1.0-M4",
        "org.apache.velocity" % "velocity" % "1.7",
        "org.codehaus.plexus" % "plexus-utils" % "2.0.6" force(),
        "org.utgenome.thirdparty" % "picard" % "1.86.0",
        "org.xerial" % "sqlite-jdbc" % "3.7.2",
        "log4j" % "log4j" % "1.2.17",
        "jfree" % "jfreechart" % "1.0.12",
        "commons-fileupload" % "commons-fileupload" % "1.2",
        "org.apache.velocity" % "velocity" % "1.7"
      )
    )
  )


  private val dependentScope = "test->test;compile->compile"

  lazy val shell = Project(
    id = "utgb-shell",
    base = file("utgb-shell"),
    settings = buildSettings ++ Seq(
      description := "UTGB command-line tools",
      libraryDependencies ++= tomcatLib ++ Seq(
        "org.codehaus.plexus" % "plexus-classworlds" % "2.4",
        "org.apache.maven" % "maven-embedder" % "3.0.4",
        "org.sonatype.aether" % "aether-connector-wagon" % "1.11",
        "org.apache.maven.wagon" % "wagon-http" % "1.0-beta-7",
        "org.eclipse.jdt.core.compiler" % "ecj" % "3.5.1"
      )
    )
  ) dependsOn (core % dependentScope)


  lazy val web = Project(
    id = "utgb-web",
    base = file("utgb-web"),
    settings = buildSettings ++ gwtSettings ++ Seq(
      description := "Pre-compiled UTGB war",
      gwtVersion := GWT_VERSION,
      gwtModules := List("org.utgenome.gwt.utgb.UTGBEntry"),
      gwtForceCompile := false,
      gwtBindAddress := {
        if(sys.props.contains("gwt.expose")) Some(InetAddress.getLocalHost.getHostAddress) else None
      },
      gwtTemporaryPath <<= (target) { (target) => target / "gwt" },
      webappResources in Compile ++= Seq(target.value / "gwt" / "utgb", baseDirectory.value / "src/main/webapp"),
      //webappResources in Compile <+= (resourceDirectory in Compile)(d => d / "xerial/silk/webui/webapp"),
      packageBin in Compile <<= (packageBin in Compile).dependsOn(gwtCompile),
      javaOptions in Gwt in Compile ++= Seq(
        "-localWorkers", cpuToUse.toString, "-strict", "-Xmx3g"
      ),
      javaOptions in Gwt ++= Seq(
        "-Xmx1g", "-Dloglevel=debug", "-Dgwt-hosted-mode=true"
      ),
      libraryDependencies ++= jettyContainer
    )
  ) dependsOn(core % dependentScope)





}
