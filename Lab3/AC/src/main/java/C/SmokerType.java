package C;

public enum SmokerType {
    TOBACCO_KEEPER {
        @Override
        public String toString() {
            return "Smoker with tobacco";
        }
    },
    PAPER_KEEPER {
        @Override
        public String toString() {
            return "Smoker with paper";
        }
    },
    MATCHES_KEEPER {
        @Override
        public String toString() {
            return "Smoker with matches";
        }
    };
}
