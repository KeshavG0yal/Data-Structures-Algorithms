package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        Map<V, E> spt = new HashMap<>();

        if (Objects.equals(start, end)) {
            return spt;
        }

        Map<V, Double> distances = new HashMap<>();
        ExtrinsicMinPQ<V> pq = createMinPQ();
        distances.put(start, 0.0);
        pq.add(start, 0.0);


        while (!pq.isEmpty()) {
            V current = pq.removeMin();
            if (Objects.equals(current, end)) {
                break;
            }

            double currentDistance = distances.get(current);

            for (E edge : graph.outgoingEdgesFrom(current)) {
                V neighbor = edge.to();
                double weight = edge.weight();
                double newDistance = currentDistance + weight;

                if (!distances.containsKey(neighbor) || newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    spt.put(neighbor, edge);
                    if (pq.contains(neighbor)) {
                        pq.changePriority(neighbor, newDistance);
                    } else {
                        pq.add(neighbor, newDistance);
                    }
                }
            }
        }

        return spt;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        if (!spt.containsKey(end) && !Objects.equals(start, end)) {
            return new ShortestPath.Failure<>();
        }


        List<E> path = new ArrayList<>();
        V current = end;


        while (!Objects.equals(current, start)) {
            E edge = spt.get(current);
            if (edge == null) {
                return new ShortestPath.Failure<>();
            }
            path.add(0, edge);
            current = edge.from();
        }
        return new ShortestPath.Success<>(path);
    }
}
