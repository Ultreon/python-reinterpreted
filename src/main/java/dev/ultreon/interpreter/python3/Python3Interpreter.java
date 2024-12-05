package dev.ultreon.interpreter.python3;

import com.khubla.antlr4example.PythonParser;
import com.khubla.antlr4example.PythonParserBaseVisitor;
import dev.ultreon.interpreter.api.*;
import dev.ultreon.interpreter.api.context.*;
import dev.ultreon.interpreter.api.obj.ClassType;
import dev.ultreon.interpreter.api.obj.FuncType;
import dev.ultreon.interpreter.api.symbols.Type;
import dev.ultreon.interpreter.api.symbols.Value;
import dev.ultreon.interpreter.python3.lib.builtins.PythonClass;
import dev.ultreon.interpreter.python3.lib.PythonStdLib;
import dev.ultreon.interpreter.python3.lib.builtins.PythonTuple;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.RuleNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.util.*;

public class Python3Interpreter extends PythonParserBaseVisitor<Object> implements Interpreter {
    private static SymbolTable rootSymbolTable;
    private final FileContext mainContext;
    private final Stack<Context> contextStack = new Stack<>();
    private Context currentContext;
    private TerminalNode lastNameId;
    private final boolean isMainFile;
    private final Type nullType;
    private final Type anyType;
    private final PythonStdLib stdLib;
    private final Value nullValue = PythonValue.None;
    private boolean isAssert;

    public Python3Interpreter(Path mainContext, boolean isMainFile) {
        getOrCreateRootSymbolTable();

        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        if (runtimeMXBean.getInputArguments().contains("-ea")) {
            isAssert = true;
        }

        stdLib = new PythonStdLib(this);
        stdLib.init();

        this.mainContext = new FileContext(mainContext, this);
        this.isMainFile = isMainFile;

        nullType = new Type(null, null, this.mainContext);
        anyType = new Type(null, null, this.mainContext);
    }

    @Override
    public Object visitFile_input(PythonParser.File_inputContext ctx) {
        SymbolTable symbolTable = mainContext.getSymbolTable();
        if (symbolTable == null) {
            throw new InterpreterError("Expected symbol table");
        }

        setupSymbolTable(symbolTable, isMainFile);

        PythonParser.StatementsContext statements = ctx.statements();
        contextStack.push(mainContext);
        currentContext = mainContext;
        visit(statements);
        TerminalNode eof = ctx.EOF();
        if (eof == null) {
            throw new InterpreterError("Expected end of file");
        }

        currentContext = contextStack.pop();
        if (currentContext != mainContext) {
            throw new InterpreterError("Expected end of file");
        }
        currentContext = null;
        return null;
    }

    private void setupSymbolTable(SymbolTable symbolTable, boolean main) {
        SymbolTable rootSymbolTable = getOrCreateRootSymbolTable();
        symbolTable.putAll(rootSymbolTable);
        if (main) {
            symbolTable.put("__name__", PythonValue.of(mainContext, "__main__"));
        }
    }

    protected SymbolTable getOrCreateRootSymbolTable() {
        rootSymbolTable = new SymbolTable();
        rootSymbolTable.put("None", nullValue);
        rootSymbolTable.put("True", Value.TRUE);
        rootSymbolTable.put("False", Value.FALSE);

        if (stdLib == null || stdLib.getBuiltins() == null) {
            return rootSymbolTable;
        }
        rootSymbolTable.put("float", PythonValue.of(mainContext, stdLib.getBuiltins().getFloatClass()));
        rootSymbolTable.put("int", PythonValue.of(mainContext, stdLib.getBuiltins().getIntClass()));
        rootSymbolTable.put("str", PythonValue.of(mainContext, stdLib.getBuiltins().getStringClass()));
        return rootSymbolTable;
    }

