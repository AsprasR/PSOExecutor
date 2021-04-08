package ParticleSwarm.src;

import java.util.Random;

class Particle {
    private double beginRange, endRange;
    private double inertia, cognitive, social, propDeath;
    private int dim;
    private static Random r;

    private Function function;
    private double[] position;
    private double[] velocity;
    private double[] bestPosition;
    private double bestEval;

    Particle (Function function, double propDeath, double inertia, double cognitive, double social) {
        r = new Random();
        this.function = function;
        this.inertia = inertia;
        this.cognitive = cognitive;
        this.social = social;
        this.propDeath = propDeath;

        bestEval = Double.MAX_VALUE;
        beginRange = function.getBeginRange();
        endRange = function.getEndRange();
        dim = function.getDim();
        position = setRandomPosition();
        velocity = setRandomVelocity();
    }

    private double[] setRandomPosition() {
        double[] randomPosition = new double[dim];

        for (int i = 0; i < randomPosition.length; i++)
            randomPosition[i] = rand(beginRange, endRange);

        return randomPosition;
    }

    private double[] setRandomVelocity() {
        double[] randomVelocity = new double[dim];

        for (int i = 0; i < randomVelocity.length; i++) {
            double min = beginRange * 0.1;
            double max = endRange * 0.1;
            randomVelocity[i] = rand(min, max);
        }

        return randomVelocity;
    }

    private static double rand (double min, double max) {
        return min + (max - min) * r.nextDouble();
    }

    public void updatePersonalBest () {
        double eval = function.count(position);
        
        if (eval < bestEval) {
            bestPosition = getClonePosition();
            bestEval = eval;
        }
    }

    public void updatePosition () {
        for (int i = 0; i < position.length; i++) 
        {
            position[i] = position[i] + velocity[i];

            if (position[i] < beginRange)
                position[i] = beginRange;
            else if (position[i] > endRange)
                position[i] = endRange;                 
        }
    }

    public void updateVelocity(double... bestGlobalPosition) {
        for (int i = 0; i < velocity.length; i++) 
        {
            double r1 = r.nextDouble();
            double r2 = r.nextDouble();

            velocity[i] = 
            (inertia * velocity[i]) +
            (cognitive * r1 * (bestPosition[i] - position[i])) +
            (social * r2 * (bestGlobalPosition[i] - position[i]));
        }
    }

    public boolean randomDeath() {
        double die = r.nextDouble();
        
        if (die >= propDeath) return false;

        for (int i = 0; i < position.length; i++)
            position[i] = rand(beginRange, endRange);

        return true;
    }

    public double[] getClonePosition() {
        return position.clone();
    }

    public double getBestEval() {
        return bestEval;
    }
}
