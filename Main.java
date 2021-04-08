public class Main {
    public static final int numParticles = 5;
    public static final int maxEpochs = 10000;
    public static final double exitError = 0;//10e-16;
    public static final double randomDeath = 0.01;

    public static void main(String[] args) {
        Function func = new Booth();

        System.out.println("\nSetting problem dimension to " + func.getDim());
        System.out.println("Setting numParticles = " + numParticles);
        System.out.println("Setting maxEpochs = " + maxEpochs);
        System.out.println("Setting early exit error = " + exitError);
        System.out.println("Setting minX, maxX = " + func.getBeginRange() + " " + func.getEndRange());
        System.out.println("\nStarting PSO");

        Swarm swarm = new Swarm(func, numParticles, maxEpochs, exitError, randomDeath);
        try {
            swarm.runWithExecutor(); 
            //swarm.runWithoutExecutor();
        } catch (Exception e) {
            e.printStackTrace();
        }

        double[] bestPosition = swarm.GetBestPosition();
        double bestError = swarm.GetBestEval();

        System.out.println("---------------------------RESULT---------------------------");
        System.out.println("Best position/solution found:");

        for (int i = 0; i < bestPosition.length; i++)
        {
            System.out.printf("x[%d] = %.16f\n", i, bestPosition[i]);
        }

        System.out.println("\nFinal Best Evaluation: " + bestError);
    }
}
