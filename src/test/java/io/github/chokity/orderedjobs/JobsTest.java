package io.github.chokity.orderedjobs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


class JobsTest {

    @Test
    void should_register_standalone_job() {
        // Given
        Jobs orderedJobs = new Jobs();
        Character job = 'b';

        // When
        orderedJobs.register(job);

        // Then
        List<Map.Entry<Character, Character>> jobs = orderedJobs.getAllJobs();
        assertTrue(jobs.stream().anyMatch(x -> x.getKey() == job),
                "should contain standalone job");
    }

    @Test
    void should_register_dependent_job() {
        // Given
        Jobs orderedJobs = new Jobs();
        Character job = 'b';
        Character dependentJob = 'a';

        // When
        orderedJobs.register(job, dependentJob);

        // Then
        List<Map.Entry<Character, Character>> jobs = orderedJobs.getAllJobs();
        assertTrue(jobs.stream().anyMatch(x -> x.getKey() == job && x.getValue() == dependentJob),
                "should contain job and dependent job");
    }

    @Test
    void should_get_initial_job() {
        // Given scenario
        // a - b - c
        Jobs jobs = new Jobs();
        jobs.register('c');
        jobs.register('c', 'b');
        jobs.register('b', 'a');

        // When
        Assertions.assertEquals('a', jobs.getInitialJob());
    }

    @Test
    void should_sort_jobs() {
        // Given scenario
        // a - b - c
        Jobs jobs = new Jobs();
        jobs.register('c');
        jobs.register('c', 'b');
        jobs.register('b', 'a');

        // When
        String[] jobsSorted = jobs.sort();

        // Then
        String[] expected = new String[]{"a", "b", "c"};
        assertArrayEquals(expected, jobsSorted);
    }

    @Test
    void should_fail_sort_on_circular_dependency() {

        // Given scenario
        // a - b - c - d - b
        Jobs jobs = new Jobs();
        jobs.register('c');
        jobs.register('c', 'b');
        jobs.register('b', 'a');
        jobs.register('d', 'c');
        jobs.register('b', 'd');

        // When
        // Then
        AssertionError assertionError = assertThrows(AssertionError.class, jobs::sort);
        assertEquals("circular dependency detected for job b", assertionError.getMessage());

    }
}