package ParticleSwarm.src;

import java.util.concurrent.*;
import java.util.*;

public class Swarm {
    private int numOfParticles, epochs;
    private double inertia, cognitiveComponent, socialComponent;
    private double[] bestGlobalPosition;
    private double bestGlobalEval;
    private double propDeath, exitError;
    private Function function;
    private Particle[] particles;

    public static final double DEFAULT_INERTIA = 0.729844;
    public static final double DEFAULT_COGNITIVE = 1.496180;
    public static final double DEFAULT_SOCIAL = 1.496180;

    public Swarm(Function function, int numParticles, int epochs, double exitError, double randomDeath) {
        this(function, numParticles, epochs, exitError, randomDeath, DEFAULT_INERTIA, DEFAULT_COGNITIVE,
                DEFAULT_SOCIAL);
    }

    public Swarm(Function function, int numParticles, int epochs, double exitError, double propDeath, double inertia,
            double cognitive, double social) {
        this.numOfParticles = numParticles;
        this.epochs = epochs;
        this.exitError = exitError;
        this.propDeath = propDeath;
        this.inertia = inertia;
        this.cognitiveComponent = cognitive;
        this.socialComponent = social;
        this.function = function;
        bestGlobalEval = Double.MAX_VALUE;
    }

    public void initialize() {
        particles = new Particle[numOfParticles];

        for (int i = 0; i < numOfParticles; i++) {
            Particle p = new Particle(function, propDeath, inertia, cognitiveComponent, socialComponent);
            p.updatePersonalBest();
            updateGlobalBest(p, -1);
            particles[i] = p;
        }

        System.out.println("Global Best Evaluation (Epoch " + 0 + "):\t" + bestGlobalEval);
    }

    public void runWithThreads() throws InterruptedException {
        boolean hasFoundTheBest = false;
        List<ParticleThread> threads = new ArrayList<>();

        for (int i = 0; i < epochs; i++) {
            for (Particle p : particles) {
                ParticleThread t = new ParticleThread(p, bestGlobalPosition);
                t.start();
                threads.add(t);
            }

            for (Thread t : threads) {
                t.join();
            }

            for (ParticleThread r : threads) {
                hasFoundTheBest = updateGlobalBest(r.getParticle(), i);
                if (hasFoundTheBest)
                    break;
            }

            threads.clear();

            if (hasFoundTheBest) {
                break;
            }
        }
    }

    public void runWithExecutor() throws InterruptedException, ExecutionException {
        ExecutorService exec = Executors.newFixedThreadPool(particles.length);
        boolean hasFoundTheBest = false;
        List<Future<Particle>> threads = new ArrayList<>();

        for (int i = 0; i < epochs; i++) {
            for (Particle p : particles) {
                Callable<Particle> task = new ParticleTask(p, bestGlobalPosition);
                Future<Particle> thread = exec.submit(task);
                threads.add(thread);
            }

            for (Future<Particle> thread : threads) {
                Particle p = thread.get();
                hasFoundTheBest = updateGlobalBest(p, i);
                if (hasFoundTheBest)
                    break;
            }

            threads.clear();

            if (hasFoundTheBest) {
                break;
            }
        }

        exec.shutdown();
    }

    public void runWithoutThreads() {
        boolean hasFoundTheBest = false;

        for (int i = 0; i < epochs; i++) {
            for (Particle p : particles) {
                p.updateVelocity(bestGlobalPosition);
                p.updatePosition();
                p.updatePersonalBest();

                hasFoundTheBest = updateGlobalBest(p, i);
                if (hasFoundTheBest)
                    break;
            }

            if (hasFoundTheBest) {
                break;
            }
        }
    }

    public boolean updateGlobalBest(Particle p, int i) {
        double eval = p.getBestEval();
        boolean isNewGlobal = eval < bestGlobalEval;

        if (isNewGlobal) {
            bestGlobalPosition = p.getClonePosition();
            bestGlobalEval = eval;

            if (i != -1) {
                ShowBestGlobalEvaluation(i, eval <= exitError);
            }
        }

        return eval <= exitError;
    }

    private void ShowBestGlobalEvaluation(int i, boolean foundTheBest) {
        var text = foundTheBest ? "Found " : "";
        System.out.println(text + "Global Best Evaluation (Epoch " + (i + 1) + "):\t" + bestGlobalEval);
    }

    public double[] GetBestPosition() {
        return bestGlobalPosition;
    }

    public double GetBestEval() {
        return bestGlobalEval;
    }
}
