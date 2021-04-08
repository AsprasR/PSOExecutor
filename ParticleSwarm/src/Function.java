package ParticleSwarm.src;

public interface Function {
    double count(double... x);
    int getDim();
    double getBeginRange();
    double getEndRange();
}

class Rosenbrock implements Function {
    /**
    * Perform Rosenbrock's function. Domain is [-100, 100] Minimum is 0 at x = 1, y = 1.
    */
    public double count(double... x) {
        double sum = 0;

        for (int i = 0; i < x.length - 1; i++)
        {
            double p1 = Math.pow(1-x[i], 2);
            double p2 = Math.pow(x[i], 2);
            double p3 = 100 * Math.pow(x[i+1] - p2, 2);

            sum += p1 + p3;
        }

        return sum;
    }

    public int getDim() {
        return 2;
    }

    public double getBeginRange() {
        return -100;
    }

    public double getEndRange() {
        return 100;
    }
}

class Booth implements Function {
    /**
    * Perform Booth's function. Domain is [-10, 10] Minimum is 0 at x = 1 & y = 3.
    */
    public double count(double... x) {
        double p1 = Math.pow(x[0] + 2 * x[1] - 7, 2);
        double p2 = Math.pow(2 * x[0] + x[1] - 5, 2);
        return p1 + p2;
    }

    public int getDim() {
        return 2;
    }

    public double getBeginRange() {
        return -10;
    }

    public double getEndRange() {
        return 10;
    }
}

class Ackley implements Function {
    /**
     * Perform Ackley's function. Domain is [-5, 5] Minimum is 0 at x = 0 & y = 0.
     */
    public double count(double... x) {
        double p1 = -20 * Math.exp(-0.2 * Math.sqrt(0.5 * ((x[0] * x[0]) + (x[1] * x[1]))));
        double p2 = Math.exp(0.5 * (Math.cos(2 * Math.PI * x[0]) + Math.cos(2 * Math.PI * x[1])));
        return p1 - p2 + Math.E + 20;
    }

    public int getDim() {
        return 2;
    }

    public double getBeginRange() {
        return -5;
    }

    public double getEndRange() {
        return 5;
    }
}
