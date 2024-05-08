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
range Directions = 1 .. 4;

dvar boolean Selected[Objects]; // if a product is selected or not 1/0

// x and y of an object if selected (top left corner/ lowest numbers so you can add the side)
dvar int+ SelectedX[Objects];
dvar int+ SelectedY[Objects];
dvar boolean Overlap[Objects][Objects][Directions];

//<<<<<<<<<<<<<<<<

maximize  // Write here the objective function.
    sum(o in Objects) p[o] * Selected[o];


//>>>>>>>>>>>>>>>>

//<<<<<<<<<<<<<<<<

subject to {
    Weight: sum(o in Objects) w[o] * Selected[o] <= c;
    
    CoordsBounds:
    forall(o in Objects) {
        SelectedX[o] - 1 >= 1 * (1 - Selected[o]);
        SelectedY[o] - 1 >= 1 * (1 - Selected[o]);
        SelectedX[o] + s[o] - 1 - x <= 1 * (1 - Selected[o]);
        SelectedY[o] + s[o] - 1 - y <= 1 * (1 - Selected[o]);
    }

	Overlaps:
	forall (o1, o2 in Objects : o1 != o2) {	  
	    (SelectedX[o1] - SelectedX[o2] + s[o1] <= 0) - 1 >= 100 * (Selected[o1] + Selected[o2] + Overlap[o1][o2][1] - 3);
	    (SelectedX[o2] - SelectedX[o1] + s[o2] <= 0) - 1 >= 100 * (Selected[o1] + Selected[o2] + Overlap[o1][o2][2] - 3);
	    (SelectedY[o1] - SelectedY[o2] + s[o1] <= 0) - 1 >= 100 * (Selected[o1] + Selected[o2] + Overlap[o1][o2][3] - 3);
	    (SelectedY[o2] - SelectedY[o1] + s[o2] <= 0) - 1 >= 100 * (Selected[o1] + Selected[o2] + Overlap[o1][o2][4] - 3);
	    sum(direction in Directions) Overlap[o1][o2][direction] >= 1;
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
      grid[i][j] = "-";
    }
  }
  // TODO: Make A B C... matrix
  for (var k = 1; k <= n; k++) {
  	if (Selected[k] == 1) {
        var productLetter = String.fromCharCode('A'.charCodeAt(0) + k - 1);
    	for (var i = SelectedX[k] - 1; i < SelectedX[k] - 1 + s[k]; i++) { //-1 because matrix is 0-4, and selected is 1-5
        	for (var j = SelectedY[k] - 1; j < SelectedY[k] - 1 + s[k]; j++) {
				grid[i][j] = productLetter;
     		}
  		}
	}
  }
    
  // Print matrix
  for (var i = 0; i < x; i++) {
    for (var j = 0; j < y; j++) {
      write(grid[i][j]);
    }
    writeln()
  }

}

//<<<<<<<<<<<<<<<<
