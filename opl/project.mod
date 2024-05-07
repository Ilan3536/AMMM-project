// PLEASE ONLY CHANGE THIS FILE WHERE INDICATED.
int       x = ...;  // Height   of the suitcase.
int       y = ...;  // Width    of the suitcase.
int       c = ...;  // Capacity of the suitcase.

int       n = ...;  // Number  of     products.
int p[1..n] = ...;  // Prices  of the products.
int w[1..n] = ...;  // Weights of the products.
int s[1..n] = ...;  // Sides   of the boxes of the products.

range Objects = 1 .. n;
range Height = 1 .. x;
range Width = 1 .. y;

// Define here your decision variables and
// any other auxiliary program variables you need.
// You can run an execute block if needed.

//>>>>>>>>>>>>>>>>


dvar boolean Selected[Objects]; // if a product is selected or not 1/0


// x and y of an object if selected (top left corner/ lowest numbers so you can add the side)
dvar int SelectedX[Objects];
dvar int SelectedY[Objects];

range Directions = 1..4; // up/down/left/right

dvar boolean overlap[Objects][Objects][Directions];

//<<<<<<<<<<<<<<<<

maximize  // Write here the objective function.
	sum(o in Objects) p[o] * Selected[o];


//>>>>>>>>>>>>>>>>

//<<<<<<<<<<<<<<<<

subject to {
    Weight: sum(o in Objects) w[o] * Selected[o] <= c;
    
	
	SelectedCoords: forall(o in Objects) {
	    SelectedX[o] >= 1 * Selected[o] && // product is selected
	    SelectedY[o] >= 1 * Selected[o] &&
	    (SelectedX[o] + s[o] - 1) <= x * Selected[o]  && // product contained in the box
	    (SelectedY[o] + s[o] - 1) <= y * Selected[o] 
	    ;
	}
	
	forall(o1, o2 in Objects: o1 != o2) {
	  SelectedX[o1] - SelectedX[o2] + s[o1] - 1 <= x * (1 - overlap[o1][o2][1]) * Selected[o1] &&
	  SelectedX[o2] - SelectedX[o1] + s[o2] - 1 <= x * (1 - overlap[o1][o2][2]) * Selected[o1] &&
	  SelectedY[o1] - SelectedY[o1] + s[o1] - 1 <= y * (1 - overlap[o1][o2][3]) * Selected[o1] &&
	  SelectedY[o2] - SelectedY[o1] + s[o2] - 1 <= y * (1 - overlap[o1][o2][4]) * Selected[o1] &&
      sum(i in Directions) overlap[o1][o2][i] >= 1;
	}
}

// You can run an execute block if needed.

//>>>>>>>>>>>>>>>>

//<<<<<<<<<<<<<<<<
