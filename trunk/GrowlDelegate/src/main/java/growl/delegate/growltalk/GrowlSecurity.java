package growl.delegate.growltalk;

public enum GrowlSecurity {
    NOAUTH {
        @Override
        public GrowlAuthentication authentication(String password) {
            return new NoGrowlAuthentication();
        }
    },
    MD5 {
        @Override
        public GrowlAuthentication authentication(String password) {
            return new MD5GrowlAuthentication(password);
        }
    },
    SHA256 {
        @Override
        public GrowlAuthentication authentication(String password) {
            return new SHA256GrowlAuthentication(password);
        }
    };
    
    public abstract GrowlAuthentication authentication(String password);
    
}
