package main.rapidware.interviews.coding;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

/**
 */
public class GraphScratchPad {

    private static final Logger LOGGER = Logger.getLogger(GraphScratchPad.class.getName());

    public static Logger getLogger() {
        return LOGGER;
    }

    public static void main(String[] args) throws Exception {
        final Graph<String> imdbGraph =
                GraphImpl.createGraphFromFile("/Users/Sonny/Developer/Research/src/uk/co/rapidware/interviews/morganstanley/cast.rated.txt", "/");

        ;

        //getLogger().info("Movies starring Benedict Cumberbatch:" + imdbGraph.neighboursOf("Cumberbatch, Benedict"));
        final String subjectOfInterest = "Bacon, Kevin";
        getLogger().info("Movies starring" + subjectOfInterest + ": " + imdbGraph.neighboursOf(subjectOfInterest));

        getLogger().info("Creating PathFinder from " + subjectOfInterest);
        final PathFinder pathFinder = new PathFinder<String>(imdbGraph, subjectOfInterest);
        getLogger().info("PathFinder created!");

        final String destination = "Kidman, Nicole";
        if (pathFinder.isReachable(destination)) {
            final Integer distance = pathFinder.distanceTo(destination);
            getLogger().info("Distance to " + destination + " is " + distance);
            getLogger().info("Path to destination is " + pathFinder.pathTo(destination));
        } else {
            getLogger().warning(destination + " is not reachable!");
        }
    }


    public static class PathFinder<T_Vertex> {

        private final Map<T_Vertex, T_Vertex> priorVertexOnShortestPath_ = new HashMap<>();
        private final Map<T_Vertex, Integer> distanceOfShortestPathTo_ = new HashMap<>();

        private final T_Vertex vertexFrom_;

        public PathFinder(final Graph<T_Vertex> graph, final T_Vertex vertexFrom) {
            vertexFrom_ = vertexFrom;

            // To build shortest distance paths we will do Breadth First Search
            final Queue<T_Vertex> vertexQueue = new LinkedBlockingQueue<>(1000);
            vertexQueue.offer(vertexFrom);

            distanceOfShortestPathTo_.put(vertexFrom, 0);

            for (T_Vertex vertex = vertexQueue.poll(); vertex != null; vertex = vertexQueue.poll()) {
                final int distanceToCurrentVertex = distanceOfShortestPathTo_.get(vertex);
                for (T_Vertex neighbour : graph.neighboursOf(vertex)) {
                    if (!distanceOfShortestPathTo_.containsKey(neighbour)) {
                        vertexQueue.offer(neighbour);
                        distanceOfShortestPathTo_.put(neighbour, distanceToCurrentVertex + 1);
                        priorVertexOnShortestPath_.put(neighbour, vertex);
                    }
                }
            }

        }

        public Map<T_Vertex, T_Vertex> getPriorVertexOnShortestPath() {
            return priorVertexOnShortestPath_;
        }

        public Map<T_Vertex, Integer> getDistanceOfShortestPathTo() {
            return distanceOfShortestPathTo_;
        }

        public T_Vertex getVertexFrom() {
            return vertexFrom_;
        }

        // is v reachable from the source s?
        public boolean isReachable(T_Vertex destination) {
            return getDistanceOfShortestPathTo().containsKey(destination);
        }

        // return the length of the shortest path from v to s
        public int distanceTo(T_Vertex destination) {
            if (!isReachable(destination)) {
                throw new IllegalArgumentException(
                        String.format("Destination [%s] cannot be reached from [%s]", destination, getVertexFrom()));
            }
            return getDistanceOfShortestPathTo().get(destination);
        }

        // return the shortest path from v to s as an Iterable
        public Iterable<T_Vertex> pathTo(T_Vertex destination) {
            final Stack<T_Vertex> path = new Stack<>();
            path.push(destination);
            for (T_Vertex priorVertex = getPriorVertexOnShortestPath().get(destination);
                 priorVertex != null;
                 priorVertex = getPriorVertexOnShortestPath().get(priorVertex)) {

                path.push(priorVertex);
            }
            return path;
        }
    }
}
