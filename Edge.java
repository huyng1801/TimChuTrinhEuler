   class Edge {
        private Vertex source, destination;

        public Edge(Vertex source, Vertex destination) {
            this.source = source;
            this.destination = destination;
        }

        public Vertex getSource() {
            return source;
        }

        public Vertex getDestination() {
            return destination;
        }
    }