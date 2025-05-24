public enum Status 
{
    TO_DO(1, "To Do")
    {
        @Override
        public Status getSuccessor() 
        {
            return Status.IN_PROGRESS;
        }
    },
    IN_PROGRESS(2, "In Progress")
    {
        @Override
        public Status getSuccessor() 
        {
            return Status.TESTING;
        }
    },
    TESTING(3, "Testing")
    {
        @Override
        public Status getSuccessor() 
        {
            return Status.DONE;
        }
    },
    DONE(4, "Done")
    {
        @Override
        public Status getSuccessor() 
        {
            return null; 
        }
    };

    private final int level;
    private final String description;

    private Status(int level, String description) 
    {
        this.level = level;
        this.description = description;
    }

    public int getLevel() 
    {
        return level;
    }
    public String getDescription() 
    {
        return description;
    }
    public abstract Status getSuccessor();
}
