import sbt._
import sbt.Keys._
import xerial.sbt.Pack._

object Build extends sbt.Build {

  val SCALA_VERSION = "2.10.0"
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


  lazy val buildSettings = Defaults.defaultSettings ++ seq(
      organization := "org.utgenome",
      organizationName := "utgenome.org",
      organizationHomepage := Some(new URL("http://utgenome.org/")),
    description := "University of Tokyo Genome Browser",
    scalaVersion := SCALA_VERSION,
    javacOptions ++= Seq("-target" , "1.6", "-Xlint:unchecked", "-Xlint:deprecation", "-encoding", "UTF-8"),
    scalacOptions ++= Seq("-encoding", "UTF-8", "-unchecked", "-deprecation", "-feature", "-target:jvm-1.6"),
    publishMavenStyle := true,
    publishArtifact in Test := false,
    publishTo <<= version { v => releaseResolver(v) },
    pomIncludeRepository := {
      _ => false
    },              
  resolvers ++= Seq(
//      "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
      "UTGB Repository" at "http://maven.utgenome.org/repository/artifact"),
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


  lazy val root = Project(
    id = "utgb",
    base = file("."),
    settings = buildSettings ++ packSettings ++ seq(
      description := "UTGB Project",
      // Mapping from program name -> Main class
      packMain := Map("utgb" -> "org.utgenome.shell.UTGBShell"),
      packExclude := Seq("utgb"),
      publish := {},
      publishLocal := {}
    )
  ) aggregate(core, shell)

  val gwtVersion = "2.4.0"

  lazy val core = Project(
    id = "utgb-core",
    base = file("utgb-core"),
    settings = buildSettings ++ seq(
        libraryDependencies ++= Seq(
       	// Add dependent jars here	
       	//"org.xerial" % "xerial-core" % "3.1",
	"org.xerial" % "xerial-lens" % "2.0.6",
	"junit" % "junit" % "4.8.1" % "test",
       	"org.scalatest" %% "scalatest" % "2.0.M5b" % "test",
	"com.google.gwt" % "gwt-user" % gwtVersion % "provided",
	"com.google.gwt" % "gwt-dev" % gwtVersion % "provided",
	"com.google.gwt" % "gwt-servlet" % gwtVersion % "provided",
	"org.xerial.snappy" % "snappy-java" % "1.0.5-M3",
	"org.utgenome.thirdparty" % "gwt-incubator" % "20101117-r1766",
	"commons-fileupload" % "commons-fileupload" % "1.2",
	"org.apache.velocity" % "velocity" % "1.7",
	"org.codehaus.plexus" % "plexus-archiver" % "2.2",
	"org.codehaus.plexus" % "plexus-classworlds" % "2.4",
	"org.utgenome.thirdparty" % "sam" % "1.56",
	"org.utgenome.thirdparty" % "picard" % "1.56",
	"org.xerial" % "sqlite-jdbc" % "3.7.2",
	"org.xerial" % "xerial-storage" % "2.0",
	"com.google.gwt.gears" % "gwt-google-apis" % "1.0.0",
	"com.allen_sauer.gwt" % "gwt-dnd" % "3.1.2",
	"log4j" % "log4j" % "1.2.17",
	"jfree" % "jfreechart" % "1.0.12"
       )
    )
  ) 

  private val dependentScope = "test->test;compile->compile"

  val tomcatVersion = "7.0.21"

  lazy val shell = Project(
    id = "utgb-shell",
    base = file("utgb-shell"),
    settings = buildSettings ++ seq(
      libraryDependencies ++= Seq(
        "org.apache.tomcat.embed" % "tomcat-embed-core" % tomcatVersion,
	"org.apache.tomcat.embed" % "tomcat-embed-jasper" % tomcatVersion,
	"org.apache.tomcat.embed" % "tomcat-embed-logging-juli" % tomcatVersion,
	"org.apache.tomcat" % "tomcat-catalina" % tomcatVersion,
	"org.apache.tomcat" % "tomcat-jasper" % tomcatVersion excludeAll(
          ExclusionRule(organization="org.eclipse.jdt.core.compiler")
	),
	"org.apache.tomcat" % "tomcat-el-api" % tomcatVersion,
	"org.apache.tomcat" % "tomcat-juli" % tomcatVersion,
	"org.xerial" % "sqlite-jdbc" % "3.7.2",
	"org.apache.maven" % "maven-embedder" % "3.0.3",
	"org.sonatype.aether" % "aether-connector-wagon" % "1.11",
	"org.apache.maven.wagon" % "wagon-http" % "1.0-beta-7",
	"org.eclipse.jdt.core.compiler" % "ecj" % "3.5.1"
      )
    )
   ) dependsOn(core % dependentScope)


}
