main {
  var files = new Array();

  files[0] = "project.0.dat"
  //files[1] = "project.1.dat"

  var source = new IloOplModelSource("project.mod");
  var cplex = new IloCplex();
  var def = new IloOplModelDefinition(source);

  for (var i = 0; i < files.length; ++i) {
    writeln("Running " + files[i]);

    var start = new Date();
    var startTime = start.getTime();

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

    var end = new Date();
    var endTime = end.getTime();
    writeln("Time: " + (endTime - startTime) + " ms");
    writeln();
  }
}