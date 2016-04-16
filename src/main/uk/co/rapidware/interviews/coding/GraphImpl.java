package main.rapidware.interviews.coding;

import java.io.FileInputStream;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by Sonny on 28/04/2014.
 */
public class GraphImpl<T_Vertex> implements Graph<T_Vertex> {

    private static final Logger LOGGER = Logger.getLogger(GraphImpl.class.getName());

    public static Logger getLogger() {
        return LOGGER;
    }

    public static Graph<String> createGraphFromFile(final String filepath, final String delimiter) throws Exception {
        final GraphImpl<String> graph = new GraphImpl<>();


        final FileInputStream inputStream = new FileInputStream(filepath);


        System.gc();
        try {
            int lineNo = 0;
            for (final Scanner scanner = new Scanner(inputStream); scanner.hasNextLine(); lineNo++) {
                final String line = scanner.nextLine();
                final String[] tokens = line.split(delimiter);
                for (int index = 1; index < tokens.length; index++) {
                    graph.addEdge(tokens[0], tokens[index]);
                }

                if (lineNo % 10000 == 0) {
                    getLogger().info(String.format("Processed [%d] lines", lineNo));
                }
            }
        } catch (Throwable throwable) {
            throw throwable;
        }

        return graph;
    }

    private static <T_Vertex> Set<T_Vertex> createVertexSet() {
        return new HashSet<T_Vertex>();
    }

    private final TreeMap<T_Vertex, Set<T_Vertex>> vertexTable_ = new TreeMap<>();
    private int edgeCount_ = 0;

    public TreeMap<T_Vertex, Set<T_Vertex>> getVertexTable() {
        return vertexTable_;
    }

    @Override
    public void addEdge(T_Vertex vertex, T_Vertex neighbour) {
        if (!hasEdge(vertex, neighbour)) {
            if (!hasVertex(vertex)) {
                getVertexTable().put(vertex, createVertexSet());
            }
            if (!hasVertex(neighbour)) {
                getVertexTable().put(neighbour, createVertexSet());
            }
            getVertexTable().get(vertex).add(neighbour);
            getVertexTable().get(neighbour).add(vertex);
            edgeCount_++;
        }
    }

    @Override
    public int vertexCount() {
        return getVertexTable().size();
    }

    @Override
    public int edgeCount() {
        return edgeCount_;
    }

    @Override
    public Set<T_Vertex> vertices() {
        return getVertexTable().keySet();
    }

    @Override
    public Set<T_Vertex> neighboursOf(T_Vertex vertex) {
        return Collections.unmodifiableSet(getVertexTable().get(vertex));
    }

    @Override
    public int degreeOf(T_Vertex vertex) {
        return neighboursOf(vertex).size();
    }

    @Override
    public boolean hasVertex(T_Vertex vertex) {
        return getVertexTable().containsKey(vertex);
    }

    @Override
    public boolean hasEdge(T_Vertex vertex, T_Vertex neighbour) {
        final Set<T_Vertex> vertices = getVertexTable().get(vertex);
        return vertices == null ? false : vertices.contains(neighbour);
    }


}
