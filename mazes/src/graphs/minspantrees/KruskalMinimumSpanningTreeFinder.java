package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        // return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        List<E> edges = new ArrayList<>(graph.allEdges());
        edges.sort(Comparator.comparingDouble(E::weight));

        DisjointSets<V> disjointSets = createDisjointSets();

        for (V vertex : graph.allVertices()) {
            disjointSets.makeSet(vertex);
        }

        List<E> mstEdges = new ArrayList<>();

        for (E edge : edges) {
            V u = edge.from();
            V v = edge.to();

            if (disjointSets.findSet(u) != disjointSets.findSet(v)) {
                mstEdges.add(edge);
                disjointSets.union(u, v);
            }
        }

        int vertexCount = graph.allVertices().size();

        if (vertexCount == 0) {
            return new MinimumSpanningTree.Success<>();
        }

        if (mstEdges.size() != vertexCount - 1) {
            return new MinimumSpanningTree.Failure<>();
        }

        return new MinimumSpanningTree.Success<>(mstEdges);
    }
}
