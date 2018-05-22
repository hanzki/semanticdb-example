package tool

import scala.meta.internal.semanticdb3.SymbolInformation.Kind
import scala.meta.internal.semanticdb3.TextDocument

class DependencyParser {

  def parse(documents: Seq[TextDocument]): DependencyGraph = {

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

case class DependencyGraph(nodes: Seq[Node], edges: Seq[Edge])

case class Node(name: String, nodeType: String)

case class Edge(from: String, to: String)
