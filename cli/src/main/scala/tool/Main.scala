package tool

import java.nio.file.Files
import java.nio.file.Paths
import scala.collection.JavaConverters._
import scala.meta.internal.{semanticdb3 => s}

object Main {
  def main(args: Array[String]): Unit = args.toList match {
    case path :: Nil =>
      val semanticdbRoot =
        Paths.get(path).resolve("META-INF").resolve("semanticdb")
      val semanticdbFiles = Files
        .walk(semanticdbRoot)
        .iterator()
        .asScala
        .filter(_.getFileName.toString.endsWith(".semanticdb"))
        .toList

      val documents = semanticdbFiles.flatMap { semanticdbFile =>
        s.TextDocuments
          .parseFrom(Files.readAllBytes(semanticdbFile))
          .documents
      }

      val graphParser = new DependencyParser()

      val dependencyGraph = graphParser.parse(documents)

      val graphPrinter = new DotPrinter()

      graphPrinter.print(dependencyGraph)
      /*
      semanticdbFiles.foreach { semanticdbFile =>
        for {
          document <- s.TextDocuments
            .parseFrom(Files.readAllBytes(semanticdbFile))
            .documents
        } {
          pprint.log(document.uri)
          pprint.log("occurences")
          document.occurrences.foreach { o =>
            println(o.toProtoString)
          }
          pprint.log("diagnostics")
          document.diagnostics.foreach { d =>
            //println(d.toProtoString)
          }
          pprint.log("symbols")
          document.symbols.foreach { d =>
            //println(d.toProtoString)
          }
        }
      }
      */
    case els =>
      sys.error(s"Expected <path>, obtained $els")
  }
}
