package tool

import scala.meta.internal.semanticdb3.SymbolInformation.Kind
import scala.meta.internal.semanticdb3.SymbolOccurrence.Role
import scala.meta.internal.semanticdb3.{SymbolOccurrence, TextDocument}

class DependencyParser {

  def parse(documents: Seq[TextDocument]): DependencyGraph = {

    documents.foreach { document =>
      pprint.log(document.uri)
      //pprint.log(document.occurrences)

      document.symbols
        .filter(s => Seq(Kind.CLASS, Kind.OBJECT, Kind.TRAIT).contains(s.kind))
        .foreach(s => println(s.toProtoString))

      document.occurrences
        .filter(_.role == Role.DEFINITION)
        .foreach { o =>
        println(o.toProtoString)
      }


    }

    val nodes = documents
      .flatMap(_.symbols)
      .collect {
        case s if s.kind == Kind.CLASS => Node(s.symbol, "class")
        case s if s.kind == Kind.OBJECT => Node(s.symbol, "object")
        case s if s.kind == Kind.TRAIT => Node(s.symbol, "trait")
      }

    DependencyGraph(nodes, Nil)
  }

}

object DependencyParser {

  private val classRegex = """.*#$""".r
  private val objectRegex = """[^#]*\.$""".r

  def isClass(symbol: String): Boolean = symbol match {
    case classRegex() => true
    case _ => false
  }

  def isObject(symbol: String): Boolean = symbol match {
    case objectRegex() => true
    case _ => false
  }
}

case class DependencyGraph(nodes: Seq[Node], edges: Seq[Edge])

case class Node(name: String, nodeType: String)

case class Edge(from: String, to: String)
