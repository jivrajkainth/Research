package main.rapidware.interviews.coding;

import java.util.Set;

/**
 * Created by Sonny on 28/04/2014.
 */
public interface Graph<T_Vertex> {

    /**
     * Adds a new edge to the Graph.  This function is reflexive - ie the mutation caused by
     * function call <code>addEdge(A, B)</code> is equivalent to <code>addEdge(B, A)</code>
     *
     * @param vertex    - one of the two vertices
     * @param neighbour - the neighbour vertex of <code>vertex</code>
     */
    public void addEdge(final T_Vertex vertex, T_Vertex neighbour);

    /**
     * @return Number of vertices in this graph
     */
    public int vertexCount();

    /**
     * @return Number of edges in this graph
     */
    public int edgeCount();

    /**
     * @return Set of all vertices in this graph
     */
    public Set<T_Vertex> vertices();

    /**
     * @param vertex
     * @return A set containing all the neighbours of <code>vertex</code>
     */
    public Set<T_Vertex> neighboursOf(final T_Vertex vertex);

    /**
     * @param vertex
     * @return Number of neighbours that <code>vertex</code> has
     */
    public int degreeOf(final T_Vertex vertex);

    public boolean hasVertex(final T_Vertex vertex);

    /**
     * This function is reflexive - ie the result of
     * function call <code>hasEdge(A, B)</code> is equal to <code>hasEdge(B, A)</code>
     *
     * @param vertex
     * @param neighbour
     * @return
     */
    public boolean hasEdge(final T_Vertex vertex, T_Vertex neighbour);
}
