package dev.ultreon.interpreter.python3;

import com.khubla.antlr4example.*;
import dev.ultreon.interpreter.api.Interpreter;
import dev.ultreon.interpreter.api.InterpreterException;
import dev.ultreon.interpreter.api.ScriptEngine;
import dev.ultreon.interpreter.api.context.FileContext;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;
import java.nio.file.Path;

public class PythonScriptEngine implements ScriptEngine {
    public static void main(String[] args) throws IOException {
        PythonScriptEngine runner = new PythonScriptEngine();
        try {
            Interpreter result = runner.eval(Path.of("main.py"));
            FileContext context = result.getMainContext();
            context.getSymbol("Main").print();
        } catch (InterpreterException e) {
            ParserRuleContext ctx = e.ctx;
            if (ctx == null) {
                System.err.println("Error: " + e.getMessage());
            } else {
                System.err.println("Error: " + e.getMessage() + " (" + Path.of("main.py").toAbsolutePath() + ":" + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine() + ")");
            }
        }

    }

    @Override
    public Interpreter eval(String code) {
        PythonLexer lexer = new PythonLexer(CharStreams.fromString(code));
        PythonParser parser = new PythonParser(new CommonTokenStream(lexer));

        ParseTree tree = parser.file_input();
        Python3Interpreter interpreter = new Python3Interpreter(Path.of("main.py"), true);
        interpreter.visit(tree);
        return interpreter;
    }

    @Override
    public Interpreter eval(String code, String filename) {
        PythonLexer lexer = new PythonLexer(CharStreams.fromString(code));
        PythonParser parser = new PythonParser(new CommonTokenStream(lexer));

        Path path = Path.of(filename);
        ParseTree tree = parser.file_input();
        Python3Interpreter interpreter = new Python3Interpreter(path, true);
        interpreter.visit(tree);
        return interpreter;
    }

    @Override
    public Interpreter eval(Path path) throws IOException {
        PythonLexer lexer = new PythonLexer(CharStreams.fromPath(path));
        PythonParser parser = new PythonParser(new CommonTokenStream(lexer));

        ParseTree tree = parser.file_input();
        Python3Interpreter interpreter = new Python3Interpreter(path, true);
        interpreter.visit(tree);
        return interpreter;
    }
}