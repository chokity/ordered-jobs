package io.chokity.orderedjobs;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

/**
 * This class provides methods to add jobs to a list
 * and retrieve them in chronological order
 */
public class Jobs implements OrderedJobs {

    private final List<Entry<Character, Character>> allJobs;
    private Character initialJob;

    public Jobs() {
        this.allJobs = new ArrayList<>();
    }

    public List<Entry<Character, Character>> getAllJobs() {
        return allJobs;
    }

    /**
     * Registers standalone job without any dependencies
     *
     * @param job standalone job
     */
    @Override
    public void register(char job) {
        allJobs.add(new SimpleEntry<>(job, null));
    }

    /**
     * Registers depending job.
     *
     * @param job          job to be registered
     * @param dependentJob dependent job running prior to registered job
     */
    @Override
    public void register(char job, char dependentJob) {
        allJobs.add(new SimpleEntry<>(job, dependentJob));
    }

    /**
     * Returns list of all registered job by their running order (considering their dependencies)
     *
     * @return Array of sorted jobs
     */
    @Override
    public String[] sort() {
        List<Character> jobsSorted = new ArrayList<>();
        initialJob = getInitialJob();
        jobsSorted.add(initialJob);
        jobsSorted = iterateThroughJobs(initialJob, jobsSorted);

        return toArray(jobsSorted);
    }

    // find the one job that is not in the list of depending jobs
    // VisibleForTesting
    Character getInitialJob() {
        return allJobs.stream()
                .filter(entry -> entry.getValue() != null)
                .filter(entry -> allJobs.stream()
                        .map(Entry::getKey)
                        .noneMatch(key -> key.equals(entry.getValue())))
                .map(Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    // find other jobs depending on the given one. These jobs itself are iterated recursively.
    private List<Character> iterateThroughJobs(Character currentJob,
                                               List<Character> jobsSorted) {
        for (Entry<Character, Character> entry : allJobs) {
            Character job = entry.getKey();
            Character dependentJob = entry.getValue();

            if (currentJob.equals(dependentJob)) {
                assert isWithoutCircularDependency(jobsSorted, job) : "circular dependency detected for job " + job;
                jobsSorted.add(job);
                jobsSorted = iterateThroughJobs(job, jobsSorted);
            }
        }
        return jobsSorted;
    }

    // circular dependent if current job is already in list of processed jobs
    // initial job can't cause circular dependency
    private boolean isWithoutCircularDependency(List<Character> jobsSorted,
                                                Character currentJob) {
        return  !jobsSorted.contains(currentJob)
                && !initialJob.equals(currentJob);
    }

    private String[] toArray(List<Character> list) {
        return list.stream()
                .map(Object::toString)
                .toArray(String[]::new);
    }
}
