main {
  var files = new Array();

  for (var i = 0; i < 40; ++i) {
    files[i] = "project." + i + ".dat";
  }

  var source = new IloOplModelSource("project.mod");
  var cplex = new IloCplex();
  var def = new IloOplModelDefinition(source);

  for (var i = 0; i < files.length; ++i) {
    writeln("Running " + files[i]);

    var opl = new IloOplModel(def, cplex);

    var data = new IloOplDataSource(files[i]);
    opl.addDataSource(data);

    opl.generate();

    if (cplex.solve()) {
      writeln("OBJECTIVE: " + cplex.getObjValue());
    } else {
      writeln("No solution");
    }
    opl.postProcess();

    opl.end();

    writeln();
  }
}