    @Override
    public Object visitInteractive(PythonParser.InteractiveContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitEval(PythonParser.EvalContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitFunc_type(PythonParser.Func_typeContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitFstring_input(PythonParser.Fstring_inputContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitStatements(PythonParser.StatementsContext ctx) {
        List<PythonParser.StatementContext> statement = ctx.statement();
        Object visit = null;
        for (PythonParser.StatementContext s : statement) {
            visit = visit(s);
        }

        return visit;
    }

    @Override
    public Object visitStatement(PythonParser.StatementContext ctx) {
        PythonParser.Simple_stmtsContext simpleStmtsContext = ctx.simple_stmts();
        if (simpleStmtsContext != null) {
            return visit(simpleStmtsContext);
        }

        PythonParser.Compound_stmtContext compoundStmtContext = ctx.compound_stmt();
        if (compoundStmtContext != null) {
            return visit(compoundStmtContext);
        }

        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitStatement_newline(PythonParser.Statement_newlineContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSimple_stmts(PythonParser.Simple_stmtsContext ctx) {
        for (PythonParser.Simple_stmtContext simpleStmtContext : ctx.simple_stmt()) {
            visit(simpleStmtContext);
        }

        return null;
    }

    @Override
    public Object visitSimple_stmt(PythonParser.Simple_stmtContext ctx) {
        PythonParser.Import_stmtContext importStmtContext = ctx.import_stmt();
        if (importStmtContext != null) {
            return visit(importStmtContext);
        }

        PythonParser.Assert_stmtContext assertStmtContext = ctx.assert_stmt();
        if (assertStmtContext != null) {
            return visit(assertStmtContext);
        }

        PythonParser.Del_stmtContext delStmtContext = ctx.del_stmt();
        if (delStmtContext != null) {
            return visit(delStmtContext);
        }

        PythonParser.Global_stmtContext globalStmtContext = ctx.global_stmt();
        if (globalStmtContext != null) {
            return visit(globalStmtContext);
        }

        PythonParser.Nonlocal_stmtContext nonlocalStmtContext = ctx.nonlocal_stmt();
        if (nonlocalStmtContext != null) {
            return visit(nonlocalStmtContext);
        }

        PythonParser.Raise_stmtContext raiseStmtContext = ctx.raise_stmt();
        if (raiseStmtContext != null) {
            return visit(raiseStmtContext);
        }

        PythonParser.Return_stmtContext returnStmtContext = ctx.return_stmt();
        if (returnStmtContext != null) {
            return visit(returnStmtContext);
        }

        PythonParser.Yield_stmtContext yieldStmtContext = ctx.yield_stmt();
        if (yieldStmtContext != null) {
            return visit(yieldStmtContext);
        }

        PythonParser.Star_expressionsContext starExpressionsContext = ctx.star_expressions();
        if (starExpressionsContext != null) {
            return visit(starExpressionsContext);
        }

        PythonParser.AssignmentContext assignment = ctx.assignment();
        if (assignment != null) {
            return visit(assignment);
        }

        TerminalNode pass = ctx.PASS();
        if (pass != null) {
            return null;
        }

        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitCompound_stmt(PythonParser.Compound_stmtContext ctx) {
        PythonParser.Function_defContext functionDefContext = ctx.function_def();
        if (functionDefContext != null) {
            return visit(functionDefContext);
        }

        PythonParser.Class_defContext classDefContext = ctx.class_def();
        if (classDefContext != null) {
            return visit(classDefContext);
        }

        PythonParser.If_stmtContext ifStmtContext = ctx.if_stmt();
        if (ifStmtContext != null) {
            return visit(ifStmtContext);
        }

        PythonParser.For_stmtContext forStmtContext = ctx.for_stmt();
        if (forStmtContext != null) {
            return visit(forStmtContext);
        }

        PythonParser.Match_stmtContext matchStmtContext = ctx.match_stmt();
        if (matchStmtContext != null) {
            return visit(matchStmtContext);
        }

        PythonParser.Try_stmtContext tryStmtContext = ctx.try_stmt();
        if (tryStmtContext != null) {
            return visit(tryStmtContext);
        }

        PythonParser.While_stmtContext whileStmtContext = ctx.while_stmt();
        if (whileStmtContext != null) {
            return visit(whileStmtContext);
        }

        PythonParser.With_stmtContext withStmtContext = ctx.with_stmt();
        if (withStmtContext != null) {
            return visit(withStmtContext);
        }

        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitAssignment(PythonParser.AssignmentContext ctx) {
        PythonParser.Annotated_rhsContext annotatedRhsContext = ctx.annotated_rhs();
        List<Value> visits = Collections.singletonList(nullValue);
        PythonParser.ExpressionContext expression = ctx.expression();
        Value visit1 = expression == null ? null : (Value) visit(expression);
        Type value = visit1 == null ? anyType : visit1.asType();
        if (annotatedRhsContext != null) {
            visits = (List<Value>) visit(annotatedRhsContext);
        }

        PythonParser.Star_expressionsContext starExpressionsContext = ctx.star_expressions();
        if (starExpressionsContext != null) {
            visits = (List<Value>) visit(starExpressionsContext);
        }

        TerminalNode name = ctx.NAME();
        if (name == null) {
            List<ValueTarget> valueTargets = new ArrayList<>();

            for (PythonParser.Star_targetsContext targetContext : ctx.star_targets()) {
                valueTargets.addAll((List<ValueTarget>) visit(targetContext));
            }

            for (int i = 0, valueTargetsSize = valueTargets.size(); i < valueTargetsSize; i++) {
                ValueTarget valueTarget = valueTargets.get(i);
                valueTarget.setter().accept(visits.get(i));
            }

            // TODO: return value
            return null;
        }
        if (ctx.EQUAL() == null) throw new UnsupportedOperationException("Not implemented yet!");

        currentContext.getSymbolTable().put(name.getText(), visits.getFirst());
        return value;
    }

    @Override
    public Object visitAnnotated_rhs(PythonParser.Annotated_rhsContext ctx) {
        PythonParser.Star_expressionsContext starExpressionsContext = ctx.star_expressions();
        if (starExpressionsContext != null) {
            return visit(starExpressionsContext);
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitAugassign(PythonParser.AugassignContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitReturn_stmt(PythonParser.Return_stmtContext ctx) {
        if (currentContext instanceof FunctionContentsContext) {
            FunctionContentsContext functionContentsContext = (FunctionContentsContext) currentContext;
            PythonParser.Star_expressionsContext expressionContext = ctx.star_expressions();
            if (expressionContext != null) {
                List<Value> visit = (List<Value>) visit(expressionContext);
                functionContentsContext.returnedValue = visit.size() == 1 ? visit.getFirst() : new PythonTuple(stdLib.getBuiltins().getTupleClass(), visit).createPythonValue();
                return functionContentsContext.returnedValue;
            } else {
                functionContentsContext.returnedValue = nullValue;
                return null;
            }
        } else {
            throw new UnsupportedOperationException("Not implemented yet!");
        }
    }

    @Override
    public Object visitRaise_stmt(PythonParser.Raise_stmtContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitGlobal_stmt(PythonParser.Global_stmtContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitNonlocal_stmt(PythonParser.Nonlocal_stmtContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitDel_stmt(PythonParser.Del_stmtContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitYield_stmt(PythonParser.Yield_stmtContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitAssert_stmt(PythonParser.Assert_stmtContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitImport_stmt(PythonParser.Import_stmtContext ctx) {
        PythonParser.Import_fromContext importFromContext = ctx.import_from();
        if (importFromContext != null) {
            return visit(importFromContext);
        }

        PythonParser.Import_nameContext importNameContext = ctx.import_name();
        if (importNameContext != null) {
            return visit(importNameContext);
        }

        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitImport_name(PythonParser.Import_nameContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitImport_from(PythonParser.Import_fromContext ctx) {
        PythonParser.Dotted_nameContext dottedNameContext = ctx.dotted_name();
        if (dottedNameContext == null) throw new UnsupportedOperationException("Not implemented yet!");
        String name = (String) visit(dottedNameContext);

        PythonParser.Import_from_targetsContext importFromTargetsContext = ctx.import_from_targets();
        if (importFromTargetsContext != null) {
            String[] targets = (String[]) visit(importFromTargetsContext);

            for (String target : targets) {
                // TODO Load python modules

                // Load Java classes here
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(name + "." + target);
                } catch (ClassNotFoundException e) {
                    throw new InterpreterError("Class not found: " + target);
                }
                currentContext.getSymbolTable().put(target, PythonValue.of(currentContext, new HostClass(currentContext, clazz)));
            }
        }

        return null;
    }

    @Override
    public Object visitImport_from_targets(PythonParser.Import_from_targetsContext ctx) {
        PythonParser.Import_from_as_namesContext importFromAsNamesContext = ctx.import_from_as_names();
        if (importFromAsNamesContext != null) {
            return visit(importFromAsNamesContext);
        }

        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitImport_from_as_names(PythonParser.Import_from_as_namesContext ctx) {
        List<PythonParser.Import_from_as_nameContext> importFromAsNameContexts = ctx.import_from_as_name();
        String[] targets = new String[importFromAsNameContexts.size()];
        for (int i = 0; i < importFromAsNameContexts.size(); i++) {
            PythonParser.Import_from_as_nameContext importFromAsNameContext = importFromAsNameContexts.get(i);
            targets[i] = (String) visit(importFromAsNameContext);
        }
        return targets;
    }

    @Override
    public Object visitImport_from_as_name(PythonParser.Import_from_as_nameContext ctx) {
        TerminalNode as = ctx.AS();
        if (as != null) {
            throw new UnsupportedOperationException("Not implemented yet!");
        }

        List<TerminalNode> name = ctx.NAME();
        List<String> names = name.stream().map(TerminalNode::getText).toList();
        if (names.size() != 1) {
            throw new UnsupportedOperationException("Not implemented yet!");
        }
        return names.getFirst();
    }

    @Override
    public Object visitDotted_as_names(PythonParser.Dotted_as_namesContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitDotted_as_name(PythonParser.Dotted_as_nameContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitDotted_name(PythonParser.Dotted_nameContext ctx) {
        return ctx.getText();
    }

    @Override
    public Object visitBlock(PythonParser.BlockContext ctx) {
        PythonParser.Simple_stmtsContext simpleStmtsContext = ctx.simple_stmts();
        if (simpleStmtsContext != null) {
            return visit(simpleStmtsContext);
        }

        PythonParser.StatementsContext statements = ctx.statements();
        if (statements != null) {
            return visit(statements);
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitDecorators(PythonParser.DecoratorsContext ctx) {
        List<PythonParser.Named_expressionContext> namedExpressionContexts = ctx.named_expression();
        List<Value> decorators = new ArrayList<>();
        for (PythonParser.Named_expressionContext namedExpressionContext : namedExpressionContexts) {
            decorators.add((Value) visit(namedExpressionContext));
        }
        return decorators;
    }

    @Override
    public Object visitClass_def(PythonParser.Class_defContext ctx) {
        PythonParser.DecoratorsContext decorators = ctx.decorators();
        List<Value> decoratorsList = decorators == null ? new ArrayList<>() : (List<Value>) visit(decorators);
        if (decorators != null) {
            for (PythonParser.Named_expressionContext decorator : decorators.named_expression()) {
                decoratorsList.add((Value) visit(decorator));
            }
        }

        PythonParser.Class_def_rawContext classDefRawContext = ctx.class_def_raw();
        if (classDefRawContext != null) {
            ClassType visit = (ClassType) visit(classDefRawContext);
            visit.setDecorators(decoratorsList);
            return visit;
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitClass_def_raw(PythonParser.Class_def_rawContext ctx) {
        TerminalNode name = ctx.NAME();
        if (name == null) throw new UnsupportedOperationException("Not implemented yet!");
        TerminalNode colon = ctx.COLON();
        if (colon == null) throw new UnsupportedOperationException("Not implemented yet!");
        PythonParser.ArgumentsContext baseClassesContext = ctx.arguments();
        List<String> baseClasses = baseClassesContext == null ? new ArrayList<>() : (List<String>) visit(baseClassesContext);
        PythonParser.BlockContext classBodyContext = ctx.block();

        PythonClass classType = new PythonClass(currentContext, name.getText());
        currentContext.getSymbolTable().put(classType.name(), new Value(classType));

        ClassDefinitionContext classDefinitionContext = new ClassDefinitionContext(currentContext, classType);
        this.contextStack.push(classDefinitionContext);
        this.currentContext = classDefinitionContext;

        visit(classBodyContext);

        this.contextStack.pop();
        this.currentContext = this.contextStack.peek();

        return classType;
    }

    @Override
    public Object visitFunction_def(PythonParser.Function_defContext ctx) {
        PythonParser.DecoratorsContext decorators = ctx.decorators();
        List<Value> decoratorsList = decorators == null ? new ArrayList<>() : (List<Value>) visit(decorators);
        if (decorators != null) {
            for (PythonParser.Named_expressionContext decorator : decorators.named_expression()) {
                decoratorsList.add((Value) visit(decorator));
            }
        }

        PythonParser.Function_def_rawContext functionDefRawContext = ctx.function_def_raw();
        if (functionDefRawContext != null) {
            FuncType visit = (FuncType) visit(functionDefRawContext);
            visit.setDecorators(decoratorsList);

            currentContext.getSymbolTable().put(visit.name(), new Value(visit));
            return visit;
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitFunction_def_raw(PythonParser.Function_def_rawContext ctx) {
        TerminalNode def = ctx.DEF();
        if (def == null) throw new UnsupportedOperationException("Not implemented yet!");
        TerminalNode name = ctx.NAME();
        if (name == null) throw new UnsupportedOperationException("Not implemented yet!");
        TerminalNode colon = ctx.COLON();
        if (colon == null) throw new UnsupportedOperationException("Not implemented yet!");
        PythonParser.BlockContext block = ctx.block();
        if (block == null) throw new UnsupportedOperationException("Not implemented yet!");

        TerminalNode async = ctx.ASYNC();
        PythonParser.ExpressionContext expression = ctx.expression();
        PythonParser.Func_type_commentContext funcTypeCommentContext = ctx.func_type_comment();

        TerminalNode rarrow = ctx.RARROW();
        Type returnType = anyType;
        if (rarrow != null) {
            PythonParser.Type_paramsContext typeParamsContext = ctx.type_params();
            throw new UnsupportedOperationException("Not implemented yet!");
        }

        PythonParser.Type_paramsContext typeParamsContext = ctx.type_params();
        if (typeParamsContext != null) {
            throw new UnsupportedOperationException("Not implemented yet!");
        }

        PythonParser.ParamsContext paramsContext = ctx.params();
        InterpParameter[] visit = (InterpParameter[]) visit(paramsContext);
        if (visit != null) {
            FuncType funcType = new FuncType(name.getText(), visit, returnType, currentContext);
            funcType.setProxy(((context, kwargs, args) -> {
                currentContext = context;
                contextStack.push(currentContext);
                Object visit1 = visit(block);
                FunctionContentsContext functionContents = (FunctionContentsContext) context;
                contextStack.pop();
                currentContext = contextStack.peek();
                Value returnedValue = functionContents.returnedValue;
                if (returnedValue != null) {
                    return returnedValue;
                }
                return (Value) visit1;
            }));
            return funcType;
        }

        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitParams(PythonParser.ParamsContext ctx) {
        PythonParser.ParametersContext parameters = ctx.parameters();
        if (parameters != null) {
            return visit(parameters);
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitParameters(PythonParser.ParametersContext ctx) {
        List<InterpParameter> parameters = new ArrayList<>();
        for (PythonParser.Param_no_defaultContext paramNoDefaultContext : ctx.param_no_default()) {
            parameters.add((InterpParameter) visit(paramNoDefaultContext));
        }

        for (PythonParser.Param_with_defaultContext paramWithDefaultContext : ctx.param_with_default()) {
            parameters.add((InterpParameter) visit(paramWithDefaultContext));
        }

        PythonParser.Star_etcContext starEtcContext = ctx.star_etc();
        if (starEtcContext != null) {
            throw new UnsupportedOperationException("Not implemented yet!");
        }

        return parameters.toArray(new InterpParameter[0]);
    }

    @Override
    public Object visitSlash_no_default(PythonParser.Slash_no_defaultContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSlash_with_default(PythonParser.Slash_with_defaultContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitStar_etc(PythonParser.Star_etcContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitKwds(PythonParser.KwdsContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitParam_no_default(PythonParser.Param_no_defaultContext ctx) {
        PythonParser.ParamContext param = ctx.param();
        TerminalNode terminalNode = ctx.TYPE_COMMENT();
        if (terminalNode != null) {
            throw new UnsupportedOperationException("Not implemented yet!");
        }
        if (param != null) {
            return visit(param);
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitParam_no_default_star_annotation(PythonParser.Param_no_default_star_annotationContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitParam_with_default(PythonParser.Param_with_defaultContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitParam_maybe_default(PythonParser.Param_maybe_defaultContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitParam(PythonParser.ParamContext ctx) {
        TerminalNode name = ctx.NAME();
        if (name == null) throw new UnsupportedOperationException("Not implemented yet!");

        PythonParser.AnnotationContext annotation = ctx.annotation();
        if (annotation != null) {
            return new InterpParameter(name.getText(), (Type) visit(annotation), false);
        }

        return new InterpParameter(name.getText(), anyType, false);
    }

    @Override
    public Object visitParam_star_annotation(PythonParser.Param_star_annotationContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitAnnotation(PythonParser.AnnotationContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitStar_annotation(PythonParser.Star_annotationContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitDefault_assignment(PythonParser.Default_assignmentContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitIf_stmt(PythonParser.If_stmtContext ctx) {
        PythonParser.Named_expressionContext namedExpressionContext = ctx.named_expression();
        boolean success = false;
        if (namedExpressionContext != null) {
            Object visit = visit(namedExpressionContext);
            if (visit instanceof Boolean) {
                success = (boolean) visit;
            } else {
                success = visit != null;
            }
        }

        if (success) {
            return visit(ctx.block());
        } else {
            PythonParser.Elif_stmtContext elifStmtContext = ctx.elif_stmt();
            if (elifStmtContext != null) {
                return visit(elifStmtContext);
            }
            PythonParser.Else_blockContext elseBlockContext = ctx.else_block();
            if (elseBlockContext != null) {
                return visit(elseBlockContext);
            }
            throw new UnsupportedOperationException("Not implemented yet!");
        }
    }

    @Override
    public Object visitElif_stmt(PythonParser.Elif_stmtContext ctx) {
        PythonParser.Named_expressionContext namedExpressionContext = ctx.named_expression();
        boolean success = false;
        if (namedExpressionContext != null) {
            Object visit = visit(namedExpressionContext);
            if (visit instanceof Boolean) {
                success = (boolean) visit;
            } else {
                success = visit != null;
            }
        }

        if (success) {
            return visit(ctx.block());
        } else {
            PythonParser.Elif_stmtContext elifStmtContext = ctx.elif_stmt();
            if (elifStmtContext != null) {
                return visit(elifStmtContext);
            }
            PythonParser.Else_blockContext elseBlockContext = ctx.else_block();
            if (elseBlockContext != null) {
                return visit(elseBlockContext);
            }
            throw new UnsupportedOperationException("Not implemented yet!");
        }
    }

    @Override
    public Object visitElse_block(PythonParser.Else_blockContext ctx) {
        PythonParser.BlockContext block = ctx.block();
        if (block != null) {
            return visit(block);
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitWhile_stmt(PythonParser.While_stmtContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitFor_stmt(PythonParser.For_stmtContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitWith_stmt(PythonParser.With_stmtContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitWith_item(PythonParser.With_itemContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitTry_stmt(PythonParser.Try_stmtContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitExcept_block(PythonParser.Except_blockContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitExcept_star_block(PythonParser.Except_star_blockContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitFinally_block(PythonParser.Finally_blockContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitMatch_stmt(PythonParser.Match_stmtContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSubject_expr(PythonParser.Subject_exprContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitCase_block(PythonParser.Case_blockContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitGuard(PythonParser.GuardContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitPatterns(PythonParser.PatternsContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitPattern(PythonParser.PatternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitAs_pattern(PythonParser.As_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitOr_pattern(PythonParser.Or_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitClosed_pattern(PythonParser.Closed_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitLiteral_pattern(PythonParser.Literal_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitLiteral_expr(PythonParser.Literal_exprContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitComplex_number(PythonParser.Complex_numberContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSigned_number(PythonParser.Signed_numberContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSigned_real_number(PythonParser.Signed_real_numberContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitReal_number(PythonParser.Real_numberContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitImaginary_number(PythonParser.Imaginary_numberContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitCapture_pattern(PythonParser.Capture_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitPattern_capture_target(PythonParser.Pattern_capture_targetContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitWildcard_pattern(PythonParser.Wildcard_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitValue_pattern(PythonParser.Value_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitAttr(PythonParser.AttrContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitName_or_attr(PythonParser.Name_or_attrContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitGroup_pattern(PythonParser.Group_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSequence_pattern(PythonParser.Sequence_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitOpen_sequence_pattern(PythonParser.Open_sequence_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitMaybe_sequence_pattern(PythonParser.Maybe_sequence_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitMaybe_star_pattern(PythonParser.Maybe_star_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitStar_pattern(PythonParser.Star_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitMapping_pattern(PythonParser.Mapping_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitItems_pattern(PythonParser.Items_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitKey_value_pattern(PythonParser.Key_value_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitDouble_star_pattern(PythonParser.Double_star_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitClass_pattern(PythonParser.Class_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitPositional_patterns(PythonParser.Positional_patternsContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitKeyword_patterns(PythonParser.Keyword_patternsContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitKeyword_pattern(PythonParser.Keyword_patternContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitType_alias(PythonParser.Type_aliasContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitType_params(PythonParser.Type_paramsContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitType_param_seq(PythonParser.Type_param_seqContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitType_param(PythonParser.Type_paramContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitType_param_bound(PythonParser.Type_param_boundContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitExpressions(PythonParser.ExpressionsContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitExpression(PythonParser.ExpressionContext ctx) {
        PythonParser.ExpressionContext expression = ctx.expression();

        if (expression != null) {
            return visit(expression);
        }

        List<PythonParser.DisjunctionContext> disjunction = ctx.disjunction();
        Value visit = null;
        boolean set = false;
        for (PythonParser.DisjunctionContext disjunctionContext : disjunction) {
            visit = (Value) visit(disjunctionContext);
            if (!set) {
                set = true;
                continue;
            }
        }

        if (set) {
            return visit;
        }
        PythonParser.LambdefContext lambdef = ctx.lambdef();
        if (lambdef != null) {
            return visit(lambdef);
        }

        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitYield_expr(PythonParser.Yield_exprContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitStar_expressions(PythonParser.Star_expressionsContext ctx) {
        List<Value> values = new ArrayList<>();
        for (PythonParser.Star_expressionContext starExpressionContext : ctx.star_expression()) {
            values.add((Value) visit(starExpressionContext));
        }
        return values;
    }

    @Override
    public Object visitStar_expression(PythonParser.Star_expressionContext ctx) {
        TerminalNode star = ctx.STAR();
        if (star != null) {
            throw new UnsupportedOperationException("Not implemented yet!");
        }

        PythonParser.Bitwise_orContext bitwiseOrContext = ctx.bitwise_or();
        if (bitwiseOrContext != null) {
            return visit(bitwiseOrContext);
        }

        return visit(ctx.expression());
    }

    @Override
    public Object visitStar_named_expressions(PythonParser.Star_named_expressionsContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitStar_named_expression(PythonParser.Star_named_expressionContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitAssignment_expression(PythonParser.Assignment_expressionContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitNamed_expression(PythonParser.Named_expressionContext ctx) {
        PythonParser.ExpressionContext expression = ctx.expression();
        if (expression != null) {
            return visit(expression);
        }

        PythonParser.Assignment_expressionContext assignmentExpressionContext = ctx.assignment_expression();
        if (assignmentExpressionContext != null) {
            return visit(assignmentExpressionContext);
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitDisjunction(PythonParser.DisjunctionContext ctx) {
        Object result = null;
        boolean set = false;

        for (PythonParser.ConjunctionContext conjunctionContext : ctx.conjunction()) {
            Object value = visit(conjunctionContext);

            if (!set) {
                result = value;
                set = true;
                continue;
            }

            if (result instanceof Boolean && value instanceof Boolean) {
                result = (Boolean) result || (Boolean) value;
            } else {
                throw new UnsupportedOperationException("Not implemented yet!");
            }
        }

        return result;
    }

    @Override
    public Object visitConjunction(PythonParser.ConjunctionContext ctx) {
        Object result = null;
        boolean set = false;

        for (PythonParser.InversionContext inversionContext : ctx.inversion()) {
            Object value = visit(inversionContext);

            if (!set) {
                result = value;
                set = true;
                continue;
            }

            if (result instanceof Boolean && value instanceof Boolean) {
                result = (Boolean) result && (Boolean) value;
            } else {
                throw new UnsupportedOperationException("Not implemented yet!");
            }
        }

        return result;
    }

    @Override
    public Object visitInversion(PythonParser.InversionContext ctx) {
        PythonParser.InversionContext inversion = ctx.inversion();
        PythonParser.ComparisonContext comparison = ctx.comparison();

        if (inversion != null && comparison != null) {
            throw new UnsupportedOperationException("Not implemented yet!");
        }

        if (inversion != null) {
            return visit(inversion);
        }

        if (comparison != null) {
            return visit(comparison);
        }

        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitComparison(PythonParser.ComparisonContext ctx) {
        Value result = null;
        boolean set = false;

        if (ctx.compare_op_bitwise_or_pair() == null || ctx.compare_op_bitwise_or_pair().isEmpty()) {
            return visit(ctx.bitwise_or());
        }

        result = ctx.bitwise_or() == null ? null : (Value) visit(ctx.bitwise_or());

        for (PythonParser.Compare_op_bitwise_or_pairContext compareOpBitwiseOrPairContext : ctx.compare_op_bitwise_or_pair()) {
            Object value = visit(compareOpBitwiseOrPairContext);

            if (!set) {
                result = (Value) value;
                set = true;
                continue;
            }

            if (result.get() instanceof Boolean b && value instanceof Boolean) {
                if (b) {
                    result = PythonValue.of(currentContext, true);
                    continue;
                }
                result = PythonValue.of(currentContext, false);
            } else {
                throw new UnsupportedOperationException("Not implemented yet!");
            }
        }

        return result;
    }

    @Override
    public Object visitCompare_op_bitwise_or_pair(PythonParser.Compare_op_bitwise_or_pairContext ctx) {
        Value result = null;

        if (ctx.eq_bitwise_or() != null) {
            return visit(ctx.eq_bitwise_or());
        }

        if (ctx.noteq_bitwise_or() != null) {
            return visit(ctx.noteq_bitwise_or());
        }

        if (ctx.lte_bitwise_or() != null) {
            return visit(ctx.lte_bitwise_or());
        }

        if (ctx.lt_bitwise_or() != null) {
            return visit(ctx.lt_bitwise_or());
        }

        if (ctx.gte_bitwise_or() != null) {
            return visit(ctx.gte_bitwise_or());
        }

        if (ctx.gt_bitwise_or() != null) {
            return visit(ctx.gt_bitwise_or());
        }

        if (ctx.in_bitwise_or() != null) {
            return visit(ctx.in_bitwise_or());
        }

        if (ctx.notin_bitwise_or() != null) {
            return visit(ctx.notin_bitwise_or());
        }

        if (ctx.is_bitwise_or() != null) {
            return visit(ctx.is_bitwise_or());
        }

        if (ctx.isnot_bitwise_or() != null) {
            return visit(ctx.isnot_bitwise_or());
        }

        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitEq_bitwise_or(PythonParser.Eq_bitwise_orContext ctx) {
        if (ctx.bitwise_or() != null) {
            return visit(ctx.bitwise_or());
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitNoteq_bitwise_or(PythonParser.Noteq_bitwise_orContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitLte_bitwise_or(PythonParser.Lte_bitwise_orContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitLt_bitwise_or(PythonParser.Lt_bitwise_orContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitGte_bitwise_or(PythonParser.Gte_bitwise_orContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitGt_bitwise_or(PythonParser.Gt_bitwise_orContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitNotin_bitwise_or(PythonParser.Notin_bitwise_orContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitIn_bitwise_or(PythonParser.In_bitwise_orContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitIsnot_bitwise_or(PythonParser.Isnot_bitwise_orContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitIs_bitwise_or(PythonParser.Is_bitwise_orContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitBitwise_or(PythonParser.Bitwise_orContext ctx) {
        Value visit = null;
        if (ctx.bitwise_or() != null) {
            visit = (Value) visit(ctx.bitwise_or());
        }
        if (ctx.bitwise_xor() != null) {
            if (visit != null) {
                return visit.or((Value) visit(ctx.bitwise_xor()));
            } else {
                return visit(ctx.bitwise_xor());
            }
        } else if (visit != null) {
            return visit;
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitBitwise_xor(PythonParser.Bitwise_xorContext ctx) {
        Value visit = null;
        if (ctx.bitwise_xor() != null) {
            visit = (Value) visit(ctx.bitwise_xor());
        }
        if (ctx.bitwise_and() != null) {
            if (visit != null) {
                return visit.xor((Value) visit(ctx.bitwise_and()));
            } else {
                return visit(ctx.bitwise_and());
            }
        } else if (visit != null) {
            return visit;
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitBitwise_and(PythonParser.Bitwise_andContext ctx) {
        Value visit = null;
        if (ctx.bitwise_and() != null) {
            visit = (Value) visit(ctx.bitwise_and());
        }
        if (ctx.shift_expr() != null) {
            if (visit != null) {
                return visit.and((Value) visit(ctx.shift_expr()));
            } else {
                return visit(ctx.shift_expr());
            }
        } else if (visit != null) {
            return visit;
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitShift_expr(PythonParser.Shift_exprContext ctx) {
        Value visit = null;
        if (ctx.shift_expr() != null) {
            visit = (Value) visit(ctx.shift_expr());
        }
        if (ctx.sum() != null) {
            if (visit != null) {
                if (ctx.LEFTSHIFT() != null) {
                    return visit.shl((Value) visit(ctx.sum()));
                } else if (ctx.RIGHTSHIFT() != null) {
                    return visit.shr((Value) visit(ctx.sum()));
                }
            } else {
                return visit(ctx.sum());
            }
        } else if (visit != null) {
            return visit;
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSum(PythonParser.SumContext ctx) {
        PythonValue visit = null;
        if (ctx.sum() != null) {
            visit = (PythonValue) visit(ctx.sum());
        }
        if (ctx.term() != null) {
            if (visit != null) {
                TerminalNode minus = ctx.MINUS();
                if (minus != null) {
                    return visit.plus(((Value) visit(ctx.term())).negate());
                }
                PythonValue visit1 = (PythonValue) visit(ctx.term());
                if (visit1 == null) {
                    throw new UnsupportedOperationException("Not implemented yet!");
                }
                return visit.plus(visit1);
            } else {
                return visit(ctx.term());
            }
        } else if (visit != null) {
            return visit;
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitTerm(PythonParser.TermContext ctx) {
        Value visit = null;
        if (ctx.term() != null) {
            visit = (Value) visit(ctx.term());
        }
        if (ctx.factor() != null) {
            if (visit != null) {
                if (ctx.STAR() != null) {
                    return visit.times((Value) visit(ctx.factor()));
                } else if (ctx.SLASH() != null) {
                    return visit.divide((Value) visit(ctx.factor()));
                } else if (ctx.DOUBLESLASH() != null) {
                    return visit.floordiv((Value) visit(ctx.factor()));
                } else if (ctx.PERCENT() != null) {
                    return visit.rem((Value) visit(ctx.factor()));
                } else if (ctx.AT() != null) {
//                    return visit.pow((Value) visit(ctx.factor()));
                    throw new UnsupportedOperationException("Not implemented yet!");
                } else {
                    throw new UnsupportedOperationException("Not implemented yet!");
                }
            } else {
                return visit(ctx.factor());
            }
        } else if (visit != null) {
            return visit;
        }

        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitFactor(PythonParser.FactorContext ctx) {
        Value visit = null;
        if (ctx.factor() != null) {
            visit = (Value) visit(ctx.factor());
        }
        if (ctx.power() != null) {
            if (visit != null) {
                TerminalNode unaryPlus = ctx.PLUS();
                TerminalNode unaryMinus = ctx.MINUS();

                if (unaryPlus != null) {
                    return visit.unaryPlus((Value) visit(ctx.power()));
                } else if (unaryMinus != null) {
                    return visit.unaryMinus((Value) visit(ctx.power()));
                } else {
                    throw new UnsupportedOperationException("Not implemented yet!");
                }
            } else {
                return visit(ctx.power());
            }
        } else if (visit != null) {
            return visit;
        }

        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitPower(PythonParser.PowerContext ctx) {
        Value visit = null;
        if (ctx.factor() != null) {
            visit = (Value) visit(ctx.factor());
        }
        if (ctx.await_primary() != null) {
            if (visit != null) {
                return visit.pow((Value) visit(ctx.await_primary()));
            } else {
                return visit(ctx.await_primary());
            }
        } else if (visit != null) {
            return visit;
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitAwait_primary(PythonParser.Await_primaryContext ctx) {
        Value visit = null;
        if (ctx.AWAIT() != null) {
            throw new UnsupportedOperationException("Not implemented yet!");
        }
        if (ctx.primary() != null) {
            visit = (Value) visit(ctx.primary());
        }
        if (visit != null) {
            return visit;
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitPrimary(PythonParser.PrimaryContext ctx) {
        if (ctx.LPAR() != null && ctx.RPAR() != null) {
            PythonParser.ArgumentsContext arguments = ctx.arguments();
            if (arguments != null) {
                Object visit = visit(arguments);
                PythonParser.PrimaryContext primary = ctx.primary();
                Value value = ctx.NAME() != null ? currentContext.get(ctx.NAME().getText()) : null;
                if (primary != null) {
                    value = (Value) visit(primary);
                }
                if (value != null) {
                    return value.invoke((FuncCall) visit);
                } else {
                    throw new UnsupportedOperationException("Not implemented yet!");
                }
            } else {
                PythonParser.PrimaryContext primary = ctx.primary();
                Value value = ctx.NAME() != null ? currentContext.get(ctx.NAME().getText()) : null;
                if (primary != null) {
                    value = (Value) visit(primary);
                }
                if (value != null) {
                    return value.invoke(new PyFuncCall(Map.of(), new Value[0]));
                } else {
                    throw new UnsupportedOperationException("Not implemented yet!");
                }
            }
        }

        PythonParser.PrimaryContext primary = ctx.primary();
        if (primary != null) {
            if (ctx.DOT() != null) {
                TerminalNode name = ctx.NAME();
                if (name != null) {
                    Value value = (Value) visit(primary);
                    if (value.isNull()) {
                        throw new InterpreterNullPtrException(this, "Cannot access member of null object.");
                    }
                    if (value.ptr instanceof ClassLike) {
                        return ((ClassLike) value.ptr).get(name.getText());
                    } else if (value.ptr instanceof ObjectLike) {
                        return ((ObjectLike) value.ptr).get(name.getText());
                    }
                    throw new InterpreterException("Class " + name.getText() + " does not allow member access!");
                } else {
                    throw new UnsupportedOperationException("Not implemented yet!");
                }
            }
            return visit(primary);
        }
        PythonParser.SlicesContext slices = ctx.slices();
        if (slices != null) {
            return visit(slices);
        }
        PythonParser.GenexpContext genexp = ctx.genexp();
        if (genexp != null) {
            return visit(genexp);
        }
        PythonParser.AtomContext atom = ctx.atom();
        if (atom != null) {
            Object visit = visit(atom);
            if (visit instanceof PythonValue) {
                return visit;
            } else if (visit instanceof Value) {
                return PythonValue.of(currentContext, ((Value) visit).ptr);
            } else if (visit == null) {
                throw new InterpreterException("Variable not found: " + atom.getText(), atom);
            } else {
                throw new InterpreterException("Unknown value!");
            }
        }
        TerminalNode name = ctx.NAME();
        if (name != null) {
            return name.getText();
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSlices(PythonParser.SlicesContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSlice(PythonParser.SliceContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitAtom(PythonParser.AtomContext ctx) {
        PythonParser.StringsContext strings = ctx.strings();
        if (strings != null) {
            return PythonValue.of(currentContext, visit(strings));
        }
        PythonParser.GenexpContext genexp = ctx.genexp();
        if (genexp != null) {
            return PythonValue.of(currentContext, visit(genexp));
        }
        PythonParser.SetContext set = ctx.set();
        if (set != null) {
            return PythonValue.of(currentContext, visit(set));
        }
        PythonParser.SetcompContext setcomp = ctx.setcomp();
        if (setcomp != null) {
            return PythonValue.of(currentContext, visit(setcomp));
        }
        PythonParser.DictContext dict = ctx.dict();
        if (dict != null) {
            return PythonValue.of(currentContext, visit(dict));
        }
        PythonParser.DictcompContext dictcomp = ctx.dictcomp();
        if (dictcomp != null) {
            return PythonValue.of(currentContext, visit(dictcomp));
        }
        PythonParser.ListContext list = ctx.list();
        if (list != null) {
            return PythonValue.of(currentContext, visit(list));
        }
        PythonParser.ListcompContext listcomp = ctx.listcomp();
        if (listcomp != null) {
            return PythonValue.of(currentContext, visit(listcomp));
        }
        TerminalNode number = ctx.NUMBER();
        if (number != null) {
            if (number.getText().contains(".")) {
                return PythonValue.of(currentContext, new BigDecimal(number.getText()));
            }
            return PythonValue.of(currentContext, new BigInteger(number.getText()));
        }

        TerminalNode ellipsis = ctx.ELLIPSIS();
        if (ellipsis != null) {
            throw new UnsupportedOperationException("Not implemented yet!");
        }

        TerminalNode aTrue = ctx.TRUE();
        if (aTrue != null) {
            return Value.TRUE;
        }

        TerminalNode aFalse = ctx.FALSE();
        if (aFalse != null) {
            return Value.FALSE;
        }

        PythonParser.TupleContext tuple = ctx.tuple();
        if (tuple != null) {
            return PythonValue.of(currentContext, visit(tuple));
        }
        TerminalNode name = ctx.NAME();
        if (name != null) {
            return currentContext.get(name.getText());
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitGroup(PythonParser.GroupContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitLambdef(PythonParser.LambdefContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitLambda_params(PythonParser.Lambda_paramsContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitLambda_parameters(PythonParser.Lambda_parametersContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitLambda_slash_no_default(PythonParser.Lambda_slash_no_defaultContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitLambda_slash_with_default(PythonParser.Lambda_slash_with_defaultContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitLambda_star_etc(PythonParser.Lambda_star_etcContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitLambda_kwds(PythonParser.Lambda_kwdsContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitLambda_param_no_default(PythonParser.Lambda_param_no_defaultContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitLambda_param_with_default(PythonParser.Lambda_param_with_defaultContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitLambda_param_maybe_default(PythonParser.Lambda_param_maybe_defaultContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitLambda_param(PythonParser.Lambda_paramContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitFstring_middle(PythonParser.Fstring_middleContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitFstring_replacement_field(PythonParser.Fstring_replacement_fieldContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitFstring_conversion(PythonParser.Fstring_conversionContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitFstring_full_format_spec(PythonParser.Fstring_full_format_specContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitFstring_format_spec(PythonParser.Fstring_format_specContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitFstring(PythonParser.FstringContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitString(PythonParser.StringContext ctx) {
        TerminalNode string = ctx.STRING();
        if (string != null) {
            if (string.getText().startsWith("\"\"\"") && string.getText().endsWith("\"\"\"")) {
                return string.getText().substring(3, string.getText().length() - 3);
            }
            if (string.getText().startsWith("'''") && string.getText().endsWith("'''")) {
                return string.getText().substring(3, string.getText().length() - 3);
            }
            if (string.getText().startsWith("'") && string.getText().endsWith("'")) {
                return string.getText().substring(1, string.getText().length() - 1);
            }
            if (string.getText().startsWith("\"") && string.getText().endsWith("\"")) {
                return string.getText().substring(1, string.getText().length() - 1);
            }
            return string.getText();
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitStrings(PythonParser.StringsContext ctx) {
        StringBuilder result = new StringBuilder();
        for (PythonParser.StringContext stringContext : ctx.string()) {
            result.append(visit(stringContext));
        }

        for (PythonParser.FstringContext fstringContext : ctx.fstring()) {
            result.append(visit(fstringContext));
        }

        return result.toString();
    }

    @Override
    public Object visitList(PythonParser.ListContext ctx) {
        if (ctx.LSQB() == null) {
            throw new UnsupportedOperationException("Not implemented yet!");
        }
        if (ctx.RSQB() == null) {
            throw new UnsupportedOperationException("Not implemented yet!");
        }

        if (ctx.children.size() == 2) {
            return new ArrayList<>();
        }

        List<Object> result = new ArrayList<>();
        if (ctx.children.size() == 3) {
            return visit(ctx.star_named_expressions());
        } else {
            throw new UnsupportedOperationException("Not implemented yet!");
        }
    }

    @Override
    public Object visitTuple(PythonParser.TupleContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSet(PythonParser.SetContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitDict(PythonParser.DictContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitDouble_starred_kvpairs(PythonParser.Double_starred_kvpairsContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitDouble_starred_kvpair(PythonParser.Double_starred_kvpairContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitKvpair(PythonParser.KvpairContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitFor_if_clauses(PythonParser.For_if_clausesContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitFor_if_clause(PythonParser.For_if_clauseContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitListcomp(PythonParser.ListcompContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSetcomp(PythonParser.SetcompContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitGenexp(PythonParser.GenexpContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitDictcomp(PythonParser.DictcompContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitArguments(PythonParser.ArgumentsContext ctx) {
        PythonParser.ArgsContext args = ctx.args();
        if (args != null) {
            return visit(args);
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitArgs(PythonParser.ArgsContext ctx) {
        Map<String, Value> kwargs = new HashMap<>();

        List<Value> args = new ArrayList<>();
        for (var argContext : ctx.children) {
            switch (argContext) {
                case TerminalNode terminalNode -> {
                    if (terminalNode.getText().equals("lambda")) {
                        throw new UnsupportedOperationException("Not implemented yet!");
                    } else if (terminalNode.getText().equals(",")) {

                    } else {
                        throw new UnsupportedOperationException("Not implemented yet!");
                    }
                }
                case PythonParser.KwargsContext kwargsContext -> kwargs = (Map<String, Value>) visit(argContext);
                case PythonParser.ExpressionContext expressionContext -> args.add((Value) visit(argContext));
                case PythonParser.Assignment_expressionContext assignmentExpressionContext ->
                        args.add((Value) visit(argContext));
                case null, default -> throw new UnsupportedOperationException("Not implemented yet!");
            }
        }

        return new PyFuncCall(kwargs, args.toArray(new Value[0]));
    }

    @Override
    public Object visitKwargs(PythonParser.KwargsContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitStarred_expression(PythonParser.Starred_expressionContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitKwarg_or_starred(PythonParser.Kwarg_or_starredContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitKwarg_or_double_starred(PythonParser.Kwarg_or_double_starredContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitStar_targets(PythonParser.Star_targetsContext ctx) {
        List<ValueTarget> visit = new ArrayList<>();
        for (PythonParser.Star_targetContext starTargetContext : ctx.star_target()) {
            visit.add((ValueTarget) visit(starTargetContext));
        }

        return visit;
    }

    @Override
    public Object visitStar_targets_list_seq(PythonParser.Star_targets_list_seqContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitStar_targets_tuple_seq(PythonParser.Star_targets_tuple_seqContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitStar_target(PythonParser.Star_targetContext ctx) {
        PythonParser.Target_with_star_atomContext targetWithStarAtomContext = ctx.target_with_star_atom();
        Object visit = null;
        if (targetWithStarAtomContext != null) {
            visit = visit(targetWithStarAtomContext);
        }
        PythonParser.Star_targetContext starTargetContext = ctx.star_target();
        if (starTargetContext != null) {
            if (visit != null) {
                throw new UnsupportedOperationException("Not implemented yet!");
            }
//            return visit(starTargetContext);
            throw new UnsupportedOperationException("Not implemented yet!");
        }

        return visit;
    }

    @Override
    public Object visitTarget_with_star_atom(PythonParser.Target_with_star_atomContext ctx) {
        PythonParser.T_primaryContext tPrimaryContext = ctx.t_primary();
        if (tPrimaryContext != null) {
            Value visit = (Value) visit(tPrimaryContext);
            TerminalNode name = ctx.NAME();
            if (visit == null) throw new InterpreterException("Variable or attribute does not exist: " + tPrimaryContext.getText());
            if (visit.isNull()) throw new InterpreterNullPtrException("Null pointer");
            if (name != null) {
                return new ValueTarget(name.getText(), v -> {
                    Object o = visit.get();
                    if (o instanceof ClassLike classLike) {
                        classLike.set(name.getText(), v);
                    } else if (o instanceof ObjectLike objectLike) {
                        objectLike.set(name.getText(), v);
                    } else {
                        throw new UnsupportedOperationException("Not implemented yet!");
                    }
                });
            }
        }

        PythonParser.Star_atomContext starAtomContext = ctx.star_atom();
        if (starAtomContext != null) {
            return visit(starAtomContext);
        }

        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitStar_atom(PythonParser.Star_atomContext ctx) {
        if (ctx.LPAR() != null) {
            throw new UnsupportedOperationException("Not implemented yet!");
        }
        TerminalNode name = ctx.NAME();
        if (name != null) {
            return new ValueTarget(name.getText(), v -> {
                throw new UnsupportedOperationException("Not implemented yet!");
            });
        }

        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSingle_target(PythonParser.Single_targetContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSingle_subscript_attribute_target(PythonParser.Single_subscript_attribute_targetContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitT_primary(PythonParser.T_primaryContext ctx) {
        PythonParser.AtomContext atom = ctx.atom();
        if (atom != null) {
            Value visit = (Value) visit(atom);
            PythonParser.ArgumentsContext arguments = ctx.arguments();
            if (ctx.LPAR() != null) {
                if (arguments != null) {
                    throw new UnsupportedOperationException("Not implemented yet!");
                } else {
                    return visit;
                }
            }
            return visit;
        }
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitDel_targets(PythonParser.Del_targetsContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitDel_target(PythonParser.Del_targetContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitDel_t_atom(PythonParser.Del_t_atomContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitType_expressions(PythonParser.Type_expressionsContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitFunc_type_comment(PythonParser.Func_type_commentContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSoft_kw_type(PythonParser.Soft_kw_typeContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSoft_kw_match(PythonParser.Soft_kw_matchContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSoft_kw_case(PythonParser.Soft_kw_caseContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSoft_kw_wildcard(PythonParser.Soft_kw_wildcardContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitSoft_kw__not__wildcard(PythonParser.Soft_kw__not__wildcardContext ctx) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public FileContext getMainContext() {
        return mainContext;
    }

    @Override
    public String getClassSymbolName() {
        return "attribute";
    }

    @Override
    public PythonStdLib getStdLib() {
        return stdLib;
    }

    @Override
    public Type nullType() {
        return nullType;
    }

    @Override
    public Type anyType() {
        return anyType;
    }

    @Override
    public Value nullValue() {
        return nullValue;
    }

    @Override
    public boolean isAssert() {
        return isAssert;
    }

    @Override
    public Object visit(ParseTree tree) {
        try {
            return super.visit(tree);
        } catch (UnsupportedOperationException e) {
            throw new InterpreterException(e.getMessage(), tree);
        }
    }

    @Override
    public Object visitChildren(RuleNode node) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitTerminal(TerminalNode node) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    @Override
    public Object visitErrorNode(ErrorNode node) {
        throw new UnsupportedOperationException("Not implemented yet!");
    }

    public Context createNativeModuleContext(String path) {
        return new ModuleContext(this, "://" + path);
    }
}
