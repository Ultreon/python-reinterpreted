from java.lang import System
from javax.swing import JFrame
from java.awt import Dimension


class Main:
    def __init__(self):
        self.mr_name = "John Doe"
        self.mr_age = 42
        self.mr_address = "123 Main Street"
        self.mr_phone = "555-1234"

    def main(self, args):
        hello: str = "Hello, World " + 50 + "!"
        System.out.println(hello)x
        return 0

    def add(self, a, b):
        return a + b


def main(args):
    hello: str = "Hello, World!"
    System.out.println(hello)
    return 0


if __name__ == "__main__":
    main([])
    f: JFrame = JFrame()
    f.visible = True
    f.size = Dimension(800, 600)

    Main().main([])
    System.out.println(Main().add(10, 15))

    a: Main = Main()
    a.mr_name = "Smith"
    System.out.println(a.mr_name)
    System.out.println(Main().mr_name)
