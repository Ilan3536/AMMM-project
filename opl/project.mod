// PLEASE ONLY CHANGE THIS FILE WHERE INDICATED.
int       x = ...;  // Height   of the suitcase.
int       y = ...;  // Width    of the suitcase.
int       c = ...;  // Capacity of the suitcase.

int       n = ...;  // Number  of     products.
int p[1..n] = ...;  // Prices  of the products.
int w[1..n] = ...;  // Weights of the products.
int s[1..n] = ...;  // Sides   of the boxes of the products.

// Define here your decision variables and
// any other auxiliary program variables you need.
// You can run an execute block if needed.

//>>>>>>>>>>>>>>>>

range Objects = 1 .. n;

dvar boolean Selected[Objects]; // if a product is selected or not 1/0

// x and y of an object if selected (top left corner/ lowest numbers so you can add the side)
dvar int+ SelectedX[Objects];
dvar int+ SelectedY[Objects];

//<<<<<<<<<<<<<<<<

maximize  // Write here the objective function.
    sum(o in Objects) p[o] * Selected[o];


//>>>>>>>>>>>>>>>>

//<<<<<<<<<<<<<<<<

subject to {
    Weight: sum(o in Objects) w[o] * Selected[o] <= c;

    CoordsBounds:
    forall(o in Objects) {
        Selected[o] == 1 => SelectedX[o] >= 1;
        Selected[o] == 1 => SelectedY[o] >= 1;
        Selected[o] == 1 => SelectedX[o] + s[o] - 1 <= x;
        Selected[o] == 1 => SelectedY[o] + s[o] - 1 <= y;
    }

    Overlaps:
    forall ( o1, o2 in Objects : o1 != o2 ) {
      ( Selected[o1] == 1 && Selected[o2] == 1 ) => (
        ( SelectedX[o1] - SelectedX[o2] + s[o1] <= 0 ) ||
        ( SelectedX[o2] - SelectedX[o1] + s[o2] <= 0 ) ||
        ( SelectedY[o1] - SelectedY[o2] + s[o1] <= 0 ) ||
        ( SelectedY[o2] - SelectedY[o1] + s[o2] <= 0 ) );
    }
}

// You can run an execute block if needed.

//>>>>>>>>>>>>>>>>

execute {
  var grid = new Array(x);

  // Create empty matrix
  for (var i = 0; i < x; i++) {
    grid[i] = new Array(y);
    for (var j = 0; j < y; j++) {
      grid[i][j] = " ";
    }
  }

  // TODO: Make A B C... matrix

  // Print matrix
  for (var i = 0; i < y; i++) {
    for (var j = 0; j < x; j++) {
      write(grid[j][i]);
    }
    writeln()
  }
}

//<<<<<<<<<<<<<<<<
