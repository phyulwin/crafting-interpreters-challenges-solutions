// The following changes are made in the actual forked repository files
// and copied here for assignment submission. 

class Doughnut {
cook() {
    print "Fry until golden brown.";
    inner();
    print "Place in a nice box.";
}
}

class BostonCream < Doughnut {
cook() {
    print "Pipe full of custard and coat with chocolate.";
}
}

BostonCream().cook();

// Output
// Fry until golden brown.
// Pipe full of custard and coat with chocolate.
// Place in a nice box.
