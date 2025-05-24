public enum Priority 
{
    LOW(1, 'L')
    {
        @Override
        public Priority getSuccessor() 
        {
            return Priority.MEDIUM;
        }
    },
    MEDIUM(2, 'M')
    {
        @Override
        public Priority getSuccessor() 
        {
            return Priority.HIGH;
        }
    },
    HIGH(3, 'H')
    {
        @Override
        public Priority getSuccessor() 
        {
            return Priority.LOW;
        }
    };

    private final int level;
    private final char symbol;

    private Priority(int level, char symbol) 
    {
        this.level = level;
        this.symbol = symbol;
    }
    public int getLevel() 
    {
        return level;
    }
    public char getSymbol() 
    {
        return symbol;
    }
    public abstract Priority getSuccessor();
}
