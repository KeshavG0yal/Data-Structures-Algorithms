package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.Random;
import java.util.Set;
import java.util.HashSet;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        Set<EdgeWithData<Room, Wall>> edges = new HashSet<>();
        for (Wall wall : walls) {
            EdgeWithData<Room, Wall> edge = new EdgeWithData<>(
                wall.getRoom1(),
                wall.getRoom2(),
                rand.nextDouble(),
                wall);
            edges.add(edge);
        }

        MazeGraph mazeGraph = new MazeGraph(edges);
        var mst = this.minimumSpanningTreeFinder.findMinimumSpanningTree(mazeGraph);
        Set<Wall> wallsToRemove = new HashSet<>();
        for (EdgeWithData<Room, Wall> edge : mst.edges()) {
            Wall wall = edge.data();
            wallsToRemove.add(wall);
        }
        return wallsToRemove;
    }
}
