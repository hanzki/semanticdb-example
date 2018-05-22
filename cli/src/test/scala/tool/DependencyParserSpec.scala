package tool

import org.scalatest.{FlatSpec, Matchers}

class DependencyParserSpec extends FlatSpec with Matchers {

  "Tests" should "be run correctly" in {
    assertResult(2)(1 + 1)
  }

  "DependencyParser.isClass" should "recognize symbols for classes" in {
      assertResult(true)(DependencyParser.isClass("example.Address#"))
    }

  it should "reject all other symbols" in {
    assertResult(false)(DependencyParser.isClass("example.Address."))
  }

  "DependencyParser.isObject" should "recognize symbols for objects" in {
    assertResult(true)(DependencyParser.isObject("example.Address."))
  }

  it should "reject all other symbols" in {
    assertResult(false)(DependencyParser.isObject("example.Address#"))
    assertResult(false)(DependencyParser.isObject("example.Address#country()."))
  }

}
