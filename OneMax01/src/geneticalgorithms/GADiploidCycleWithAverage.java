package geneticalgorithms;

import individuals.IDiploidWithAverage;
import individuals.Individual;
import parentselectors.TypeSelectionParents;
import populations.PDiploidWithAverage;
import survivalselectors.SSDiploidWithAverage;
import survivalselectors.TypeSelectionSurvival;
import util.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GADiploidCycleWithAverage extends GeneticAlgorithm {
    public GADiploidCycleWithAverage(int size, int length, int countOfPoint, double probabilityCrossover, TypeSelectionParents typeSelectionParents, TypeSelectionSurvival typeSelectionSurvival, AlgorithmType type) {
        super(countOfPoint, probabilityCrossover, typeSelectionParents, typeSelectionSurvival, length, type);
        population = new PDiploidWithAverage(size, length);
        individualWithMaximalFitness = population.maximalFitness();
        maximalFitness = individualWithMaximalFitness.calcFitness();
        sSelector = new SSDiploidWithAverage();
    }

    protected void SinglePointCrossover() throws GAException {
        List<Individual> parents = population.getMaximal(2);
        byte[][] newGenoms0 = spCrossover(Lists.of(parents.get(0).getGenom(0), parents.get(0).getGenom(1)));
        byte[][] newGenoms1 = spCrossover(Lists.of(parents.get(1).getGenom(0), parents.get(1).getGenom(1)));
        int firstGamete = ThreadLocalRandom.current().nextInt(4);
        int secondGamete = ThreadLocalRandom.current().nextInt(4);
        byte[] gamete0, gamete1;
        if (firstGamete < 2) {
            gamete0 = parents.get(0).getGenom(firstGamete);
        } else {
            gamete0 = newGenoms0[firstGamete - 2];
        }
        if (secondGamete < 2) {
            gamete1 = parents.get(1).getGenom(secondGamete);
        } else {
            gamete1 = newGenoms1[secondGamete - 2];
        }
        Individual i = new IDiploidWithAverage(SBM(gamete0), SBM(gamete1), parents.get(0).getChanged(firstGamete % 2),
                parents.get(1).getChanged(secondGamete % 2));
        List<Individual> p = new ArrayList<>(population.getPopulation());
        if (!p.contains(i) && i.calcFitness() > p.get(p.size() - 1).calcFitness()) {
            p.set(p.size() - 1, i);
        }
        population = new PDiploidWithAverage(p);
        updateMaximalFitness();
    }

    private byte[][] spCrossover(List<byte[]> parents) {
        int point = ThreadLocalRandom.current().nextInt(length);
        byte[][] c = new byte[2][length];
        for (int i = 0; i <= point; i++) {
            c[0][i] = parents.get(0)[i];
            c[1][i] = parents.get(1)[i];
        }
        for (int i = point + 1; i < length; i++) {
            c[0][i] = parents.get(1)[i];
            c[1][i] = parents.get(0)[i];
        }
        return c;
    }

    @Override
    protected void greedyMGA() throws GAException {
        List<Individual> parents = population.getMaximal(2);
        byte[][] newGenoms0 = uniformCrossoverTwo(Lists.of(parents.get(0).getGenom(0), parents.get(0).getGenom(1)));
        byte[][] newGenoms1 = uniformCrossoverTwo(Lists.of(parents.get(1).getGenom(0), parents.get(1).getGenom(1)));
        int firstGamete = ThreadLocalRandom.current().nextInt(4);
        int secondGamete = ThreadLocalRandom.current().nextInt(4);
        byte[] gamete0, gamete1;
        if (firstGamete < 2) {
            gamete0 = parents.get(0).getGenom(firstGamete);
        } else {
            gamete0 = newGenoms0[firstGamete - 2];
        }
        if (secondGamete < 2) {
            gamete1 = parents.get(1).getGenom(secondGamete);
        } else {
            gamete1 = newGenoms1[secondGamete - 2];
        }
        Individual i = new IDiploidWithAverage(SBM(gamete0), SBM(gamete1), parents.get(0).getChanged(firstGamete % 2),
                parents.get(1).getChanged(secondGamete % 2));
        List<Individual> p = new ArrayList<>(population.getPopulation());
        if (!p.contains(i) && i.calcFitness() > p.get(p.size() - 1).calcFitness()) {
            p.set(p.size() - 1, i);
        }
        population = new PDiploidWithAverage(p);
        updateMaximalFitness();
    }

    private byte[][] uniformCrossoverTwo(List<byte[]> parents) throws GAException {
        if (parents.size() != 2) {
            throw new GAException("Sorry, expected two parents");
        }
        int length = parents.get(0).length;
        byte[][] c = new byte[2][length];
        for (int i = 0; i < length; i++) {
            if (probabilityCrossover < Math.random()) {
                c[0][i] = parents.get(1)[i];
                c[1][i] = parents.get(0)[i];
            } else {
                c[0][i] = parents.get(0)[i];
                c[1][i] = parents.get(1)[i];
            }
        }
        return c;
    }


    @Override
    protected void randomLocalSearch() throws GAException {
        int size = population.getSize();
        List<IDiploidWithAverage> children = new ArrayList<>();
        population.deleteConstant();
        List<Individual> inds = pSelector.select(population, 2, typeSelectionParents);
        int first = ThreadLocalRandom.current().nextInt(2);
        int second = ThreadLocalRandom.current().nextInt(2);
        IDiploidWithAverage i = new IDiploidWithAverage(inds.get(0).getGenom(first), inds.get(1).getGenom(second),
                inds.get(0).getChanged(first), inds.get(1).getChanged(second));
        int index = ThreadLocalRandom.current().nextInt(length);
        while (!i.inverseGene(index, 0)) {
            index = ThreadLocalRandom.current().nextInt(length);
        }
        index = ThreadLocalRandom.current().nextInt(length);
        while (!i.inverseGene(index, 1)) {
            index = ThreadLocalRandom.current().nextInt(length);
        }
        if (i.calcFitness() >= inds.get(0).calcFitness() || i.calcFitness() >= inds.get(1).calcFitness()) {
            children.add(i);
        }
        population = sSelector.select(population, children, typeSelectionSurvival, size);
        population.incrementAges();
        evalPopulation();
        updateMaximalFitness();
    }

    @Override
    protected void standardBitMutation() throws GAException {
        List<IDiploidWithAverage> children = new ArrayList<>();
        List<Individual> inds = pSelector.select(population, 1, typeSelectionParents);
        for (Individual ind : inds) {
            int firstGamete = ThreadLocalRandom.current().nextInt(4);
            int secondGamete = ThreadLocalRandom.current().nextInt(4);
            byte[] gamete0, gamete1;
            if (firstGamete < 2) {
                gamete0 = ind.getGenom(firstGamete);
            } else {
                gamete0 = SBM(ind.getGenom(firstGamete % 2));
            }
            if (secondGamete < 2) {
                gamete1 = ind.getGenom(secondGamete);
            } else {
                gamete1 = SBM(ind.getGenom(secondGamete % 2));
            }
            IDiploidWithAverage i = new IDiploidWithAverage(gamete0, gamete1, ind.getChanged(0), ind.getChanged(1));
            if (i.calcFitness() >= ind.calcFitness()) {
                children.add(i);
            }
        }
        population = sSelector.select(population, children, typeSelectionSurvival, population.getSize());
        population.incrementAges();
        evalPopulation();
        updateMaximalFitness();
    }

    @Override
    public boolean isTerminated(int maxValue) {
        return ((PDiploidWithAverage) population).isTerminated(maxValue);
    }
}