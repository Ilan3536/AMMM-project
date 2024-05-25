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
range Directions = 1 .. 4; // up,down,left,right

int sides[Objects] = s;
int prices[Objects] = p;
int weights[Objects] = w;
int height = x;
int width = y;
int capacity = c;

int M;

float startTime;

execute {
  var maxs = 0;
  for (var i = 1; i <= n; i++) {
    if (sides[i] > maxs) {
      maxs = sides[i];
    }
  }
  M = (x+y+maxs) * 2; // big enough M to handle biggest side
  
  // Benchmark
  var start = new Date();
  startTime = start.getTime();
}

dvar boolean Chosen[Objects]; // if a product is Chosen or not 1/0

// x and y of an object if Chosen (top left corner/ lowest numbers so you can add the side)
dvar int+ PointsX[Objects];
dvar int+ PointsY[Objects];
// boolean variables if objects i and j overlap
dvar boolean Overlap[Objects][Objects][Directions];

//<<<<<<<<<<<<<<<<

maximize  // Write here the objective function.
    sum(i in Objects) prices[i] * Chosen[i];

//>>>>>>>>>>>>>>>>

//<<<<<<<<<<<<<<<<

subject to {
    MaxWeight:
    sum(i in Objects) weights[i] * Chosen[i] <= capacity;

    CoordsBounds:
    forall(i in Objects) {
       PointsX[i] >= 1;
       PointsY[i] >= 1;
       PointsX[i] + sides[i] - 1 <= height;
       PointsY[i] + sides[i] - 1 <= width;
    }

	Overlaps1:
	forall (i, j in Objects : i != j) {
	    PointsX[i] - PointsX[j] + sides[i] <= -M * (Chosen[i] + Chosen[j] + Overlap[i][j][1] - 3);
	}

	Overlaps2:
	forall (i, j in Objects : i != j) {
	    PointsX[j] - PointsX[i] + sides[j] <= -M * (Chosen[i] + Chosen[j] + Overlap[i][j][2] - 3);
	}

	Overlaps3:
	forall (i, j in Objects : i != j) {
	    PointsY[i] - PointsY[j] + sides[i] <= -M * (Chosen[i] + Chosen[j] + Overlap[i][j][3] - 3);
	}

	Overlaps4:
	forall (i, j in Objects : i != j) {
	    PointsY[j] - PointsY[i] + sides[j] <= -M * (Chosen[i] + Chosen[j] + Overlap[i][j][4] - 3);
	}

	AtLeastOneNotOverlap:
	forall (i, j in Objects : i != j) {
	    sum(direction in Directions) Overlap[i][j][direction] >= 1;
	}

}

// You can run an execute block if needed.

//>>>>>>>>>>>>>>>>

execute {
  var end = new Date();
  var endTime = end.getTime();
  writeln("TIME: " + (endTime - startTime) + " ms");
  
  var alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  var alphabetLower = "abcdefghijklmnopqrstuvwxyz";
  var numbers = "0123456789";
  var greekChars = "αβγδεζηθικλμνξοπρστυφχψω";
  var russianChars = "абвгдежзийклмнопрстуфхцчшщъыьэюяё";
  var armenian = "աբգդեզէըթժիլխծկհձղճմյնշոչպջռսվտրցւփքօֆ";
  var georgian = "ႠႡႢႣႤႥႦႧႨႩႪႫႬႭႮႯႰႱႲႳႴႵႶႷႸႹႺႻႼႽႾႿჀჁჂჃჄჅ";
  var all = alphabet + alphabetLower + numbers + greekChars + russianChars + armenian + georgian;
  
  var grid = new Array(x);

  // Create empty matrix
  for (var i = 0; i < x; i++) {
    grid[i] = new Array(y);
    for (var j = 0; j < y; j++) {
      grid[i][j] = "_";
    }
  }
  for (var k = 1; k <= n; k++) {
    if (Chosen[k] == 1) {
      var productLetter = all.charAt(k - 1);
      for (var i = PointsX[k] - 1; i < PointsX[k] - 1 + s[k]; i++) { //-1 because matrix is 0-4, and Chosen is 1-5
        for (var j = PointsY[k] - 1; j < PointsY[k] - 1 + s[k]; j++) {
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
