package ParticleSwarm.src;

import java.util.concurrent.Callable;

public class ParticleThread extends Thread {
    private Particle particle;
    private double[] bestGlobalPosition;

    public ParticleThread(Particle particle,double[] bestGlobalPosition) {
        this.bestGlobalPosition = bestGlobalPosition;
        this.particle = particle;
    }

    @Override
    public void run() {
        particle.updateVelocity(bestGlobalPosition);
        particle.updatePosition();
        particle.updatePersonalBest();
    }

    public Particle getParticle() {
        return particle;
    }
}

class ParticleTask implements Callable<Particle> {
    private Particle particle;
    private double[] bestGlobalPosition;

    public ParticleTask(Particle particle,double[] bestGlobalPosition) {
        this.bestGlobalPosition = bestGlobalPosition;
        this.particle = particle;
    }

    @Override
    public Particle call() throws Exception {
        particle.updateVelocity(bestGlobalPosition);
        particle.updatePosition();
        particle.updatePersonalBest();
        return particle;
    }
}