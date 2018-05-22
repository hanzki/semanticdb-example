package tool

import java.io.{BufferedWriter, File, FileWriter}

class DotPrinter {

  def print(graph: DependencyGraph): Unit = {
    println("Exporting dependency graph...")
    val file = new File("output.dot")
    val bw = new BufferedWriter(new FileWriter(file))

    bw.write("digraph g {\n")
    // Trait nodes
    bw.write("node [shape=diamond];\n")
    graph.nodes.filter(_.nodeType == "trait").foreach { n =>
      bw.write(s""""${n.name}";\n""")
    }
    // Object nodes
    bw.write("node [shape=box];\n")
    graph.nodes.filter(_.nodeType == "object").foreach { n =>
      bw.write(s""""${n.name}";\n""")
    }
    // Class nodes
    bw.write("node [shape=ellipse];\n")
    graph.nodes.filter(_.nodeType == "class").foreach { n =>
      bw.write(s""""${n.name}";\n""")
    }
    bw.write("}")
    bw.close()
  }

}
